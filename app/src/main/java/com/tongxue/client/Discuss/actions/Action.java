package com.tongxue.client.Discuss.actions;

import android.graphics.Paint;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.client.Discuss.FloatPoint;
import com.tongxue.client.Discuss.commands.Command;

/**
 * Created by newnius on 16-2-23.
 */
public abstract class Action {
    protected CanvasContext canvasContext;
    protected Paint paint;

    public Action(CanvasContext canvasContext) {
        this.canvasContext = canvasContext;
        paint = new Paint();
    }

    public void start(FloatPoint point){}

    public void move(FloatPoint point){}

    public void finish(FloatPoint point){}

    public void draw(Command command){}

    public void draw(){}

    public abstract Command toCommand();
}
