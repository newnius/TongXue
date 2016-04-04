package com.tongxue.client.Discuss;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.tongxue.client.Discuss.actions.ActionFactory;
import com.tongxue.client.Discuss.actions.ClearAction;
import com.tongxue.client.Discuss.actions.LineAction;
import com.tongxue.client.Discuss.actions.RedoAction;
import com.tongxue.client.Discuss.actions.UndoAction;
import com.tongxue.connector.CallBackInterface;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.Receiver;
import com.tongxue.connector.RequestCode;
import com.tongxue.connector.Server;
import com.tongxue.client.Discuss.actions.Action;
import com.tongxue.client.Discuss.actions.CurveAction;
import com.tongxue.client.Discuss.actions.EraserAction;
import com.tongxue.client.R;

/**
 * 涂鸦View
 *
 * @author guo
 */
@SuppressLint("ClickableViewAccessibility")
public class MyView extends SurfaceView implements Callback, CallBackInterface {
    public static String TAG = "WhiteBoard";

    private CanvasContext canvasContext;
    private final SurfaceHolder holder;
    private Action currentAction;

    private TXObject discuss;
    private boolean canOperate = false;

    DrawThread mThread;
    Matrix matrix;


    public void setSize(int weight) {
        canvasContext.setWeight(weight);
    }

    public void setColor(int color) {
        canvasContext.setColor(color);
    }

    private DrawThread getThread() {
        if (mThread == null)
            mThread = new DrawThread();
        return mThread;
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        canvasContext = new CanvasContext();

        /* get screen width and height */
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);

        canvasContext.setScreenWidth(size.x);
        canvasContext.setScreenHeight(size.y);

