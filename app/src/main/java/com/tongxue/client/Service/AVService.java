package com.tongxue.client.Service;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaosi on 2015/9/16.
 */
public class AVService {

    public static void login(String username, String password, LogInCallback callback) {
        AVUser.logInInBackground(username, password, callback);
    }

    public static void getGroupList(String username, AVIMConversationQueryCallback callback){
        final List<String> members = new ArrayList<>();
        members.add(username);
        AVIMClient imClient = AVIMClient.getInstance(username);
        AVIMConversationQuery query = imClient.getQuery();
        query.containsMembers(members);
        query.orderByDescending("updatedAt");
        query.findInBackground(callback);
    }

    public static void cretaeGroup(String username,String groupName, String kind, String autho, String intro, AVIMConversationCreatedCallback createdCallback){

        String date= new SimpleDateFormat("yyyy年MM月dd日").format(new Date());

        List<String> clientIds = new ArrayList<>();
        clientIds.add(username);

        Map<String, Object> attr = new HashMap<>();
        attr.put("type", 0);
        attr.put("GroupName",groupName);
        attr.put("kind",kind);
        attr.put("autho",autho);
        attr.put("intro",intro);
        attr.put("date",date);

        AVIMClient imClient = AVIMClient.getInstance(username);
        imClient.createConversation(clientIds, attr, createdCallback);

    }

}
