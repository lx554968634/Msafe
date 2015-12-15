package org.com.lix_.enable.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.com.lix_.enable.EnableOfRubbishClear;
import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class FileInfoEngine {
	private String[] m_szType;
	private String[] m_szTypeName;
	private String TAG = "FileInfoEngine";

	private Handler m_pHandler;

	private Context m_pContext;

	public FileInfoEngine(Context pContext, Handler m_pRubbishHandler) {
		m_pHandler = m_pRubbishHandler;
		m_pContext = pContext;
		m_szType = m_pContext.getResources().getStringArray(
				R.array.type_rubbishclear_array);
		m_szTypeName = m_pContext.getResources().getStringArray(
				R.array.type_rubbishclear_array);
	}

	public int retSDCardStatus() {
		int nTag = 0;
		String sTag = "";
		String nStatus = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(nStatus)) {
			sTag = "sd卡准备成功";
			nTag = 1;
		} else if (Environment.MEDIA_UNMOUNTED.equals(nStatus)) {
			sTag = "手机设置为sd卡卸载";
		} else if (Environment.MEDIA_REMOVED.equals(nStatus)) {
			sTag = "正常取出sd卡";
		} else if (Environment.MEDIA_BAD_REMOVAL.equals(nStatus)) {
			sTag = "手机直接拔出sd卡";
		} else if (Environment.MEDIA_SHARED.equals(nStatus)) {
			sTag = "手机sd卡正在链接电脑 ";
		} else if (Environment.MEDIA_CHECKING.equals(nStatus)) {
			sTag = "手机正在扫描sd卡过程中..";
		} else {
			sTag = "手机sd卡异常";
		}
		Debug.i(TAG, sTag);
		return nTag;
	}

	private int checkFileName(String sTmpName) {
		for (int i = 0; i < m_szType.length; i++) {
			if (sTmpName.indexOf(m_szType[i]) != -1) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param pFile
	 *            文件夹
	 * @param szContents
	 *            空白文件夹集合
	 * @param szTypeCache
	 *            缓存文件集合
	 */
	public void readFile(File pFile, ArrayList<String> szContents,
			HashMap<String, ArrayList<String>> szTypeCache) {
		File[] szFiles = null;
		String sTmpName = "";
		int nType = -1;
		int i = 0;
		Message pMsg = null;
		if (pFile == null)
			Debug.e(TAG, "这个文件 为null");
		else {
			sTmpName = pFile.getName();
			if (pFile.isDirectory()) {
				szFiles = pFile.listFiles();
				if (szFiles == null || szFiles.length == 0) {
					szContents.add("[" + pFile.getAbsolutePath() + "]");
					Debug.i(TAG, "[空文件夹:" + pFile.getAbsolutePath() + "]");
					return;
				}
			}
			nType = checkFileName(sTmpName);
			Debug.i(TAG, "判断文件：" + sTmpName + ":" + nType);
			if (nType != -1) {
				ArrayList<String> szTmp = szTypeCache.get(m_szTypeName[nType]);
				if (szTmp == null) {
					szTmp = new ArrayList<String>();
					Debug.i(TAG, "扫描到:" + m_szTypeName[nType] + ":sTmpName");
					pMsg = Message.obtain();
					pMsg.what = EnableOfRubbishClear.RAM_SHOW;
					m_pHandler.sendMessage(pMsg);
					long nSize = UiUtils.getFileSize(pFile);
					pMsg.obj = nSize + "";
					szTypeCache.put(m_szTypeName[nType], szTmp);
				}
				szTmp.add(pFile.getAbsolutePath());
				return;
			}
			if (pFile.isDirectory()) {
				szFiles = pFile.listFiles();
				for (i = 0; i < szFiles.length; i++) {
					readFile(szFiles[i], szContents, szTypeCache);
				}
			}
		}
	}
}
