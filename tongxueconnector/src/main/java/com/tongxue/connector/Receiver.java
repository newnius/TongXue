package com.tongxue.connector;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tongxue.connector.Objs.TXObject;


public class Receiver implements Runnable {

	private static Map<Integer, CallBackInterface> callbacks;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private TXObject user;

	public Receiver(TXObject user) {
		this.user = user;
        Log.i("receiver", "running");
	}

	public static boolean attachCallback(int code, CallBackInterface cbi){
        if(callbacks == null)
            callbacks = new HashMap<>();
        try {
            if (callbacks.containsKey(code))
                ;
            else callbacks.put(code, cbi);
        }catch (Exception ex){
            ex.printStackTrace();
        }
		return true;
	}

	public static boolean detachCallback(int code){
		callbacks.remove(code);
		return true;
	}

	@Override
	public void run() {
		try {
			socket = new Socket(Config.getServerIP(), Config.getS2CPORT());// 创建客户端套接字
			socket.setKeepAlive(true);
			socket.setSoTimeout(0);
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);// 获得客户端输出流)
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 获取输入流

			String str = new Gson().toJson(new Msg(91, user));
            Log.i("Receiver", "sent: " + str);
			writer.println(str);
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
                            Thread.sleep(5000);
							writer.println();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}).start();

			while (Config.isClientWork() && socket.isConnected()) {
				str = reader.readLine();
				if (str == null || str.length()==0)
					continue;
                Log.i("Receiver","received:" + str);
				Msg msg = new Gson().fromJson(str, Msg.class);
                if(callbacks.containsKey(msg.getCode()))
                    callbacks.get(msg.getCode()).callBack(msg);
			}

			socket.close();
		} catch (

				Exception e)

		{
			e.printStackTrace();
		}
	}

}
