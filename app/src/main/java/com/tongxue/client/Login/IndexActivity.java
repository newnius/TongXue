package com.tongxue.client.Login;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.UpdateUtils;
import com.tongxue.client.Utils.Utils;
import com.tongxue.client.View.AlertDialog;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/5.
 */
public class IndexActivity extends BaseActivity{
    @Bind(R.id.layout) LinearLayout layout;
    @Bind(R.id.ImageView) ImageView image;
    public Map<String,String> updateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);

        image.setVisibility(View.GONE);


        new AsyncTask<Object,Integer,Integer>(){
            @Override
            protected Integer doInBackground(Object... params) {
                try {
                    URL url = new URL(getUpdateUrl());
                    HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    InputStream is =conn.getInputStream();
                    updateInfo =  UpdateUtils.getUpdataInfo(is);

                    if(updateInfo.get("version").equals(getCurrentVersion())){
                        return 1;
                    }else{
                        return 2;
                    }
                } catch (Exception e) {
                    return 0;
                }
            }

            @Override
            protected void onPostExecute(Integer msg) {
                super.onPostExecute(msg);
                switch (msg){
                    case 0:
                        Utils.log("网络错误，获取更新信息失败！");
                        toLogin();
                        break;
                    case 1:
                        Utils.log("版本号相同，无需更新");
                        toLogin();
                        break;
                    case 2:
                        Utils.log("版本号不同，提醒用户更新");
                        showUpdateDialog();             //提醒更新
                }
            }
        }.execute();


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void toLogin(){
        layout.setBackground(getResources().getDrawable(R.drawable.welcome_1));
        image.setVisibility(View.VISIBLE);
        startIntent(LoginActivity.class);
        overridePendingTransition(R.anim.in_login, R.anim.empty);
    }

    public void showUpdateDialog(){
        new AlertDialog(this,updateInfo.get("description"),"下次再更新","立即更新"){
            @Override
            public void onOkClick() {
                this.cancel();
                downLoadApk();
            }

            @Override
            public void onNoClick() {
                this.cancel();
                toLogin();
            }
        }.show();
    }

    protected void downLoadApk() {
        final ProgressDialog pd = new  ProgressDialog(this);              //进度条对话框
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = UpdateUtils.getFileFromServer(updateInfo.get("url"), pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss();                                        //结束掉进度条对话框
                    finishThisActivity();
                } catch (Exception e) {
                    Utils.log("新安装包下载错误！");
                    toLogin();
                }
            }}.start();
    }

    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }



    public String getCurrentVersion(){
        return getResources().getString(R.string.version);
    }

    public String getUpdateUrl(){
        return getResources().getString(R.string.update_url);
    }

}
