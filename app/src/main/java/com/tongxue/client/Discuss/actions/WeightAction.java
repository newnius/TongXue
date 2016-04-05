package com.tongxue.client.Discuss.actions;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.connector.Objs.TXObject;

/**
 *
 * Created by newnius on 16-3-31.
 */
public class WeightAction extends Action{
    private int weight;
    public WeightAction(CanvasContext canvasContext) {
        super(canvasContext);
    }

    public WeightAction(CanvasContext canvasContext, int weight) {
        super(canvasContext);
        this.weight = weight;
    }

    @Override
    public void draw(TXObject command) {
        if(command==null)
            return;
        if(!command.hasKey("type") || command.getInt("type")!=Action.ACTION_TYPE_WEIGHT)
            return;
        if(!command.hasKey("weight"))
            return;
        this.weight = command.getInt("weight");
        canvasContext.setColor(weight);
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public TXObject toCommand() {
        TXObject command = new TXObject();
        command.set("type", Action.ACTION_TYPE_WEIGHT);
        command.set("weight", weight);
        return command;
    }
}
