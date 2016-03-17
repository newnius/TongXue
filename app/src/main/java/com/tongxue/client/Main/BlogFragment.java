package com.tongxue.client.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.BaseFragment;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.Blog.BlogInfoActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Config;
import com.tongxue.client.Utils.Utils;
import com.tongxue.client.View.PullableView.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/14.
 */
public class BlogFragment extends BaseFragment {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    public View mView;
    public Context mContext;
    public FragmentManager mFragmentManager;
    public List<View> mViewList;
    public List<String> mTitleList;
    public ListView newListView, favListView;
    public ListAdapter newAdapter, favAdapter;
    public PullToRefreshLayout newLayout, favLayout;
    public List<Map<String, Object>> newList, favList;
    public int blogIndex = 0;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.init();
        mContext = getActivity();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mView = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, mView);

        mTitleList = new ArrayList<>();
        mViewList = new ArrayList<>();
        newList = MainActivity.newList;
        favList = MainActivity.favList;

        mTitleList.add("最新博客");
        mTitleList.add("排行榜");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View newView = mInflater.inflate(R.layout.fragment_list_blog, null);
        View favView = mInflater.inflate(R.layout.fragment_list_blog, null);
        mViewList.add(newView);
        mViewList.add(favView);

        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList, mTitleList);
        viewPager.setAdapter(mAdapter);                 //给ViewPager设置适配器
        tabLayout.setupWithViewPager(viewPager);        //将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);    //给Tabs设置适配器

        newLayout = (PullToRefreshLayout) newView;
        favLayout = (PullToRefreshLayout) favView;
        newListView = (ListView) newLayout.getChildAt(1);
        favListView = (ListView) favLayout.getChildAt(1);

        newAdapter = new ListAdapter(mContext, newList);
        newListView.setAdapter(newAdapter);
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                waitingDialogShow();
                LookArticleById(position);
            }
        });
        newLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNewData(1);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newLayout.loadmoreFinish(PullToRefreshLayout.FULL);
                    }
                }, 1000);
            }
        }, 2);

        favAdapter = new ListAdapter(mContext, favList);
        favListView.setAdapter(favAdapter);
        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                waitingDialogShow();
                LookArticleById(position);
            }
        });
        favLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFavData(1);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        favLayout.loadmoreFinish(PullToRefreshLayout.FULL);
                    }
                }, 1000);
            }
        }, 3);

        if (MainActivity.shouldBlogRefresh) {
            getNewData(0);
            getFavData(0);
            blogIndex = new Random().nextInt(10);
            MainActivity.shouldBlogRefresh = false;
        }

        return mView;
    }

    private void getNewData(final int flag) {
        new ServerTask(mContext) {
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject article = new TXObject();
                return Server.searchArticle(article);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if (msg.getCode() == 74200) {
                    newList.clear();
                    List<TXObject> articles = (List<TXObject>) msg.getObj();
                    int i = blogIndex;
                    for (TXObject article : articles) {
                        Map map = new HashMap();
                        map.put("blogId", article.get("articleID"));
                        map.put("blogAuthor", article.get("author"));
                        map.put("blogTime", Utils.formatTime(article.getLong("time")));
                        map.put("blogTitle", article.get("title"));
                        map.put("blogContent", article.get("content"));
                        map.put("blogLan", article.get("views"));
                        map.put("blogZan", article.get("upVotes"));
                        map.put("blogCover", Config.bg[(i++) % 10]);
                        newList.add(map);
                    }
                    if (articles.size() == 0) {
                        toast("暂无更多");
                    }
                    MainActivity.newList = newList;
                    newAdapter.notifyDataSetChanged();
                    if (flag == 1) newLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    if (flag == 1) newLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }
        }.execute();
    }

    private void getFavData(final int flag) {
        new ServerTask(mContext) {
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject article = new TXObject();
                return Server.searchArticle(article);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if (msg.getCode() == 75200) {
                    favList.clear();
                    List<TXObject> articles = (List<TXObject>) msg.getObj();
                    int i = blogIndex;
                    for (TXObject article : articles) {
                        Map map = new HashMap();
                        map.put("blogId", article.get("articleID"));
                        map.put("blogAuthor", article.get("author"));
                        map.put("blogTime", Utils.formatTime(article.getLong("time")));
                        map.put("blogTitle", article.get("title"));
                        map.put("blogContent", article.get("content"));
                        map.put("blogLan", article.get("views"));
                        map.put("blogZan", article.get("upVotes"));
                        map.put("blogCover", Config.bg[(i++) % 10]);
                        favList.add(map);
                    }
                    if (articles.size() == 0) {
                        toast("暂无更多");
                    }
                    MainActivity.favList = favList;
                    favAdapter.notifyDataSetChanged();
                    if (flag == 1) favLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    if (flag == 1) favLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }
        }.execute();
    }

    public void LookArticleById(final int position) {
        new ServerTask(mContext) {
            @Override
            protected Msg doInBackground(Object... params) {
                int blogId = (int) newList.get(position).get("blogId");
                TXObject article = new TXObject();
                article.set("articleID", blogId);
                return Server.searchArticle(article);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if (msg.getCode() == 72200) {
                    TXObject article = (TXObject) msg.getObj();
                    Intent intent = new Intent(mContext, BlogInfoActivity.class);
                    intent.putExtra("blogId", article.get("articleID"));
                    intent.putExtra("blogUser", article.get("author"));
                    intent.putExtra("blogTime", Utils.formatTime(article.getLong("time")));
                    intent.putExtra("blogTitle", article.get("time"));
                    intent.putExtra("blogContent", article.get("content"));
                    intent.putExtra("blogLan", article.get("views"));
                    intent.putExtra("blogZan", article.get("upVotes"));
                    startActivity(intent);
                }
            }
        }.execute();
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;
        private List<String> mTitleList;

        public MyPagerAdapter(List<View> mViewList, List<String> mTitleList) {
            this.mViewList = mViewList;
            this.mTitleList = mTitleList;
        }

        @Override
        public int getCount() {
            return mViewList.size();                             //页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;                              //官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));          //添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));      //删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);                    //页卡标题
        }
    }

    public class ListAdapter extends BaseAdapter {
        public LayoutInflater mInflater;
        public List<Map<String, Object>> mList;

        public ListAdapter(Context c, List<Map<String, Object>> list) {
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

            log("" + position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_list_blog, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.blogAuthor.setText(mList.get(position).get("blogAuthor").toString());
            holder.blogTime.setText(mList.get(position).get("blogTime").toString());
            holder.blogTitle.setText(mList.get(position).get("blogTitle").toString());
            String content = "　" + Html.fromHtml(mList.get(position).get("blogContent").toString()).toString();
            holder.blogSummary.setText(content);
            holder.image.setImageDrawable(getResources().getDrawable((int) mList.get(position).get("blogCover")));
            holder.blogLan.setText(mList.get(position).get("blogLan").toString());
            holder.blogZan.setText(mList.get(position).get("blogZan").toString());

            return convertView;
        }

        public class ViewHolder {
            @Bind(R.id.title)
            TextView blogTitle;
            @Bind(R.id.author)
            TextView blogAuthor;
            @Bind(R.id.blogTime)
            TextView blogTime;
            @Bind(R.id.summary)
            TextView blogSummary;
            @Bind(R.id.blogLan)
            TextView blogLan;
            @Bind(R.id.blogZan)
            TextView blogZan;
            @Bind(R.id.image)
            ImageView image;

            public ViewHolder(View mView) {
                ButterKnife.bind(this, mView);
            }
        }
    }

}
