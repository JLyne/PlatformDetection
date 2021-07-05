package uk.co.notnull.platformdetection;

import com.techjar.vbe.VivecraftAPI;
import com.velocitypowered.api.proxy.Player;

public class VivecraftHandlerVelocity implements VivecraftHandler<Player> {
	public boolean isVivecraftPlayer(Player player) {
		return VivecraftAPI.isVive(player);
	}

	public Platform getPlatform(Player player) {
		if(!VivecraftAPI.isVive(player)) {
			return null;
		}

		return VivecraftAPI.isVR(player) ? Platform.JAVA_VIVECRAFT : Platform.JAVA_VIVECRAFT_NOVR;
	}
 }
