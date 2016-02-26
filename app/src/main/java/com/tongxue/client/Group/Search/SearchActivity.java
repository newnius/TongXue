package com.tongxue.client.Group.Search;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
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
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.R;
import com.tongxue.client.Service.AVService;
import com.tongxue.client.Utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/18.
 */
public class SearchActivity extends BaseActivity {
    @Bind(R.id.actionbar_back)  ImageView back;
    @Bind(R.id.moreKind)        TextView moreKind;
    @Bind(R.id.moreGroup)       TextView moreGroup;
    @Bind(R.id.gridView)        GridView gridView;
    @Bind(R.id.listView)        ListView listView;
    @Bind(R.id.searchListView)  ListView searchListView;
    @Bind(R.id.bt_srarch)       Button btSrarch;
    @Bind(R.id.et_search)       EditText etSearch;
    @Bind(R.id.iv_clear)        ImageView ivClear;
    @Bind(R.id.toolbar)         RelativeLayout toolbar;
    @Bind(R.id.kind_head)       LinearLayout kindHead;
    @Bind(R.id.group_head)      LinearLayout groupHead;
    public GridAdapter kindAdapter;
    public SimpleAdapter groupAdapter, searchAdapter;
    public List<Map<String, Object>> kindList, groupList, searchList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
        ButterKnife.bind(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        moreKind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("暂无更多分类");
            }
        });

        moreGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("暂无更多推荐");
            }
        });

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        btSrarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key= etSearch.getText().toString().trim();
                if(btSrarch.getText().toString().trim().equals("取消")){
                    toolbar.setVisibility(View.VISIBLE);
                    kindHead.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.VISIBLE);
                    groupHead.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    ivClear.setVisibility(View.GONE);
                    btSrarch.setVisibility(View.GONE);
                    searchListView.setVisibility(View.GONE);
                }else{
                    searchByName(key);
                }
            }
        });

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.GONE);
                kindHead.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                groupHead.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                ivClear.setVisibility(View.VISIBLE);
                btSrarch.setVisibility(View.VISIBLE);
                searchListView.setVisibility(View.VISIBLE);
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    toolbar.setVisibility(View.GONE);
                    kindHead.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    groupHead.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    ivClear.setVisibility(View.VISIBLE);
                    btSrarch.setVisibility(View.VISIBLE);
                    searchListView.setVisibility(View.VISIBLE);
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key= etSearch.getText().toString().trim();
                if(key.equals("")){
                    btSrarch.setText("取消");
                    searchList.clear();
                    searchAdapter.notifyDataSetChanged();
                }else{
                    btSrarch.setText("搜索");
                    wakeSearchList(key);
                }
            }
        });

        kindList = new ArrayList<>();
        initKindList();
        kindAdapter = new GridAdapter(this, kindList);
        gridView.setAdapter(kindAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String kind= kindList.get(position).get("tv").toString();
                Intent intent= new Intent(SearchActivity.this, SKindActivity.class);
                intent.putExtra("kind", kind);
                startActivity(intent);
            }
        });

        groupList = new ArrayList<>();
        initGroupList();
        groupAdapter = new SimpleAdapter(this, groupList, R.layout.item_list_group_recom,
                new String[]{"img", "name", "kind", "intro"}, new int[]{R.id.image, R.id.name, R.id.tv_kind, R.id.tv_intro});
        listView.setAdapter(groupAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mid=(String)groupList.get(position).get("id");
                searchById(mid);
            }
        });

        searchList = new ArrayList<>();
        searchAdapter = new SimpleAdapter(this, searchList, R.layout.item_list_group_recom,
                new String[]{"img", "name", "kind", "intro"}, new int[]{R.id.image, R.id.name, R.id.tv_kind, R.id.tv_intro});
        searchListView.setAdapter(searchAdapter);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mid=(String)searchList.get(position).get("id");
                searchById(mid);
            }
        });
    }

    public void initKindList() {
        int img[] = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,
                R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8};
        String tv[] = getResources().getStringArray(R.array.groupKind);
        for (int i = 0; i < 8; i++) {
            Map<String, Object> map = new HashMap<>();
            Drawable drawable = getResources().getDrawable(img[i]);
            map.put("img", drawable);
            map.put("tv", tv[i]);
            kindList.add(map);
        }
    }

    public void initGroupList() {
        String username = LearnApplication.preferences.getString("username", "");
        AVService.getGroupList(username, new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (e != null) {
                    Log.i("learn", "Exception:" + e.getCode() + e.getMessage());
                } else {
                    groupList.clear();
                    int size = conversations.size();
                    for (int i = 0; i < size && i < 3; i++) {
                        Map<String, Object> map = new HashMap<>();
                        AVIMConversation conv = conversations.get(i);
                        map.put("name", conv.getAttribute("GroupName").toString());
                        map.put("kind", conv.getAttribute("kind").toString());
                        map.put("intro", conv.getAttribute("intro").toString());
                        map.put("id", conv.getConversationId());
                        map.put("img", Config.img[new Random().nextInt(50)]);
                        groupList.add(map);
                    }
                    groupAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void wakeSearchList(String key){
        String username= LearnApplication.preferences.getString("username","");
        AVIMClient imClient=AVIMClient.getInstance(username);
        AVIMConversationQuery query = imClient.getQuery();
        query.whereContains(ConversationType.ATTR_GNAME_KEY, key);
        query.orderByDescending("updatedAt");
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (e != null) {
                    toast("世界上最遥远的距离就是没网,请检查网络连接！");
                    Log.i("learn", "Exception:" + e.getCode() + e.getMessage());
                } else {
                    searchList.clear();
                    int index=0;
                    for(AVIMConversation conv: conversations){
                        Map<String, Object> map=new HashMap<>();
                        map.put("name", conv.getAttribute("GroupName").toString());
                        map.put("kind", conv.getAttribute("kind").toString());
                        map.put("intro", conv.getAttribute("intro").toString());
                        map.put("id", conv.getConversationId());
                        map.put("img", Config.img[index++]);
                        searchList.add(map);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void searchByName(final String key){
        waitingDialogShow();
        final String username= LearnApplication.preferences.getString("username","");
        AVIMClient imClient=AVIMClient.getInstance(username);
        AVIMConversationQuery query = imClient.getQuery();
        query.whereEqualTo(ConversationType.ATTR_GNAME_KEY, key);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (e != null) {
                    waitingDialogDismiss();
                    toast("世界上最遥远的距离就是没网,请检查网络连接！");
                    log("Exception:" + e.getCode() + e.getMessage());
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
                                waitingDialogDismiss();
                                if (e == null) {
                                    String result = "";
                                    if (avObjects.size() > 0) {
                                        AVObject object = avObjects.get(0);
                                        result = object.get("result").toString();
                                    }
                                    Log.i("learn", "result:" + result);
                                    Intent intent = new Intent(SearchActivity.this, SInfoActivity.class);
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
                                    Log.i("learn", conv.getConversationId() + " 、" + conv.getAttribute("GroupName").toString() + " 、" + conv.getAttribute("kind").toString() + " 、" + conv.getAttribute("autho").toString() + " 、" + conv.getAttribute("intro").toString() + "、 " + conv.getAttribute("date").toString());
                                    startActivity(intent);
                                } else {
                                    toast("网络故障");
                                    log(e.getCode()+e.toString());
                                }
                            }
                        });
                    } else {
                        waitingDialogDismiss();
                        toast("没有查到名为" + key + "的小组！");
                    }
                }
            }
        });
    }

    public void searchById(final String mid){
        waitingDialogShow();
        final String username= LearnApplication.preferences.getString("username","");
        AVIMClient imClient=AVIMClient.getInstance(username);
        AVIMConversationQuery query = imClient.getQuery();
        query.whereEqualTo("objectId", mid);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (e != null) {
                    waitingDialogDismiss();
                    toast("世界上最遥远的距离就是没网,请检查网络连接！");
                    Log.i("learn", "Exception:" + e.getCode() + e.getMessage());
                } else {
                    if (conversations.size() > 0) {
                        final AVIMConversation conv= conversations.get(0);
                        final boolean in=conv.getMembers().contains(username);
                        final String num = String.valueOf(conv.getMembers().size());
                        final String creator=conv.getCreator();

                        AVQuery<AVObject> query = new AVQuery<>("VerifyMsg");
                        query.whereEqualTo("creator", creator);
                        query.whereEqualTo("from",username);
                        query.whereEqualTo("to",conv.getAttribute("GroupName").toString());
                        query.findInBackground(new FindCallback<AVObject>() {
                            public void done(List<AVObject> avObjects, AVException e) {
                                waitingDialogDismiss();
                                if (e == null) {
                                    String result = "";
                                    if(avObjects.size()>0){
                                        AVObject object=avObjects.get(0);
                                        result= object.get("result").toString();
                                    }
                                    Intent intent=new Intent(SearchActivity.this, SInfoActivity.class);
                                    intent.putExtra("id",conv.getConversationId());
                                    intent.putExtra("name",conv.getAttribute("GroupName").toString());
                                    intent.putExtra("kind",conv.getAttribute("kind").toString());
                                    intent.putExtra("autho",conv.getAttribute("autho").toString());
                                    intent.putExtra("intro",conv.getAttribute("intro").toString());
                                    intent.putExtra("date",conv.getAttribute("date").toString());
                                    intent.putExtra("num", num);
                                    intent.putExtra("in",in);
                                    intent.putExtra("creator", creator);
                                    intent.putExtra("result", result);
                                    Log.i("learn", conv.getConversationId() + " 、" + conv.getAttribute("GroupName").toString() + " 、" + conv.getAttribute("kind").toString() + " 、" + conv.getAttribute("autho").toString() + " 、" + conv.getAttribute("intro").toString() + "、 " + conv.getAttribute("date").toString());
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

    class GridAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<Map<String, Object>> mAppList;

        public GridAdapter(Context c, List<Map<String, Object>> appList) {
            mInflater = LayoutInflater.from(c);
            mAppList = appList;
        }

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_grid_group_search, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.img.setImageDrawable((Drawable) mAppList.get(position).get("img"));
            holder.tv.setText(mAppList.get(position).get("tv").toString());

            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.img)
            ImageView img;
            @Bind(R.id.tv)
            TextView tv;

            public ViewHolder(View mView) {
                ButterKnife.bind(this, mView);
            }
        }
    }

}
