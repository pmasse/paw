package com.pmasse.web.http.server;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

public class HttpHandler extends SimpleChannelUpstreamHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		HttpRequest req = (HttpRequest)e.getMessage();
		
		if (req.getMethod() == HttpMethod.GET) {
			handleGet(ctx, req);
		}
		else if (req.getMethod() == HttpMethod.POST) {
			handlePost(ctx, req);
		}
		else if (req.getMethod() == HttpMethod.PUT) {
			handlePut(ctx, req);
		}
		else if (req.getMethod() == HttpMethod.DELETE) {
			handleDelete(ctx, req);
		}
		else {
			sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
		}
	}
	
	protected void handleGet(ChannelHandlerContext ctx, HttpRequest request) {
	}
	protected void handlePost(ChannelHandlerContext ctx, HttpRequest request) {
	}
	protected void handlePut(ChannelHandlerContext ctx, HttpRequest request) {
	}
	protected void handleDelete(ChannelHandlerContext ctx, HttpRequest request) {
	}
	
	protected void sendResponse(ChannelHandlerContext ctx, String contentType, String content) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK);
		response.setContent(ChannelBuffers.copiedBuffer(content, CharsetUtil.UTF_8));
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE, contentType);
		
		Channel ch = ctx.getChannel();
		ch.write(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		
		Channel ch = e.getChannel();
		ch.close();
	}
	
	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; chaset=UTF-8");
		response.setContent(ChannelBuffers.copiedBuffer(
				"Failure: " + status.toString() + "\r\n",
				CharsetUtil.UTF_8));
		
		// Close the connection as soon as the error message is sent
		ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
	}
}
