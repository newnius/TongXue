package com.tongxue.client.Discuss.actions;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.client.Discuss.FloatPoint;
import com.tongxue.client.Discuss.commands.Command;
import com.tongxue.client.Discuss.commands.ImageCommand;

/**
 * Created by newnius on 16-2-23.
 */
public class ImageAction extends Action{
    private Bitmap image;
    private ImageCommand command;

    public ImageAction(CanvasContext canvasContext, Bitmap image) {
        super(canvasContext);
        this.image = image;
    }

    @Override
    public void start(FloatPoint point) {
    }

    @Override
    public void move(FloatPoint point) {

    }

    @Override
    public void finish(FloatPoint point) {
        if(image == null)
            return ;
        canvasContext.getCanvas().drawBitmap(image, point.getX(), point.getY(), null);
        command = new ImageCommand();
        command.setLTPoint(point);
    }

    @Override
    public void draw(Command command) {
        super.draw(command);
    }

    @Override
    public void draw() {
        canvasContext.getCanvas().drawBitmap(image, new Matrix(), null);
    }

    @Override
    public Command toCommand() {
        return null;
    }
}
