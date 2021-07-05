package uk.co.notnull.platformdetection;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

public class FloodgateHandlerPaper implements FloodgateHandler<Player> {
	private final FloodgateApi floodgateApi;

	public FloodgateHandlerPaper() {
		floodgateApi = FloodgateApi.getInstance();
	}

	public boolean isFloodgatePlayer(Player player) {
		return floodgateApi.isFloodgatePlayer(player.getUniqueId());
	}

	public String getPlatformVersion(Player player) {
		if(floodgateApi.isFloodgatePlayer(player.getUniqueId())) {
			return floodgateApi.getPlayer(player.getUniqueId()).getVersion();
		}

		return null;
	}

	public Platform getPlatform(Player player) {
		if(!floodgateApi.isFloodgatePlayer(player.getUniqueId())) {
			return null;
		}

		return Platform.fromFloodgate(floodgateApi.getPlayer(player.getUniqueId()).getDeviceOs());
	}
 }
