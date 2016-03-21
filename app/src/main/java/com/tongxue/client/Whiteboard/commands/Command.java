package com.tongxue.client.Whiteboard.commands;

/**
 * Created by newnius on 16-2-19.
 */
public class Command {
    protected long commandId;
    protected int boardId;
    protected String user;
    protected long time;
    protected int weight;
    protected int color;
    protected int commandType;

    public static final int COMMAND_TYPE_CURVE = 1;
    public static final int COMMAND_TYPE_ERASER = 2;
    public static final int COMMAND_TYPE_LINE = 3;
    public static final int COMMAND_TYPE_CIRCLE = 4;
    public static final int COMMAND_TYPE_CLEAR = 5;
    public static final int COMMAND_TYPE_REDO = 6;
    public static final int COMMAND_TYPE_UNDO = 7;
    public static final int COMMAND_TYPE_COLOR = 8;
    public static final int COMMAND_TYPE_WEIGHT = 9;
    public static final int COMMAND_TYPE_TEXT = 10;
    public static final int COMMAND_TYPE_IMAGE = 11;
    public static final int COMMAND_TYPE_VIDEO = 12;

    public String getUser() {
        return "newnius";
    }

    public int getWeight() {
        return weight;
    }

    public int getColor() {
        return color;
    }

    public int getCommandType() {
        return commandType;
    }
}
