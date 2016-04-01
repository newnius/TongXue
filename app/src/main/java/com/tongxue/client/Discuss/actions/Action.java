package com.tongxue.client.Discuss.actions;

import android.graphics.Paint;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.client.Discuss.FloatPoint;
import com.tongxue.connector.Objs.TXObject;

/**
 * Created by newnius on 16-2-23.
 */
public abstract class Action {
    public static final int ACTION_TYPE_CURVE = 1;
    public static final int ACTION_TYPE_ERASER = 2;
    public static final int ACTION_TYPE_LINE = 3;
    public static final int ACTION_TYPE_CIRCLE = 4;
    public static final int ACTION_TYPE_CLEAR = 5;
    public static final int ACTION_TYPE_REDO = 6;
    public static final int ACTION_TYPE_UNDO = 7;
    public static final int ACTION_TYPE_COLOR = 8;
    public static final int ACTION_TYPE_WEIGHT = 9;
    public static final int ACTION_TYPE_TEXT = 10;
    public static final int ACTION_TYPE_IMAGE = 11;
    public static final int ACTION_TYPE_VIDEO = 12;


    protected CanvasContext canvasContext;
    protected Paint paint;

    public Action(CanvasContext canvasContext) {
        this.canvasContext = canvasContext;
        paint = new Paint();
    }

    public Action setCanvasContext(CanvasContext canvasContext) {
        this.canvasContext = canvasContext;
        return this;
    }

    public void start(FloatPoint point){}

    public void move(FloatPoint point){}

    public void finish(FloatPoint point){}

    public void draw(TXObject command){}

    public void draw(){}

    public abstract TXObject toCommand();
}
