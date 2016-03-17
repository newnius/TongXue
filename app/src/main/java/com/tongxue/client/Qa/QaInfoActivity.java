package com.tongxue.client.Qa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Config;
import com.tongxue.client.Utils.Utils;
import com.tongxue.client.View.NoScrollListView;
import com.tongxue.client.View.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/10/11.
 */
public class QaInfoActivity extends BaseActivity {
    @Bind(R.id.img)
    RoundImageView imgIv;
    @Bind(R.id.asker)
    TextView askerTv;
    @Bind(R.id.qaTime)
    TextView qaTimeTv;
    @Bind(R.id.brief)
    TextView briefTv;
    @Bind(R.id.detail)
    TextView detailTv;
    @Bind(R.id.qaLan)
    TextView qaLanTv;
    @Bind(R.id.qaDing)
    TextView qaDingTv;
    @Bind(R.id.ding)
    LinearLayout dingLayout;
    @Bind(R.id.ansTv)
    TextView ansTv;
    @Bind(R.id.ansListView)
    NoScrollListView ansListView;
    @Bind(R.id.toanswer)
    TextView toanswerTv;
    @Bind(R.id.ansEt)
    EditText ansEt;
    @Bind(R.id.submitAns)
    Button submitAns;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.cLayout)
    RelativeLayout cLayout;
    public int qaId;
    public int index;
    public int ansNum;
    public List<Map<String, Object>> list;
    public SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        qaId = intent.getIntExtra("qaId", 0);
        String qaAsker = intent.getStringExtra("qaAsker");
        String qaTime = intent.getStringExtra("qaTime");
        String qaBrief = intent.getStringExtra("qaBrief");
        String qaDetail = intent.getStringExtra("qaDetail");
        String qaLan = intent.getStringExtra("qaLan");
        String qaDing = intent.getStringExtra("qaDing");

        briefTv.setText(Html.fromHtml("<h4>" + qaBrief + "</h4>"));
        briefTv.setMovementMethod(LinkMovementMethod.getInstance());

        askerTv.setText(qaAsker);
        qaTimeTv.setText(qaTime);
        detailTv.setText("补充：" + qaDetail);
        qaLanTv.setText("浏览" + qaLan + "次");
        qaDingTv.setText("帮顶" + qaDing + "次");
        imgIv.setImageDrawable(getResources().getDrawable(Config.img[new Random().nextInt(50)]));

        list = new ArrayList<>();
        adapter = new SimpleAdapter(this, list, R.layout.item_list_qa_ans, new String[]{"userImg", "userName", "userTime", "comment"}, new int[]{R.id.userImg, R.id.userName, R.id.userTime, R.id.comment});
        ansListView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        dingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        submitAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ans = ansEt.getText().toString().trim();
                if (ans.equals("")) {
                    toast("您还没有撰写答案呢");
                } else {
                    askQuestion(ans);
                }
            }
        });

        getAns();

    }

    public void askQuestion(final String ans) {
        waitingDialogShow();
        new ServerTask(this) {
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject answer = new TXObject();
                answer.set("questionID", qaId);
                answer.set("content", ans);
                return Server.answerQuestion(answer);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if (msg.getCode() == 98200) {
                    toast("提交成功");
                    ansEt.setText("");
                    if (ansNum == 0) {
                        cLayout.setVisibility(View.GONE);
                        toanswerTv.setText("我有更好的答案");
                        ansListView.setVisibility(View.VISIBLE);
                    }
                    String user = LearnApplication.preferences.getString("username", "");
                    Map map = new HashMap();
                    map.put("userImg", Config.img[(index++) % 50]);
                    map.put("userName", user);
                    map.put("userTime", Utils.formatTime(System.currentTimeMillis()));
                    map.put("comment", ans);
                    list.add(map);
                    adapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

    public void getAns() {
        new ServerTask(this) {
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject question = new TXObject();
                question.set("questionID", qaId);
                return Server.getQuestionAnswer(question);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                if (msg.getCode() == 97200) {
                    list.clear();
                    List<TXObject> answers = (List<TXObject>) msg.getObj();
                    index = 0;
                    for (TXObject answer : answers) {
                        Map map = new HashMap();
                        map.put("userImg", Config.img[(index++) % 50]);
                        map.put("userName", answer.get("author"));
                        map.put("userTime", answer.get("time"));
                        map.put("comment", answer.get("content"));
                        list.add(map);
                    }
                    ansNum = answers.size();
                    if (answers.size() == 0) {
                        progressBar.setVisibility(View.GONE);
                        ansTv.setText("暂时没有回答");
                        toanswerTv.setText("我来回答这个这个问题");
                        ansListView.setVisibility(View.GONE);
                    } else {
                        cLayout.setVisibility(View.GONE);
                        toanswerTv.setText("我有更好的答案");
                        ansListView.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    finishThisActivity();
                }
            }
        }.execute();
    }
}
