package com.tongxue.client.Test;

import android.os.Bundle;
import android.webkit.WebView;

import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.R;

/**
 * Created by chaosi on 2015/10/8.
 */
public class test_webview extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testwebview);

        WebView webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl("http://write.blog.csdn.net/postedit");
    }
}
