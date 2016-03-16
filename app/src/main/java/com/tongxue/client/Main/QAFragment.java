package com.tongxue.client.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.BaseFragment;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.Qa.QaInfoActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Utils;
import com.tongxue.client.View.PullableView.PullToRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/14.
 */
public class QAFragment extends BaseFragment{
    @Bind(R.id.listView)       ListView listView;
    @Bind(R.id.refresh_view)   PullToRefreshLayout listLayout;
    public View mView;
    public List<Map<String, Object>> qaList;
    public SimpleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init();
        mView= inflater.inflate(R.layout.fragment_qa, container, false);
        ButterKnife.bind(this, mView);

        qaList = MainActivity.qaList;
        adapter = new SimpleAdapter(mContext, qaList, R.layout.item_list_qa, new String[]{"qaAsker","qaTime","qaBrief","qaDetail","qaLan","qaDing"}, new int[]{R.id.asker,R.id.askTime,R.id.brief,R.id.detail,R.id.qaLan,R.id.qaDing});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LookQaById(position);
            }
        });
        listLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
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
                        listLayout.loadmoreFinish(PullToRefreshLayout.FULL);
                    }
                },1000);
            }
        },4);

        if(MainActivity.shouldQaRefresh){
            getData(0);
            MainActivity.shouldQaRefresh = false;
        }

        return mView;
    }

    public void getData(final int flag){
        new ServerTask(mContext){
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject question = new TXObject();
                return Server.searchQuestion(question);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if(msg.getCode()==91200){
                    qaList.clear();
                    List<TXObject> questions = (List<TXObject>)msg.getObj();
                    for(TXObject question : questions){
                        Map map = new HashMap();
                        map.put("qaId", question.get("questionID"));
                        map.put("qaAsker", question.get("author"));
                        map.put("qaTime", Utils.formatTime(question.getLong("time")));
                        map.put("qaBrief", question.get("title"));
                        map.put("qaDetail", question.get("content"));
                        map.put("qaLan", question.get("views"));
                        map.put("qaDing", question.get("upVotes"));
                        qaList.add(map);
                    }
                    if(questions.size()==0){
                        toast("暂无更多");
                    }
                    MainActivity.qaList = qaList;
                    adapter.notifyDataSetChanged();
                    if(flag == 1)  listLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }else {
                    if(flag == 1)  listLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }
        }.execute();

    }

    public void LookQaById(final int position){
        new ServerTask(mContext){
            @Override
            protected Msg doInBackground(Object... params) {
                int qaId = (int)qaList.get(position).get("qaId");
                TXObject question = new TXObject();
                question.set("questionID", qaId);
                return Server.searchQuestion(question);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if(msg.getCode()==96200){
                    List<TXObject> questions = (List<TXObject>)msg.getObj();
                    TXObject question = questions.size()==0?null:questions.get(0);
                    Intent intent =new Intent(mContext, QaInfoActivity.class);
                    intent.putExtra("qaId", question.get("question"));
                    intent.putExtra("qaAsker", question.get("author"));
                    intent.putExtra("qaTime", Utils.formatTime(question.getLong("time")));
                    intent.putExtra("qaBrief", question.get("title"));
                    intent.putExtra("qaDetail", question.get("content"));
                    intent.putExtra("qaLan", question.get("views"));
                    intent.putExtra("qaDing", question.get("upVotes"));
                    startActivity(intent);
                }
            }
        }.execute();

    }

}
