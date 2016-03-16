package com.tongxue.client.Blog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tongxue.connector.Article;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.BaseBarActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Utils;
import com.tongxue.client.View.NoScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by chaosi on 2015/10/10.
 */
public class BlogInfoActivity extends BaseBarActivity {
    @Bind(R.id.toolbar)          Toolbar toolbar;
    @Bind(R.id.back)             ImageView back;
    @Bind(R.id.view)             View view;
    @Bind(R.id.send)             Button send;
    @Bind(R.id.comment)          EditText comment;
    @Bind(R.id.blogUser)         TextView blogUserTv;
    @Bind(R.id.blogTime)         TextView blogTimeTv;
    @Bind(R.id.blogTitle)        TextView blogTitleTv;
    @Bind(R.id.blogLan)          TextView blogLanTv;
    @Bind(R.id.blogZan)          TextView blogZanTv;
    @Bind(R.id.blogLun)          TextView blogLunTv;
    @Bind(R.id.blogLunIv)        ImageView blogLunIv;
    @Bind(R.id.blogZanIv)        ImageView blogZanIv;
    @Bind(R.id.blogContent)      RichEditor blogContentRe;
    @Bind(R.id.noComment)        TextView noComment;
    @Bind(R.id.addCommentLayout) LinearLayout addCommentLayout;
    @Bind(R.id.commentListView)  NoScrollListView listView;
    @Bind(R.id.bottom1)          LinearLayout bottom1;
    @Bind(R.id.bottom2)          RelativeLayout bottom2;
    public MenuInflater menuInflater;
    public List<Map<String, Object>> list;
    public SimpleAdapter adapter;
    public String blogUser;
    public String blogTime;
    public String blogTitle;
    public String blogContent;
    public int blogLan;
    public int blogZan;
    public int blogLun;
    public int blogId;
    public int commentNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_info);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        blogId= intent.getIntExtra("blogId",0);
        blogUser= intent.getStringExtra("blogUser");
        blogTime= intent.getStringExtra("blogTime");
        blogTitle= intent.getStringExtra("blogTitle");
        blogContent= intent.getStringExtra("blogContent");
        blogLan= intent.getIntExtra("blogLan", 0)+1;
        blogZan= intent.getIntExtra("blogZan", 0);

        blogContentRe.setEditorHeight(200);
        blogContentRe.setEditorFontSize(20);
        blogContentRe.setEditorFontColor(Color.parseColor("#818181"));
        blogContentRe.setPadding(10, 10, 10, 10);
        blogContentRe.setHtml(blogContent);
        blogContentRe.setOnTouchListener(null);
        blogContentRe.setOnClickListener(null);
        blogContentRe.setOnKeyListener(null);

//        blogContentRe.setText(Html.fromHtml(blogContent));
//        blogContentRe.setMovementMethod(LinkMovementMethod.getInstance());

        blogUserTv.setText(blogUser);
        blogTimeTv.setText(blogTime);
        blogTitleTv.setText(blogTitle);
        blogLanTv.setText("浏览量："+blogLan);
        blogZanTv.setText(blogZan+"");
        blogLunTv.setText("10");

        list = new ArrayList<>();
        getList();
        adapter = new SimpleAdapter(this, list, R.layout.item_list_blog_comment, new String[]{"userName","userTime","comment"}, new int[]{R.id.userName, R.id.userTime, R.id.comment});
        listView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom1.setVisibility(View.GONE);
                bottom2.setVisibility(View.VISIBLE);
            }
        });

        blogLunIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelection(0);
            }
        });

        blogZanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = comment.getText().toString().trim();
                if(str.equals("")){
                    toast("评论内容不能为空");
                }else{
                    sendComment(str);
                }
            }
        });


    }

    public void getList(){
        waitingDialogShow();
        new ServerTask(this){
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject article = new TXObject();
                article.set("articleID", blogId);
                return Server.getCommentsByArticle(article);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if(msg.getCode()==79200){
                    List<TXObject> comments = (List<TXObject>)msg.getObj();
                    commentNum = comments.size();
                    for(TXObject comment : comments){
                        Map map = new HashMap();
                        map.put("userName", comment.get("author"));
                        map.put("userTime", comment.get("time"));
                        map.put("comment", comment.get("content"));
                        list.add(map);
                    }
                    adapter.notifyDataSetChanged();
                    if(commentNum==0){
                        listView.setVisibility(View.GONE);
                        noComment.setVisibility(View.VISIBLE);
                    }
                }
            }
        }.execute();
    }

    public void sendComment(final String content){
        waitingDialogShow();
        new ServerTask(this){
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject comment = new TXObject();
                comment.set("articleID", blogId);
                comment.set("content", content);
                return Server.makeComment(comment);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if(msg.getCode()==78200){
                    String user = LearnApplication.preferences.getString("username","");
                    Map map = new HashMap();
                    map.put("userName", user);
                    map.put("userTime", Utils.formatTime(System.currentTimeMillis()));
                    map.put("comment", content);
                    list.add(map);
                    adapter.notifyDataSetChanged();
                    if(commentNum==0){
                        listView.setVisibility(View.VISIBLE);
                        noComment.setVisibility(View.GONE);
                        commentNum++;
                    }
                    comment.setText("");
                    bottom1.setVisibility(View.VISIBLE);
                    bottom2.setVisibility(View.GONE);
                    toast("评论成功");
                }
            }
        }.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_blog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fen:
                toast("分享成功");
                break;
            case R.id.shou:
                toast("收藏成功");
                break;
            case R.id.ju:
                toast("举报成功");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
