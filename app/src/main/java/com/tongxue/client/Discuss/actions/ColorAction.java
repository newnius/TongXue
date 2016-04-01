package com.tongxue.client.Discuss.actions;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.connector.Objs.TXObject;

/**
 * Created by newnius on 16-2-23.
 */
public class ColorAction extends Action{
    private int color;

    public ColorAction(CanvasContext canvasContext) {
        super(canvasContext);
    }

    public ColorAction(CanvasContext canvasContext, int color) {
        super(canvasContext);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(TXObject command) {
        if(command==null)
            return;
        if(!command.hasKey("type") || command.getInt("type")!=Action.ACTION_TYPE_COLOR)
            return;
        if(!command.hasKey("color"))
            return;
        this.color = command.getInt("color");
        canvasContext.setColor(color);
    }

    @Override
    public TXObject toCommand() {
        TXObject command = new TXObject();
        command.set("type", Action.ACTION_TYPE_COLOR);
        command.set("color", color);
        return command;
    }
}
