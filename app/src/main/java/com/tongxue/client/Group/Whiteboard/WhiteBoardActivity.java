package com.tongxue.client.Group.Whiteboard;

import android.content.ContentResolver;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import java.io.FileNotFoundException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Group.GroupChatActivity;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/20.
 */
public class WhiteBoardActivity extends BaseActivity{
    @Bind(R.id.myView)           MyView myView;
    @Bind(R.id.bt_pen)           Button bt_pen;
    @Bind(R.id.bt_text)          Button bt_text;
    @Bind(R.id.bt_pic)           Button bt_pic;
    @Bind(R.id.bt_size)          Button bt_size;
    @Bind(R.id.bt_color)         Button bt_color;
    @Bind(R.id.bt_undo)          Button bt_undo;
    @Bind(R.id.bt_redo)          Button bt_redo;
    @Bind(R.id.bt_clear)         Button bt_clear;
    @Bind(R.id.bt_save)          Button bt_save;
    @Bind(R.id.cancel)           TextView cancel;
    @Bind(R.id.finish)           TextView finish;
    @Bind(R.id.penSizeLayout)    LinearLayout penSizeLayout;
    @Bind(R.id.penSizeIv)        PenSizeView penSizeIv;
    @Bind(R.id.penSizeSb)        SeekBar penSizeSb;
    @Bind(R.id.moveIv)           ImageView moveIv;
    public static int width;
    public static int height;
    public static int ScreenWidth;
    public static int ScreenHeight;
    public String filename;
    public String groupName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupName = getIntent().getStringExtra("groupName");
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        ScreenWidth = wm.getDefaultDisplay().getWidth();
        ScreenHeight = wm.getDefaultDisplay().getHeight();
        setContentView(R.layout.activity_group_whiteboard);
        ButterKnife.bind(this);
        initSize();


        bt_pen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = bt_pen.getText().toString().trim();
                if(str.equals("橡皮")){
                    myView.setEraser();
                    bt_pen.setText("画笔");
                }else{
                    myView.setPen();
                    bt_pen.setText("橡皮");
                }
            }
        });

        bt_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("暂不提供此功能");
            }
        });

        bt_pic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        bt_size.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penSizeLayout.getVisibility() == View.GONE) {
                    penSizeLayout.bringToFront();
                    penSizeLayout.setVisibility(View.VISIBLE);
                } else {
                    penSizeLayout.setVisibility(View.GONE);
                }
            }
        });

        bt_color.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerDialog(WhiteBoardActivity.this, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(int color) {
                        penSizeIv.setPaintColor(color);
                        myView.setColor(color);
                    }
                }, Color.BLUE).show();
            }
        });

        bt_undo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.undo();
            }
        });

        bt_redo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.redo();
            }
        });

        bt_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.clear();
            }
        });

        bt_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm = myView.getBitmap();
                filename = Utils.getRandomString(15);
                waitingDialogShow();
                boolean b = Utils.savePic(bm, filename);
                waitingDialogDismiss();
                if (b) {
                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "保存失敗", Toast.LENGTH_SHORT).show();
                }
            }
        });

        penSizeSb.setMax(80);
        penSizeSb.setProgress(10);
        penSizeSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                penSizeIv.setPaintSize(progress);
                myView.setSize(progress);
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                waitingDialogShow();
                final ChatManager chatManager = ChatManager.getInstance();
                chatManager.fetchConversationWithGroupName(groupName, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation conversation, AVException e) {
                        if (e != null) {
                            e.getMessage();
                            waitingDialogDismiss();
                            toast("网络错误");
                        } else {
                            chatManager.registerConversation(conversation);
                            Intent intent = new Intent(WhiteBoardActivity.this, GroupChatActivity.class);
                            intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                            intent.putExtra("group_name",groupName);
                            intent.putExtra("filename",filename);
                            waitingDialogDismiss();
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.common_left_in, R.anim.common_right_out);
                        }
                    }
                });
            }
        });

        finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm = myView.getBitmap();
                filename = Utils.getRandomString(15);
                waitingDialogShow();
                boolean b = Utils.savePic(bm, filename);

                if(b){
                    final ChatManager chatManager = ChatManager.getInstance();
                    chatManager.fetchConversationWithGroupName(groupName, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation conversation, AVException e) {
                            if (e != null) {
                                e.getMessage();
                                waitingDialogDismiss();
                                toast("网络错误");
                            } else {
                                chatManager.registerConversation(conversation);
                                Intent intent = new Intent(WhiteBoardActivity.this, GroupChatActivity.class);
                                intent.putExtra(ChatActivity.CONVID, conversation.getConversationId());
                                intent.putExtra("group_name",groupName);
                                intent.putExtra("filename",filename);
                                waitingDialogDismiss();
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.common_left_in, R.anim.common_right_out);
                            }
                        }
                    });
                }else{
                    toast("绘图中出现错误");
                }
            }
        });

    }

    public void initSize(){
        ViewTreeObserver vto = myView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                myView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = myView.getWidth();
                height = myView.getHeight();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 10:
                if (resultCode == Activity.RESULT_CANCELED)
                    return;
                if (data == null)
                    return;
                Uri uri = data.getData();
                if (uri == null)
                    return;
                getBitmap(uri);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getBitmap(final Uri uri) {

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                ContentResolver cr = WhiteBoardActivity.this.getContentResolver();
                try {
                    return BitmapFactory.decodeStream(cr.openInputStream(uri));
                } catch (FileNotFoundException e) {
                    log(e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (result != null)
                    myView.setBitmap(result);
            }
        }.execute();

    }

}
