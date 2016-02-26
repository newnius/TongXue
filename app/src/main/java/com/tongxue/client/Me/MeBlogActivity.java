package com.tongxue.client.Me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.Article;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.Blog.BlogInfoActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Config;
import com.tongxue.client.Utils.Utils;
import com.tongxue.client.View.PullableView.PullToRefreshLayout;
import com.tongxue.client.View.PullableView.PullableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/10/13.
 */
public class MeBlogActivity extends BaseActivity{
    @Bind(R.id.refresh_view)    PullToRefreshLayout  layout;
    @Bind(R.id.recyclerView)    PullableListView listView;
    @Bind(R.id.back)            ImageView back;
    @Bind(R.id.title)           TextView  title;
    public List<Map<String ,Object>> list;
    public ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_blog_qa);
        ButterKnife.bind(this);

        title.setText("我的博客");

        list = new ArrayList<>();
        adapter = new ListAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                waitingDialogShow();
                LookArticleById(position);
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
        },6);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        getData(0);

    }

    private void getData(final int flag){
        new ServerTask(this){
            @Override
            protected Msg doInBackground(Object... params) {
                return Server.getArticleByUser(null);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if(msg.getCode()==73200){
                    list.clear();
                    List<Article> articles = (List<Article>)msg.getObj();
                    int i = 0;
                    for(Article article : articles){
                        Map map = new HashMap();
                        map.put("blogId", article.getArticleID());
                        map.put("blogAuthor", article.getAuthor());
                        map.put("blogTime", Utils.formatTime(article.getTime()));
                        map.put("blogTitle", article.getTitle());
                        map.put("blogContent", article.getContent());
                        map.put("blogLan", article.getViews());
                        map.put("blogZan", article.getUps());
                        map.put("blogCover", Config.bg[(i++)%10]);
                        list.add(map);
                    }
                    if(articles.size()==0){
                        toast("暂无更多");
                    }
                    adapter.notifyDataSetChanged();
                    if(flag==1) layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }else{
                    if(flag==1) layout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }
        }.execute();
    }

    public void LookArticleById(final int position){
        new ServerTask(this){
            @Override
            protected Msg doInBackground(Object... params) {
                int blogId = (int)list.get(position).get("blogId");
                return Server.getArticleById(blogId);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if(msg.getCode()==72200){
                    Article article = (Article)msg.getObj();
                    Intent intent =new Intent(MeBlogActivity.this, BlogInfoActivity.class);
                    intent.putExtra("blogId", article.getArticleID());
                    intent.putExtra("blogUser", article.getAuthor());
                    intent.putExtra("blogTime", Utils.formatTime(article.getTime()));
                    intent.putExtra("blogTitle", article.getTitle());
                    intent.putExtra("blogContent", article.getContent());
                    intent.putExtra("blogLan", article.getViews());
                    intent.putExtra("blogZan", article.getUps());
                    startActivity(intent);
                }
            }
        }.execute();
    }

    public class ListAdapter extends BaseAdapter {
        public LayoutInflater mInflater;
        public List<Map<String, Object>> mList;

        public ListAdapter(Context c, List<Map<String, Object>> list){
            mInflater = LayoutInflater.from(c);
            mList = list;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList == null ? null : mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            log(""+position);
            ViewHolder holder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_list_blog, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.blogAuthor.setText(mList.get(position).get("blogAuthor").toString());
            holder.blogTime.setText(mList.get(position).get("blogTime").toString());
            holder.blogTitle.setText(mList.get(position).get("blogTitle").toString());
            String content = "　"+ Html.fromHtml(mList.get(position).get("blogContent").toString()).toString();
            holder.blogSummary.setText(content);
            holder.image.setImageDrawable(getResources().getDrawable((int)mList.get(position).get("blogCover")));
            holder.blogLan.setText(mList.get(position).get("blogLan").toString());
            holder.blogZan.setText(mList.get(position).get("blogZan").toString());

            return convertView;
        }

        public class ViewHolder{
            @Bind(R.id.title)       TextView blogTitle;
            @Bind(R.id.author)      TextView blogAuthor;
            @Bind(R.id.blogTime)    TextView blogTime;
            @Bind(R.id.summary)     TextView blogSummary;
            @Bind(R.id.blogLan)     TextView blogLan;
            @Bind(R.id.blogZan)     TextView blogZan;
            @Bind(R.id.image)       ImageView image;

            public ViewHolder(View mView){
                ButterKnife.bind(this, mView);
            }
        }
    }
}

