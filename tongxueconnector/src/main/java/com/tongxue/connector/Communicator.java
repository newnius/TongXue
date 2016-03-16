package com.tongxue.connector;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Newnius
 */
class Communicator {

	private static final String TAG="COMMUNICATE";
	private static Socket socket = null;
	private static  PrintWriter writer = null;
	private static BufferedReader reader = null;

	private static boolean init() {
		try {
			socket = new Socket(Config.getServerIP(), Config.getC2SPORT());
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
		} catch (Exception ex) {
            socket = null;
            writer = null;
            reader = null;
			Log.e(TAG, "Communicator part init fail.");
            return false;
		}
	}

	public static synchronized String send(String content) {
		if (socket == null || socket.isClosed()) {
			init();
		}
		try {
            Log.i(TAG, "Sent: " + content);
			writer.println(content);
			String str = reader.readLine();
			Log.i(TAG, "Received: " + str);
			return str;
		} catch (Exception ex) {
            socket = null;
            writer = null;
            reader = null;
            Log.e(TAG, "Error occur during close.");
			return null;
		}
	}


	public static void close() {
		try {
			writer.close();
			reader.close();
			socket.close();

			writer = null;
			reader = null;
			socket = null;
		} catch (IOException ex) {
            Log.e(TAG, "Error occur during close.");
		}
	}

}
