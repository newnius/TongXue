package com.tongxue.client.Group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pcs.flyrefreshlib.FlyRefreshLayout;
import com.tongxue.client.Base.ActivityManager;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Login.IndexActivity;
import com.tongxue.client.Me.MeBlogActivity;
import com.tongxue.client.Me.MeQaActivity;
import com.tongxue.client.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/17.
 */
public class GroupMemInfoActivity extends BaseActivity implements FlyRefreshLayout.OnPullRefreshListener{
    @Bind(R.id.fly_layout)     FlyRefreshLayout mFlylayout;
    @Bind(R.id.layout1)        RelativeLayout layout1;
    @Bind(R.id.layout2)        RelativeLayout layout2;
    @Bind(R.id.layout3)        RelativeLayout layout3;
    @Bind(R.id.actionbar_back) ImageView  back;
    @Bind(R.id.myName)         TextView myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_mem_info);
        ButterKnife.bind(this);

        mFlylayout.setOnPullRefreshListener(this);

        String username = getIntent().getStringExtra("username");
        myName.setText(username);

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中");
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中");
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

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

}
