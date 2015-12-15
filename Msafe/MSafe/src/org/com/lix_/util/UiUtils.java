package org.com.lix_.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class UiUtils {

	private static String TAG = "UiUtils";

	static public int getScreenWidthPixels(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	static public int dipToPx(Context context, int dip) {
		return (int) (dip * getScreenDensity(context) + 0.5f);
	}

	static public float getScreenDensity(Context context) {
		try {
			DisplayMetrics dm = new DisplayMetrics();
			((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getMetrics(dm);
			return dm.density;
		} catch (Exception e) {
			return DisplayMetrics.DENSITY_DEFAULT;
		}
	}

	static public Paint decoratePaint(int nColor, boolean bAntiAlias,
			Paint.Style pStyle, Paint pPaint, Shader pShade) {
		if (pPaint == null)
			pPaint = new Paint();
		if (nColor == -1)
			pPaint.setColor(nColor);
		pPaint.setAntiAlias(bAntiAlias);
		if (pStyle != null)
			pPaint.setStyle(pStyle);
		if (pShade != null)
			pPaint.setShader(pShade);
		return pPaint;
	}

	public static float sin(int i) {
		// TODO Auto-generated method stub
		return (float) (Math.sin(i * Math.PI * 2 / 360));
	}

	public static float cos(int i) {
		return (float) (Math.cos(i * Math.PI * 2 / 360));
	}

	public static String getCacheSize(long i) {
		if (i <= 0)
			return "0B";
		if (i < 1000) {
			return i + "B";
		}
		if (i >= 1000 && i < 1000000) {
			return i / 1000 + "." + i % 1000 % 100 % 10 + "KB";
		} else {
			return i / 1000000 + "." + i / 1000 % 1000 % 100 % 10 + "M";
		}
	}

	public static long getFileSize(File pFile) {
		long size = 0;
		if (pFile == null)
			return 0;
		if (pFile.isDirectory()) {
			size = size + getFileSize(pFile);
		} else {
			size = size + pFile.length();
		}
		return size;
	}

}
