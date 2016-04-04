package com.tongxue.client.Discuss.actions;

import android.graphics.Paint;
import android.graphics.Path;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.client.Discuss.FloatPoint;
import com.tongxue.connector.Objs.TXObject;

/**
 *
 * Created by newnius on 16-4-1.
 */
public class LineAction extends Action {
    private Path path;
    private FloatPoint startPoint;
    private FloatPoint endPoint;

    public LineAction(CanvasContext canvasContext) {
        super(canvasContext);
        path = new Path();
        paint.setColor(canvasContext.getColor());
        paint.setStrokeWidth(canvasContext.getWeight());
    }

    @Override
    public void start(FloatPoint point) {
        try {
            this.startPoint = point;
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            path.moveTo(startPoint.x, startPoint.y);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void finish(FloatPoint point) {
        this.endPoint = point;
        path.lineTo(point.x, point.y);
        canvasContext.getCanvas().drawPath(path, paint);
    }

    @Override
    public void draw(TXObject command) {
        if (command == null || !command.hasKey("type"))
            return;
        if (command.getInt("type") != Action.ACTION_TYPE_LINE)
            return;
        if (!command.hasKey("startX") || !command.hasKey("startY"))
            return;
        if (!command.hasKey("endX") || !command.hasKey("endY"))
            return;
        startPoint = new FloatPoint(command.getFloat("startX")*canvasContext.getScreenWidth(), command.getFloat("startY")*canvasContext.getScreenHeight());
        endPoint = new FloatPoint(command.getFloat("endX")*canvasContext.getScreenWidth(), command.getFloat("endY")*canvasContext.getScreenHeight());
        start(startPoint);
        finish(endPoint);
        super.draw(command);
    }

    @Override
    public void draw() {
        canvasContext.getCanvas().drawPath(path, paint);
    }

    @Override
    public TXObject toCommand() {
        TXObject command = new TXObject();
        command.set("type", ACTION_TYPE_LINE);
        command.set("startX", startPoint.x / canvasContext.getScreenWidth());
        command.set("startY", startPoint.y / canvasContext.getScreenHeight());
        command.set("endX", endPoint.x / canvasContext.getScreenWidth());
        command.set("endY", endPoint.y / canvasContext.getScreenHeight());
        return command;
    }
}
