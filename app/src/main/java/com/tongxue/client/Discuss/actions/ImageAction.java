package com.tongxue.client.Discuss.actions;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.client.Discuss.FloatPoint;
import com.tongxue.connector.Objs.TXObject;

/**
 * Created by newnius on 16-2-23.
 */
public class ImageAction extends Action{
    private Bitmap image;

    private String imageUrl;// image web url
    private FloatPoint LTPoint;//left and top
    private FloatPoint RBPoint;//right and bottom

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
        canvasContext.getCanvas().drawBitmap(image, point.x, point.y, null);
    }

    @Override
    public void draw(TXObject command) {
        super.draw(command);
    }

    @Override
    public void draw() {
        canvasContext.getCanvas().drawBitmap(image, new Matrix(), null);
    }

    @Override
    public TXObject toCommand() {
        return null;
    }
}
