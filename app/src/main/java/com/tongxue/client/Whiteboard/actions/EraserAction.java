package com.tongxue.client.Whiteboard.actions;


import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.tongxue.client.Whiteboard.CanvasContext;
import com.tongxue.client.Whiteboard.FloatPoint;
import com.tongxue.client.Whiteboard.commands.Command;
import com.tongxue.client.Whiteboard.commands.EraserCommand;

import java.util.Iterator;

/**
 * Created by newnius on 16-2-23.
 */
public class EraserAction extends Action{
    private Path path;
    private FloatPoint lastPoint;
    private EraserCommand command;
    public EraserAction(CanvasContext canvasContext) {
        super(canvasContext);
        paint.setStrokeWidth(canvasContext.getWeight());
    }

    @Override
    public void start(FloatPoint point) {
        path = new Path();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        path.moveTo(point.getX(), point.getY());
        lastPoint = new FloatPoint(point.getX(), point.getY());
        command = new EraserCommand();
        command.addPoint(lastPoint);
    }

    @Override
    public void move(FloatPoint point) {
        float dx = Math.abs(point.getX() - lastPoint.getX());
        float dy = Math.abs(point.getY() - lastPoint.getY());
        if (dx >= 4 || dy >= 4) {
            path.quadTo(lastPoint.getX(), lastPoint.getY(), (point.getX() + lastPoint.getX()) / 2, (point.getY() + lastPoint.getY()) / 2);
            lastPoint = new FloatPoint(point.getX(), point.getY());
            command.addPoint(lastPoint);
            canvasContext.getCanvas().drawPath(path, paint);
        }
    }

    @Override
    public void finish(FloatPoint point) {
        super.finish(point);
    }

    @Override
    public void draw(Command command) {
        if(!(command instanceof EraserCommand))
            return ;
        Iterator<FloatPoint> points = ((EraserCommand) command).getPoints().iterator();
        if(points.hasNext()){//get start point
            FloatPoint point = points.next();
            lastPoint = new FloatPoint(point.getX(), point.getY());
            start(point);
        }
        while (points.hasNext()) {
            FloatPoint point = points.next();
            path.quadTo(lastPoint.getX(), lastPoint.getY(), (point.getX() + lastPoint.getX()) / 2, (point.getY() + lastPoint.getY()) / 2);
            lastPoint = new FloatPoint(point.getX(), point.getY());
            canvasContext.getCanvas().drawPath(path, paint);
        }
    }

    @Override
    public void draw() {
        canvasContext.getCanvas().drawPath(path, paint);
    }

    @Override
    public Command toCommand() {
        return command;
    }
}
