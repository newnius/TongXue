package com.tongxue.connector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Newnius
 */
public class Config {
	private static String ServerIP = "114.215.146.189";
	private static final int C2SPORT = 7096;  // accept connect from client, one response for one request
	private static final int S2CPORT = 32719; // client registers here, push msg from server
	private static final int FILEPORT = 32720; // port for file transfer
	private static boolean isClientWork = true;
	
	public static String getServerIP() {
		return ServerIP;
	}


	public static void setServerIP(String serverIP) {
		ServerIP = serverIP;
	}


	public static int getC2SPORT() {
		return C2SPORT;
	}
	
	
	public static int getS2CPORT() {
		return S2CPORT;
	}
	

    public static int getFileport() {
		return FILEPORT;
	}


	public static boolean isClientWork() {
		return isClientWork;
	}


	public static void setClientWork(boolean isClientWork) {
		Config.isClientWork = isClientWork;
	}
    
    
      
    
    
}
