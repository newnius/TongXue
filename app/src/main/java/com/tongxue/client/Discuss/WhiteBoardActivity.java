package com.tongxue.client.Discuss;

import android.content.ContentResolver;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Base.LearnApplication;
import com.tongxue.client.R;
import com.tongxue.client.Utils.Utils;
import com.tongxue.connector.CallBackInterface;
import com.tongxue.connector.ErrorCode;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Receiver;
import com.tongxue.connector.RequestCode;
import com.tongxue.connector.Server;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/20.
 */
public class WhiteBoardActivity extends BaseActivity implements CallBackInterface {
    @Bind(R.id.myView)
    MyView myView;
    @Bind(R.id.bt_pen)
    Button bt_pen;
    @Bind(R.id.bt_text)
    Button bt_text;
    @Bind(R.id.bt_pic)
    Button bt_pic;
    @Bind(R.id.bt_size)
    Button bt_size;
    @Bind(R.id.bt_color)
    Button bt_color;
    @Bind(R.id.bt_undo)
    Button bt_undo;
    @Bind(R.id.bt_redo)
    Button bt_redo;
    @Bind(R.id.bt_clear)
    Button bt_clear;
    @Bind(R.id.bt_save)
    Button bt_save;
    @Bind(R.id.bt_voice)
    Button bt_voice;
    @Bind(R.id.finish)
    TextView finish;
    @Bind(R.id.penSizeLayout)
    LinearLayout penSizeLayout;
    @Bind(R.id.penSizeIv)
    PenSizeView penSizeIv;
    @Bind(R.id.penSizeSb)
    SeekBar penSizeSb;
    @Bind(R.id.moveIv)
    ImageView moveIv;
    @Bind(R.id.chat_list)
    ListView chatView;
    @Bind(R.id.send)
    Button btn_send;
    @Bind(R.id.message)
    EditText messageInput;


    public static int width;
    public static int height;
    public static int ScreenWidth;
    public static int ScreenHeight;
    public String filename;
    private List<HashMap<String, Object>> messageList;
    private SimpleAdapter adapterForChatList;
    private int discussID = 0;


    // When an android device changes orientation usually the activity is destroyed and recreated with a new
    // orientation layout. This method, along with a setting in the the manifest for this activity
    // tells the OS to let us handle it instead.
    //
    // This increases performance and gives us greater control over activity creation and destruction for simple
    // activities.
    //
    // Must place this into the AndroidManifest.xml file for this activity in order for this to work properly
    //   android:configChanges="keyboardHidden|orientation"
    //   optionally
    //   android:screenOrientation="landscape"
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            messageList = new ArrayList<>();

            //创建SimpleAdapter适配器将数据绑定到item显示控件上
            adapterForChatList = new SimpleAdapter(WhiteBoardActivity.this, messageList, R.layout.item_whiteboard_message,
                    new String[]{"sender", "content"}, new int[]{R.id.sender, R.id.content});

            if (getIntent().hasExtra("discussID")) {
                discussID = getIntent().getIntExtra("discussID", 0);
                joinDiscuss(discussID);
            } else {
                //do create discuss
                createDiscuss(1);
            }
            Log.i("discuss", discussID + "");

            WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
            ScreenWidth = wm.getDefaultDisplay().getWidth();
            ScreenHeight = wm.getDefaultDisplay().getHeight();
            setContentView(R.layout.activity_group_whiteboard);
            ButterKnife.bind(this);
            initSize();

            Receiver.attachCallback(RequestCode.NEW_BOARD_MESSAGE, this);


            initChatList(discussID);

            //实现列表的显示
            chatView.setAdapter(adapterForChatList);

