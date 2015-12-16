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
			sTag = "sd��׼���ɹ�";
			nTag = 1;
		} else if (Environment.MEDIA_UNMOUNTED.equals(nStatus)) {
			sTag = "�ֻ�����Ϊsd��ж��";
		} else if (Environment.MEDIA_REMOVED.equals(nStatus)) {
			sTag = "����ȡ��sd��";
		} else if (Environment.MEDIA_BAD_REMOVAL.equals(nStatus)) {
			sTag = "�ֻ�ֱ�Ӱγ�sd��";
		} else if (Environment.MEDIA_SHARED.equals(nStatus)) {
			sTag = "�ֻ�sd���������ӵ��� ";
		} else if (Environment.MEDIA_CHECKING.equals(nStatus)) {
			sTag = "�ֻ�����ɨ��sd��������..";
		} else {
			sTag = "�ֻ�sd���쳣";
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
	 *            �ļ���
	 * @param szContents
	 *            �հ��ļ��м���
	 * @param szTypeCache
	 *            �����ļ�����
	 */
	public void readFile(File pFile, ArrayList<String> szContents,
			HashMap<String, ArrayList<String>> szTypeCache, StringBuffer sb) {
		File[] szFiles = null;
		String sTmpName = "";
		int nType = -1;
		int i = 0;
		Message pMsg = null;
		if (pFile == null) {
			sb.append("����ļ�����null");
			Debug.e(TAG, "����ļ� Ϊnull");
		} else {
			sTmpName = pFile.getName();
			pMsg = Message.obtain();
			pMsg.what = EnableOfRubbishClear.TXT_SHOW;
			pMsg.obj = sTmpName;
			m_pHandler.sendMessage(pMsg);
			sb.append("[����ļ�:" + sTmpName + "]");
			if (pFile.isDirectory()) {
				szFiles = pFile.listFiles();
				if (szFiles == null || szFiles.length == 0) {
					sb.append("[" + pFile.getAbsolutePath() + ":���ļ���]");
					szContents.add("[" + pFile.getAbsolutePath() + "]");
					Debug.i(TAG, "[���ļ���:" + pFile.getAbsolutePath() + "]");
					return;
				}
			}
			nType = checkFileName(sTmpName);
			sb.append("[sTmpName:" + nType + "]");
			if (pFile.isDirectory()) {
				szFiles = pFile.listFiles();
				for (i = 0; i < szFiles.length; i++) {
					readFile(szFiles[i], szContents, szTypeCache, sb);
				}
			} else {
				Debug.i(TAG, "�ж��ļ���" + sTmpName + ":" + nType);
				if (nType != -1) {
					ArrayList<String> szTmp = szTypeCache
							.get(m_szTypeName[nType]);
					if (szTmp == null) {
						szTmp = new ArrayList<String>();
						szTypeCache.put(m_szTypeName[nType], szTmp);
					}
					Debug.i(TAG, "ɨ�赽:" + m_szTypeName[nType] + ":sTmpName");
					long nSize = UiUtils.getFileSize(pFile);
					sb.append("[" + sTmpName + "]:" + nSize + ">");
					pMsg = Message.obtain();
					pMsg.obj = nSize + "";
					pMsg.what = EnableOfRubbishClear.RAM_SHOW;
					m_pHandler.sendMessage(pMsg);
					szTmp.add(pFile.getAbsolutePath());
				}
			}
		}
	}
}
