package com.tongxue.client.Main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongxue.client.Base.ActivityManager;
import com.tongxue.client.Base.BaseBarActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Group.Create.AuthActivity;
import com.tongxue.client.Group.GroupVerifyActivity;
import com.tongxue.client.Group.Search.SearchActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.SerializableMapList;
import com.tongxue.connector.Server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseBarActivity {
    @Bind(R.id.toolbar)         Toolbar toolbar;
    @Bind(R.id.drawerLayout)    DrawerLayout mDrawerLayout;
    @Bind(R.id.bottom_group)    LinearLayout bottom_group;
    @Bind(R.id.bottom_qa)       LinearLayout bottom_qa;
    @Bind(R.id.bottom_blog)     LinearLayout bottom_blog;
    @Bind(R.id.bottom_me)       LinearLayout bottom_me;
    @Bind(R.id.bottom_add)      LinearLayout bottom_add;
    @Bind(R.id.bottom_add_img)  ImageView addImg;
    @Bind(R.id.nv_menu)         NavigationView navigationView;
    @Bind(R.id.layout)          RelativeLayout head;
    @Bind(R.id.id_username)     TextView username;
    public FragmentManager mFragmentManager;
    public GroupFragment groupFragment;
    public QAFragment qaFragment;
    public BlogFragment blogFragment;
    public MeFragment meFragment;
    public WriteFragment writeFragment;
    public long mExitTime;
    public MenuInflater menuInflater;
    public Animation rightAnim;
    public Animation leftAnim;
    public boolean isAddPressed= false;
    public static List<Map<String,Object>> talkList;
    public static List<Map<String,Object>> groupList;
    public static List<Map<String,Object>> newList;
    public static List<Map<String,Object>> favList;
    public static List<Map<String,Object>> qaList;
    public static boolean shouldRefresh=true;
    public static boolean shouldRecord=true;
    public static boolean firstRefresh=true;
    public static boolean shouldBlogRefresh = true;
    public static boolean shouldQaRefresh = true;
    public int cur = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initAnim();

        Intent intent=getIntent();
        Serializable obj1= intent.getSerializableExtra("talkList");
        if(obj1== null){
            talkList= new ArrayList<>();
        }else{
            talkList= ((SerializableMapList)obj1).getMapList();
        }

        Serializable obj2= intent.getSerializableExtra("groupList");
        if(obj2== null){
            groupList= new ArrayList<>();
        }else{
            groupList= ((SerializableMapList)obj2).getMapList();
        }

        newList = new ArrayList<>();
        favList = new ArrayList<>();
        qaList = new ArrayList<>();

        mFragmentManager= getSupportFragmentManager();
        groupFragment= new GroupFragment();
        qaFragment= new QAFragment();
        blogFragment= new BlogFragment();
        meFragment= new MeFragment();
        writeFragment= new WriteFragment();

        final boolean toBlogFragment = intent.getBooleanExtra("blog",false);
        boolean toQaFragment = intent.getBooleanExtra("qa",false);

        if(toQaFragment){
            changeFragment(2);
            shouldQaRefresh = true;
        }else if(toBlogFragment){
            changeFragment(3);
            shouldBlogRefresh = true;
        }else{
            changeFragment(1);
        }

        bottom_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(1);
            }
        });

        bottom_qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changeFragment(2);
            }
        });

        bottom_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(3);
            }
        });

        bottom_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(4);
            }
        });

        bottom_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAddPressed){
                    changeFragment();
                }else{
                    addImg.startAnimation(rightAnim);
                    toolbar.setVisibility(View.GONE);
                    mFragmentManager.beginTransaction().replace(R.id.main_container_layout, writeFragment).commit();
                    isAddPressed = true;
                }
            }
        });

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                changeFragment(4);
            }
        });

        username.setText(LearnApplication.preferences.getString("username", ""));

        Server.getCourses();

    }

    public void initToolBar(){

        toolbar.setTitle("同学");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        final ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.order1:
                        item.setChecked(true);
                        break;
                    case R.id.order2:
                        toast("developing");
                        item.setChecked(true);
                        break;
                    case R.id.set:
                        toast("developing");
                        break;
                    case R.id.about:
                        toast("Powered by Super pan");
                        break;
                    case R.id.check:
                        toast("已是最新版本");
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    public void initAnim(){
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.right_spin);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_spin);
        rightAnim.setInterpolator(new LinearInterpolator());
        leftAnim.setInterpolator(new LinearInterpolator());
        rightAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onAnimationEnd(Animation animation) {
                addImg.setImageResource(R.drawable.close_blue);
            }
        });
        leftAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                addImg.setImageResource(R.drawable.add_blue);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menuInflater= getMenuInflater();
        if(cur == 1){
            menuInflater.inflate(R.menu.menu_main, menu);
        }else{
            menuInflater.inflate(R.menu.menu_com, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_msg:
                startIntent(GroupVerifyActivity.class);
                break;
            case R.id.create_group:
                startIntent(AuthActivity.class);
                break;
            case R.id.search_group:
                startIntent(SearchActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            makeToast(getString(R.string.before_quit));
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityManager.getActivityManager().popAllActivity();
        }
    }

    public void changeFragment(){
        changeFragment(cur);
    }

    public void changeFragment(int f){
        if(isAddPressed){
            addImg.startAnimation(leftAnim);
            isAddPressed = false;
        }
        if(f==1){
            cur = 1;
            mFragmentManager.beginTransaction().replace(R.id.main_container_layout, groupFragment).commit();
            invalidateOptionsMenu();
            changeStyle(bottom_group);
        }else if(f==2){
            cur = 2;
            mFragmentManager.beginTransaction().replace(R.id.main_container_layout, qaFragment).commit();
            invalidateOptionsMenu();
            changeStyle(bottom_qa);
        }else if(f==3){
            cur = 3;
            mFragmentManager.beginTransaction().replace(R.id.main_container_layout, blogFragment).commit();
            invalidateOptionsMenu();
            changeStyle(bottom_blog);
        }else if(f==4){
            cur = 4;
            mFragmentManager.beginTransaction().replace(R.id.main_container_layout, meFragment).commit();
            invalidateOptionsMenu();
            changeStyle(bottom_me);
        }
        toolbar.setVisibility(View.VISIBLE);
    }

    private void changeStyle(LinearLayout linearLayout) {
        Resources res = getResources();
        ((ImageView)bottom_group.getChildAt(0)).setColorFilter(res.getColor(R.color.m5));
        ((TextView)bottom_group.getChildAt(1)).setTextColor(res.getColor(R.color.mb));
        ((ImageView)bottom_qa.getChildAt(0)).setColorFilter(res.getColor(R.color.m5));
        ((TextView)bottom_qa.getChildAt(1)).setTextColor(res.getColor(R.color.mb));
        ((ImageView)bottom_blog.getChildAt(0)).setColorFilter(res.getColor(R.color.m5));
        ((TextView)bottom_blog.getChildAt(1)).setTextColor(res.getColor(R.color.mb));
        ((ImageView)bottom_me.getChildAt(0)).setColorFilter(res.getColor(R.color.m5));
        ((TextView)bottom_me.getChildAt(1)).setTextColor(res.getColor(R.color.mb));
        ((ImageView)linearLayout.getChildAt(0)).setColorFilter(res.getColor(R.color.lightblue2));
        ((TextView)linearLayout.getChildAt(1)).setTextColor(res.getColor(R.color.lightblue2));
    }

}
