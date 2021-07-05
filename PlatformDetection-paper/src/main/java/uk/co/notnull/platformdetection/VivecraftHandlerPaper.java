package uk.co.notnull.platformdetection;

import org.bukkit.entity.Player;
import org.vivecraft.VSE;

public class VivecraftHandlerPaper implements VivecraftHandler<Player> {
	public boolean isVivecraftPlayer(Player player) {
		return VSE.vivePlayers.containsKey(player.getUniqueId());
	}

	public Platform getPlatform(Player player) {
		if(!VSE.vivePlayers.containsKey(player.getUniqueId())) {
			return null;
		}

		return VSE.isVive(player) ? Platform.JAVA_VIVECRAFT : Platform.JAVA_VIVECRAFT_NOVR;
	}
 }
