package com.tongxue.client.Whiteboard.commands;

import com.tongxue.client.Whiteboard.FloatPoint;

/**
 * Created by newnius on 16-2-23.
 */
public class ImageCommand extends Command{
    private String imageUrl;// image web url
    private FloatPoint LTPoint;//left and top
    private FloatPoint RBPoint;//right and bottom

    public ImageCommand() {
        this.commandType = Command.COMMAND_TYPE_IMAGE;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public FloatPoint getLTPoint() {
        return LTPoint;
    }

    public void setLTPoint(FloatPoint LTPoint) {
        this.LTPoint = LTPoint;
    }

    public FloatPoint getRBPoint() {
        return RBPoint;
    }

    public void setRBPoint(FloatPoint RBPoint) {
        this.RBPoint = RBPoint;
    }
}
