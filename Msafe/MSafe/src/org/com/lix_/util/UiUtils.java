package org.com.lix_.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.com.lix_.enable.engine.AppInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.text.Spanned;
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

	static public int dipToPx(Context context, float dip) {
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
		} else if (i > 1000000 && i < 1000000000) {
			return i / 1000000 + "." + i / 1000 % 1000 % 100 % 10 + "M";
		} else {
			return i / 1000000000 + "." + i / 1000 / 1000 % 1000 % 100 % 10
					+ "G";
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

	public static void setText(View m_pViewTitle, int tipChild0Tab,
			String string) {
		TextView pTextView = (TextView) (m_pViewTitle
				.findViewById(tipChild0Tab));
		if (string != null)
			pTextView.setText(string);
	}

	public static void setText(View m_pViewTitle, String string) {
		TextView pTextView = (TextView) (m_pViewTitle);
		if (string != null)
			pTextView.setText(string);
	}

	/*
	 * 百分比时间 2136 分钟
	 */
	public static CharSequence getTime(String sTotal, String string) {
		int nTotalTime = Integer.parseInt(sTotal);
		int nNow = nTotalTime / 100 * Integer.parseInt(string);
		return nNow / 60 + "小时" + nNow % 60 + "分钟";
	}

	public static void setImg(View findViewById, Drawable drawable) {
		ImageView pImageView = (ImageView) findViewById;
		pImageView.setImageDrawable(drawable);
	}

	public static void setText(View findViewById, Spanned fromHtml) {
		TextView pTextView = (TextView) (findViewById);
		if (fromHtml != null)
			pTextView.setText(fromHtml);
	}

	public static String getHtmlString(String string, String string2) {
		return "<font color=\"" + string2 + "\" >" + string + "</font>";
	}

	public static String getInstalledTime(long m_nFirstInstalledTime) {
		long nTime = System.currentTimeMillis() - m_nFirstInstalledTime;
		return getTime(nTime);
	}

	public static String getTime(long nTime) {
		long day = nTime / 1000 / 60 / 60 / 24;
		if (day > 0) {
			return day + "天";
		}
		long hour = nTime / 1000 / 60 / 60;
		if (hour > 0) {
			return hour + "小时";
		}
		long fen = nTime / 1000 / 60;
		if (fen > 0)
			return fen + "分";
		long miao = nTime / 1000;
		return miao + "秒";
	}

	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			return bitmap;
		} else {
			return null;
		}
	}

	public static boolean isNum(String str) {
		return !str.equals("")
				&& str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	public static void inVisiable(View findViewById) {
		if (findViewById == null) {
			Debug.e(TAG, "inVisiable:对象为null :");
		} else {
			findViewById.setVisibility(View.INVISIBLE);
		}
	}

	public static void visiable(View findViewById) {
		if (findViewById == null) {
			Debug.e(TAG, "visiable :对象为null ");
		} else {
			findViewById.setVisibility(View.VISIBLE);
		}
	}

	public static void log(HashMap<String, ArrayList<AppInfo>> m_szTargetList) {
		ArrayList pArr = null;
		int nCount = 0;
		StringBuffer sb = new StringBuffer();
		for (String sInfo : m_szTargetList.keySet()) {
			pArr = m_szTargetList.get(sInfo);
			nCount = pArr.size();
			if (pArr == null) {
				Debug.i(TAG,
						"sInfo : " + sInfo + "----------------->" + pArr == null);
			} else if (pArr.size() == 0) {
				Debug.i(TAG, "sInfo : " + sInfo
						+ "---------pArr.size() == 0--------->"
						+ (pArr.size() == 0));
			} else {
				sb = new StringBuffer();
				for (int i = 0; i < nCount; i++) {
					sb.append(pArr.get(i) + ":");
				}
			}
			Debug.i(TAG, "sInfo:" + sInfo + ":--------------:" + sb.toString());
		}
	}
}
