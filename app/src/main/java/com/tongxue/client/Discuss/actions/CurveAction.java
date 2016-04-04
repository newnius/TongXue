package com.tongxue.client.Discuss.actions;

import android.graphics.Paint;
import android.graphics.Path;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.client.Discuss.FloatPoint;
import com.tongxue.connector.Objs.TXObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Created by newnius on 16-2-23.
 */
public class CurveAction extends Action{
    private Path path;
    private FloatPoint lastPoint;
    private List<FloatPoint> points;

    public CurveAction(CanvasContext canvasContext) {
        super(canvasContext);
        path = new Path();
        paint.setColor(canvasContext.getColor());
        paint.setStrokeWidth(canvasContext.getWeight());
    }

    @Override
    public void start(FloatPoint point) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        path.moveTo(point.x, point.y);
        lastPoint = point;
        points = new ArrayList<>();
        points.add(lastPoint);
    }

    @Override
    public void move(FloatPoint point) {
        float dx = Math.abs(point.x - lastPoint.x);
        float dy = Math.abs(point.y - lastPoint.y);
        if (dx >= 4 || dy >= 4) {
            path.quadTo(lastPoint.x, lastPoint.y, (point.x + lastPoint.x) / 2, (point.y + lastPoint.y) / 2);
            lastPoint = point;
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
            return;
        if(command.getInt("type")!=Action.ACTION_TYPE_CURVE)
            return;
        if(!command.hasKey("points"))
            return;

        List<FloatPoint> pointsArray = new Gson().fromJson(command.get("points"), new TypeToken<List<FloatPoint>>(){}.getType());
        Iterator<FloatPoint> points = pointsArray.iterator();
        if(points.hasNext()){//get start point
            FloatPoint point = points.next();
            point.x = point.x * canvasContext.getScreenWidth();
            point.y = point.y * canvasContext.getScreenHeight();
            lastPoint = point;
            start(point);
        }
        while (points.hasNext()) {
            FloatPoint point = points.next();
            point.x = point.x * canvasContext.getScreenWidth();
            point.y = point.y * canvasContext.getScreenHeight();
            path.quadTo(lastPoint.x, lastPoint.y, (point.x + lastPoint.x) / 2, (point.y + lastPoint.y) / 2);
            lastPoint = point;
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
        List<FloatPoint> pointstmp = new ArrayList<>();
        for(FloatPoint point: points){
            pointstmp.add(new FloatPoint(point.x/canvasContext.getScreenWidth(), point.y/canvasContext.getScreenHeight()));
        }
        command.set("type", Action.ACTION_TYPE_CURVE);
        command.set("points", new Gson().toJson(pointstmp));
        return command;
    }
}
