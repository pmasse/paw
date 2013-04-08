package com.pmasse.web.service.example;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmasse.common.log.MyLogger;
import com.pmasse.web.http.server.HttpHandler;
import com.pmasse.web.http.server.HttpService;
import com.pmasse.web.service.base.status.StatusServer;
import com.pmasse.web.service.base.status.StatusServer.Status;

public class HelloWorld extends HttpHandler {
	
	private static Logger logger;
	private static StatusServer statusServer;
	
	public HelloWorld() {
		statusServer = new StatusServer(8002, "HelloWorld");
		statusServer.updateMetric("port", "8001");
		statusServer.setStatus(Status.UNKNOWN);
	}
	
	@Override
	protected void handleGet(ChannelHandlerContext ctx, HttpRequest request) {
		logger.log(Level.INFO, "GET request received");
		sendResponse(ctx, "text/plain; charset=UTF-8", getJSONResponse());
		logger.log(Level.INFO, "Get request completed");
	}
	
	private String getJSONResponse() {
		JsonObject obj = new JsonObject();
		obj.addProperty("version", 1);
		obj.addProperty("msg", "Hello World!");
		return new Gson().toJson(obj);
	}
	
	public void terminate() {
		statusServer = null;
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) {
		logger = MyLogger.getLogger("helloworld");
		
		logger.log(Level.INFO, "Service start");
		HttpService service = new HttpService(8001, new HelloWorld());
		service.start();

		statusServer.setStatus(Status.OK);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		statusServer.updateMetric("start_time", timeStamp);
		logger.log(Level.INFO, "Service running");
	}

}
