package org.com.lix_.enable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.com.lix_.plugin.AppInfo;
import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * ���enable��Ҫʵ��5������ 1���ڴ���٣�ɱ�����̺ͷ����̨ 2��ϵͳӦ�û��棬ɾ��/data/data/packagename/..cache
 * ..databases .. files .. fhareprefs 3�������ļ� 4�����లװ�� 5��Ӧ��ж�ز��� adb shell mount -o
 * remount rw /
 * 
 * @author punsher
 *
 */
public class EnableOfRubbishClear extends Enable {

	public static final int FINSH_SD_RUBBISH = 1;

	public static final int FINSH_INNER_PROP = 2;

	public static final int PREPARE_FINISH = 3;

	private String TAG = "EnableOfRubbishClear";

	private Context m_pContext;

	private Thread m_pInnerThread;

	private String[] m_szType;
	private String[] m_szTypeName;

	public EnableOfRubbishClear(Context pContext) {
		super(pContext);
		m_pContext = pContext;
		init();
	}

	@Override
	public void finish() {

	}

	private void init() {
		m_pRubbishTask.execute(0);
	}

	@Override
	public void onViewClick(int nId) {

	}

	/**
	 * ������
	 */
	Handler m_pRubbishHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int nTag = msg.arg1;
			switch (nTag) {
			case FINSH_SD_RUBBISH:
				if (m_pCallback != null)
					m_pCallback.callback(msg.arg1, null);
				break;
			case PREPARE_FINISH:
				if (m_pCallback != null)
					m_pCallback.callback(msg.arg1, null);
				break;
			case FINSH_INNER_PROP:
				if (m_pCallback != null)
					m_pCallback.callback(msg.arg1, null);
				break;
			}
		}

	};

	AsyncTask m_pRubbishTask = new AsyncTask() {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Debug.e(TAG, "��ʼ׼��ɨ��sd��");
		}

		@Override
		protected Object doInBackground(Object... params) {
			Message pMsg = null;
			String nStatus = Environment.getExternalStorageState();
			String sTag = "";
			int nTag = 0;
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
			Debug.e(TAG, "sd����" + sTag);
			pMsg = Message.obtain();
			pMsg.arg1 = PREPARE_FINISH;
			m_pRubbishHandler.sendMessage(pMsg);
			m_szType = m_pContext.getResources().getStringArray(
					R.array.type_rubbishclear_array);
			m_szTypeName = m_pContext.getResources().getStringArray(
					R.array.type_rubbishclear_array);
			if (nTag == 1 && Debug.DEBUG_STR != null) {
				Debug.i(TAG, "��Ŀ¼��"
						+ Environment.getRootDirectory().getAbsolutePath());
				Debug.i(TAG, "extrĿ¼��"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath());
				ArrayList<String> szBlackDir = new ArrayList<String>();
				HashMap<String, ArrayList<String>> szDetailCache = new HashMap<String, ArrayList<String>>();
				readFile(Environment.getExternalStorageDirectory(), szBlackDir,
						szDetailCache);
				StringBuffer sb = new StringBuffer();
				for (String sInfo : szBlackDir) {
					sb.append(sInfo);
				}
				// Debug.DEBUG_STR = sb.toString();
				sb = new StringBuffer();
				for (String pSet : szDetailCache.keySet()) {
					ArrayList pArr = szDetailCache.get(pSet);
					for (int i = 0; i < pArr.size(); i++) {
						sb.append("pSet[" + pArr.get(i) + "]");
					}
				}
				pMsg = Message.obtain();
				pMsg.arg1 = FINSH_SD_RUBBISH;
				m_pRubbishHandler.sendMessage(pMsg);
				// Debug.DEBUG_STR += sb.toString();
				// Debug.logFile(Debug.DEBUG_STR);
			}

			checkProps();

			return null;
		}
	};

	public HashMap<String, AppInfo> getAllAppInfo() {
		PackageManager pPgeManager = m_pContext.getPackageManager();
		HashMap<String, AppInfo> myApps = new HashMap<String, AppInfo>();
		AppInfo myApp = null;
		List<PackageInfo> packageInfos = pPgeManager
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo packageInfo : packageInfos) {
			myApp = new AppInfo();
			String packageName = packageInfo.packageName;
			myApp.setPackageName(packageName);
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			myApp.setAppName(appInfo.loadLabel(pPgeManager).toString());
			myApp.setIcon(appInfo.loadIcon(pPgeManager));
			myApp.setSystemApp(!filterApp(appInfo));
			myApps.put(packageName, myApp);
		}
		return myApps;
	}

	/**
	 * �ж�ĳ��Ӧ�ó����� ����������Ӧ�ó���
	 * 
	 * @param info
	 * @return ����ǵ�����Ӧ�ó����򷵻�true�������ϵͳ�����򷵻�false
	 */
	public boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}

	private void checkProps() {
		// ��ʼɨ��ϵͳ���� ,������ϵͳ���̺��û����̣�ǰ�߲��ܣ����߸�
		Map<String, AppInfo> szAppInfos = getAllAppInfo();
		ActivityManager am = (ActivityManager) m_pContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
		Map<String, Integer> szTotal = new HashMap<String, Integer>();
		int[] szPid = null;
		// ��ȡ���������еİ����Լ��ڴ�ռ��
		for (RunningAppProcessInfo pProp : run) {
			if (pProp.processName.equals("system")
					|| pProp.processName.equals("com.Android.phone")) {
				continue;
			}
			int nPid = pProp.pid;
			szPid = new int[] { nPid };
			Debug.i(TAG,
					"����["
							+ pProp.processName.toString()
							+ "]:"
							+ am.getProcessMemoryInfo(szPid)[0].dalvikPrivateDirty
							+ " KB");
			szTotal.put(pProp.processName.toString(),
					am.getProcessMemoryInfo(szPid)[0].dalvikPrivateDirty);
		}
		ArrayList<AppInfo> szRubbishProp = new ArrayList<AppInfo>();
		AppInfo pTmp = null;
		for (String str : szTotal.keySet()) {
			pTmp = szAppInfos.get(str);
			if (pTmp == null) {
				Debug.i(TAG, "pck:"+str+"�ڳ����б��в�����!");
			} else {
				if (pTmp.isSystemApp()) {
					Debug.i(TAG, "pck:"+str+"��ϵͳ���򲻼�¼");
				} else {
					Debug.i(TAG, "pck:"+str+"�ǿ���ɾ��������!~");
					pTmp.setM_nRam(szTotal.get(str));
					szRubbishProp.add(pTmp);
				}
			}
		}
		StringBuffer sb = new StringBuffer() ;
		for(AppInfo pTmp1 : szRubbishProp)
		{
			sb.append(pTmp1.toString()) ;
		}
		Debug.DEBUG_STR = sb.toString() ;
		Debug.logFile(Debug.DEBUG_STR);
		Message pMsg = null;
		pMsg = Message.obtain();
		pMsg.arg1 = FINSH_INNER_PROP;
		m_pRubbishHandler.sendMessage(pMsg);
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

	private int checkFileName(String sTmpName) {
		for (int i = 0; i < m_szType.length; i++) {
			if (sTmpName.indexOf(m_szType[i]) != -1) {
				return i;
			}
		}
		return -1;
	}

}
