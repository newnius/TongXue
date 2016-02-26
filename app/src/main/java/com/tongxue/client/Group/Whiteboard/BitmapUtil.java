package com.tongxue.client.Group.Whiteboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author guo
 *
 */
public class BitmapUtil {
	public static Bitmap casualStroke(Context context, int drawableId, int color) {
		Bitmap mode = ((BitmapDrawable) context.getResources().getDrawable(
				drawableId)).getBitmap();
		Bitmap bitmap = mode.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		Paint paintUnder = new Paint();
		paintUnder.setColor(color);
		canvas.drawPaint(paintUnder);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		canvas.drawBitmap(mode, 0, 0, paint);
		return bitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap(

		drawable.getIntrinsicWidth(),

		drawable.getIntrinsicHeight(),

		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

		: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		drawable.draw(canvas);

		return bitmap;

	}

	public static Bitmap zoomBitmap(Bitmap bm, int w, int h) {

		Bitmap newBitmap;
		if (bm == null) {
			return null;
		}
		float scaleWidth = bm.getWidth() * 1.0f / w;
		float scaleHeight = bm.getHeight() * 1.0f / h;
		float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
		if (scale > 1.01) {
			newBitmap = Bitmap.createScaledBitmap(bm,
					(int) (bm.getWidth() / scale),
					(int) (bm.getHeight() / scale), false);
		} else {
			newBitmap = bm;
		}
		return newBitmap;
	}

	public static Bitmap loadFromSdCard(String filePath) {
		File file = new File(filePath);
		Bitmap bmp = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			bmp = BitmapFactory.decodeStream(fis);
			if (bmp != null) {
				return bmp;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
