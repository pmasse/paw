package com.pmasse.web.http.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

public class HttpService {
	
	private int port;
	private SimpleChannelUpstreamHandler handler;
	
	public HttpService(int port, SimpleChannelUpstreamHandler handler) {
		this.port = port;
		this.handler = handler;
	}
	
	public void start() {
		ChannelFactory factory = 
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool());
		
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		
		setPipelineFactory(bootstrap);
		
		setOptions(bootstrap);
		
		bootstrap.bind(new InetSocketAddress(port));
	}
	
	protected void setOptions(ServerBootstrap bootstrap) {
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
	}
	
	protected void setPipelineFactory(ServerBootstrap bootstrap) {
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new HttpRequestDecoder());
				// pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
				pipeline.addLast("encoder", new HttpResponseEncoder());
				pipeline.addLast("handler", handler);
				return pipeline;
			}
		});
	}

}
