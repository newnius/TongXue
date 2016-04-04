package com.tongxue.client.Blog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongxue.connector.ErrorCode;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Server;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.ServerTask;
import com.tongxue.client.Main.MainActivity;
import com.tongxue.client.R;
import com.tongxue.client.View.AlertDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by chaosi on 2015/10/8.
 */
public class BlogEditActivity extends BaseActivity{
    @Bind(R.id.editor)  RichEditor mEditor;
    @Bind(R.id.undo)    ImageView undo;
    @Bind(R.id.redo)    ImageView redo;
    @Bind(R.id.text)    ImageView text;
    @Bind(R.id.add)     ImageView add;
    @Bind(R.id.title)   EditText title;
    @Bind(R.id.bold)    ImageView bold;
    @Bind(R.id.italic)  ImageView italic;
    @Bind(R.id.quote)   ImageView quote;
    @Bind(R.id.h1)      ImageView h1;
    @Bind(R.id.h2)      ImageView h2;
    @Bind(R.id.h3)      ImageView h3;
    @Bind(R.id.h4)      ImageView h4;
    @Bind(R.id.back)    TextView back;
    @Bind(R.id.publish) TextView publish;
    @Bind(R.id.addLine) LinearLayout addLine;
    @Bind(R.id.addImg)  LinearLayout addImg;
    @Bind(R.id.addLink) LinearLayout addLink;
    @Bind(R.id.footer)  HorizontalScrollView footer;
    @Bind(R.id.footer2) LinearLayout footer2;
    public boolean EditorHasFocus = false;
    public AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_edit);
        ButterKnife.bind(this);

        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(20);
        mEditor.setEditorFontColor(Color.parseColor("#818181"));
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Click here ...");

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                log(text);
            }
        });

        mEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditorHasFocus = hasFocus;
                if(hasFocus){
                    text.setImageDrawable(getResources().getDrawable(R.drawable.t_text));
                    add.setImageDrawable(getResources().getDrawable(R.drawable.t_add));
                }else{
                    text.setImageDrawable(getResources().getDrawable(R.drawable.d_text));
                    add.setImageDrawable(getResources().getDrawable(R.drawable.d_add));
                }
                footer.setVisibility(View.GONE);
                footer2.setVisibility(View.GONE);
            }
        });

        mEditor.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                    bold.setImageDrawable(getResources().getDrawable(R.drawable.t_bold));
                    italic.setImageDrawable(getResources().getDrawable(R.drawable.t_italic));
                    quote.setImageDrawable(getResources().getDrawable(R.drawable.t_quote));
                    h1.setImageDrawable(getResources().getDrawable(R.drawable.t_h1));
                    h2.setImageDrawable(getResources().getDrawable(R.drawable.t_h2));
                    h3.setImageDrawable(getResources().getDrawable(R.drawable.t_h3));
                    h4.setImageDrawable(getResources().getDrawable(R.drawable.t_h4));
                }
                log("keycode:"+keyCode);
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog(BlogEditActivity.this, "是否保存当前文本至草稿？","直接返回","保存后返回"){
                    @Override
                    public void onOkClick() {
                        alertDialog.dismiss();
                        finishThisActivity();
                    }

                    @Override
                    public void onNoClick() {
                        alertDialog.dismiss();
                        finishThisActivity();
                    }
                };
                alertDialog.show();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blogTitle = title.getText().toString();
                String blogContent = mEditor.getHtml()+"";
                log("BlogContent : "+blogContent);
                log("BlogText:"+Html.fromHtml(blogContent));
                if(blogTitle.equals("")){
                    toast("博客标题不能为空！");
                }else if(blogContent.equals("")){
                    toast("博客内容不能为空！");
                }else{
                    PublishBlog(blogTitle, blogContent);
                }

            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            boolean textClicked = false;

            @Override
            public void onClick(View v) {
                if(!EditorHasFocus)  return;
                if(textClicked){
                    footer.setVisibility(View.GONE);
                    text.setImageDrawable(getResources().getDrawable(R.drawable.t_text));
                }else{
                    footer.setVisibility(View.VISIBLE);
                    text.setImageDrawable(getResources().getDrawable(R.drawable.p_text));
                }
                textClicked = !textClicked;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            boolean addClicked = false;

            @Override
            public void onClick(View v) {
                if(!EditorHasFocus)  return;
                if(addClicked){
                    add.setImageDrawable(getResources().getDrawable(R.drawable.t_add));
                    footer2.setVisibility(View.GONE);
                }else{
                    add.setImageDrawable(getResources().getDrawable(R.drawable.p_add));
                    footer2.setVisibility(View.VISIBLE);
                }
                addClicked = !addClicked;
            }
        });

        bold.setOnClickListener(new View.OnClickListener() {
            boolean boldClicked = false;

            @Override
            public void onClick(View v) {
                mEditor.setBold();
                if(boldClicked){
                    bold.setImageDrawable(getResources().getDrawable(R.drawable.t_bold));
                }else{
                    bold.setImageDrawable(getResources().getDrawable(R.drawable.p_bold));
                }
                boldClicked = !boldClicked;
            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            boolean italicClicked = false;

            @Override
            public void onClick(View v) {
                mEditor.setItalic();
                if(italicClicked){
                    italic.setImageDrawable(getResources().getDrawable(R.drawable.t_italic));
                }else{
                    italic.setImageDrawable(getResources().getDrawable(R.drawable.p_italic));
                }
                italicClicked = !italicClicked;
            }
        });

        quote.setOnClickListener(new View.OnClickListener() {
            boolean quoteClicked = false;

            @Override
            public void onClick(View v) {
                if(quoteClicked){
                    mEditor.setHeading(5);
                    quote.setImageDrawable(getResources().getDrawable(R.drawable.t_quote));
                }else{
                    mEditor.setBlockquote();
                    changeHStyle(5);
                }
                quoteClicked = !quoteClicked;
            }
        });

        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
                changeHStyle(1);
            }
        });

        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
                changeHStyle(2);
            }
        });

        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
                changeHStyle(3);
            }
        });

        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
                changeHStyle(4);
            }
        });

        addLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中...");
            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中...");
            }
        });

        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("内测中...");
            }
        });

    }

    public void PublishBlog(final String blogTitle, final String blogContent){
        waitingDialogShow();
        new ServerTask(this){
            @Override
            protected Msg doInBackground(Object... params) {
                TXObject article = new TXObject();
                article.set("title", blogTitle);
                article.set("content", blogContent);
                return Server.postArticle(article);
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                waitingDialogDismiss();
                if(msg.getCode()== ErrorCode.SUCCESS){
                    toast("发布成功！");
                    Intent intent = new Intent(BlogEditActivity.this, MainActivity.class);
                    intent.putExtra("blog", true);
                    startActivity(intent);
                    finishThisActivity();
                }
            }
        }.execute();

    }

    public void changeHStyle(int h){
        h1.setImageDrawable(getResources().getDrawable(R.drawable.t_h1));
        h2.setImageDrawable(getResources().getDrawable(R.drawable.t_h2));
        h3.setImageDrawable(getResources().getDrawable(R.drawable.t_h3));
        h4.setImageDrawable(getResources().getDrawable(R.drawable.t_h4));
        quote.setImageDrawable(getResources().getDrawable(R.drawable.t_quote));
        if(h==1){
            h1.setImageDrawable(getResources().getDrawable(R.drawable.p_h1));
        }else if(h==2){
            h2.setImageDrawable(getResources().getDrawable(R.drawable.p_h2));
        }else if(h==3){
            h3.setImageDrawable(getResources().getDrawable(R.drawable.p_h3));
        }else if(h==4){
            h4.setImageDrawable(getResources().getDrawable(R.drawable.p_h4));
        }else if(h==5){
            quote.setImageDrawable(getResources().getDrawable(R.drawable.p_quote));
        }
    }
}
