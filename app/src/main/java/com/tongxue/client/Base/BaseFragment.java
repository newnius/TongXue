package com.tongxue.client.Base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.tongxue.client.View.WaitingDialog;

/**
 * Created by chaosi on 2015/9/12.
 */
public class BaseFragment extends Fragment {

    public Context mContext;
    public String userId;
    public WaitingDialog waitingDialog;

    public void init(){
        mContext = getActivity();

        waitingDialog=new WaitingDialog(mContext);
        waitingDialog.setCanceledOnTouchOutside(false);

        userId= LearnApplication.preferences.getString("username","");
    }

    public void makeToast(String text){
        Toast.makeText(mContext , text, Toast.LENGTH_LONG).show();
    }

    public void toast(String text){
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
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

}
