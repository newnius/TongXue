package com.tongxue.client.Discuss;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.tongxue.client.R;

/**
 * 画笔大小view
 * 
 * @author guo
 */
public class PenSizeView extends View {

    private int size = 10;
    private Paint mPaint;
    private int viewWidth;
    private int viewHeigt;

	public PenSizeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setColor(Color.RED);

        viewWidth = (int) getResources().getDimension(R.dimen.penSizeView_Width);
        viewHeigt = (int) getResources().getDimension(R.dimen.penSizeView_Height);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		canvas.drawCircle(viewWidth / 2, viewHeigt / 2, size / 2, mPaint);
	}

	public void setPaintSize(int size) {
		mPaint.setStrokeWidth(size);
		this.size = size;
		invalidate();
	}

	public void setPaintColor(int color) {
		mPaint.setColor(color);
		invalidate();
	}
}
