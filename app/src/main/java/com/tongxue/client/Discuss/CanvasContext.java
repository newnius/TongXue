package com.tongxue.client.Discuss;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import com.tongxue.client.Discuss.actions.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newnius on 16-2-23.
 */
public class CanvasContext {
    private Canvas canvas;
    private List<Action> actions;
    private List<Action> cancelledActions;
    private int color = Color.BLACK;
    private int weight = 10;
    private int screenWidth;
    private int screenHeight;
    private Bitmap bgImage;
    private int currentActionType = ACTION_TYPE_CURVE;

    public static final int ACTION_TYPE_CURVE = 1;
    public static final int ACTION_TYPE_ERASER = 2;

    public CanvasContext() {
        this.actions = new ArrayList<>();
        this.cancelledActions = new ArrayList<>();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Action> getCancelledActions() {
        return cancelledActions;
    }

    public void setCancelledActions(List<Action> cancelledActions) {
        this.cancelledActions = cancelledActions;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getCurrentActionType() {
        return currentActionType;
    }

    public void setCurrentActionType(int currentActionType) {
        this.currentActionType = currentActionType;
    }
}
