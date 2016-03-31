package com.tongxue.client.Discuss.actions;


import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.client.Discuss.FloatPoint;
import com.tongxue.connector.Objs.TXObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by newnius on 16-2-23.
 */
public class EraserAction extends Action{
    private Path path;
    private FloatPoint lastPoint;
    private List<FloatPoint> points;

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
        points = new ArrayList<>();
        points.add(lastPoint);
    }

    @Override
    public void move(FloatPoint point) {
        float dx = Math.abs(point.getX() - lastPoint.getX());
        float dy = Math.abs(point.getY() - lastPoint.getY());
        if (dx >= 4 || dy >= 4) {
            path.quadTo(lastPoint.getX(), lastPoint.getY(), (point.getX() + lastPoint.getX()) / 2, (point.getY() + lastPoint.getY()) / 2);
            lastPoint = new FloatPoint(point.getX(), point.getY());
            points.add(lastPoint);
            canvasContext.getCanvas().drawPath(path, paint);
        }
    }

    @Override
    public void finish(FloatPoint point) {
        super.finish(point);
    }

    @Override
    public void draw(TXObject command) {
        if(!command.hasKey("type"))
            return ;
        if(command.getInt("type")!=Action.ACTION_TYPE_ERASER)
            return;
        if(!command.hasKey("points"))
            return;

        List<FloatPoint> pointsArray = new Gson().fromJson(command.get("points"), new TypeToken<List<FloatPoint>>(){}.getType());
        Iterator<FloatPoint> points = pointsArray.iterator();
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
    public TXObject toCommand() {
        TXObject command = new TXObject();
        command.set("type", Action.ACTION_TYPE_ERASER);
        command.set("points", new Gson().toJson(points));
        return command;
    }
}
