package org.com.lix_.enable.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.com.lix_.enable.EnableOfFileAdmin;
import org.com.lix_.enable.EnableOfRubbishClear;
import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/*
 * �ļ�������
 * Environment.getExternalStorageDirectory()  : /mnt/sdcard
 */
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
	public void readFile(File pFile, ArrayList<FileInfo> szContents,
			HashMap<String, ArrayList<FileInfo>> szTypeCache, StringBuffer sb) {
		File[] szFiles = null;
		String sTmpName = "";
		int nType = -1;
		int i = 0;
		FileInfo pFileInfo;
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
					pFileInfo = new FileInfo();
					pFileInfo.m_sFileName = pFile.getName();
					pFileInfo.m_sAbFilePath = pFile.getAbsolutePath();
					szContents.add(pFileInfo);
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
				if (nType != -1) {
					ArrayList<FileInfo> szTmp = szTypeCache
							.get(m_szTypeName[nType]);
					if (szTmp == null) {
						szTmp = new ArrayList<FileInfo>();
						Debug.i(TAG, "m_szDetailCache:���:"
								+ m_szTypeName[nType] + ":" + sTmpName);
						szTypeCache.put(m_szTypeName[nType], szTmp);
					}
					pFileInfo = new FileInfo();
					pFileInfo.m_sFileName = sTmpName;
					pFileInfo.m_sAbFilePath = pFile.getAbsolutePath();
					long nSize = UiUtils.getFileSize(pFile);
					sb.append("[" + sTmpName + "]:" + nSize + ">");
					pMsg = Message.obtain();
					pMsg.obj = nSize + "";
					pMsg.what = EnableOfRubbishClear.RAM_SHOW;
					pMsg.arg1 = nType;
					m_pHandler.sendMessage(pMsg);
					szTmp.add(pFileInfo);
				}
			}
		}
	}

	private String STR_SPLITE_0 = "@@@";

	/*
	 * ��ȡϵͳ�����ļ��������ļ�
	 */
	public void getSimpleBigFile() {
		HashMap<String, ArrayList<FileInfo>> szSimpelFils = null;
		String[] sArr = m_pContext.getResources().getStringArray(
				R.array.file_type_simple);
		int nCount = sArr.length;
		int nIndex = 0;
		Message pMsg = null;
		String sTmpFile = null;
		FileInfo pFileInfo = null;
		String[] szTmp = null;
		ArrayList<FileInfo> szArr = new ArrayList<FileInfo>();
		for (nIndex = 0; nIndex < nCount; nIndex++) {
			pMsg = Message.obtain();
			pMsg.what = EnableOfFileAdmin.SCAN_SIM_FILE;
			szTmp = sArr[nIndex].split(STR_SPLITE_0);
			sTmpFile = szTmp[0];
			pMsg.obj = sTmpFile + ":"
					+ Environment.getExternalStorageDirectory();
			pFileInfo = new FileInfo();
			pFileInfo.m_sAbFilePath = Environment.getExternalStorageDirectory()
					+ "/" + sTmpFile;
			pFileInfo.m_sType = szTmp[1];
			if (pFileInfo.exists()) {
				szArr.add(pFileInfo);
			}
		}
		nCount = szArr.size();
		ArrayList<FileInfo> szArr2 = null;
		szSimpelFils = new HashMap<String, ArrayList<FileInfo>>();
		for (nIndex = 0; nIndex < nCount; nIndex++) {
			pFileInfo = szArr.get(nIndex);
			Debug.i(TAG, "ɨ��pFileInfo:" + pFileInfo.m_sType);
			szArr2 = listFiles(pFileInfo);
			if (szArr2 != null && szArr2.size() != 0)
				szSimpelFils.put(pFileInfo.m_sType, szArr2);
		}
		//���ܳɹ�û�ж�ֱ�ӻص�
		pMsg = Message.obtain();
		pMsg.what = EnableOfFileAdmin.FINISH_SIMFILE_SCAN;
		if(szSimpelFils == null)
			Debug.i(TAG, "szSimpelFils == null");
		else
			Debug.i(TAG, "szSimpelFils "+szSimpelFils.size());
		pMsg.obj = szSimpelFils ;
		m_pHandler.sendMessage(pMsg) ;
	}


	/*
	 * �ҵ����в����ļ��е�ʵ���ļ�
	 */
	private ArrayList<FileInfo> listFiles(FileInfo pFile) {
		ArrayList<FileInfo> szTmp = new ArrayList<FileInfo>();
		if (pFile.exists()) {
			getExistFiles(pFile, szTmp);
		} else
			return null;
		return szTmp;
	}

	private void getExistFiles(FileInfo pFile, ArrayList<FileInfo> szArr) {
		File[] szFiles = pFile.getFile().listFiles();
		if (szFiles == null || szFiles.length == 0) {
			return;
		}
		int nCount = szFiles.length;
		int nIndex = -1;
		File pFileTmp = null;
		FileInfo pFileInfo = null;
		for (nIndex = 0; nIndex < nCount; nIndex++) {
			pFileTmp = szFiles[nIndex];
			if (pFileTmp != null && pFileTmp.exists()) {
				if (pFileTmp.isDirectory()) {
					getExistFiles(new FileInfo(pFileTmp.getAbsolutePath()),
							szArr);
				} else {
					pFileInfo = FileInfo.init(pFileTmp);
					szArr.add(pFileInfo);
				}
			}
		}
	}

}
