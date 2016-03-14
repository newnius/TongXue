package com.tongxue.client.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.tongxue.client.Adapter.GroupAdapter;
import com.tongxue.client.Adapter.TalkAdapter;
import com.tongxue.client.Base.BaseFragment;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Bean.GroupBean;
import com.tongxue.client.Bean.LatestGroup;
import com.tongxue.client.Group.GroupChatActivity;
import com.tongxue.client.R;
import com.tongxue.client.Service.AVService;
import com.tongxue.client.Utils.Config;
import com.tongxue.client.View.PullableView.PullToRefreshLayout;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/12.
 */
public class GroupFragment extends BaseFragment{
    @Bind(R.id.viewPager)  ViewPager viewPager;
    @Bind(R.id.tabLayout)  TabLayout tabLayout;
    public View mView;
    public Context mContext;
    public FragmentManager mFragmentManager;
    public List<View> mViewList;
    public List<String> mTitleList;
    public ListView talkListView, groupListView;
    public TalkAdapter talkAdapter;
    public GroupAdapter groupAdapter;
    public PullToRefreshLayout talkLayout, groupLayout;
    public List<Map<String, Object>> talkList, groupList;
    public int talkIndex = 15;
    public int groupIndex = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.init();
        mContext= getActivity();
        mFragmentManager= getActivity().getSupportFragmentManager();
        mView= inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, mView);

        mTitleList= new ArrayList<>();
        mViewList= new ArrayList<>();
        talkList = MainActivity.talkList;
        groupList = MainActivity.groupList;

        mTitleList.add("会话");
        mTitleList.add("小组");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View talkView = mInflater.inflate(R.layout.fragment_list, null);
        View groupView = mInflater.inflate(R.layout.fragment_list, null);
        mViewList.add(talkView);
        mViewList.add(groupView);

        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList, mTitleList);
        viewPager.setAdapter(mAdapter);                 //给ViewPager设置适配器
        tabLayout.setupWithViewPager(viewPager);        //将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);    //给Tabs设置适配器

        talkLayout= (PullToRefreshLayout)talkView;
        groupLayout= (PullToRefreshLayout)groupView;
        talkListView= (ListView)talkLayout.getChildAt(1);
        groupListView= (ListView)(groupLayout).getChildAt(1);

        talkAdapter= new TalkAdapter(mContext, talkList);
        talkListView.setAdapter(talkAdapter);
        talkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String groupName = (String)groupList.get(position).get("name");
                if (!TextUtils.isEmpty(groupName)) {
                    final ChatManager chatManager = ChatManager.getInstance();
                    chatManager.fetchConversationWithGroupName(groupName, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation conversation, AVException e) {
                            if (e != null) {
                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                chatManager.registerConversation(conversation);
                                Intent intent = new Intent(mContext, GroupChatActivity.class);
                                intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                                intent.putExtra("group_name",groupName);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
        talkLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getTalkData(1);
                    }
                },1000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        },0);

        groupAdapter=new GroupAdapter(mContext, groupList);
        groupListView.setAdapter(groupAdapter);
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String groupName = (String)groupList.get(position).get("name");
                if (!TextUtils.isEmpty(groupName)) {
                    final ChatManager chatManager = ChatManager.getInstance();
                    chatManager.fetchConversationWithGroupName(groupName, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation conversation, AVException e) {
                            if (e != null) {
                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                chatManager.registerConversation(conversation);
                                Intent intent = new Intent(mContext, GroupChatActivity.class);
                                intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                                intent.putExtra("group_name",groupName);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
        groupLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getGroupData(1);
                    }
                },1000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        },1);

        if(MainActivity.shouldRefresh||groupList.size()<=0){
            getTalkData(0);
            getGroupData(0);
            groupIndex = new Random().nextInt(20);
            talkIndex = groupIndex+15;
            log("should refresh");
            MainActivity.shouldRefresh= false;
        }else {
            if(MainActivity.firstRefresh){
                Log.i("learn", "已从本地数据库获取小组信息");
                getTalkData(0);
                getGroupData(0);
                MainActivity.firstRefresh=false;
            }else{
                Log.i("learn", "刷新上次已获取小组信息");
            }
        }

        return mView;
    }

    private void getTalkData(final int flag){
        new AsyncTask<Object, Integer, Integer>(){
            @Override
            protected Integer doInBackground(Object[] params) {
                try{
                    FinalDb db = FinalDb.create(mContext);
                    String username = LearnApplication.preferences.getString("username","");
                    List<LatestGroup> talkBeans= db.findAllByWhere(LatestGroup.class, "username=\"" + username + "\"");
                    log("talkBeans "+ talkBeans.size());

                    talkList.clear();
                    if(talkBeans.size()>0){
                        int size= talkBeans.size();
                        int index = talkIndex;
                        for(int i= size-1; i>=0&&i>=size-15; i--){
                            String groupName= talkBeans.get(i).getGroupName();
                            List<GroupBean> beans= db.findAllByWhere(GroupBean.class, "groupName=\""+ groupName + "\"");
                            Map<String, Object> map= new HashMap<>();
                            map.put("name",beans.get(0).getGroupName());
                            map.put("id",beans.get(0).getGroupId());
                            map.put("kind", beans.get(0).getKind());
                            map.put("intro",beans.get(0).getIntro());
                            map.put("num",""+ new Random().nextInt(20));
                            Drawable drawable= getResources().getDrawable(Config.img[index++]);
                            map.put("img", drawable);
                            talkList.add(map);
                        }
                        return 1;
                    }else{
                        return 2;
                    }
                }catch(Exception e){
                    log(e.getCause()+"---"+e.toString());
                    return 0;
                }
            }

            @Override
            protected void onPostExecute(Integer msg) {
                super.onPostExecute(msg);
                if(msg==0 && flag==1){
                    talkLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }else if(msg==1){
                    talkAdapter.notifyDataSetChanged();
                    if(flag==1) talkLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }else{
                    talkAdapter.notifyDataSetChanged();
                    Toast.makeText(mContext, "暂时没有近期会话", Toast.LENGTH_LONG).show();
                    if(flag==1) talkLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        }.execute();
    }

    private void getGroupData(final int flag){
        String username= LearnApplication.preferences.getString("username","");
        AVService.getGroupList(username, new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (e != null) {
                    Toast.makeText(mContext, "世界上最遥远的距离就是没网,请检查网络连接！", Toast.LENGTH_LONG).show();
                    Log.i("learn", "Exception:" + e.getCode() + e.getMessage());
                    if(flag==1){
                        groupLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                } else {
                    SharedPreferences.Editor editor = LearnApplication.preferences.edit();
                    editor.putInt("groupSize", conversations.size());
                    editor.apply();

                    groupList.clear();
                    if (conversations.size() > 0) {
                        log("size ： "+conversations.size());
                        int index = groupIndex;
                        for (AVIMConversation conv : conversations) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", conv.getAttribute("GroupName").toString());
                            map.put("kind", conv.getAttribute("kind").toString());
                            map.put("intro", conv.getAttribute("intro").toString());
                            map.put("id", conv.getConversationId());
                            Drawable drawable = getResources().getDrawable(Config.img[index++]);
                            map.put("img", drawable);
                            groupList.add(map);

                        }
                        log("List : "+groupList.size());
                        groupAdapter.notifyDataSetChanged();
                        if(flag==1){
                            groupLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }

                        if (MainActivity.shouldRecord) {
                            Log.i("learn", "记录到数据库啦！");
                            String username = LearnApplication.preferences.getString("username", "");
                            FinalDb db = FinalDb.create(mContext);
                            db.deleteByWhere(GroupBean.class, "username=\"" + username + "\"");
                            for (int i = 0; i < groupList.size(); i++) {
                                GroupBean bean = new GroupBean();
                                bean.setUsername(username);
                                bean.setGroupName(groupList.get(i).get("name").toString());
                                bean.setGroupId(groupList.get(i).get("id").toString());
                                bean.setKind(groupList.get(i).get("kind").toString());
                                bean.setIntro(groupList.get(i).get("intro").toString());
                                db.save(bean);
                            }
                            MainActivity.shouldRecord = false;
                        }

                    } else {
                        groupAdapter.notifyDataSetChanged();
                        Toast.makeText(mContext, "您暂时没有学习小组，赶快去添加一个小组吧！", Toast.LENGTH_LONG).show();
                        if(flag==1){
                            groupLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                        Log.i("learn", "没有小组，我把数据库的数据全删了吧!");
                        FinalDb db = FinalDb.create(mContext);
                        db.deleteAll(GroupBean.class);

                    }
                }
            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;
        private List<String> mTitleList;

        public MyPagerAdapter(List<View> mViewList, List<String> mTitleList) {
            this.mViewList = mViewList;
            this.mTitleList = mTitleList;
        }

        @Override
        public int getCount() {
            return mViewList.size();                             //页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;                              //官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));          //添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));      //删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);                    //页卡标题
        }

    }

}
