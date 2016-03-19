package com.tongxue.client.Base;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.tongxue.connector.ErrorCode;
import com.tongxue.connector.Msg;
import com.tongxue.client.Utils.Utils;


/**
 * Created by chaosi on 2015/9/6.
 */
public abstract class ServerTask extends AsyncTask<Object, Integer, Msg> {
    public Context context;
    public boolean isStop;
    public boolean timeout;

    public ServerTask(final Context context) {
        this.context = context;

        isStop = false;
        timeout = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isStop) {
                    isStop = true;
                    timeout = true;
                    ((BaseActivity) context).waitingDialogDismiss();
                    toast("加载超时！");
                }
            }
        }, 10000);
    }

    @Override
    protected abstract Msg doInBackground(Object... params);

    @Override
    protected void onPostExecute(Msg msg) {
        super.onPostExecute(msg);
        isStop = true;
        if (!timeout) {
            if (msg.getCode() != ErrorCode.SUCCESS)
                toast(ErrorCode.getMsg(msg.getCode()));
        }
    }

    public void toast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
