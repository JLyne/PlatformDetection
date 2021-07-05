package uk.co.notnull.platformdetection;

public interface VivecraftHandler<P> {
	Platform getPlatform(P player);
	boolean isVivecraftPlayer(P player);
}