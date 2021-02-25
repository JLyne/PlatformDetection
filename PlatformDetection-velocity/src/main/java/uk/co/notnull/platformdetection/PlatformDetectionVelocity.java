package uk.co.notnull.platformdetection;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.techjar.vbe.VivecraftAPI;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.geysermc.floodgate.FloodgateAPI;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Plugin(
		id = "platform-detection",
		name = "PlatformDetection",
		version = "1.0-SNAPSHOT",
		dependencies = {
				@Dependency(id = "floodgate", optional = true),
				@Dependency(id = "vivecraft-velocity-extensions", optional = true)
		}
)
public class PlatformDetectionVelocity implements PlatformDetectionPlugin<Player> {
	@Inject
	private Logger logger;

	@Inject
	private ProxyServer proxy;

	private boolean floodgateEnabled = false;
	private boolean vivecraftEnabled = false;

	private ConcurrentHashMap<Player, Platform> platforms;
	private final ChannelIdentifier platformChannel = MinecraftChannelIdentifier.create("platform", "brand");

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		platforms = new ConcurrentHashMap<>();
		floodgateEnabled = proxy.getPluginManager().isLoaded("floodgate");
		vivecraftEnabled = proxy.getPluginManager().isLoaded("vivecraft-velocity-extensions");

		proxy.getChannelRegistrar().register(platformChannel);
	}

	@Subscribe
	public void onPluginMessageEvent(PluginMessageEvent event) {
		// Received plugin message, check channel identifier matches
		if (event.getIdentifier().equals(platformChannel)) {
			// Since this message was meant for this listener set it to handled
			// We do this so the message doesn't get routed through.
			event.setResult(PluginMessageEvent.ForwardResult.handled());

			if (event.getSource() instanceof ServerConnection) {
				ServerConnection connection = (ServerConnection) event.getSource();

				// Read the data written to the message
				ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
				// Example:
				String message = in.readLine();

				if(message.contains("fabric")) {
					platforms.put(connection.getPlayer(), Platform.JAVA_FABRIC);
				} else if(message.contains("forge")) {
					platforms.put(connection.getPlayer(), Platform.JAVA_FORGE);
				} else if(message.contains("vanilla")) {
					platforms.put(connection.getPlayer(), Platform.JAVA);
				} else if(message.contains("lunarclient")) {
					platforms.put(connection.getPlayer(), Platform.JAVA_LUNAR);
				}
			}
		}
	}

	public boolean isFloodgateEnabled() {
		return floodgateEnabled;
	}

	public boolean isVivecraftEnabled() {
		return vivecraftEnabled;
	}

	public Platform getPlatform(Player player) {
		if(player == null || !player.isActive()) {
			return Platform.UNKNOWN;
		}

		Platform platform = platforms.compute(player, (Player key, Platform value) -> {
			if(value != null && !value.equals(Platform.JAVA)) {
				return value;
			}

			if(isFloodgateEnabled() && FloodgateAPI.isBedrockPlayer(player)) {
				return Platform.fromFloodgate(FloodgateAPI.getPlayer(player).getDeviceOS());
			}

			if(isVivecraftEnabled() && VivecraftAPI.isVive(player)) {
				return VivecraftAPI.isVR(player) ? Platform.JAVA_VIVECRAFT : Platform.JAVA_VIVECRAFT_NOVR;
			}

			return null;
		});

		return platform != null ? platform : Platform.JAVA;
	}

	public Platform getPlatform(UUID uuid) {
		Optional<Player> player = proxy.getPlayer(uuid);

		if(!player.isPresent()) {
			return Platform.UNKNOWN;
		}

		return getPlatform(player.get());
	}

	public String getPlatformVersion(Player player) {
		if(isFloodgateEnabled() && FloodgateAPI.isBedrockPlayer(player)) {
			return FloodgateAPI.getPlayer(player).getVersion();
		}

		return String.valueOf(player.getProtocolVersion().getName());
	}

	public String getPlatformVersion(UUID uuid) {
		if(uuid == null) {
			return null;
		}

		return proxy.getPlayer(uuid).map(this::getPlatformVersion).orElse(null);
	}
}
