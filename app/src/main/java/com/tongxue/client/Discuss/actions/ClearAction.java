package com.tongxue.client.Discuss.actions;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.connector.Objs.TXObject;

/**
 * Created by newnius on 16-4-1.
 */
public class ClearAction extends Action{

    public ClearAction(CanvasContext canvasContext) {
        super(canvasContext);
    }

    @Override
    public void draw(TXObject command) {
        if (canvasContext.getActions() != null) {
            canvasContext.getActions().clear();
        }
        if (canvasContext.getCancelledActions() != null) {
            canvasContext.getCancelledActions().clear();
        }
        canvasContext.setImageBitmap(null);
        canvasContext.rePaint();
    }

    @Override
    public TXObject toCommand() {
        TXObject command = new TXObject();
        command.set("type", Action.ACTION_TYPE_CLEAR);
        return command;
    }
}