package uk.co.notnull.platformdetection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.MessageTooLargeException;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.geysermc.floodgate.FloodgateAPI;
import org.jetbrains.annotations.NotNull;
import org.vivecraft.VSE;

import java.util.UUID;

public final class PlatformDetectionPaper extends JavaPlugin implements Listener, PlatformDetectionPlugin<Player>, PluginMessageListener {
	private PlatformPlaceholders expansion;
	private boolean floodgateEnabled = false;
	private boolean vivecraftEnabled = false;

	@Override
	public void onEnable () {
		expansion = new PlatformPlaceholders(this);
		expansion.register();

		floodgateEnabled = getServer().getPluginManager().isPluginEnabled("floodgate-bukkit");
		vivecraftEnabled = getServer().getPluginManager().isPluginEnabled("Vivecraft-Spigot-Extensions");

		getServer().getPluginManager().registerEvents(this, this);
		getServer().getMessenger().registerIncomingPluginChannel(this, "minecraft:brand", this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "platform:brand");
	}

	@Override
	public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
		try {
			player.sendPluginMessage(this, "platform:brand", message);
		} catch(IllegalArgumentException|MessageTooLargeException ignored) {}
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		if(event.getPlugin().getName().equals("floodgate-bukkit")) {
			floodgateEnabled = true;
		}

		if(event.getPlugin().getName().equals("Vivecraft-Spigot-Extensions")) {
			vivecraftEnabled = true;
		}
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if(event.getPlugin().getName().equals("floodgate-bukkit")) {
			floodgateEnabled = false;
		}

		if(event.getPlugin().getName().equals("Vivecraft-Spigot-Extensionst")) {
			vivecraftEnabled = false;
		}
	}

	@Override
	public void onDisable() {
		expansion.unregister();
	}

	public boolean isFloodgateEnabled() {
		return floodgateEnabled;
	}

	public boolean isVivecraftEnabled() {
		return vivecraftEnabled;
	}

	public Platform getPlatform(Player player) {
		if(player == null || !player.isOnline()) {
			return Platform.UNKNOWN;
		}

		if(isFloodgateEnabled() && FloodgateAPI.isBedrockPlayer(player)) {
			return Platform.fromFloodgate(FloodgateAPI.getPlayer(player).getDeviceOS());
		}

		if(isVivecraftEnabled() && VSE.vivePlayers.containsKey(player.getUniqueId())) {
			return VSE.isVive(player) ? Platform.JAVA_VIVECRAFT : Platform.JAVA_VIVECRAFT_NOVR;
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
		if(isFloodgateEnabled() && FloodgateAPI.isBedrockPlayer(player)) {
			return FloodgateAPI.getPlayer(player).getVersion();
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
}
