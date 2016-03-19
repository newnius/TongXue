package com.tongxue.client.Login;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.UpdateController;
import com.tongxue.client.Utils.Utils;
import com.tongxue.client.View.AlertDialog;
import com.tongxue.connector.ErrorCode;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/5.
 */
public class IndexActivity extends BaseActivity {
    @Bind(R.id.layout)
    LinearLayout layout;
    @Bind(R.id.ImageView)
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);

        image.setVisibility(View.GONE);


        new AsyncTask<Void, Void, TXObject>() {
            @Override
            protected TXObject doInBackground(Void... params) {
                try {
                    Msg msg = UpdateController.checkForUpdate();
                    return msg.getCode() == ErrorCode.SUCCESS ? (TXObject) msg.getObj() : null;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(TXObject info) {
                if (info == null) {
                    Utils.log("网络错误，获取更新信息失败！");
                    toLogin();
                } else if (UpdateController.compareVersion(info.get("versionID"), getCurrentVersion()) == 0) {
                    Utils.log("版本号相同，无需更新");
                    toLogin();
                } else if (UpdateController.compareVersion(info.get("versionID"), getCurrentVersion()) > 0) {
                    Utils.log("版本号不同，提醒用户更新");
                    boolean forceUpdate = UpdateController.compareVersion(info.get("minVersion"), getCurrentVersion()) > 0;
                    showUpdateDialog(info, forceUpdate);             //提醒更新
                } else {
                    Utils.log("update error");
                    toLogin();
                }
            }
        }.execute();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void toLogin() {
        layout.setBackground(getResources().getDrawable(R.drawable.welcome_1));
        image.setVisibility(View.VISIBLE);
        startIntent(LoginActivity.class);
        overridePendingTransition(R.anim.in_login, R.anim.empty);
    }

    public void showUpdateDialog(final TXObject info, final boolean forceUpdate) {
        new AlertDialog(this, info.get("description"), "下次再更新", "立即更新") {
            @Override
            public void onOkClick() {
                this.cancel();
                downLoadApk(info.get("downloadUrl"));
            }

            @Override
            public void onNoClick() {
                if(forceUpdate){
                    Toast.makeText(IndexActivity.this, "低版本不再支持，请更新", Toast.LENGTH_SHORT).show();
                    return;
                }
                this.cancel();
                toLogin();
            }
        }.show();
    }

    protected void downLoadApk(final String url) {
        final ProgressDialog pd = new ProgressDialog(this);              //进度条对话框
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = UpdateController.getFileFromServer(url, pd);
                    sleep(2000);
                    installApk(file);
                    pd.dismiss();                                        //结束掉进度条对话框
                    finishThisActivity();
                } catch (Exception e) {
                    Utils.log("安装包下载错误！");
                    toLogin();
                }
            }
        }.start();
    }

    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }


    public String getCurrentVersion() {
        return getResources().getString(R.string.version);
    }

}
