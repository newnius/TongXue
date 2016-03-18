package com.tongxue.connector;

import com.google.gson.Gson;
import com.tongxue.connector.Objs.TXObject;

import java.util.Random;

/**
 * Created by newnius on 16-3-16.
 */
public class User {
    private User(){}

    public static Msg login(TXObject user) {
        if (!user.hasKey("username"))
            return new Msg(ErrorCode.USERNAME_IS_EMPTY);
        if (!user.hasKey("password"))
            return new Msg(ErrorCode.PASSWORD_IS_EMPTY);
        user.set("password", cryptPwd(user.get("password")));

        String con = new Gson().toJson(new Msg(RequestCode.LOGIN, user));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            if (msg.getCode() == ErrorCode.SUCCESS) {
                String obj = new Gson().toJson(msg.getObj());
                user = new Gson().fromJson(obj, TXObject.class);
                new Thread(new Receiver(user)).start();
            }
        }
        return msg;
    }

    public static Msg register(TXObject user) {
        if (!user.hasKey("username"))
            return new Msg(ErrorCode.USERNAME_IS_EMPTY);
        if (!user.hasKey("password"))
            return new Msg(ErrorCode.PASSWORD_IS_EMPTY);
        if(!user.hasKey("email"))
            return new Msg(ErrorCode.EMAIL_IS_EMPTY);
        user.set("password", cryptPwd(user.get("password")));

        String con = new Gson().toJson(new Msg(RequestCode.REGISTER, user));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }


    public static boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        if (email.length() > 45) {
            return false;
        }
        String regex = "[0-9A-Za-z\\-_\\.]+@[0-9a-z]+\\.edu(.cn)?";
        return email.matches(regex);
    }

    public static boolean validateUsername(String username) {
        if (username == null) {
            return false;
        }

        if (username.contains("@")) {
            return false;
        }

        if (username.length() < 1 || username.length() > 12) {
            return false;
        }
        return true;
    }


    public static String cryptPwd(String password) {
        String str = randomString(32);

        return password;
    }

    private static String randomString(int length) {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String str = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str += chars.charAt(random.nextInt(chars.length() - 1));
        }
        return str;
    }


}
