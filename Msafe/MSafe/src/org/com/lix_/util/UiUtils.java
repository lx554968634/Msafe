package org.com.lix_.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
		if (pFile.isDirectory() && pFile.listFiles() != null
				&& pFile.listFiles().length != 0) {
			for (int i = 0; i < pFile.listFiles().length; i++) {
				size = size + getFileSize(pFile.listFiles()[i]);
			}
		} else {
			if (!pFile.isDirectory())
				size = size + pFile.length();
		}
		return size;
	}

	public static CharSequence getCurrentTxt(String sTxt) {
		if (sTxt == null)
			return "";
		else {
			if (sTxt.length() < 10) {
				return sTxt;
			} else {
				return sTxt.substring(0, 4) + "..."
						+ sTxt.substring(sTxt.length() - 4, sTxt.length());
			}
		}
	}

	public static long getLongValue(Long pLong0) {
		if (pLong0 == null)
			return 0;
		return pLong0.longValue();
	}

	public static int getHashMapSize(HashMap szDatas) {
		if (szDatas == null)
			return 0;
		else {
			int nCount = 0;
			for (Object obj : szDatas.keySet()) {
				nCount += ((List) szDatas.get(obj)).size();
			}
			return nCount = 0;
		}
	}

	public static void logStringArr(String[] requestedPermissions) {
		if (requestedPermissions == null || requestedPermissions.length == 0)
			Debug.e(TAG, "requestPermissions为null~");

		StringBuffer sb = new StringBuffer();
		for (String sInfo : requestedPermissions) {
			sb.append(sInfo + ":");
		}
		Debug.i(TAG, sb.toString());
	}

	public static void setText(View m_pViewTitle, int tipChild0Tab, String string) {
		TextView pTextView = (TextView) (m_pViewTitle.findViewById(tipChild0Tab));
		if (string != null)
			pTextView.setText(string);
	}
	public static void setText(View m_pViewTitle, String string) {
		TextView pTextView = (TextView) (m_pViewTitle);
		if (string != null)
			pTextView.setText(string);
	}

	/*
	 * 百分比时间
	 * 2136 分钟 
	 */
	public static CharSequence getTime(String sTotal ,String string) { 
		int nTotalTime = Integer.parseInt(sTotal) ;
		int nNow = nTotalTime / 100 * Integer.parseInt(string) ;
		return nNow / 60 +"小时"+nNow%60+"分钟";
	}

	public static void setImg(View findViewById, Drawable drawable) {
		ImageView pImageView = (ImageView) findViewById ;
		pImageView.setImageDrawable(drawable);
	}

}
