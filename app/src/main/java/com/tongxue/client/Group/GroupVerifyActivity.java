package com.tongxue.client.Group;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tongxue.client.Adapter.VerifyAdapter;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Config;

/**
 * Created by chaosi on 2015/8/8.
 */
public class GroupVerifyActivity extends BaseActivity implements VerifyAdapter.VerifyInterface{

    public ImageView back;
    public ListView listView;
    public VerifyAdapter adapter;
    public List<Map<String, Object>> list;
    public final String username= LearnApplication.preferences.getString("username","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupverify);

        back=(ImageView)findViewById(R.id.back);
        listView=(ListView)findViewById(R.id.listView);

        list=new ArrayList<>();
        getData(true,null,null,0);
        adapter=new VerifyAdapter(this, this, list);
        listView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupVerifyActivity.this.finish();
            }
        });


    }

    private void getData(final boolean init, final TextView textView, final ProgressBar progressBar, final int size){
        AVQuery<AVObject> query = new AVQuery<>("VerifyMsg");
        query.whereEqualTo("creator", username);
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {

                if (e == null) {
                    list.clear();

                    for(int i=0; i<avObjects.size(); i++){
                        AVObject object = avObjects.get(i);
                        String id=object.getObjectId();
                        String from=object.get("from").toString();
                        String to=object.get("to").toString();
                        String toId=object.get("toId").toString();
                        String result=object.get("result").toString();

                        Map<String, Object> map=new HashMap<>();
                        map.put("id",id);
                        map.put("from",from);
                        map.put("to",to);
                        map.put("toId",toId);
                        map.put("text",from+"申请加入"+to);
                        Drawable drawable= getResources().getDrawable(Config.img[i]);
                        map.put("img", drawable);
                        if(result.equals("申请中")){
                            map.put("hasAgree",false);
                        }else{
                            map.put("hasAgree",true);
                        }
                        list.add(map);
                    }

                    adapter.notifyDataSetChanged();

                    if(!init){
                        if(size==list.size()){
                            makeToast("已无更多");
                        }
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    makeToast("网络错误，请检查网络");
                    log(e.getCode() + "  " + e.toString());
                    if(!init){
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void agree(final int position){
        String id=list.get(position).get("id").toString();
        final String from=list.get(position).get("from").toString();
        final String to=list.get(position).get("to").toString();
        final String toId=list.get(position).get("toId").toString();

        AVQuery<AVObject> query = new AVQuery<>("VerifyMsg");
        query.getInBackground(id, new GetCallback<AVObject>() {
            public void done(AVObject post, AVException e) {
                if (e == null) {
                    post.put("result","已申请");
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Map<String, Object> mMap= list.get(position);
                                mMap.put("hasAgree",true);
                                list.set(position, mMap);
                                adapter.notifyDataSetChanged();

                                AVIMClient imClient= AVIMClient.getInstance(username);
                                AVIMConversationQuery query = imClient.getQuery();
                                query.whereEqualTo("objectId",toId);
                                query.findInBackground(new AVIMConversationQueryCallback() {
                                    @Override
                                    public void done(List<AVIMConversation> conversations, AVException ee) {
                                        if(ee==null){
                                            if(conversations.size()>0){
                                                AVIMConversation conv=conversations.get(0);
                                                conv.addMembers(Arrays.asList(from), new AVIMConversationCallback() {
                                                    @Override
                                                    public void done(AVException eee) {
                                                        if(eee==null){
                                                            log(from+"成功加入"+to);
                                                        }else{
                                                            makeToast("网络故障");
                                                            log("eee:"+eee.getCode()+"  "+eee.toString());
                                                        }
                                                    }
                                                });
                                            }
                                        }else{
                                            makeToast("网络故障");
                                            log("ee:"+ee.getCode()+"  "+ee.toString());
                                        }
                                    }
                                });

                            } else {
                                makeToast("网络错误，请检查网络");
                                log("e:"+e.getCode() + "  " + e.toString());
                            }
                        }
                    });
                } else {
                    makeToast("网络错误，请检查网络");
                    log(e.getCode() + "  " + e.toString());
                }
            }
        });
    }

    @Override
    public void loadMore(final TextView textView, final ProgressBar progressBar){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(false, textView, progressBar, list.size());
            }
        },1000);

    }
}

