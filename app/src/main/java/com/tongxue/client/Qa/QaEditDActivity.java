package com.tongxue.client.Qa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.Main.MainActivity;
import com.tongxue.client.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/10/11.
 */
public class QaEditDActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.detail)
    EditText detailEt;
    @Bind(R.id.askBtn)
    Button ask;
    public String brief;
    public String detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_edit_d);
        ButterKnife.bind(this);

        brief = getIntent().getStringExtra("brief");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
                overridePendingTransition(R.anim.common_right_out, R.anim.empty);
            }
        });

        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detail = detailEt.getText().toString().trim();
                publishQa();
            }
        });

    }

    public void publishQa() {
        new ServerTask(this) {
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject question = new TXObject();
                question.set("title", brief);
                question.set("content", detail);

                return Server.askQuestion(question);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                if (msg.getCode() == 90200) {
                    toast("问题发布成功");
                    Intent intent = new Intent(QaEditDActivity.this, MainActivity.class);
                    intent.putExtra("qa", true);
                    startActivity(intent);
                }
            }
        }.execute();
    }
}
