package com.tongxue.client.Group.Whiteboard.commands;

/**
 * Created by newnius on 16-2-24.
 */
public class UndoCommand extends Command{
    public UndoCommand() {
        this.commandType = Command.COMMAND_TYPE_UNDO;
    }
}
