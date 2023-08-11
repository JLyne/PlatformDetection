package uk.co.notnull.platformdetection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@SuppressWarnings("unused")
public final class PlatformDetectionPaper extends JavaPlugin implements Listener, PlatformDetectionPlugin<Player> {
	private PlatformPlaceholders expansion;

	private FloodgateHandler<Player> floodgateHandler;
	private VivecraftHandler<Player> vivecraftHandler;

	@Override
	public void onEnable() {
		boolean floodgateEnabled = getServer().getPluginManager().isPluginEnabled("floodgate");
		boolean vivecraftEnabled = getServer().getPluginManager().isPluginEnabled("Vivecraft-Spigot-Extensions");

		if(floodgateEnabled) {
			floodgateHandler = new FloodgateHandlerPaper();
		}

		if(vivecraftEnabled) {
			vivecraftHandler = new VivecraftHandlerPaper();
		}

		expansion = new PlatformPlaceholders(this);
		expansion.register();

		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		if(event.getPlugin().getName().equals("floodgate")) {
			floodgateHandler = new FloodgateHandlerPaper();
		}

		if(event.getPlugin().getName().equals("Vivecraft-Spigot-Extensions")) {
			vivecraftHandler = new VivecraftHandlerPaper();
		}
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if(event.getPlugin().getName().equals("floodgate")) {
			floodgateHandler = null;
		}

		if(event.getPlugin().getName().equals("Vivecraft-Spigot-Extensions")) {
			vivecraftHandler = null;
		}
	}

	@Override
	public void onDisable() {
		expansion.unregister();
	}

	public Platform getPlatform(Player player) {
		if(player == null || !player.isOnline()) {
			return Platform.UNKNOWN;
		}

		if(isFloodgateEnabled() && floodgateHandler.isFloodgatePlayer(player)) {
			return floodgateHandler.getPlatform(player);
		}

		if(isVivecraftEnabled() && vivecraftHandler.isVivecraftPlayer(player)) {
			return vivecraftHandler.getPlatform(player);
		}

		return Platform.fromClientBrand(player.getClientBrandName());
	}

	public Platform getPlatform(UUID uuid) {
		if(uuid == null) {
			return Platform.UNKNOWN;
		}

		Player player = Bukkit.getPlayer(uuid);

		if(player == null) {
			return Platform.UNKNOWN;
		}

		return getPlatform(player);
	}

	public String getPlatformVersion(Player player) {
		if(isFloodgateEnabled() && floodgateHandler.isFloodgatePlayer(player)) {
			return floodgateHandler.getPlatformVersion(player);
		}

		return String.valueOf(player.getProtocolVersion());
	}

	public String getPlatformVersion(UUID uuid) {
		if(uuid == null) {
			return null;
		}

		Player player = Bukkit.getPlayer(uuid);

		if(player == null) {
			return null;
		}

		return getPlatformVersion(player);
	}

	public boolean isFloodgateEnabled() {
		return floodgateHandler != null;
	}

	public boolean isVivecraftEnabled() {
		return vivecraftHandler != null;
	}
}
