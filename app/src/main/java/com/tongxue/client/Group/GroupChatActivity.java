package com.tongxue.client.Group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.tongxue.client.Base.ActivityManager;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Bean.LatestGroup;
import com.tongxue.client.Group.Search.SInfoActivity;
import com.tongxue.client.Discuss.WhiteBoardActivity;
import com.tongxue.client.Main.MainActivity;

import net.tsz.afinal.FinalDb;
import com.tongxue.client.R;
import com.tongxue.client.View.WaitingDialog;

import java.util.List;

/**
 * Created by chaosi on 2015/7/20.
 */

public class GroupChatActivity extends ChatActivity {
    public String groupName;
    public String groupId;
    public String username;
    public WaitingDialog waitingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager activityManager = ActivityManager.getActivityManager();
        activityManager.pushActivity(this);

        addLocationBtn.setVisibility(View.VISIBLE);
        username = LearnApplication.preferences.getString("username","");
        groupName = getIntent().getStringExtra("group_name");
        groupId = getIntent().getStringExtra(ChatActivity.CONVID);
        String filename = getIntent().getStringExtra("filename");
        threatFileName(filename);
        waitingDialog = new WaitingDialog(this);
    }

    public void threatFileName(String filename){
        if(filename != null && !filename.equals("")){
            String path = Environment.getExternalStorageDirectory().getPath()+"/Learn/"+filename+".png";
            messageAgent.sendImage(path);
        }
    }

    @Override
    public void onBackClick(){
        Intent intent= new Intent(GroupChatActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.common_left_in, R.anim.common_right_out);
    }

    @Override
    public void onInfoClick(){
        searchById(groupId);
    }

    @Override
    public void onMessageClick() {
        /* lack of discussID, this can not work */
        //startActivity(new Intent(GroupChatActivity.this, WhiteBoardActivity.class));
        overridePendingTransition(R.anim.common_right_in, R.anim.empty);
        finish();
    }

    @Override
    public void sendText() {
        super.sendText();
        FinalDb db = FinalDb.create(this);
        db.deleteByWhere(LatestGroup.class, "groupName=\"" + groupName + "\" and username=\"" + username + "\"");
        LatestGroup bean= new LatestGroup();
        bean.setUsername(username);
        bean.setGroupName(groupName);
        db.save(bean);
        MainActivity.shouldRefresh= true;
    }

    @Override
    public void addWhiteBoard(){
        /* lack of discussID, this can not work */
        //sendWhiteBoard("您的好友"+ username +"发起白板共享，快去看看吧");
        startActivity(new Intent(GroupChatActivity.this, WhiteBoardActivity.class));
        overridePendingTransition(R.anim.common_right_in, R.anim.empty);
        finish();
    }

    @Override
    protected void onAddLocationButtonClicked(View v) {
        toast("这里可以跳转到地图界面，选取地址");
    }

    @Override
    protected void onLocationMessageViewClicked(AVIMLocationMessage locationMessage) {
        toast("onLocationMessageViewClicked");
    }

    public void searchById(String mid) {
        waitingDialog.show();
        final String username = LearnApplication.preferences.getString("username", "");
        AVIMClient imClient = AVIMClient.getInstance(username);
        AVIMConversationQuery query = imClient.getQuery();
        query.whereEqualTo("objectId", mid);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (e != null) {
                    waitingDialog.dismiss();
                    toast("世界上最遥远的距离就是没网,请检查网络连接！");
                    Log.i("learn", "Exception:" + e.getCode() + e.getMessage());
                } else {
                    if (conversations.size() > 0) {
                        final AVIMConversation conv = conversations.get(0);
                        final boolean in = conv.getMembers().contains(username);
                        final String num = String.valueOf(conv.getMembers().size());
                        final String creator = conv.getCreator();

                        AVQuery<AVObject> query = new AVQuery<>("VerifyMsg");
                        query.whereEqualTo("creator", creator);
                        query.whereEqualTo("from", username);
                        query.whereEqualTo("to", conv.getAttribute("GroupName").toString());
                        query.findInBackground(new FindCallback<AVObject>() {
                            public void done(List<AVObject> avObjects, AVException e) {
                                waitingDialog.dismiss();
                                if (e == null) {
                                    String result = "";
                                    if (avObjects.size() > 0) {
                                        AVObject object = avObjects.get(0);
                                        result = object.get("result").toString();
                                    }
                                    Intent intent = new Intent(GroupChatActivity.this, SInfoActivity.class);
                                    intent.putExtra("id", conv.getConversationId());
                                    intent.putExtra("name", conv.getAttribute("GroupName").toString());
                                    intent.putExtra("kind", conv.getAttribute("kind").toString());
                                    intent.putExtra("autho", conv.getAttribute("autho").toString());
                                    intent.putExtra("intro", conv.getAttribute("intro").toString());
                                    intent.putExtra("date", conv.getAttribute("date").toString());
                                    intent.putExtra("num", num);
                                    intent.putExtra("in", in);
                                    intent.putExtra("creator", creator);
                                    intent.putExtra("result", result);
                                    startActivity(intent);
                                } else {
                                    toast("网络故障");
                                    Log.i("learn", e.getCode() + e.toString());
                                }
                            }
                        });
                    } else {
                        waitingDialog.dismiss();
                        toast("网络故障");
                    }
                }
            }
        });
    }
}
