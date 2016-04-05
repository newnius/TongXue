package com.tongxue.client.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.tongxue.connector.ErrorCode;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.ActivityManager;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.Main.MainActivity;
import com.tongxue.client.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/5.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.name)
    EditText et_name;
    @Bind(R.id.pwd)
    EditText et_pwd;
    @Bind(R.id.login)
    Button bt_login;
    @Bind(R.id.register)
    LinearLayout register;
    @Bind(R.id.layout)
    RelativeLayout layout;
    public String username;
    public String password;
    public boolean remember;
    public List<Map<String, Object>> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        controlKeyboardLayout(layout, 65);

        groupList = new ArrayList<>();
        remember = LearnApplication.preferences.getBoolean("Remember", false);
        if (remember) {
            et_name.setText(LearnApplication.preferences.getString("username", null));
            et_pwd.setText(LearnApplication.preferences.getString("password", null));
        }

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_name.getText().toString().trim();
                password = et_pwd.getText().toString().trim();

                if (!username.equals("")) {
                    if (!password.equals("")) {
                        login();
                    } else {
                        makeToast("密码不能为空");
                    }
                } else {
                    makeToast("用户名不能为空");
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(RegisterActivity.class);
                finishThisActivity();
                overridePendingTransition(R.anim.in_register, R.anim.empty);
            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityManager.getActivityManager().popAllActivity();
    }

    private void login() {

        waitingDialogShow();
        new ServerTask(this) {
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject user = new TXObject();
                user.set("username", username);
                user.set("password", password);
                return Server.login(user);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);

                if (msg.getCode() == ErrorCode.SUCCESS) {
                    log("Login success");
                    SharedPreferences.Editor edit = LearnApplication.preferences.edit();
                    edit.putBoolean("Remember", true);
                    edit.putString("username", username);
                    edit.putString("password", password);
                    edit.apply();

                    ChatManager chatManager = ChatManager.getInstance();
                    chatManager.setupDatabaseWithSelfId(username);
                    chatManager.openClientWithSelfId(username, new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVException e) {
                            waitingDialogDismiss();
                            if (e != null) {
                                toast("网络错误，稍后再试");
                                log(e.getCode() + e.toString());
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    waitingDialogDismiss();
                    toast("登录失败，"+ErrorCode.getMsg(msg.getCode()));
                }
            }
        }.execute();
    }

}
