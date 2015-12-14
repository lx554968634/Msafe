package org.com.lix_.util;

import java.io.File;
import java.io.FileOutputStream;

import org.com.lix_.Define;

import android.os.Environment;
import android.util.Log;

/**
 * debugœ‡πÿ
 * 
 * @author punsher
 *
 */
public class Debug {

	public static final String SPLIT_STR = "@@@@";
	public static final String DEBUG_FILENAME = "MSafe.log";
	public static String DEBUG_STR = "";

	public static void i(Object sTag, Object sInfo) {
		if (Define.DEBUG)
			Log.i(sTag.toString(), sInfo.toString());
	}

	public static void e(Object sTag, Object sInfo) {
		if (Define.DEBUG)
			Log.e(sTag.toString(), sInfo.toString());
	}

	public static void logFile(Object sInfo) {
		if (Define.DEBUG
				&& Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED))
			try {
				File pFile = new File(Environment.getExternalStorageDirectory() + "/"
								+ DEBUG_FILENAME) ;
				if(pFile!=null && pFile.exists())
				{
					pFile.delete() ;
				}
				FileOutputStream fout = new FileOutputStream(Environment.getExternalStorageDirectory() + "/"
						+ DEBUG_FILENAME);

				byte[] bytes = sInfo.toString().getBytes();

				fout.write(bytes);

				fout.close();

			} catch (Exception e) {

				e.printStackTrace();

			}
	}
}
