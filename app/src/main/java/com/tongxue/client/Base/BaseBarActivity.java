package com.tongxue.client.Base;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.tongxue.client.Login.LoginActivity;
import com.tongxue.client.View.WaitingDialog;

import java.lang.reflect.Method;

/**
 * Created by chaosi on 2015/9/10.
 */
public class BaseBarActivity extends ActionBarActivity {
    public String userId;
    public WaitingDialog waitingDialog;
    protected ActivityManager activityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityManager = ActivityManager.getActivityManager();
        activityManager.pushActivity(this);

        waitingDialog=new WaitingDialog(this);
        waitingDialog.setCanceledOnTouchOutside(false);

        userId= LearnApplication.preferences.getString("username","");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onPause();
        AVAnalytics.onResume(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(menu!=null && menu.getClass().getSimpleName().equals("MenuBuilder")){
            try {
                Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void makeToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void log(String content){
        Log.i("learn", content);
    }

    public void waitingDialogShow(){
        waitingDialog.show();
    }

    public void waitingDialogDismiss(){
        waitingDialog.cancel();
    }

    protected void controlKeyboardLayout(final View root, final int height) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                root.getWindowVisibleDisplayFrame(rect);
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                if (rootInvisibleHeight > 50) {
                    root.scrollTo(0, height);
                } else {
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    public void ReLogin(){
        Toast.makeText(getApplicationContext(), "网络异常，请重新登录", Toast.LENGTH_LONG).show();
        activityManager = ActivityManager.getActivityManager();
        activityManager.popAllActivity();
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void startIntent(Class mClass){
        Intent intent= new Intent(this, mClass);
        startActivity(intent);
    }

    public void finishThisActivity(){
        this.finish();
    }
}
