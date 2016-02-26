package com.tongxue.client.Base;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * Created by chaosi on 2015/7/14.
 */
public class ActivityManager {
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;
    private ActivityManager(){
    }
    public static ActivityManager getActivityManager(){
        if(instance==null){
            instance=new ActivityManager();
        }
        return instance;
    }
    public void popActivity(){
        Activity activity=activityStack.lastElement();
        if(activity!=null){
            activity.finish();
            activity=null;
        }
    }
    public void popActivity(Activity activity){
        if(activity!=null){
            activity.finish();
            activityStack.remove(activity);
            activity=null;
        }
    }
    public Activity currentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }

    public void pushActivity(Activity activity){
        if(activityStack==null){
            activityStack=new Stack<>();
        }
        activityStack.add(activity);
    }

    public void popAllActivity(){

        int number=activityStack.size();

        for(int i=0;i<number;i++){

            Activity activity=currentActivity();
            Log.i("learn", activity.getClass().getName());
            popActivity(activity);
        }
    }
}
