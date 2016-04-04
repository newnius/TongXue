package com.tongxue.connector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongxue.connector.Objs.TXObject;

import java.util.List;

/**
 *
 * Created by newnius on 16-3-22.
 */
public class Discuss {
    public  static Msg createDiscuss(TXObject discuss){
        if (!discuss.hasKey("name"))
            return new Msg(ErrorCode.DISCUSS_NAME_IS_EMPTY);
        if (!discuss.hasKey("groupID"))
            return new Msg(ErrorCode.GROUP_ID_NOT_ASSIGNED);

        String con = new Gson().toJson(new Msg(RequestCode.CREATE_DISCUSS, discuss));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            TXObject discusstmp = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<TXObject>() {
            }.getType());
            msg.setObj(discusstmp);
        }
        return msg;
    }

    public static Msg getAllDiscusses(TXObject discuss){
        try {
            String con = new Gson().toJson(new Msg(RequestCode.GET_ALL_DISCUSSES, discuss));
            String res = Communicator.send(con);
            Msg msg;
            if (res == null) {
                msg = new Msg(ErrorCode.CONNECTION_FAIL);
            } else {
                msg = new Gson().fromJson(res, Msg.class);
                List<TXObject> discusses = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<TXObject>>() {
                }.getType());
                msg.setObj(discusses);
            }
            return msg;
        }catch(Exception ex){
            ex.printStackTrace();
            return new Msg(ErrorCode.UNKNOWN);
        }
    }

    public static Msg getDiscussByID(TXObject discuss){
        try {
            String con = new Gson().toJson(new Msg(RequestCode.GET_DISCUSS_BY_DISCUSS_ID, discuss));
            String res = Communicator.send(con);
            Msg msg;
            if (res == null) {
                msg = new Msg(ErrorCode.CONNECTION_FAIL);
            } else {
                msg = new Gson().fromJson(res, Msg.class);
                TXObject discusstmp = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<TXObject>() {
                }.getType());
                msg.setObj(discusstmp);
            }
            return msg;
        }catch(Exception ex){
            ex.printStackTrace();
            return new Msg(ErrorCode.UNKNOWN);
        }
    }



    public static Msg joinDiscuss(TXObject discuss){
        if (!discuss.hasKey("discussID"))
            return new Msg(ErrorCode.DISCUSS_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.JOIN_DISCUSS, discuss));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg quitDiscuss(TXObject discuss){
        if (!discuss.hasKey("discussID"))
            return new Msg(ErrorCode.DISCUSS_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.QUIT_DISCUSS, discuss));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg sendBoardAction(TXObject action){
        if(action == null)
            return new Msg(ErrorCode.INCOMPLETE_INFORMATION);
        if (!action.hasKey("discussID"))
            return new Msg(ErrorCode.DISCUSS_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.SEND_WHITEBOARD_ACTION, action));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg getBoardActions(TXObject discuss){
        if(discuss==null || !discuss.hasKey("discussID"))
            return new Msg(ErrorCode.DISCUSS_NOT_EXIST);
        try {
            String con = new Gson().toJson(new Msg(RequestCode.GET_WHITEBOARD_ACTION, discuss));
            String res = Communicator.send(con);
            Msg msg;
            if (res == null) {
                msg = new Msg(ErrorCode.CONNECTION_FAIL);
            } else {
                msg = new Gson().fromJson(res, Msg.class);
                List<TXObject> actions = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<TXObject>>() {
                }.getType());
                msg.setObj(actions);
            }
            return msg;
        }catch(Exception ex){
            ex.printStackTrace();
            return new Msg(ErrorCode.UNKNOWN);
        }
    }

    public static Msg sendDiscussMessage(TXObject message){
        if (message == null)
            return new Msg(ErrorCode.MESSAGE_IS_EMPTY);
        if (!message.hasKey("discussID"))
            return new Msg(ErrorCode.GROUP_NOT_EXIST);
        if (!message.hasKey("type"))
            return new Msg(ErrorCode.TYPE_IS_EMPTY);
        if (!message.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);
        String con = new Gson().toJson(new Msg(RequestCode.SEND_BOARD_MESSAGE, message));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg getDiscussMessage(TXObject discuss){
        if(!discuss.hasKey("discussID"))
            return new Msg(ErrorCode.GROUP_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.GET_BOARD_MESSAGE, discuss));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            List<TXObject> messages = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<TXObject>>() {}.getType());
            msg.setObj(messages);
        }
        return msg;
    }

}
