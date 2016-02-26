package com.tongxue.client.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pcs.flyrefreshlib.FlyRefreshLayout;
import com.tongxue.client.Base.ActivityManager;
import com.tongxue.client.Base.BaseFragment;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Group.GroupVerifyActivity;
import com.tongxue.client.Login.IndexActivity;
import com.tongxue.client.Me.MeBlogActivity;
import com.tongxue.client.Me.MeQaActivity;
import com.tongxue.client.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/14.
 */
public class MeFragment extends BaseFragment implements FlyRefreshLayout.OnPullRefreshListener {
    @Bind(R.id.fly_layout)    FlyRefreshLayout mFlylayout;
    @Bind(R.id.layout1)       RelativeLayout layout1;
    @Bind(R.id.layout2)       RelativeLayout layout2;
    @Bind(R.id.layout3)       RelativeLayout layout3;
    @Bind(R.id.layout4)       RelativeLayout layout4;
    @Bind(R.id.layout5)       RelativeLayout layout5;
    @Bind(R.id.layout6)       RelativeLayout layout6;
    @Bind(R.id.layout7)       RelativeLayout layout7;
    @Bind(R.id.layout8)       RelativeLayout layout8;
    @Bind(R.id.layout9)       RelativeLayout layout9;
    @Bind(R.id.layout10)       RelativeLayout layout10;
    @Bind(R.id.myName)        TextView myName;
    public View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.init();
        mView = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, mView);

        mFlylayout.setOnPullRefreshListener(this);

        String username = LearnApplication.preferences.getString("username", "");
        myName.setText(username);

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MeBlogActivity.class));
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MeQaActivity.class));
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中");
            }
        });

        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中");
            }
        });

        layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中");
            }
        });

        layout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, GroupVerifyActivity.class));
            }
        });

        layout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中");
            }
        });

        layout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中");
            }
        });

        layout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager activityManager = ActivityManager.getActivityManager();
                activityManager.popAllActivity();
                Intent intent=new Intent(mContext, IndexActivity.class);
                startActivity(intent);
            }
        });

        layout10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager activityManager = ActivityManager.getActivityManager();
                activityManager.popAllActivity();
            }
        });

        return mView;
    }

    @Override
    public void onRefresh(FlyRefreshLayout view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mFlylayout!=null){
                    mFlylayout.onRefreshFinish();
                }
            }
        }, 2000);
    }

    @Override
    public void onRefreshAnimationEnd(FlyRefreshLayout view) {
        //TODO
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
