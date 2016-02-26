package com.tongxue.client.Group.Whiteboard.commands;

import com.tongxue.client.Group.Whiteboard.FloatPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newnius on 16-2-23.
 */
public class EraserCommand extends Command{
    public EraserCommand() {
        this.commandType = Command.COMMAND_TYPE_ERASER;
    }

    private List<FloatPoint> points = new ArrayList<>();

    public void addPoint(FloatPoint point){
        points.add(point);
    }

    public List<FloatPoint> getPoints() {
        return points;
    }
}
