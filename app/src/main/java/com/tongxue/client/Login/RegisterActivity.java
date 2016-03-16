package com.tongxue.client.Login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.R;
import com.tongxue.client.View.AlertDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/5.
 */
public class RegisterActivity extends BaseActivity{

    @Bind(R.id.name)      EditText et_name;
    @Bind(R.id.email)     EditText et_email;
    @Bind(R.id.pwd)       EditText et_pwd;
    @Bind(R.id.register)  Button register;
    @Bind(R.id.login)     LinearLayout login;
    @Bind(R.id.layout)    RelativeLayout layout;
    public String username;
    public String email;
    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        controlKeyboardLayout(layout, 65);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username= et_name.getText().toString().trim();
                email=et_email.getText().toString().trim();
                password= et_pwd.getText().toString().trim();
                if(!username.equals("")){
                    if(!email.equals("")){
                        if(!password.equals("")){
                            if(email.contains("edu")){
                                waitingDialogShow();
                                register();
                            }else{
                                toast("邮箱格式不正确，请输入校园邮箱！");
                            }
                        }else{
                            toast("密码不能为空");
                        }
                    }else{
                        toast("邮箱不能为空");
                    }
                }else{
                    toast("用户名不能为空");
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(LoginActivity.class);
                finishThisActivity();
                overridePendingTransition(R.anim.out_register, R.anim.empty);
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog(this,"亲，您不注册了吗？","下次再注册","继续注册"){
            @Override
            public void onOkClick() {
                this.cancel();
            }

            @Override
            public void onNoClick() {
                startIntent(LoginActivity.class);
                finishThisActivity();
                overridePendingTransition(R.anim.out_register, R.anim.empty);
            }
        }.show();
    }

    private void register(){

        waitingDialogShow();
        new ServerTask(this){
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject user = new TXObject();
                user.set("username", username);
                user.set("password", password);
                user.set("email", email);
                return Server.register(user);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();

                if(msg.getCode()==12200){
                    toast("注册成功！");

                    SharedPreferences.Editor edit= LearnApplication.preferences.edit();
                    edit.putBoolean("Remember", true);
                    edit.putString("username", username);
                    edit.putString("password", password);
                    edit.apply();

                    startIntent(LoginActivity.class);
                    finishThisActivity();
                    overridePendingTransition(R.anim.out_register, R.anim.empty);
                }
            }
        }.execute();

    }

}
