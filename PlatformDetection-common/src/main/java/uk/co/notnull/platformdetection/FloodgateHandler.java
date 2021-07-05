package uk.co.notnull.platformdetection;

public interface FloodgateHandler<P> {
	Platform getPlatform(P player);
	String getPlatformVersion(P player);
	boolean isFloodgatePlayer(P player);
}