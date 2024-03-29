package uk.co.notnull.platformdetection;

import java.util.UUID;

@SuppressWarnings("unused")
public interface PlatformDetectionPlugin<P> {
	boolean isFloodgateEnabled();
	boolean isVivecraftEnabled();
	Platform getPlatform(P player);
	Platform getPlatform(UUID uuid);
}