            bt_pen.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = bt_pen.getText().toString().trim();
                    if (str.equals("橡皮")) {
                        myView.setEraser();
                        bt_pen.setText("画笔");
                    } else {
                        myView.setPen();
                        bt_pen.setText("橡皮");
                    }
                }
            });

            bt_voice.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast("声音已关闭");
                }
            });


            bt_text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast("暂不提供此功能");
                    myView.setLine();
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
                        Toast.makeText(getApplicationContext(), "截屏成功,已保存到手机相册", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "截屏失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btn_send.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast(messageInput.getText().toString());
                    if (messageInput.getText().toString().length() != 0) {
                        TXObject message = new TXObject();
                        message.set("discussID", discussID);
                        message.set("type", "text");
                        message.set("username", LearnApplication.preferences.getString("username", "sender"));
                        message.set("content", messageInput.getText().toString());
                        addNewMessage(message);
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
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    penSizeIv.setPaintSize(progress);
                    myView.setSize(progress);
                }
            });

            finish.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void joinDiscuss(final int discussID) {
        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                TXObject discuss = new TXObject();
                discuss.set("discussID", discussID);
                Msg msg = Server.joinDiscuss(discuss);
                return msg;
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    Log.i("join", "ok");
                    getActions(discussID);
                } else {
                    Log.i("join", ErrorCode.getMsg(msg.getCode()));
                }

            }
        }.execute();
    }

    private void quitDiscuss(final int discussID) {
        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                TXObject discuss = new TXObject();
                discuss.set("discussID", discussID);
                Msg msg = Server.quitDiscuss(discuss);
                return msg;
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    Log.i("quit", "ok");
                } else {
                    Log.i("quit", ErrorCode.getMsg(msg.getCode()));
                }

            }
        }.execute();
    }

    private void getActions(final int discussID) {
        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                TXObject discuss = new TXObject();
                discuss.set("discussID", discussID);
                Msg msg = Server.getBoardActions(discuss);
                return msg;
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    Log.i("board", "ok");
                } else {
                    Log.i("board", ErrorCode.getMsg(msg.getCode()));
                }

            }
        }.execute();
    }

    private void createDiscuss(final int groupID) {
        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                TXObject discuss = new TXObject();
                discuss.set("groupID", groupID);
                discuss.set("name", "discuss" + groupID);
                Msg msg = Server.createDiscuss(discuss);
                return msg;
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    Log.i("create", "ok");
                } else {
                    Log.i("create", ErrorCode.getMsg(msg.getCode()));
                }

            }
        }.execute();
    }


    public void addNewMessage(final TXObject message) {
        try {
            new AsyncTask<Void, Void, Msg>() {
                @Override
                protected Msg doInBackground(Void... params) {
                    Msg msg = Server.sendDiscussMessage(message);
                    return msg;
                }

                @Override
                protected void onPostExecute(Msg msg) {
                    super.onPostExecute(msg);
                    if (msg.getCode() == ErrorCode.SUCCESS) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("sender", message.get("username"));
                        item.put("content", message.get("content"));
                        messageList.add(0, item);
                        adapterForChatList.notifyDataSetChanged();
                    }
                }
            }.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void initChatList(final int discussID) {
        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                TXObject discuss = new TXObject();
                discuss.set("discussID", discussID);
                Msg msg = Server.getDiscussMessage(discuss);
                return msg;
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    List<TXObject> messages = (List<TXObject>) msg.getObj();
                    for (final TXObject message : messages) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("sender", message.get("username"));
                        item.put("content", message.get("content"));
                        messageList.add(item);
                    }
                    adapterForChatList.notifyDataSetChanged();
                }

            }
        }.execute();
    }

    public void initSize() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("activity", "destroy");
        quitDiscuss(discussID);
        Receiver.detachCallback(RequestCode.NEW_BOARD_MESSAGE);
    }

    @Override
    public void callBack(final Msg msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("board", "new message");
                String tmp = new Gson().toJson(msg.getObj());
                TXObject message = new Gson().fromJson(tmp, TXObject.class);
                HashMap<String, Object> item = new HashMap<>();
                item.put("sender", message.get("username"));
                item.put("content", message.get("content"));
                messageList.add(0, item);
                adapterForChatList.notifyDataSetChanged();
            }
        });
    }

}
