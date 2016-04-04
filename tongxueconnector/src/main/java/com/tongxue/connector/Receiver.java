package com.tongxue.connector;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tongxue.connector.Objs.TXObject;


public class Receiver implements Runnable {

    private static Map<Integer, CallBackInterface> callbacks = new HashMap<>();
    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static TXObject user;
    private static final String TAG = "Receiver";

    public Receiver(TXObject user) {
        Receiver.user = user;
        Log.i(TAG, " started");
    }

    public static boolean attachCallback(int code, CallBackInterface cbi) {
        try {
            if (callbacks.containsKey(code))
                callbacks.remove(code);
            callbacks.put(code, cbi);
            Log.i(TAG, "Attach callback, code:" + code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static boolean detachCallback(int code) {
        callbacks.remove(code);
        Log.i(TAG, "Detach callback, code:" + code);
        return true;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(Config.getServerIP(), Config.getS2CPORT());
            socket.setKeepAlive(true);
            socket.setSoTimeout(0);
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            /* request to bind push */
            String str = new Gson().toJson(new Msg(91, user));
            Log.i(TAG, "Sent: " + str);
            writer.println(str);

            /* heart packet */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!writer.checkError()) {
                            Thread.sleep(5000);
                            writer.println();
                        }
                        Log.i(TAG, "Heart packet stopped.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            while (Config.isClientWork() && !socket.isClosed()) {
                str = reader.readLine();
                //connection closed
                if (str == null)
                    break;
                //received heart packet
                if (str.length() == 0)
                    continue;
                Log.i(TAG, "Received:" + str);
                Msg msg = new Gson().fromJson(str, Msg.class);
                if (callbacks.containsKey(msg.getCode())) {
                    callbacks.get(msg.getCode()).callBack(msg);
                }
            }
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void close() {
        try {
            Log.i(TAG, "Close socket");
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
