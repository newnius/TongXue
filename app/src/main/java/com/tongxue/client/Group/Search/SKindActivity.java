package com.tongxue.client.Group.Search;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avoscloud.leanchatlib.model.ConversationType;
import com.tongxue.client.Adapter.GroupAdapter;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/19.
 */
public class SKindActivity extends BaseActivity {
    @Bind(R.id.title)            TextView title;
    @Bind(R.id.actionbar_back)   ImageView back;
    @Bind(R.id.listView)         ListView listView;
    public String kind;
    public GroupAdapter adapter;
    public List<Map<String, Object>> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search_kind);
        ButterKnife.bind(this);
        kind = getIntent().getStringExtra("kind");
        title.setText(kind);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        list = new ArrayList<>();
        initList();
        log("size : "+list.size());
        adapter = new GroupAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mid=(String)list.get(position).get("id");
                searchById(mid);
            }
        });
    }

    public void initList() {
        String username = LearnApplication.preferences.getString("username", "");
        AVIMClient imClient = AVIMClient.getInstance(username);
        AVIMConversationQuery query = imClient.getQuery();
        query.whereEqualTo(ConversationType.ATTR_GKIND_KEY, kind);
        query.orderByDescending("updatedAt");
        waitingDialogShow();
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                list.clear();
                waitingDialogDismiss();
                if (e != null) {
                    toast("世界上最遥远的距离就是没网,请检查网络连接！");
                    log("Exception:" + e.getCode() + e.getMessage());
                } else {
                    if (conversations.size() > 0) {
                        for (AVIMConversation conv : conversations) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", conv.getAttribute("GroupName").toString());
                            map.put("kind", conv.getAttribute("kind").toString());
                            map.put("intro", conv.getAttribute("intro").toString());
                            map.put("id", conv.getConversationId());
                            Drawable drawable= getResources().getDrawable(Config.img[new Random().nextInt(50)]);
                            map.put("img", drawable);
                            list.add(map);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        toast("没有查到符合条件的小组");
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        initList();
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    public void searchById(String mid){
        waitingDialogShow();
        final String username= LearnApplication.preferences.getString("username","");
        AVIMClient imClient=AVIMClient.getInstance(username);
        AVIMConversationQuery query = imClient.getQuery();
        query.whereEqualTo("objectId", mid);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                list.clear();
                if (e != null) {
                    waitingDialogDismiss();
                    toast("世界上最遥远的距离就是没网,请检查网络连接！");
                    log("Exception:" + e.getCode() + e.getMessage());
                } else {
                    if (conversations.size() > 0) {
                        final AVIMConversation conv = conversations.get(0);
                        final boolean in=conv.getMembers().contains(username);
                        final String num = String.valueOf(conv.getMembers().size());
                        final String creator=conv.getCreator();

                        AVQuery<AVObject> query = new AVQuery<>("VerifyMsg");
                        query.whereEqualTo("creator", creator);
                        query.whereEqualTo("from", username);
                        query.whereEqualTo("to", conv.getAttribute("GroupName").toString());
                        query.findInBackground(new FindCallback<AVObject>() {
                            public void done(List<AVObject> avObjects, AVException e) {
                                waitingDialogDismiss();
                                if (e == null) {
                                    String result= "";
                                    if (avObjects.size() > 0) {
                                        AVObject object = avObjects.get(0);
                                        result = object.get("result").toString();
                                    }
                                    Intent intent = new Intent(SKindActivity.this, SInfoActivity.class);
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
                                    log(conv.getConversationId() + " 、" + conv.getAttribute("GroupName").toString() + " 、" + conv.getAttribute("kind").toString() + " 、" + conv.getAttribute("autho").toString() + " 、" + conv.getAttribute("intro").toString() + "、 " + conv.getAttribute("date").toString());
                                    startActivity(intent);
                                } else {
                                    toast("网络故障");
                                    log(e.getCode()+e.toString());
                                }
                            }
                        });
                    } else {
                        waitingDialogDismiss();
                        toast("网络故障");
                    }
                }
            }
        });
    }
}
