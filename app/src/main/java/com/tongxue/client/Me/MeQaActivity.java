package com.tongxue.client.Me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.Question;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.Qa.QaInfoActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Utils;
import com.tongxue.client.View.PullableView.PullToRefreshLayout;
import com.tongxue.client.View.PullableView.PullableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/10/13.
 */
public class MeQaActivity extends BaseActivity{
    @Bind(R.id.refresh_view)    PullToRefreshLayout layout;
    @Bind(R.id.recyclerView)    PullableListView listView;
    @Bind(R.id.back)            ImageView back;
    @Bind(R.id.title)           TextView title;
    public List<Map<String ,Object>> list;
    public SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_blog_qa);
        ButterKnife.bind(this);

        title.setText("我的提问");

        list = new ArrayList<>();
        adapter = new SimpleAdapter(this, list, R.layout.item_list_qa, new String[]{"qaAsker","qaTime","qaBrief","qaDetail","qaLan","qaDing"}, new int[]{R.id.asker,R.id.askTime,R.id.brief,R.id.detail,R.id.qaLan,R.id.qaDing});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LookQaById(position);
            }
        });
        layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData(1);
                    }
                },1000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.loadmoreFinish(PullToRefreshLayout.FULL);
                    }
                },1000);
            }
        },7);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        getData(0);

    }

    public void getData(final int flag){
        new ServerTask(this){
            @Override
            protected Msg doInBackground(Object... params) {
                return Server.getAllQuestionsByAuthorBefore(null);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if(msg.getCode()==93200){
                    list.clear();
                    List<Question> questions = (List<Question>)msg.getObj();
                    for(Question question : questions){
                        Map map = new HashMap();
                        map.put("qaId", question.getQuestionID());
                        map.put("qaAsker", question.getAuthor());
                        map.put("qaTime", Utils.formatTime(question.getTime()));
                        map.put("qaBrief", question.getTitle());
                        map.put("qaDetail", question.getDescription());
                        map.put("qaLan", question.getViews()+"");
                        map.put("qaDing", new Random().nextInt(50)+"");
                        list.add(map);
                    }
                    if(questions.size()==0){
                        toast("暂无更多");
                    }
                    adapter.notifyDataSetChanged();
                    if(flag == 1) layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }else {
                    if(flag == 1) layout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }
        }.execute();

    }

    public void LookQaById(final int position){
        new ServerTask(this){
            @Override
            protected Msg doInBackground(Object... params) {
                int qaId = (int)list.get(position).get("qaId");
                return Server.getQuestionByID(qaId);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if(msg.getCode()==96200){
                    Question question = (Question)msg.getObj();
                    Intent intent =new Intent(MeQaActivity.this, QaInfoActivity.class);
                    intent.putExtra("qaId", question.getQuestionID());
                    intent.putExtra("qaAsker", question.getAuthor());
                    intent.putExtra("qaTime", Utils.formatTime(question.getTime()));
                    intent.putExtra("qaBrief", question.getTitle());
                    intent.putExtra("qaDetail", question.getDescription());
                    intent.putExtra("qaLan", question.getViews()+"");
                    intent.putExtra("qaDing", new Random().nextInt(40)+"");
                    startActivity(intent);
                }
            }
        }.execute();

    }


}
