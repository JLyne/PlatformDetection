package uk.co.notnull.platformdetection;

import com.google.inject.Inject;
import com.velocitypowered.api.event.player.PlayerClientBrandEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class PlatformDetectionVelocity implements PlatformDetectionPlugin<Player> {
	@Inject
	private Logger logger;

	@Inject
	private ProxyServer proxy;

	private VivecraftHandlerVelocity vivecraftHandler;
	private FloodgateHandlerVelocity floodgateHandler;

	private ConcurrentHashMap<Player, Platform> platforms;
	private final ChannelIdentifier platformChannel = MinecraftChannelIdentifier.create("platform", "brand");

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		platforms = new ConcurrentHashMap<>();

		boolean floodgateEnabled = proxy.getPluginManager().isLoaded("floodgate");
		boolean vivecraftEnabled = proxy.getPluginManager().isLoaded("vivecraft-velocity-extensions");

		if(floodgateEnabled) {
			floodgateHandler = new FloodgateHandlerVelocity();
		}

		if(vivecraftEnabled) {
			vivecraftHandler = new VivecraftHandlerVelocity();
		}

		proxy.getChannelRegistrar().register(platformChannel);
	}

	@Subscribe
	public void onClientBrand(PlayerClientBrandEvent event) {
		String brand = event.getBrand();

		if(brand.contains("fabric")) {
			platforms.put(event.getPlayer(), Platform.JAVA_FABRIC);
		} else if(brand.contains("forge")) {
			platforms.put(event.getPlayer(), Platform.JAVA_FORGE);
		} else if(brand.contains("vanilla")) {
			platforms.put(event.getPlayer(), Platform.JAVA);
		} else if(brand.contains("lunarclient")) {
			platforms.put(event.getPlayer(), Platform.JAVA_LUNAR);
		} else if(brand.contains("quilt")) {
			platforms.put(event.getPlayer(), Platform.JAVA_QUILT);
		}
	}

	public Platform getPlatform(Player player) {
		if(player == null || !player.isActive()) {
			return Platform.UNKNOWN;
		}

		Platform platform = platforms.compute(player, (Player key, Platform value) -> {
			if(value != null && !value.equals(Platform.JAVA)) {
				return value;
			}

			if(isFloodgateEnabled() && floodgateHandler.isFloodgatePlayer(player)) {
				return floodgateHandler.getPlatform(player);
			}

			if(isVivecraftEnabled() && vivecraftHandler.isVivecraftPlayer(player)) {
				return vivecraftHandler.getPlatform(player);
			}

			return null;
		});

		return platform != null ? platform : Platform.JAVA;
	}

	public Platform getPlatform(UUID uuid) {
		Optional<Player> player = proxy.getPlayer(uuid);

		if(player.isEmpty()) {
			return Platform.UNKNOWN;
		}

		return getPlatform(player.get());
	}

	public String getPlatformVersion(Player player) {
		if(isFloodgateEnabled() && floodgateHandler.isFloodgatePlayer(player)) {
			return floodgateHandler.getPlatformVersion(player);
		}

		return String.valueOf(player.getProtocolVersion().getVersionIntroducedIn());
	}

	public String getPlatformVersion(UUID uuid) {
		if(uuid == null) {
			return null;
		}

		return proxy.getPlayer(uuid).map(this::getPlatformVersion).orElse(null);
	}

	@Override
	public boolean isFloodgateEnabled() {
		return floodgateHandler != null;
	}

	public boolean isVivecraftEnabled() {
		return vivecraftHandler != null;
	}
}
