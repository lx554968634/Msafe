package org.com.lix_.util;

import java.io.File;
import java.util.ArrayList;

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

	public static void readFile(File pFile, ArrayList<String> szContents) {
		File[] szFiles = null;
		int i = 0;
		if (pFile == null)
			Debug.e(TAG, "这个文件 为null");
		else {
			if (pFile.isDirectory()) {
				szFiles = pFile.listFiles();
				Debug.i(TAG, "找到文件夹:" + pFile.getName());
				if (szFiles == null || szFiles.length == 0) {
					szContents
							.add(pFile.getName()
									+ ":"
									+ (szFiles == null ? "为null"
											: "szFiles.length = 0"));
					Debug.i(TAG,
							pFile.getName()
									+ ":"
									+ (szFiles == null ? "为null"
											: "szFiles.length = 0"));
					return;
				}
				szContents.add(pFile.getAbsolutePath()+":");
				for (i = 0; i < szFiles.length; i++) {
					readFile(szFiles[i], szContents);
				}
			} else {
				szContents.add(pFile.getAbsolutePath()+":");
				Debug.i(TAG, pFile.getName());
			}
		}
	}

}
