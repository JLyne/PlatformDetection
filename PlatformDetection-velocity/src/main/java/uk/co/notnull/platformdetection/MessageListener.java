package uk.co.notnull.platformdetection;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import org.slf4j.Logger;

public class MessageListener {

	private final ChannelIdentifier identifier;
	private final Logger logger;

	public MessageListener(ChannelIdentifier identifier, Logger logger) {
		this.identifier = identifier;
		this.logger = logger;
	}


}