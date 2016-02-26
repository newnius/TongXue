package com.tongxue.client.Group.Create;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/17.
 */
public class AuthActivity extends BaseActivity {
    @Bind(R.id.publicAuth)       RelativeLayout publicLayout;
    @Bind(R.id.protectAuth)      RelativeLayout protectLayout;
    @Bind(R.id.privateAuth)      RelativeLayout privateLayout;
    @Bind(R.id.actionbar_back)   ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_auth);
        ButterKnife.bind(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        publicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AuthActivity.this, KindActivity.class);
                intent.putExtra("autho", "接受所有申请");
                startActivity(intent);
                overridePendingTransition(R.anim.common_right_in, R.anim.empty);
            }
        });

        protectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AuthActivity.this, KindActivity.class);
                intent.putExtra("autho", "验证后接受申请");
                startActivity(intent);
                overridePendingTransition(R.anim.common_right_in, R.anim.empty);
            }
        });

        privateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AuthActivity.this, KindActivity.class);
                intent.putExtra("autho", "拒绝所有申请");
                startActivity(intent);
                overridePendingTransition(R.anim.common_right_in, R.anim.empty);
            }
        });

    }
}
