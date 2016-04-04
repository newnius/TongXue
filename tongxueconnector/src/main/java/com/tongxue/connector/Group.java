package com.tongxue.connector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongxue.connector.Objs.TXObject;

import java.util.List;

/**
 *
 * Created by newnius on 16-3-16.
 */
public class Group {
    /*
    private int groupID;
    private String groupName;
    private int category;
    private String introduction;
    private String icon;
    private int pub;
    private long time;
    */

    /*
    	private int cid = 0;
	private int type = 0;
	private int topicID = 0;
	private int groupID = 0;
	private String username = null;
	private String content = null;
	private long time = 0;
     */

    private Group() {
    }

    public static Msg searchGroup(TXObject group) {
        String con = new Gson().toJson(new Msg(RequestCode.SEARCH_GROUP, group));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            List<TXObject> groups = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<TXObject>() {}.getType());
            msg.setObj(groups);
        }
        return msg;
    }

    public static Msg createGroup(TXObject group) {
        if (!group.hasKey("groupName"))
            return new Msg(ErrorCode.GROUP_NAME_IS_EMPTY);
        if (!group.hasKey("category"))
            return new Msg(ErrorCode.CATEGORY_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.CREATE_GROUP, group));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg updateGroup(TXObject group) {
        if (!group.hasKey("groupID"))
            return new Msg(ErrorCode.GROUP_NOT_EXIST);
        if (!group.hasKey("groupName"))
            return new Msg(ErrorCode.GROUP_NAME_IS_EMPTY);
        if (!group.hasKey("category"))
            return new Msg(ErrorCode.CATEGORY_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.UPDATE_GROUP, group));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg dismissGroup(TXObject group) {
        if (!group.hasKey("groupID"))
            return new Msg(ErrorCode.GROUP_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.DISMISS_GROUP, group));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg searchGroupByUser(TXObject user){
        if(!user.hasKey("username"))
            return new Msg(ErrorCode.USERNAME_IS_EMPTY);
        String con = new Gson().toJson(new Msg(RequestCode.SEARCH_GROUP_BY_USER, user));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            List<TXObject> groups = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<TXObject>() {}.getType());
            msg.setObj(groups);
        }
        return msg;
    }

    public static Msg applyGroup(TXObject group){
        if(!group.hasKey("groupID"))
            return new Msg(ErrorCode.GROUP_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.APPLY_GROUP, group));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg quitGroup(TXObject group){
        if(!group.hasKey("groupID"))
            return new Msg(ErrorCode.GROUP_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.QUIT_GROUP, group));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg sendGroupMessage(TXObject message){
        if(!message.hasKey("type"))
            return new Msg(ErrorCode.TYPE_IS_EMPTY);
        if(!message.hasKey("groupID"))
            return new Msg(ErrorCode.GROUP_NOT_EXIST);
        if(!message.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);
        String con = new Gson().toJson(new Msg(RequestCode.SEND_GROUP_MESSAGE, message));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg getGroupMessage(TXObject group){
        if(!group.hasKey("groupID"))
            return new Msg(ErrorCode.GROUP_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.GET_GROUP_MESSAGE, group));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            List<TXObject> messages = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<TXObject>() {}.getType());
            msg.setObj(messages);
        }
        return msg;
    }


}
