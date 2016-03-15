package com.tongxue.client.Group.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Group.GroupMemActivity;
import com.tongxue.client.Main.MainActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Config;
import com.tongxue.client.Utils.Utils;
import com.tongxue.client.View.PullScrollView;
import com.tongxue.client.View.RoundImageView;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/19.
 */
public class SInfoActivity extends BaseActivity implements PullScrollView.OnTurnListener {


    @Bind(R.id.bgImg)           ImageView bgImg;
    @Bind(R.id.scrollView)      PullScrollView scrollView;
    @Bind(R.id.actionbar_back)  ImageView back;
    @Bind(R.id.group_name)      TextView tv_groupName;
    @Bind(R.id.group_intro)     TextView tv_groupIntro;
    @Bind(R.id.group_avatar)    RoundImageView iv_groupAvatar;
    @Bind(R.id.groupId)         TextView tv_groupId;
    @Bind(R.id.groupCreator)    TextView tv_groupCreator;
    @Bind(R.id.groupKind)       TextView tv_groupKind;
    @Bind(R.id.groupSize)       TextView tv_groupSize;
    @Bind(R.id.groupTime)       TextView tv_groupTime;
    @Bind(R.id.add)             Button bt_add;
    @Bind(R.id.groupMem)        RelativeLayout groupMem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search_info);
        ButterKnife.bind(this);

        scrollView.setHeader(bgImg);
        scrollView.setOnTurnListener(this);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        final String name = intent.getStringExtra("name");
        String kind = intent.getStringExtra("kind");
        final String autho = intent.getStringExtra("autho");
        String intro = intent.getStringExtra("intro");
        String num = intent.getStringExtra("num");
        String date = intent.getStringExtra("date");
        boolean in = intent.getBooleanExtra("in", false);
        final String creator=intent.getStringExtra("creator");
        String result=intent.getStringExtra("result");

        tv_groupName.setText(name);
        tv_groupIntro.setText("简介："+intro);
        tv_groupId.setText(id);
        tv_groupCreator.setText(creator);
        tv_groupKind.setText(kind);
        tv_groupSize.setText(num);
        tv_groupTime.setText(date);

        if(in){
            bt_add.setText("已加入");
        }else if(result.equals("申请中")){
            bt_add.setText("申请中");
        }else if(autho.equals("拒绝所有申请")){
            bt_add.setText("拒绝所有申请");
        }else{
            bt_add.setText("申请加入");
        }
        iv_groupAvatar.setImageDrawable(getResources().getDrawable(Config.img[new Random().nextInt(50)]));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = bt_add.getText().toString().trim();
                int groupSize = LearnApplication.preferences.getInt("groupSize", 0);
                if(text.equals("已加入")){
                    toast("您已加入该小组");
                }else if(text.equals("申请中")){
                    toast("您已经发过申请，请耐心等待组长审核");
                }else if(text.equals("拒绝所有申请")){
                    toast("该小组拒绝所有申请，您无法加入");
                }else if(groupSize >= 10){
                    toast("您加入的小组已达上限，不能继续加入小组");
                }else{
                    String username = LearnApplication.preferences.getString("username", "");
                    AVIMClient imClient=AVIMClient.getInstance(username);
                    if(autho.equals("接受所有申请")){
                        waitingDialogShow();
                        AVIMConversationQuery query = imClient.getQuery();
                        query.whereEqualTo("objectId",id);
                        query.findInBackground(new AVIMConversationQueryCallback() {
                            @Override
                            public void done(List<AVIMConversation> conversations, AVException e) {
                                if (e != null) {
                                    makeToast("世界上最遥远的距离就是没网,请检查网络连接！");
                                    waitingDialogDismiss();
                                    log("Exception:" + e.getCode() + e.getMessage());
                                } else {
                                    if (conversations.size() > 0) {
                                        AVIMConversation conv= conversations.get(0);
                                        conv.join(new AVIMConversationCallback(){
                                            @Override
                                            public void done(AVException e) {
                                                if (null != e) {
                                                    makeToast("世界上最遥远的距离就是没网,请检查网络连接！");
                                                    waitingDialogDismiss();
                                                    log("Exception:" + e.getCode() + e.getMessage());
                                                } else {
                                                    waitingDialogDismiss();
                                                    makeToast("加入成功！");
                                                    Intent intent = new Intent(SInfoActivity.this, MainActivity.class);
                                                    intent.putExtra("shouldRefresh",true);
                                                    MainActivity.shouldRecord=true;
                                                    SInfoActivity.this.startActivity(intent);
                                                    SInfoActivity.this.finish();
                                                }
                                            }
                                        });
                                    } else {
                                        makeToast("网络故障");
                                        waitingDialogDismiss();
                                    }
                                }
                            }
                        });
                    }else if(autho.equals("验证后接受申请")){

                        AVObject post = new AVObject("VerifyMsg");
                        post.put("creator", creator);
                        post.put("from", username);
                        post.put("to", name);
                        post.put("toId",id);
                        post.put("result", "申请中");
                        post.saveInBackground(new SaveCallback() {
                            public void done(AVException e) {
                                if (e == null) {
                                    makeToast("已发出申请，等待组长验证");
                                    bt_add.setText("申请中");
                                } else {
                                    switch (e.getCode()){
                                        case 1:
                                            makeToast("服务器内部错误，稍后再试");
                                            log(e.getCode()+"  "+e.toString());
                                            break;
                                        default:
                                            makeToast("网络错误，请稍后再试！");
                                            log(e.getCode()+"  "+e.toString());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        groupMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitingDialogShow();
                final String username= LearnApplication.preferences.getString("username","");
                AVIMClient imClient=AVIMClient.getInstance(username);
                AVIMConversationQuery query = imClient.getQuery();
                query.whereEqualTo("objectId", id);
                query.findInBackground(new AVIMConversationQueryCallback() {
                    @Override
                    public void done(List<AVIMConversation> conversations, AVException e) {
                        if (e != null) {
                            waitingDialogDismiss();
                            toast("世界上最遥远的距离就是没网,请检查网络连接！");
                            log("Exception:" + e.getCode() + e.getMessage());
                        } else {
                            if (conversations.size() > 0) {
                                waitingDialogDismiss();

                                final AVIMConversation conv = conversations.get(0);
                                List<String> mems = conv.getMembers();
                                Intent intent = new Intent(SInfoActivity.this, GroupMemActivity.class);
                                intent.putExtra("memIds", (Serializable) mems);
                                startActivity(intent);
                                overridePendingTransition(R.anim.common_right_in, R.anim.common_left_out);

                            } else {
                                waitingDialogDismiss();
                                toast("网络故障");
                            }
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onTurn() {

    }
}
