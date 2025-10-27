package uk.co.notnull.platformdetection;

import org.bukkit.entity.Player;
import org.vivecraft.ViveMain;
import org.vivecraft.VivePlayer;

public class VivecraftHandlerPaper implements VivecraftHandler<Player> {
	public boolean isVivecraftPlayer(Player player) {
		return ViveMain.getVivePlayer(player) != null;
	}

	public Platform getPlatform(Player player) {
		VivePlayer vivePlayer = ViveMain.getVivePlayer(player);

		if(vivePlayer == null) {
			return null;
		}

		return vivePlayer.isVR() ? Platform.JAVA_VIVECRAFT : Platform.JAVA_VIVECRAFT_NOVR;
	}
 }
