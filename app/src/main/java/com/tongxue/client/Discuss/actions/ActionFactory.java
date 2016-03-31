package com.tongxue.client.Discuss.actions;

import com.tongxue.client.Discuss.CanvasContext;
import com.tongxue.connector.Objs.TXObject;

/**
 * Created by newnius on 16-2-24.
 */
public class ActionFactory {
    public static Action toAction(TXObject command, CanvasContext canvasContext){
        if(!command.hasKey("type"))
            return null;
        switch(command.getInt("type")){
            case Action.ACTION_TYPE_CURVE:
                return new CurveAction(canvasContext);
            case Action.ACTION_TYPE_ERASER:
                return new EraserAction(canvasContext);
            default:
                return null;
        }
    }
}
