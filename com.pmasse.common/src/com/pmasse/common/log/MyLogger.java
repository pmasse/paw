package com.pmasse.common.log;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {
	
	private final static String logRootPath = "/var/log/pmasse";
	
	public static Logger getLogger(String logname) {
		Logger logger = Logger.getLogger(logname);
		
		try {			
			// @TODO put log path in configuration
			// @TODO create directory if it doesn't exist (Instead of throwing an exception)
			FileHandler fh;
			fh = new FileHandler(logRootPath + "/" + logname + "/" + logname + ".log");
	
			logger.addHandler(fh);
			// set level to all until we know otherwise from configuration
			logger.setLevel(Level.ALL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Can't get to the disk. We get out.
			// Propagating an exception would just force everyone to deal with it
			// for no good reason
			System.exit(-2);
		}
		return logger;
	}

}
