package com.tongxue.connector;

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
class Communicate {

	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	public Communicate() {
		try {
			socket = new Socket(Config.getServerIP(), Config.getC2SPORT());// 创建客户端套接字
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);// 获取输出流
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 获取输入流
																						// 获得服务器返回的数据
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized String send(String content) {
		if (socket == null || socket.isClosed()) {
			return null;
		}
		try {
			System.out.println("Server sent: " + content);
			writer.println(content);
			String str = reader.readLine();
			System.out.println("Received: " + str);
			return str;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	protected void finalize() {
		try {
			writer.close();
			reader.close();
			socket.close();

			writer = null;
			reader = null;
			socket = null;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
