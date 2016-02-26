package com.tongxue.client.Login;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/5.
 */
public class IndexActivity extends BaseActivity{
    @Bind(R.id.layout) LinearLayout layout;
    @Bind(R.id.ImageView) ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);

        image.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                layout.setBackground(getResources().getDrawable(R.drawable.welcome_1));
                image.setVisibility(View.VISIBLE);
                startIntent(LoginActivity.class);
                overridePendingTransition(R.anim.in_login,R.anim.empty);
            }
        },500);

    }

}
