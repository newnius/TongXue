package com.tongxue.client.Discuss;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
    @Bind(R.id.chat_list)
    ListView chatView;
    @Bind(R.id.send)
    Button btn_send;
    @Bind(R.id.message)
    EditText messageInput;

    @Bind(R.id.bt_hide)
    Button bt_hide;

    @Bind(R.id.toolbar)
    LinearLayout toolbarLayout;


    public static int ScreenWidth;
    public static int ScreenHeight;
    private List<HashMap<String, Object>> messageList;
    private SimpleAdapter adapterForChatList;
    private TXObject currentDiscuss = null;

    private final String TAG = "WhiteBoard";

    public static final int DISCUSS_STATUS_LIVE = 0;
    public static final int DISCUSS_STATUS_PAUSE = 1;
    public static final int DISCUSS_STATUS_FINISHED = 2;


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
            super.onCreate(savedInstanceState);
        try {
            /* init chat panel*/
            messageList = new ArrayList<>();
            adapterForChatList = new SimpleAdapter(WhiteBoardActivity.this, messageList, R.layout.item_whiteboard_message,
                    new String[]{"sender", "content"}, new int[]{R.id.sender, R.id.content});

            /* get screen width and height */
            WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            ScreenWidth = size.x;
            ScreenHeight = size.y;

            setContentView(R.layout.activity_group_whiteboard);
            ButterKnife.bind(this);

            chatView.setAdapter(adapterForChatList);
            /* prevent from operating before initialed */
            //waitingDialog.show();

            /* judge if join or create */
            if (!getIntent().hasExtra("discussID")) {
                // 1 is groupID, tmp value
                createDiscuss(1);
            } else {
                int discussID = getIntent().getIntExtra("discussID", 0);
                getInformation(discussID);
                Log.i("discuss", discussID + "");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void init(){
        Log.i(TAG, "init");
        /* two ifs can not be reOrdered */
        if(currentDiscuss.get("controller").equals(LearnApplication.preferences.getString("username",""))){
            myView.setCanOperate(true);
        }

        if(currentDiscuss.getInt("status") != DISCUSS_STATUS_LIVE ){
            myView.setCanOperate(false);
        }

        initChatList();
        initListeners();
        myView.init(currentDiscuss);
        Receiver.attachCallback(RequestCode.NEW_BOARD_MESSAGE, this);
        waitingDialog.dismiss();

        play(currentDiscuss.getInt("status") == DISCUSS_STATUS_FINISHED);
    }

    private void joinDiscuss(final TXObject discuss) {
        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                Msg msg = Server.joinDiscuss(discuss);
                return msg;
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    Log.i(TAG, "Join discuss " + discuss.getInt("discussID"));
                    currentDiscuss = discuss;
                    init();
                } else {
                    Log.i(TAG, ErrorCode.getMsg(msg.getCode()));
                    toast("Join discuss fail because " + ErrorCode.getMsg(msg.getCode()));
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
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    Log.i(TAG, "Quit discuss " + discussID);
                } else {
                    Log.i(TAG, "Quit discuss fail, " + ErrorCode.getMsg(msg.getCode()));
                }
                return msg;
            }
        }.execute();
    }


    private void createDiscuss(final int groupID) {
        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                TXObject discuss = new TXObject();
                discuss.set("groupID", groupID);
                discuss.set("name", "Discuss Of " + groupID);
                discuss.set("introduction", "Introduction");
                Msg msg = Server.createDiscuss(discuss);
                return msg;
            }

            @Override
            protected void onPostExecute(Msg msg) {
                super.onPostExecute(msg);
                if (msg.getCode() == ErrorCode.SUCCESS) {
                    Log.i(TAG, "create discuss success");
                    try {
                        currentDiscuss = (TXObject) msg.getObj();
                        currentDiscuss.set("status", 0);
                        init();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                } else {
                    Log.i(TAG, "create discuss fail " + ErrorCode.getMsg(msg.getCode()));
                    toast("Create discuss fail, because " + ErrorCode.getMsg(msg.getCode()));
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


    public void initChatList() {
        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                /* create new object to cut down json length */
                TXObject discuss = new TXObject();
                discuss.set("discussID", currentDiscuss.getInt("discussID"));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
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
        Log.i(TAG, "onDestroy");
        if(currentDiscuss!=null) {
            quitDiscuss(currentDiscuss.getInt("discussID"));
            Receiver.detachCallback(RequestCode.NEW_BOARD_MESSAGE);
        }
    }

    @Override
    public void callBack(final Msg msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "New message");
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


    private void play(final boolean isReplay) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /* create new object to cut down json length */
                TXObject discuss = new TXObject();
                discuss.set("discussID", currentDiscuss.getInt("discussID"));
                Msg msg = Server.getBoardActions(discuss);
                try {
                    if (msg.getCode() == ErrorCode.SUCCESS) {
                        Log.i(TAG, "Commands fetched");
                        long t = currentDiscuss.getLong("time");
                        for (final TXObject action : (List<TXObject>) msg.getObj()) {
                            if (t != 0 && isReplay)
                                Thread.sleep((action.getLong("time") - t));
                            t = action.getLong("time");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myView.callBack(new Msg(RequestCode.NEW_BOARD_ACTION, action));
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(isReplay)
                                    toast("Replay finished");
                            }
                        });
                    } else {
                        Log.i(TAG, "Play fail " + ErrorCode.getMsg(msg.getCode()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getInformation(final int discussID) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_discuss_detail,
                (ViewGroup) findViewById(R.id.discuss_detail));
        final AlertDialog.Builder builder = new AlertDialog.Builder(WhiteBoardActivity.this);
        builder.setTitle("讨论组信息").setView(layout);
        builder.setNegativeButton("leave", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WhiteBoardActivity.this.finish();
            }
        });

        new AsyncTask<Void, Void, Msg>() {
            @Override
            protected Msg doInBackground(Void... params) {
                TXObject discuss = new TXObject();
                discuss.set("discussID", discussID);
                Msg msg = Server.getDiscussById(discuss);
                return msg;
            }

            @Override
            protected void onPostExecute(Msg msg) {
                try {
                    if (msg.getCode() == ErrorCode.SUCCESS) {
                        final TXObject discuss = (TXObject) msg.getObj();
                        discuss.set("discussID", discussID);
                        builder.setPositiveButton("join", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                joinDiscuss(discuss);
                            }
                        });
                        AlertDialog dialog = builder.show();
                        final TextView nameView = (TextView) dialog.findViewById(R.id.discuss_name);
                        final TextView introductionView = (TextView) dialog.findViewById(R.id.discuss_introduction);
                        final TextView controllerView = (TextView) dialog.findViewById(R.id.discuss_controller);
                        final TextView statusView = (TextView) dialog.findViewById(R.id.discuss_status);
                        final TextView publicView = (TextView) dialog.findViewById(R.id.discuss_public);
                        nameView.setText(discuss.get("name"));
                        introductionView.setText(discuss.get("introduction"));
                        controllerView.setText(discuss.get("controller"));
                        statusView.setText(discuss.getInt("status") == 0 ? "直播中" : "已结束");
                        publicView.setText(discuss.getInt("public") == 0 ? "公开" : "有权限");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void initListeners(){
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
                String filename = Utils.getRandomString(15);
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
                if (messageInput.getText().toString().length() != 0) {
                    TXObject message = new TXObject();
                    message.set("discussID", currentDiscuss.getInt("discussID"));
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

        bt_hide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

}
