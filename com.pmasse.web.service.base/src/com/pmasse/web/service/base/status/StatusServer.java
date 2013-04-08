package com.pmasse.web.service.base.status;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pmasse.common.log.MyLogger;
import com.pmasse.web.http.server.HttpHandler;
import com.pmasse.web.http.server.HttpService;

public class StatusServer extends HttpHandler {
	private static Logger logger;
	public enum Status {
		OK, WARNING, ERROR, UNKNOWN
	}
	
	private Status status = Status.UNKNOWN;
	private String serviceName;
	private ConcurrentHashMap<String, Object> statusInfo = new ConcurrentHashMap<String, Object>();
	
	public StatusServer(int port, String serviceName) {
		this.serviceName = serviceName;
		logger = MyLogger.getLogger("statusserver");
		
		logger.log(Level.INFO, "Status Service start");
		HttpService service = new HttpService(port, this);
		
		service.start();
		logger.log(Level.INFO, "Status Service running");
	}
	
	public void updateMetric(String metric, Object value) {
		statusInfo.put(metric, value);
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	protected void handleGet(ChannelHandlerContext ctx, HttpRequest request) {
		logger.log(Level.INFO, "GET request received");
		sendResponse(ctx, "application/json; charset=UTF-8", getJSONResponse());
		logger.log(Level.INFO, "Get request completed");
	}
	
	private String getJSONResponse() {
		Gson gson = new GsonBuilder().create();
		
		JsonObject obj = new JsonObject();
		obj.addProperty("version", 1);
		obj.addProperty("status", gson.toJson(status));
		obj.addProperty("service_name", serviceName);
		
		JsonElement info = gson.toJsonTree(statusInfo);
		obj.add("statusinfo", info);
		return new Gson().toJson(obj);
	}
}
