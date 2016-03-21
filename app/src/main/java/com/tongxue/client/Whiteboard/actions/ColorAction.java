package com.tongxue.client.Whiteboard.actions;

import com.tongxue.client.Whiteboard.CanvasContext;
import com.tongxue.client.Whiteboard.commands.Command;

/**
 * Created by newnius on 16-2-23.
 */
public class ColorAction extends Action{
    private int color;

    public ColorAction(CanvasContext canvasContext, int color) {
        super(canvasContext);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public Command toCommand() {
        return null;
    }
}