        holder = this.getHolder();
        holder.addCallback(this);
        this.setFocusable(true);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.smallpaper00);
        canvasContext.setBgBitmap(FitScreenImage(bm));

        Bitmap tmp = Bitmap.createBitmap(canvasContext.getScreenWidth(), canvasContext.getScreenHeight(), Config.ARGB_8888);
        canvasContext.setMainBitmap(tmp);
        canvasContext.setCanvas(new Canvas(tmp));
        canvasContext.getCanvas().setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
    }

    public void init(TXObject discuss){
        this.discuss = discuss;
        Log.i(TAG, "myView init");
        Receiver.attachCallback(RequestCode.NEW_BOARD_ACTION, this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!canOperate){
            Log.i(TAG, "No access to operate on this board");
            return false;
        }
        try {
            int action = event.getAction();
            float mX = event.getX();
            float mY = event.getY();
            if (action == MotionEvent.ACTION_DOWN) {
                setCurAction(mX, mY);
                currentAction.start(new FloatPoint(mX, mY));
            }
            if (action == MotionEvent.ACTION_MOVE) {
                // 滑动时候
                if (currentAction != null) {
                    currentAction.move(new FloatPoint(mX, mY));
                }
            }
            if (action == MotionEvent.ACTION_UP) {
                // 抬起,一次画图action完成，把这次action添加到actionList中以便记录下来可以撤销和前进。
                currentAction.finish(new FloatPoint(mX, mY));
                if (currentAction != null) {
                    canvasContext.getActions().add(currentAction);
                    sendAction(currentAction);
                    currentAction = null;
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }


    private void setCurAction(float mX, float mY) {
        switch (canvasContext.getCurrentActionType()) {
            case Action.ACTION_TYPE_CURVE:// 光滑曲线
                currentAction = new CurveAction(canvasContext);
                break;

            case Action.ACTION_TYPE_ERASER:// 橡皮擦
                currentAction = new EraserAction(canvasContext);
                break;

            case Action.ACTION_TYPE_LINE:
                currentAction = new LineAction(canvasContext);
                break;
        }
        currentAction.start(new FloatPoint(mX, mY));
    }

    /*
     * 把图片转换成一张全屏的图片
     */
    private Bitmap FitScreenImage(Bitmap m) {
        float width = (float) (canvasContext.getScreenWidth()) / m.getWidth();
        float height = (float) (canvasContext.getScreenHeight()) / m.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(width, height);
        return Bitmap.createBitmap(m, 0, 0, m.getWidth(), m.getHeight(), matrix, true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRun = true;
        getThread().start();
        Log.i(TAG, "surfaceCreated...");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged...");
    }

    /*
     * surfaceview失去焦点，
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            isFreeze = true;
            Log.i(TAG, "noWindowFocus..");
        } else {
            Log.i(TAG, "hasWindowFocus");
            isFreeze = false;
        }
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRun = false;
        boolean retry = true;
        while (retry) {
            try {
                getThread().join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mThread = null;
        Log.i(TAG, "surfaceDestroyed...");
    }

    @Override
    public void callBack(Msg msg) {
        Log.i(TAG, "new action");
        try {
            String obj = new Gson().toJson(msg.getObj());
            TXObject command = new Gson().fromJson(obj, TXObject.class);
            Action action = ActionFactory.toAction(command, canvasContext);
            if (action != null) {
                if (action instanceof CurveAction || action instanceof EraserAction || action instanceof LineAction)
                    canvasContext.getActions().add(action);
                action.draw(command);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    boolean isRun = false;
    boolean isFreeze = false;

    /*
     * 画图线程
     */
    class DrawThread extends Thread {
        @Override
        public void run() {

            while (isRun) {
                Canvas mCanvas = null;
                try {
                    mCanvas = holder.lockCanvas();
                    synchronized (holder) {
                        drawView(mCanvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mCanvas != null)
                        holder.unlockCanvasAndPost(mCanvas);
                    if (isFreeze) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    public void drawView(Canvas mCanvas) {
        // 画背景
        mCanvas.drawBitmap(canvasContext.getBgBitmap(), 0, 0, null);
        // 画打开的图片--如，相册的照片。
        if (canvasContext.getImageBitmap() != null)
            mCanvas.drawBitmap(canvasContext.getImageBitmap(), matrix, null);
        mCanvas.drawBitmap(canvasContext.getMainBitmap(), 0, 0, null);
    }

    // 撤销
    public void undo() {
        if(!canOperate)
            return;
        new UndoAction(canvasContext).draw(null);
        sendAction(new UndoAction(canvasContext));
    }

    /*
     * 取消撤销，前进
     */
    public void redo() {
        if(!canOperate)
            return;
        new RedoAction(canvasContext).draw(null);
        sendAction(new RedoAction(canvasContext));
    }

    /*
     *
     */
    public void setBitmap(Bitmap bm) {
        if(!canOperate)
            return;
        matrix = new Matrix();
        int trX = 0;
        if (bm.getWidth() < canvasContext.getScreenWidth()) {
            trX = canvasContext.getScreenWidth() / 2 - bm.getWidth() / 2;
        }
        matrix.postTranslate(trX, canvasContext.getScreenHeight() / 2 - bm.getHeight() / 2);
        // m 是图片居中显示
        canvasContext.setImageBitmap(bm);
    }

    /*
     * 清屏操作
     */
    public void clear() {
        if(!canOperate)
            return;
        new ClearAction(canvasContext).draw(null);
        sendAction(new ClearAction(canvasContext));
    }

    /*
     * 用于保存涂鸦完，按了保存按钮返回的图片
     */
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(canvasContext.getScreenWidth(), canvasContext.getScreenHeight(), Config.ARGB_8888);
        Canvas tCanvas = new Canvas(bitmap);
        tCanvas.drawBitmap(canvasContext.getBgBitmap(), 0, 0, null);
        if (canvasContext.getImageBitmap() != null) {
            tCanvas.drawBitmap(canvasContext.getImageBitmap(), matrix, null);
        }
        tCanvas.drawBitmap(canvasContext.getMainBitmap(), 0, 0, null);
        tCanvas.save(Canvas.ALL_SAVE_FLAG);
        tCanvas.restore();
        return bitmap;
    }

    public void setEraser() {
        if(!canOperate)
            return;
        canvasContext.setCurrentActionType(Action.ACTION_TYPE_ERASER);
    }

    public void setPen() {
        if(!canOperate)
            return;
        canvasContext.setCurrentActionType(Action.ACTION_TYPE_CURVE);
    }

    public void setLine() {
        if(!canOperate)
            return;
        canvasContext.setCurrentActionType(Action.ACTION_TYPE_LINE);
    }

    public void sendAction(Action action) {
        final TXObject command = action.toCommand();
        if (command == null) {
            Log.i(TAG, "command is null");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                command.set("discussID", discuss.getInt("discussID"));
                Server.sendBoardAction(command);
            }
        }).start();
    }

    public void setCanOperate(boolean value){
        this.canOperate = value;
    }

}
