package com.tongxue.client.Discuss.actions;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.connector.Objs.TXObject;

/**
 *
 * Created by newnius on 16-4-1.
 */
public class RedoAction extends Action{
    public RedoAction(CanvasContext canvasContext) {
        super(canvasContext);
    }

    @Override
    public void draw(TXObject command) {
        if (canvasContext.getCancelledActions().size() < 1)
            return;
        if (canvasContext.getCancelledActions() != null && canvasContext.getCancelledActions().size() > 0) {
            Action action = canvasContext.getCancelledActions().get(canvasContext.getCancelledActions().size() - 1);
            canvasContext.getActions().add(action);
            canvasContext.getCancelledActions().remove(action);
            canvasContext.rePaint();
        }
    }

    @Override
    public TXObject toCommand() {
        TXObject command = new TXObject();
        command.set("type", Action.ACTION_TYPE_REDO);
        return command;
    }
}
