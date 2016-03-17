package com.tongxue.client.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tongxue.client.R;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by chaosi on 2015/9/7.
 */
public abstract class AlertDialog extends Dialog{
    @Bind(R.id.dialog_ok)          TextView ok;
    @Bind(R.id.dialog_cancel)      TextView cancel;
    @Bind(R.id.dialog_text)        TextView text;
    public Context context;
    public String text_title;
    public String text_ok;
    public String text_cancel;

    public AlertDialog(Context context, String text_title, String text_cancel, String text_ok){
        super(context, R.style.WaitingDialogStyle);
        this.context= context;
        this.text_title=text_title;
        this.text_cancel=text_cancel;
        this.text_ok=text_ok;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);
        ButterKnife.bind(this);

        setCancelable(false);

        text.setText(text_title);
        ok.setText(text_ok);
        cancel.setText(text_cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNoClick();
            }
        });

    }

    public abstract void onOkClick();

    public abstract void onNoClick();
}