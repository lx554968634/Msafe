package org.com.lix_.enable.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.os.Environment;

public class FileInfoEngine {
	private String[] m_szType;
	private String[] m_szTypeName;
	private String TAG = "FileInfoEngine";

	private Context m_pContext;

	public FileInfoEngine(Context pContext) {
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
	public void readFile(File pFile, ArrayList<String> szContents,
			HashMap<String, ArrayList<String>> szTypeCache) {
		File[] szFiles = null;
		String sTmpName = "";
		int nType = -1;
		int i = 0;
		if (pFile == null)
			Debug.e(TAG, "����ļ� Ϊnull");
		else {
			if (pFile.isDirectory()) {
				szFiles = pFile.listFiles();
				sTmpName = pFile.getName();
				if (szFiles == null || szFiles.length == 0) {
					szContents.add("[" + pFile.getAbsolutePath() + "]");
					Debug.i(TAG, "[���ļ���:" + pFile.getAbsolutePath() + "]");
					return;
				}
				nType = checkFileName(sTmpName);
				if (nType != -1) {
					ArrayList<String> szTmp = szTypeCache
							.get(m_szTypeName[nType]);
					if (szTmp == null) {
						szTmp = new ArrayList<String>();
						Debug.i(TAG,
								m_szTypeName[nType] + "["
										+ pFile.getAbsolutePath() + "]");
						szTypeCache.put(m_szTypeName[nType], szTmp);
					}
					szTmp.add(pFile.getAbsolutePath());
					return;
				}
				for (i = 0; i < szFiles.length; i++) {
					readFile(szFiles[i], szContents, szTypeCache);
				}
			} else {
				Debug.i(TAG, pFile.getName());
			}
		}
	}
}
