package com.tongxue.client.Discuss.actions;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.connector.Objs.TXObject;

/**
 * Created by newnius on 16-4-1.
 */
public class UndoAction extends Action{


    public UndoAction(CanvasContext canvasContext) {
        super(canvasContext);
    }

    @Override
    public void draw(TXObject command) {
        if (canvasContext.getActions() != null && canvasContext.getActions().size() > 0) {
            Action action = canvasContext.getActions().get(canvasContext.getActions().size() - 1);
            canvasContext.getCancelledActions().add(action);
            canvasContext.getActions().remove(action);
            canvasContext.rePaint();
        }
    }

    @Override
    public TXObject toCommand() {
        TXObject command = new TXObject();
        command.set("type", Action.ACTION_TYPE_UNDO);
        return command;
    }

}
