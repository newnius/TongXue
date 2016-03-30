package com.tongxue.client.Discuss.commands;

import com.tongxue.client.Discuss.FloatPoint;

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
