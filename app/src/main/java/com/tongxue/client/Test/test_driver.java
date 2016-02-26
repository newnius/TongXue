package com.tongxue.client.Test;

import android.os.Bundle;

import com.tongxue.connector.LearnDriver;
import com.tongxue.client.R;
import com.tongxue.client.Base.BaseActivity;


public class test_driver extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new LearnDriver()).start();


    }

}


