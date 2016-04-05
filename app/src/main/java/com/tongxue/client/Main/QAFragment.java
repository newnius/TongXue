package com.tongxue.client.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.tongxue.connector.ErrorCode;
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
public class QAFragment extends BaseFragment {
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout listLayout;
    public View mView;
    public List<Map<String, Object>> qaList;
    public SimpleAdapter adapter;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init();
        mView = inflater.inflate(R.layout.fragment_qa, container, false);
        ButterKnife.bind(this, mView);

        qaList = MainActivity.qaList;
        adapter = new SimpleAdapter(mContext, qaList, R.layout.item_list_qa, new String[]{"qaAsker", "qaTime", "qaBrief", "qaDetail", "qaLan", "qaDing"}, new int[]{R.id.asker, R.id.askTime, R.id.brief, R.id.detail, R.id.qaLan, R.id.qaDing});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LookQaById(position);
                Log.i("blog", "clicked");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteQuestion(position);
                return true;
            }
        });

        listLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentPage = 0;
                        qaList.clear();
                        getData(1);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData(2);
                        listLayout.loadmoreFinish(PullToRefreshLayout.FULL);
                    }
                }, 1000);
            }
        }, 4);

        if (MainActivity.shouldQaRefresh) {
            getData(0);
            MainActivity.shouldQaRefresh = false;
        }

        return mView;
    }

    public void getData(final int flag) {
        new ServerTask(mContext) {
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject question = new TXObject();
                question.set("page-no", ++currentPage);
                Log.i("blog", "getting data");
                return Server.searchQuestion(question);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    List<TXObject> questions = (List<TXObject>) msg.getObj();
                    for (TXObject question : questions) {
                        Map map = new HashMap();
                        map.put("qaId", question.getInt("questionID"));
                        map.put("qaAsker", question.get("author"));
                        map.put("qaTime", Utils.formatTime(question.getLong("time")));
                        map.put("qaBrief", question.get("title"));
                        map.put("qaDetail", question.get("content"));
                        map.put("qaLan", question.get("views") + "");
                        map.put("qaDing", question.get("upVotes"));
                        qaList.add(map);
                    }
                    if (questions.size() == 0) {
                        toast("暂无更多");
                    }
                    MainActivity.qaList = qaList;
                    adapter.notifyDataSetChanged();
                    if (flag > 0) listLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    if (flag > 0) listLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }
        }.execute();

    }

    public void LookQaById(final int position) {
        new ServerTask(mContext) {
            @Override
            protected Msg doInBackground(Object... params) {
                try {
                    int qaId = (int) qaList.get(position).get("qaId");
                    TXObject question = new TXObject();
                    question.set("questionID", qaId);
                    Log.i("get by id", "aa");
                    return Server.searchQuestion(question);
                }catch(Exception ex){
                    ex.printStackTrace();
                    return new Msg(ErrorCode.UNKNOWN);
                }
            }

            @Override
            protected void onPostExecute(Msg msg) {
                try {
                    super.onPostExecute(msg);
                    waitingDialogDismiss();
                    if (msg.getCode() == ErrorCode.SUCCESS) {
                        List<TXObject> questions = (List<TXObject>) msg.getObj();
                        if (questions.size() == 0)
                            return;
                        TXObject question = questions.get(0);
                        Intent intent = new Intent(mContext, QaInfoActivity.class);
                        intent.putExtra("qaId", question.getInt("questionID"));
                        intent.putExtra("qaAsker", question.get("author"));
                        intent.putExtra("qaTime", Utils.formatTime(question.getLong("time")));
                        intent.putExtra("qaBrief", question.get("title"));
                        intent.putExtra("qaDetail", question.get("content"));
                        intent.putExtra("qaLan", question.getInt("views"));
                        intent.putExtra("qaDing", question.getInt("upVotes"));
                        startActivity(intent);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }.execute();

    }

    public void deleteQuestion(final int position) {
        new ServerTask(mContext) {
            @Override
            protected Msg doInBackground(Object... params) {
                try {
                    int questionID = (int)qaList.get(position).get("qaId");
                    TXObject question = new TXObject();
                    question.set("questionID", questionID);
                    Log.i("blog", "delete");
                    return Server.deleteQuestion(question);
                }catch(Exception ex){
                    ex.printStackTrace();
                    return new Msg(ErrorCode.UNKNOWN);
                }
            }

            @Override
            protected void onPostExecute(Msg msg) {
                try {
                    super.onPostExecute(msg);
                    waitingDialogDismiss();
                    if(msg==null){
                        toast("删除失败，未知错误");
                        return;
                    }
                    if (msg.getCode() == ErrorCode.SUCCESS){
                        qaList.remove(position);
                        toast("删除问题成功");
                        adapter.notifyDataSetChanged();
                    }else{
                        toast("删除失败，"+ErrorCode.getMsg(msg.getCode()));
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }.execute();

    }

}
