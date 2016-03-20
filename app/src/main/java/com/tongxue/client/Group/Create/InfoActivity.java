package com.tongxue.client.Group.Create;

import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Main.MainActivity;
import com.tongxue.client.R;

import com.tongxue.client.View.AlertDialog;
import com.tongxue.connector.CallBackInterface;
import com.tongxue.connector.ErrorCode;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Server;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/17.
 */
public class InfoActivity extends BaseActivity{
    @Bind(R.id.actionbar_back)   ImageView back;
    @Bind(R.id.img)              ImageView img;
    @Bind(R.id.et_name)          EditText et_name;
    @Bind(R.id.et_intro)         EditText et_intro;
    @Bind(R.id.finish)           TextView finish;
    @Bind(R.id.layout)           LinearLayout layout;
    @Bind(R.id.toolbar)          RelativeLayout toolbar;
    public String auth;
    public String kind;
    public String name;
    public String intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_info);
        ButterKnife.bind(this);
        controlKeyboardLayout(layout, 0);

        Intent intent= getIntent();
        auth= intent.getStringExtra("public");
        kind= intent.getStringExtra("kind");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString().trim();
                intro = et_intro.getText().toString().trim();
                if (!name.equals("")) {
                    waitingDialogShow();

                    TXObject group = new TXObject();
                    group.set("groupName", name);
                    group.set("category", kind);
                    group.set("introduction", kind);
                    group.set("public", auth);
                    createGroup(group);
                } else {
                    toast("小组名不能为空");
                }
            }
        });
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
                    img.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                } else {
                    root.scrollTo(0, 0);
                    img.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void createGroup(final TXObject group) {
        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                return Server.createGroup(group);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    waitingDialogDismiss();
                    new AlertDialog(InfoActivity.this, "恭喜！\n\n小组创建成功", "继续完善资料", "以后再完善") {
                        @Override
                        public void onOkClick() {
                            Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                            intent.putExtra("shouldRefresh", true);
                            MainActivity.shouldRecord = true;
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onNoClick() {
                            Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                            intent.putExtra("shouldRefresh", true);
                            MainActivity.shouldRecord = true;
                            startActivity(intent);
                            finish();
                        }
                    }.show();

                } else {
                    makeToast("小组创建失败！(" + ErrorCode.getMsg(msg.getCode()) + ")");
                }
            }
        }.execute();

    }
}
