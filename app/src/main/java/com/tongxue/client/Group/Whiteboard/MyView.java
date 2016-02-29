package com.tongxue.client.Group.Whiteboard;

import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.tongxue.connector.CallBackInterface;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Receiver;
import com.tongxue.connector.Server;
import com.tongxue.client.Group.Whiteboard.actions.Action;
import com.tongxue.client.Group.Whiteboard.actions.CurveAction;
import com.tongxue.client.Group.Whiteboard.actions.EraserAction;
import com.tongxue.client.Group.Whiteboard.commands.Command;
import com.tongxue.client.Group.Whiteboard.commands.CurveCommand;
import com.tongxue.client.Group.Whiteboard.commands.EraserCommand;
import com.tongxue.client.R;
/**
 * 涂鸦View
 * 
 * @author guo
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class MyView extends SurfaceView implements Callback, CallBackInterface{

    private CanvasContext canvasContext;
    private SurfaceHolder holder;
    private Action currentAction;
    public static String TAG = "WhiteBoard";

	VideoView videoView = (VideoView)findViewById(R.id.video_view);

	// 背景图片
	Bitmap bgBitmap;
    Bitmap picBitmap;
    Bitmap mBitmap;
    DrawThread mThread;
    Matrix m;

	public void setSize(int weight) {
		canvasContext.setWeight(weight);
	}

	public void setColor(int color) {
		canvasContext.setColor(color);
	}

	private DrawThread getThread() {
		if (mThread == null) {
			mThread = new DrawThread();
		}
		return mThread;
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
        canvasContext = new CanvasContext();
		canvasContext.setScreenWidth(WhiteBoardActivity.ScreenWidth);
		canvasContext.setScreenHeight(WhiteBoardActivity.ScreenHeight);

		holder = this.getHolder();
		holder.addCallback(this);
		this.setFocusable(true);
	    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.smallpaper00);
        bgBitmap = FitScreenImage(bm);
        mBitmap = Bitmap.createBitmap(canvasContext.getScreenWidth(), canvasContext.getScreenHeight(), Config.ARGB_8888);
        canvasContext.setCanvas(new Canvas(mBitmap));
        canvasContext.getCanvas().setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        Receiver.attachCallback(50, this);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
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
                final Command command = currentAction.toCommand();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Server.sendBoardAction(new Gson().toJson(new Msg(50, command)));
                    }
                }).start();
                    currentAction = null;
                }
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return true;
	}



	private void setCurAction(float mX, float mY) {
		switch (canvasContext.getCurrentActionType()) {
		    case CanvasContext.ACTION_TYPE_CURVE:// 光滑曲线
			    currentAction = new CurveAction(canvasContext);
			    break;

		    case CanvasContext.ACTION_TYPE_ERASER:// 橡皮擦
			    currentAction = new EraserAction(canvasContext);
			    break;
		}
        currentAction.start(new FloatPoint(mX, mY));
	}

	/*
	 * 创建把图片转换成一张全屏的图片
	 */
	public Bitmap FitScreenImage(Bitmap m) {
		float width = (float) (canvasContext.getScreenWidth()) / m.getWidth();
		float height = (float) (canvasContext.getScreenHeight()) / m.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(width, height);
		return Bitmap.createBitmap(m, 0, 0, m.getWidth(), m.getHeight(),matrix, true);
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
        Command command = new Gson().fromJson(msg.getObj().toString(), Command.class);
		Action action = null;
		switch(command.getCommandType()){
			case Command.COMMAND_TYPE_CURVE:
				command = new Gson().fromJson(msg.getObj().toString(), CurveCommand.class);
				action = new CurveAction(canvasContext);
				break;
			case Command.COMMAND_TYPE_ERASER:
				command = new Gson().fromJson(msg.getObj().toString(), EraserCommand.class);
				action = new EraserAction(canvasContext);
				break;
		}

		if(action != null)
        	action.draw(command);
    }

    public void playVideo(String url, Activity activity){
        MediaController mc = new MediaController(activity);
        mc.setAnchorView(videoView);
        videoView.setMediaController(mc);
        // videoView.setVideoPath("file:///my.mp4");
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();
        videoView.start();
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
		mCanvas.drawBitmap(bgBitmap, 0, 0, null);
		// 画打开的图片--如，相册的照片。
		if (picBitmap != null)
			mCanvas.drawBitmap(picBitmap, m, null);
		mCanvas.drawBitmap(mBitmap, 0, 0, null);
	}

	// 撤销
	public void undo() {
		if (canvasContext.getActions() != null && canvasContext.getActions().size() > 0) {
			Action action = canvasContext.getActions().get(canvasContext.getActions().size() - 1);
			canvasContext.getCancelledActions().add(action);
			canvasContext.getActions().remove(action);
			reFresh();
		}
	}

	/*
	 * 取消撤销，前进
	 */
	public void redo() {
		if (canvasContext.getCancelledActions().size() < 1)
			return;
		if (canvasContext.getCancelledActions() != null && canvasContext.getCancelledActions().size() > 0) {
			Action action = canvasContext.getCancelledActions().get(canvasContext.getCancelledActions().size() - 1);
			canvasContext.getActions().add(action);
			canvasContext.getCancelledActions().remove(action);
            reFresh();
		}
	}

	public void reFresh(){
        mBitmap = Bitmap.createBitmap(canvasContext.getScreenWidth(), canvasContext.getScreenHeight(), Config.ARGB_8888);
        canvasContext.getCanvas().setBitmap(mBitmap);
        if (canvasContext.getActions() != null) {
            Iterator<Action> iter = canvasContext.getActions().iterator();
            while (iter.hasNext()) {
                Action a = iter.next();
                a.draw();
            }
        }
	}

	/*
	 * 
	 */
	public void setBitmap(Bitmap bm) {
		m = new Matrix();
		int trX = 0;
		if (bm.getWidth() < canvasContext.getScreenWidth()) {
			trX = canvasContext.getScreenWidth() / 2 - bm.getWidth() / 2;
		}
		m.postTranslate(trX, canvasContext.getScreenHeight() / 2 - bm.getHeight() / 2);
		// m 是图片居中显示
		picBitmap = bm;
	}

	/*
	 * 清屏操作
	 */
	public void clear() {
		if (canvasContext.getActions() != null) {
			canvasContext.getActions().clear();
		}
		if (canvasContext.getCancelledActions() != null) {
			canvasContext.getCancelledActions().clear();
		}
        picBitmap = null;
        reFresh();
	}

	/*
	 * 用于保存涂鸦完，按了保存按钮返回的图片
	 */
	public Bitmap getBitmap() {
        Bitmap bitmap= Bitmap.createBitmap(canvasContext.getScreenWidth(), canvasContext.getScreenHeight(), Config.ARGB_8888);
        Canvas tCanvas = new Canvas(bitmap);
        tCanvas.drawBitmap(bgBitmap, 0, 0, null);
        if(picBitmap != null){
            tCanvas.drawBitmap(picBitmap, m, null);
        }
        tCanvas.drawBitmap(mBitmap, 0, 0, null);
        tCanvas.save(Canvas.ALL_SAVE_FLAG);
        tCanvas.restore();
		return bitmap;
	}

	public void setEraser() {
		canvasContext.setCurrentActionType(CanvasContext.ACTION_TYPE_ERASER);
	}

	public void setPen() {
		canvasContext.setCurrentActionType(CanvasContext.ACTION_TYPE_CURVE);
	}

}
