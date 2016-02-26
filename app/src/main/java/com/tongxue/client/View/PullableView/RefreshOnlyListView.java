package com.tongxue.client.View.PullableView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class RefreshOnlyListView extends ListView implements Pullable{

    public RefreshOnlyListView(Context context){
        super(context);
    }

    public RefreshOnlyListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public RefreshOnlyListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown(){
        if (getCount() == 0){
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0&& getChildAt(0).getTop() >= 0){
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp(){
        return false;
    }
}
