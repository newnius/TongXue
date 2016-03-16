package com.tongxue.client.Base;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.tongxue.connector.Msg;
import com.tongxue.client.Utils.Utils;


/**
 * Created by chaosi on 2015/9/6.
 */
public abstract class ServerTask extends AsyncTask<Object, Integer, Msg>{
    public Context context;
    public boolean isStop;
    public boolean timeout;

    public ServerTask(final Context context){
        this.context=context;

        isStop=false;
        timeout=false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isStop){
                    isStop=true;
                    timeout=true;
                    ((BaseActivity)context).waitingDialogDismiss();
                    toast("加载超时！");
                }
            }
        },10000);
    }

    @Override
    protected abstract Msg doInBackground(Object... params);

    @Override
    protected void onPostExecute(Msg msg) {
        super.onPostExecute(msg);
        isStop=true;
        if(!timeout){
            switch (msg.getCode()){
                case 11201:
                    toast("不存在该用户，请检查用户名！");
                    break;
                case 11202:
                    toast("密码不正确，请重新输入密码！");
                    break;
                case 11203:
                    toast("邮箱未验证，请先验证邮箱！");
                    break;
                case 11204:
                    toast("网络错误，请检查网络再试");
                    break;
                case 10000:
                    toast("网络错误，请检查网络后再试");
                    break;
                case 10001:
                    toast("数据格式不正确！");
                    break;
                case 12201:
                    toast("邮箱格式不正确");
                    break;
                case 12202:
                    toast("该用户名已注册，请换用其他用户名");
                    break;
                case 12203:
                    toast("该邮箱已注册！");
                    break;
                case 12204:
                    toast("服务器内部错误，注册失败");
                    break;
                case 12205:
                    toast("注册失败，未知错误");
                    break;
                case 70201:
                    toast("博客标题或内容太长");
                    break;
                case 70202:
                    toast("含有不合法的字符，请重新发布");
                    break;
                case 72201:
                    toast("该博客不存在");
                    break;
                case 78201:
                    toast("该博客不存在");
                    break;
                case 78202:
                    toast("评论内容太长，评论失败");
                    break;
                case 79201:
                    toast("该问题不存在");
                    break;
                case 96201:
                    toast("该问题不存在");
                    break;
                case 90201:
                    toast("问题的标题或内容太长");
                    break;
                case 97201:
                    toast("该问题不存在");
                    break;
                case 98201:
                    toast("该问题不存在");
                    break;
                case 70203:
                case 70204:
                case 73211:
                case 74201:
                case 72202:
                case 78203:
                case 79202:
                case 91201:
                case 93201:
                case 96202:
                case 90202:
                case 97202:
                case 98202:
                case 98203:
                    Utils.log("错误类型："+msg.getCode());
                    toast("网络错误！");
                    break;
            }
        }
    }

    public void toast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
