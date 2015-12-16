package org.com.lix_.enable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.FileInfoEngine;
import org.com.lix_.enable.engine.PropInfoEngine;
import org.com.lix_.util.Debug;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
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

	public static final int FIND_CACHE_USERPCK = 4;

	public static final int FIND_CACHE_NO = 5;

	public static final int FIND_CACHE_SYSTEMPCK = 7;

	public static final int FIND_START_SCANCACHE = 6;

	public static final int RAM_SHOW = 8;
	public static final int READY2CHECK_RUBBISHSERVICE = 9;
	public static final int FINISH_SCAN_RUBBISH = 10;

	public static final int TXT_SHOW =11;

	private int m_nScanPckCacheCount = 0;

	private int m_nScanCount;

	private boolean m_bCacheReady = false;
	private boolean m_bPropScanReady = false;

	/**
	 * ������
	 */
	Handler m_pRubbishHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int nTag = msg.what;
			try {
				switch (nTag) {
				case FIND_CACHE_SYSTEMPCK:
					m_nScanCount++;
					Debug.i(TAG, "FIND_CACHE_SYSTEMPCK :" + m_nScanCount + ":"
							+ msg.obj);
					if (m_nScanPckCacheCount == m_nScanCount) {
						msg = Message.obtain();
						msg.what = READY2CHECK_RUBBISHSERVICE;
						sendMessage(msg);
					}
					break;
				case FIND_START_SCANCACHE:
					m_nScanPckCacheCount = msg.arg1;
					m_bCacheReady = false;
					m_bPropScanReady = false;
					Debug.i(TAG, "��ʼɨ�軺��:" + m_nScanPckCacheCount);
					m_nScanCount = 0;
					break;
				case FIND_CACHE_USERPCK:
					m_nScanCount++;
					Debug.i(TAG, "FIND_CACHE_USERPCK :" + m_nScanCount + ":"
							+ msg.obj);
					if (m_nScanPckCacheCount == m_nScanCount) {
						msg = Message.obtain();
						msg.what = READY2CHECK_RUBBISHSERVICE;
						sendMessage(msg);
					}
					break;
				case FINISH_SCAN_RUBBISH:
					if (!m_bPropScanReady) {
						Debug.i(TAG, "ɨ����������");
						m_bPropScanReady = true;
					}
					if (m_bCacheReady && m_bPropScanReady)
						checkService();
					break;
				case READY2CHECK_RUBBISHSERVICE:
					if (!m_bCacheReady && m_nScanPckCacheCount == m_nScanCount) {
						Debug.i(TAG, "ɨ�����л���");
						m_bCacheReady = true;
					}
					if (m_bCacheReady && m_bPropScanReady)
						checkService();
					break;
				case FIND_CACHE_NO:
					m_nScanCount++;
					Debug.i(TAG, "FIND_CACHE_NO :" + m_nScanCount);
					if (m_nScanPckCacheCount == m_nScanCount) {
						msg = Message.obtain();
						msg.what = READY2CHECK_RUBBISHSERVICE;
						sendMessage(msg);
					}
					break;
				case FINSH_SD_RUBBISH:
					if (m_pCallback != null)
						m_pCallback.callback(msg.what, null);
					break;
				case PREPARE_FINISH:
					if (m_pCallback != null)
						m_pCallback.callback(msg.what, null);
					break;
				case FINSH_INNER_PROP:
					if (m_pCallback != null)
						m_pCallback.callback(msg.what, null);
					break;
				case RAM_SHOW:
					if (msg.obj == null)
						break;
					Long pValue = new Long(msg.obj.toString());
					m_nTotalCache += pValue.longValue();
//					Debug.logFile("cache add :"+m_nTotalCache +":"+msg.obj.toString(), true);
					
					if (m_pCallback != null && pValue != null)
						m_pCallback.callback(msg.what, m_nTotalCache);

					break;
				case TXT_SHOW:
					if(msg.obj != null)
					{
						if(m_pCallback != null && msg.obj != null)
							m_pCallback.callback(msg.what,msg.obj);
					}
					break ;
				}
			} catch (Error e) {
				e.printStackTrace();
				Debug.logFile("��ram_show����:" + e.getMessage(), false);
			}
		}

	};

	private long m_nTotalCache;
	private String TAG = "EnableOfRubbishClear";

	private Context m_pContext;
	private FileInfoEngine m_pFileEngine;
	private PropInfoEngine m_pTaskWorkEngine;

	public EnableOfRubbishClear(Context pContext) {
		super(pContext);
		m_pContext = pContext;
		init();
	}

	@Override
	public void finish() {

	}

	private void init() {
		try {
			m_nTotalCache = 0 ;
			m_pTaskWorkEngine = new PropInfoEngine(m_pContext,
					m_pRubbishHandler);
			m_pFileEngine = new FileInfoEngine(m_pContext, m_pRubbishHandler);
			m_pRubbishTask.execute(0);
		} catch (Error e) {
			e.printStackTrace();
			Debug.logFile("���ﻹ�ܳ���?", false);
		}
	}

	@Override
	public void onViewClick(int nId) {

	}

	AsyncTask m_pRubbishTask = new AsyncTask() {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Debug.e(TAG, "��ʼ׼��ɨ��sd��");
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				checkSD();
				checkProps();
			} catch (Error e) {
				e.printStackTrace();
				StringBuffer sb = new StringBuffer();
				for (int b = 0; b < e.getStackTrace().length; b++) {
					sb.append(e.getStackTrace()[b].toString());
				}
				Debug.logFile("����:" + sb.toString() + ":"
						+ e.getClass().getName(), false);
			}

			return null;
		}
	};

	private void checkSD() {
		Message pMsg = null;
		String nStatus = Environment.getExternalStorageState();
		String sTag = "";
		int nTag = m_pFileEngine.retSDCardStatus();
		pMsg = Message.obtain();
		pMsg.what = PREPARE_FINISH;
		m_pRubbishHandler.sendMessage(pMsg);
		if (nTag == 1 && Debug.DEBUG_STR != null) {
			Debug.i(TAG, "��Ŀ¼��"
					+ Environment.getRootDirectory().getAbsolutePath());
			Debug.i(TAG, "extrĿ¼��"
					+ Environment.getExternalStorageDirectory()
							.getAbsolutePath());
			ArrayList<String> szBlackDir = new ArrayList<String>();
			HashMap<String, ArrayList<String>> szDetailCache = new HashMap<String, ArrayList<String>>();
			StringBuffer sb = new StringBuffer();
			m_pFileEngine.readFile(Environment.getExternalStorageDirectory(),
					szBlackDir, szDetailCache,sb);
//			Debug.logFile(sb.toString(), true);
			pMsg = Message.obtain();
			pMsg.what = FINSH_SD_RUBBISH;
			m_pRubbishHandler.sendMessage(pMsg);
		}
	}

	private void checkService() {
		Debug.i(TAG, "service");
		List<ActivityManager.RunningServiceInfo> szServices = m_pTaskWorkEngine
				.getRunningService();
		ArrayList<ActivityManager.RunningServiceInfo> szRubbishService = m_pTaskWorkEngine
				.getRubbishServices(m_szAppInfos, szServices);
		Debug.i(TAG, "szRubbishService == null :" + (szRubbishService == null));
		ArrayList<AppInfo> szRubbishTask = m_pTaskWorkEngine.checkRubbish(
				m_szRubbishProp, szRubbishService);
		Debug.i(TAG, "checkRubbish over!:" + szRubbishTask.size());
		Debug.DEBUG_STR = "��ʼ��¼";
		Debug.DEBUG_STR = "�ļ���¼���";
		finishScan();
	}

	private void finishScan() {
		Message pMsg = null;
		pMsg = Message.obtain();
		pMsg.what = FINSH_INNER_PROP;
		m_pRubbishHandler.sendMessage(pMsg);
	}

	HashMap<String, AppInfo> m_szAppInfos;
	Map<String, AppInfo> m_szRubbishProp;

	private void checkProps() {
		Debug.DEBUG_STR = "׼����������";
		// ��ʼɨ��ϵͳ���� ,������ϵͳ���̺��û����̣�ǰ�߲��ܣ����߸�
		m_szAppInfos = m_pTaskWorkEngine.getAllAppInfo();
		List<RunningAppProcessInfo> szTotal = m_pTaskWorkEngine
				.getRunningTask();
		m_szRubbishProp = m_pTaskWorkEngine.getRunningRubbishInfo(m_szAppInfos,
				szTotal);
		Message msg = Message.obtain();
		msg.what = FINISH_SCAN_RUBBISH;
		Debug.i(TAG, "checkProp over:" + (m_szRubbishProp.size()));
		m_pRubbishHandler.sendMessage(msg);
	}

}
