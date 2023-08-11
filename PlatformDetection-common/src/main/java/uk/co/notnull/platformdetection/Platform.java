package uk.co.notnull.platformdetection;

import org.geysermc.floodgate.util.DeviceOs;

@SuppressWarnings("unused")
public enum Platform {
	UNKNOWN("Unknown", '\uE1E1'),
	JAVA("Java Edition", '\uE1DD'),
	JAVA_FABRIC("Fabric", '\uE1DF', false, true),
	JAVA_FORGE("Forge", '\uE1E0', false, true),
	JAVA_VIVECRAFT("ViveCraft", '\uE1DC', false, true),
	JAVA_VIVECRAFT_NOVR("ViveCraft - No VR", '\uE1DE', false, true),
	JAVA_LUNAR("Lunar Client", '\uE1E2', false, true),
	JAVA_QUILT("Quilt", '\uE1E5', false, true),
	JAVA_OPTIFINE("Optifine", '\uE1E6', false, true),
	BEDROCK("Bedrock Edition", '\uE11B', true, false),
	BEDROCK_UWP("Bedrock Edition - Windows 10", '\uE1D6', true, false),
	BEDROCK_WIN32("Bedrock Edition - Windows", '\uE1D7', true, false),
	BEDROCK_XBOX("Bedrock Edition - Xbox", '\uE1DA', true, false),
	BEDROCK_MACOS("Bedrock Edition - MacOS",'\uE1D2', true, false),
	BEDROCK_IOS("Bedrock Edition - iOS", '\uE1D1', true, false),
	BEDROCK_APPLE_TV("Bedrock Edition - AppleTV", '\uE1D1', true, false),
	BEDROCK_ANDROID("Bedrock Edition - Android", '\uE1D0', true, false),
	BEDROCK_AMAZON("Bedrock Edition - Amazon", '\uE1D3', true, false),
	BEDROCK_GEARVR("Bedrock Edition - GearVR", '\uE1D4', true, false),
	BEDROCK_HOLOLENS("Bedrock Edition - HoloLens", '\uE1D5', true, false),
	BEDROCK_PS4("Bedrock Edition - PS4", '\uE1D8', true, false),
	BEDROCK_SWITCH("Bedrock Edition - Switch", '\uE1D9', true, false),
	BEDROCK_WINDOWS_PHONE("Bedrock Edition - Windows Phone", '\uE1DB', true, false);

	private final String label;
	private final String icon;
	private final boolean isBedrock;
	private final boolean isModded;

	Platform(String label, Character icon) {
		this.label = label;
		this.icon = icon.toString();
		this.isModded = false;
		this.isBedrock = false;
	}

	Platform(String label, Character icon, boolean isBedrock, boolean isModded) {
		this.label = label;
		this.icon = icon.toString();
		this.isBedrock = isBedrock;
		this.isModded = isModded;
	}

	public String getLabel() {
		return label;
	}

	public String getIcon() {
		return icon;
	}

	public boolean isBedrock() {
		return isBedrock;
	}

	public boolean isModded() {
		return isModded;
	}

	public static Platform fromFloodgate(DeviceOs floodgateOs) {
		switch(floodgateOs) {
			case GOOGLE:
				return BEDROCK_ANDROID;
			case IOS:
				return BEDROCK_IOS;
			case OSX:
				return BEDROCK_MACOS;
			case AMAZON:
				return BEDROCK_AMAZON;
			case GEARVR:
				return BEDROCK_GEARVR;
			case HOLOLENS:
				return BEDROCK_HOLOLENS;
			case UWP:
				return BEDROCK_UWP;
			case WIN32:
				return BEDROCK_WIN32;
			case PS4:
				return BEDROCK_PS4;
			case NX:
				return BEDROCK_SWITCH;
			case XBOX:
				return BEDROCK_XBOX;
			case WINDOWS_PHONE:
				return BEDROCK_WINDOWS_PHONE;
			default:
				return UNKNOWN;
		}
	}

	public static Platform fromClientBrand(String brand) {
		if(brand == null) {
			return UNKNOWN;
		}

		if(brand.startsWith("vanilla")) {
			return JAVA;
		}

		if(brand.contains("forge")) {
			return JAVA_FORGE;
		}

		if(brand.contains("fabric")) {
			return JAVA_FABRIC;
		}

		if(brand.contains("lunarclient")) {
			return JAVA_LUNAR;
		}

		if(brand.contains("quilt")) {
			return JAVA_QUILT;
		}

		if(brand.contains("optifine")) {
			return JAVA_OPTIFINE;
		}

		return UNKNOWN;
	}
}
