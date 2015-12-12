package org.com.lix_.util;

import org.com.lix_.Define;

import android.util.Log;

/**
 * debug相关
 * @author punsher
 *
 */
public class Debug {
	
	public static final String SPLIT_STR = "@@@@";
	public static String DEBUG_STR = "";
	public static void i(Object sTag, Object sInfo) {
		if (Define.DEBUG)
			Log.i(sTag.toString(), sInfo.toString());
	}
	public static void e(Object sTag, Object sInfo) {
		if (Define.DEBUG)
			Log.e(sTag.toString(), sInfo.toString());
	}
	public static void logFile(Object sTag,Object sInfo)
	{
		//文件记录log`1
	}
}
