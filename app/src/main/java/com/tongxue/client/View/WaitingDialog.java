package com.tongxue.client.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.tongxue.client.R;

/**
 * Created by chaosi on 2015/9/6.
 */
public class WaitingDialog extends Dialog {
    public TextView tv;

    public WaitingDialog(Context context) {
        super(context, R.style.WaitingDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_waiting);
        tv = (TextView)this.findViewById(R.id.tv);
        tv.setText("正在加载...");

    }
}

