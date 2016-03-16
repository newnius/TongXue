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
import com.tongxue.client.Bean.GroupBean;
import com.tongxue.client.Bean.LatestGroup;
import com.tongxue.client.Main.MainActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.SerializableMapList;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public List<Map<String, Object>> talkList;
    public List<Map<String, Object>> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        controlKeyboardLayout(layout, 65);

        talkList = new ArrayList<>();
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
                try {
                    if (!username.equals("")) {
                        if (!password.equals("")) {
                            login();
                        } else {
                            makeToast("密码不能为空");
                        }
                    } else {
                        makeToast("用户名不能为空");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
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
                try {
                    super.onPostExecute(msg);

                    if (msg.getCode() == ErrorCode.SUCCESS) {
                        log("登上自己的服务器了");
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
                                if (e != null) {
                                    toast("网络错误，稍后再试");
                                    log(e.getCode() + e.toString());
                                    waitingDialogDismiss();
                                } else {
                                    getPreData();
                                }
                            }
                        });
                    } else {
                        waitingDialogDismiss();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();

    }

    private void getPreData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                FinalDb db = FinalDb.create(LoginActivity.this);
                List<LatestGroup> talkBeans = db.findAll(LatestGroup.class);
                List<GroupBean> groupBeans = db.findAllByWhere(GroupBean.class, "username=\"" + username + "\"");

                if (talkBeans.size() > 0) {
                    int size = talkBeans.size();
                    for (int i = size - 1; i >= 0 && i >= size - 15; i--) {
                        String groupName = talkBeans.get(i).getGroupName();
                        List<GroupBean> beans = db.findAllByWhere(GroupBean.class, "groupName=\"" + groupName + "\"");
                        if (beans.size() > 0) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", beans.get(0).getGroupName());
                            map.put("id", beans.get(0).getGroupId());
                            map.put("kind", beans.get(0).getKind());
                            map.put("intro", beans.get(0).getIntro());
                            map.put("num", "" + new Random().nextInt(100));
                            talkList.add(map);
                        } else {
                            db.deleteByWhere(LatestGroup.class, "groupName=\"" + groupName + "\"");
                        }
                    }
                }

                if (groupBeans.size() > 0) {
                    for (GroupBean bean : groupBeans) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", bean.getGroupName());
                        map.put("id", bean.getGroupId());
                        map.put("kind", bean.getKind());
                        map.put("intro", bean.getIntro());
                        groupList.add(map);
                    }
                }
                SerializableMapList talkSml = new SerializableMapList();
                SerializableMapList groupSml = new SerializableMapList();
                talkSml.setMapList(talkList);
                groupSml.setMapList(groupList);
                Bundle bundle = new Bundle();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                bundle.putSerializable("talkList", talkSml);
                bundle.putSerializable("groupList", groupSml);
                bundle.putBoolean("shouldRefresh", false);
                intent.putExtras(bundle);
                waitingDialogDismiss();
                startActivity(intent);
                LoginActivity.this.finish();

            }
        }).start();
    }

}
