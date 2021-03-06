package org.com.lix_.enable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.com.lix_.Define;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.FileInfo;
import org.com.lix_.enable.engine.FileInfoEngine;
import org.com.lix_.enable.engine.PropInfoEngine;
import org.com.lix_.ui.R;
import org.com.lix_.ui.SceneOfSettingRubbishClear;
import org.com.lix_.ui.SceneOfShowRubbish;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 这个enable需要实现5个功能 1、内存加速，杀死进程和服务后台 2、系统应用缓存，删除/data/data/packagename/..cache
 * ..databases .. files .. fhareprefs 3、垃圾文件 4、多余安装包 5、应用卸载残余 adb shell mount -o
 * remount rw /
 * 
 * @author punsher
 *
 */
public class EnableOfRubbishClear extends Enable {

	private static final long serialVersionUID = 3906809163162500337L;

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
	public static final int START_SCAN_GETALLAPPINFO = 13;
	public static final int START_SCAN_GETRUNNINGTASK = 14;
	public static final int START_SCAN_GETRUNNINGRUBBISH = 15;
	public static final int START_SCAN_RUNNINGSERVICE = 16;
	public static final int START_SCAN_RUBBISHSERVICE = 17;
	public static final int START_SCAN_CHECKRUBBISH = 18;
	public static final int START_HAS_CACHE= 19 ;

	public static final int TXT_SHOW = 11;

	public static final int NONE_SDCARD = 12;

	

	private int m_nScanPckCacheCount = 0;

	public static final int RAM_TYPE_APK_CACHE = 5;

	public static final int RAM_TYPE_PROCESS = 6;

	private int m_nScanCount;

	private boolean m_bCacheReady = false;
	private boolean m_bPropScanReady = false;

	private Map<String, Long> m_szRamRecord = null;

	private HashMap<String, AppInfo> m_szApkCache;

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
	
	public void start()
	{
		doAsyWork();
	}

	private void init() {
		try {
			m_szBlackDir = new ArrayList<FileInfo>();
			m_szApkCache = new HashMap<String, AppInfo>();
			m_szRamRecord = new HashMap<String, Long>();
			m_szDetailCache = new HashMap<String, ArrayList<FileInfo>>();
			m_nTotalCache = 0;
			m_pTaskWorkEngine = new PropInfoEngine(m_pContext, m_pAsyHandler);
			m_pFileEngine = new FileInfoEngine(m_pContext, m_pAsyHandler);
		} catch (Error e) {
			e.printStackTrace();
			Debug.logFile("这里还能出错?", false);
		}
	}
	
	public long getRubbishCache()
	{
		return m_nTotalCache ;
	}
	
	@Override
	protected void doSynchrWork(Message msg) {
		super.doSynchrWork(msg);
		int nTag = msg.what;
		try {
			switch (nTag) {
			case NONE_SDCARD:
				break;
			case START_SCAN_GETALLAPPINFO:
			case START_SCAN_GETRUNNINGTASK:
			case START_SCAN_GETRUNNINGRUBBISH:
			case START_SCAN_RUNNINGSERVICE:
			case START_SCAN_RUBBISHSERVICE:
			case START_SCAN_CHECKRUBBISH:
				m_pCallback.callback(nTag);
				break;
			case FIND_CACHE_SYSTEMPCK:
				m_nScanCount++;
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
				m_nScanCount = 0;
				break;
			case FIND_CACHE_USERPCK:
				m_nScanCount++;
				if (m_nScanPckCacheCount == m_nScanCount) {
					msg = Message.obtain();
					msg.what = READY2CHECK_RUBBISHSERVICE;
					sendMessage(msg);
				}
				break;
			case FINISH_SCAN_RUBBISH:
				if (!m_bPropScanReady) {
					m_bPropScanReady = true;
				}
				if (m_bCacheReady && m_bPropScanReady)
					checkService();
				break;
			case READY2CHECK_RUBBISHSERVICE:
				if (!m_bCacheReady && m_nScanPckCacheCount == m_nScanCount) {
					m_bCacheReady = true;
				}
				if (m_bCacheReady && m_bPropScanReady)
					checkService();
				break;
			case FIND_CACHE_NO:
				m_nScanCount++;
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
				Long pValue = null;
				if (msg.arg1 == RAM_TYPE_APK_CACHE) {
					pValue = new Long(msg.obj.toString().split(":")[0]);
					if (m_szApkCache.get(msg.obj.toString().split(":")[1]) == null) {
						AppInfo pAppInfo = new AppInfo();
						pAppInfo.setPackageName(msg.obj.toString().split(":")[1]);
						pAppInfo.setM_nRam(pValue);
						m_szApkCache.put(msg.obj.toString().split(":")[1],
								pAppInfo);
					}
				} else {
					pValue = new Long(msg.obj.toString());
				}
				if(m_nTotalCache == 0 && pValue.longValue() !=0 )
				{
					m_pCallback.callback(START_HAS_CACHE);
				}
				m_nTotalCache += pValue.longValue();
				if (m_pCallback != null && pValue != null)
					m_pCallback.callback(msg.what, m_nTotalCache);
				if (pValue.longValue() != 0) {
					Long pInt;
					String sKey = Define.RAM_STR + (msg.arg1 + "");
					if ((pInt = m_szRamRecord.get(sKey)) == null) {
						m_szRamRecord.put((sKey), pValue);
					} else {
						m_szRamRecord.put(sKey,
								pInt.longValue() + pValue.longValue());
					}
				}
				break;
			case TXT_SHOW:
				if (msg.obj != null) {
					if (m_pCallback != null && msg.obj != null)
						m_pCallback.callback(msg.what, msg.obj);
				}
				break;
			}
		} catch (Error e) {
			e.printStackTrace();
			Debug.logFile("在ram_show出错:" + e.getMessage(), false);
		}
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		try {
			checkSD();
			checkProps();
		} catch (Error e) {
			e.printStackTrace();
			StringBuffer sb = new StringBuffer();
			for (int b = 0; b < e.getStackTrace().length; b++) {
				sb.append(e.getStackTrace()[b].toString());
			}
			Debug.logFile("出错:" + sb.toString() + ":" + e.getClass().getName(),
					false);
		}
	}
	
	@Override
	public void onViewClick(int nId) {
		switch(nId)
		{
		case R.id.ui_tag1:
			Intent pIntent = new Intent() ;
			pIntent.setClass(m_pContext, SceneOfSettingRubbishClear.class) ;
			m_pContext.startActivity(pIntent);
			break ;
		}
	}

	ArrayList<FileInfo> m_szBlackDir;
	HashMap<String, ArrayList<FileInfo>> m_szDetailCache;

	private void checkSD() {
		Message pMsg = null;
		int nTag = m_pFileEngine.retSDCardStatus();
		pMsg = Message.obtain();
		pMsg.what = PREPARE_FINISH;
		sendMessage(pMsg);
		if (nTag == 1 && Debug.DEBUG_STR != null) {
			StringBuffer sb = new StringBuffer();
			m_pFileEngine.readFile(Environment.getExternalStorageDirectory(),
					m_szBlackDir, m_szDetailCache, sb);
			Debug.i(TAG, "垃圾文件大小:" + m_szDetailCache.size());
			pMsg = Message.obtain();
			pMsg.what = FINSH_SD_RUBBISH;
			sendMessage(pMsg);
		} else {
			pMsg = Message.obtain();
			pMsg.what = NONE_SDCARD;
			sendMessage(pMsg);
		}
	}
	private void checkService() {
		Message pMsg = Message.obtain();
		pMsg.what = START_SCAN_RUNNINGSERVICE;
		sendMessage(pMsg);
		List<ActivityManager.RunningServiceInfo> szServices = m_pTaskWorkEngine
				.getRunningService();
		pMsg = Message.obtain();
		pMsg.what = START_SCAN_RUBBISHSERVICE;
		sendMessage(pMsg);
		ArrayList<ActivityManager.RunningServiceInfo> szRubbishService = m_pTaskWorkEngine
				.getRubbishServices(m_szAppInfos, szServices);
		pMsg = Message.obtain();
		pMsg.what = START_SCAN_CHECKRUBBISH;
		sendMessage(pMsg);
		ArrayList<AppInfo> szRubbishTask = m_pTaskWorkEngine.checkRubbish(
				m_szRubbishProp, szRubbishService);
		Debug.DEBUG_STR = "开始记录";
		Debug.DEBUG_STR = "文件记录完毕";
		finishScan();
	}

	private HashMap<String, ArrayList<FileInfo>> m_szRubbishFlies;

	private HashMap<String, ArrayList<FileInfo>> m_szApkFiles;

	private void finishScan() {
		m_szRubbishFlies = new HashMap<String, ArrayList<FileInfo>>();
		String[] szArray = m_pContext.getResources().getStringArray(
				R.array.type_rubbishclear_array);
		for (int i = 0; i < 4; i++) {
			if (m_szDetailCache.get(szArray[i]) != null
					&& m_szDetailCache.get(szArray[i]).size() != 0) {
				m_szRubbishFlies.put(szArray[i],
						m_szDetailCache.get(szArray[i]));
			}
		}
		;
		m_szApkFiles = new HashMap<String, ArrayList<FileInfo>>();
		if (m_szDetailCache.get(szArray[4]) != null
				&& m_szDetailCache.get(szArray[4]).size() != 0) {
			m_szApkFiles.put(szArray[4], m_szDetailCache.get(szArray[4]));
		}
		Message pMsg = null;
		pMsg = Message.obtain();
		pMsg.what = FINSH_INNER_PROP;
		sendMessage(pMsg);
	}

	HashMap<String, AppInfo> m_szAppInfos;
	HashMap<String, AppInfo> m_szRubbishProp;

	private void checkProps() {
		Debug.DEBUG_STR = "准备分析进程";
		// 开始扫描系统进程 ,区分了系统进程和用户进程，前者不管，后者干
		Message pMsg = Message.obtain();
		pMsg.what = START_SCAN_GETALLAPPINFO;
		sendMessage(pMsg);
		m_szAppInfos = m_pTaskWorkEngine.getAllAppInfo();
		pMsg = Message.obtain();
		pMsg.what = START_SCAN_GETRUNNINGTASK;
		sendMessage(pMsg);
		List<RunningAppProcessInfo> szTotal = m_pTaskWorkEngine
				.getRunningTask();
		pMsg = Message.obtain();
		pMsg.what = START_SCAN_GETRUNNINGRUBBISH;
		sendMessage(pMsg);
		m_szRubbishProp = m_pTaskWorkEngine.getRunningRubbishInfo(m_szAppInfos,
				szTotal);
		Message msg = Message.obtain();
		msg.what = FINISH_SCAN_RUBBISH;
		sendMessage(msg);
	}

	public Map<String, Long> getRubbishSizeData() {
		return m_szRamRecord;
	}

	public String getRubbishSize(int i) {
		String sTag = "";
		Long pLong0, pLong1, pLong2, pLong3;
		long nTarget = 0;
		switch (i) {
		case 0:
			pLong0 = m_szRamRecord
					.get(Define.RAM_STR + (RAM_TYPE_PROCESS + ""));
			nTarget = UiUtils.getLongValue(pLong0);
			break;
		case 1:
			pLong0 = m_szRamRecord.get(Define.RAM_STR
					+ (RAM_TYPE_APK_CACHE + ""));
			nTarget = UiUtils.getLongValue(pLong0);
			break;
		case 2:
			pLong0 = m_szRamRecord.get(Define.RAM_STR + (0 + ""));
			pLong1 = m_szRamRecord.get(Define.RAM_STR + (1 + ""));
			pLong2 = m_szRamRecord.get(Define.RAM_STR + (2 + ""));
			pLong3 = m_szRamRecord.get(Define.RAM_STR + (3 + ""));
			nTarget = UiUtils.getLongValue(pLong0)
					+ UiUtils.getLongValue(pLong1)
					+ UiUtils.getLongValue(pLong2)
					+ UiUtils.getLongValue(pLong3);
			break;
		case 3:
			pLong0 = m_szRamRecord.get(Define.RAM_STR + (4 + ""));
			nTarget = UiUtils.getLongValue(pLong0);
			break;
		case 4:
			return m_szBlackDir.size() + "个";
		}
		return UiUtils.getCacheSize(nTarget);
	}

	public OnItemClickListener getOnGridItemListener() {
		return new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent pIntent = new Intent();
				String[] szArray = m_pContext.getResources().getStringArray(
						R.array.type_rubbishclear_array);
				pIntent.putExtra(Define.INTENT_TAG0, arg2 + "");
				switch (arg2) {
				case 0:
					Debug.i(TAG, "进程垃圾界面:" + m_szRubbishProp);
					pIntent.putExtra(Define.RAM_STR, m_szRubbishProp);
					break;
				case 1:// apk缓存
					Debug.i(TAG, "apk缓存:" + m_szApkCache.size());
					pIntent.putExtra(Define.RAM_STR, m_szApkCache);
					break;
				case 2:// log+logs+cache+tmp
					if (szArray == null)
						return;
					pIntent.putExtra(Define.RAM_STR, m_szRubbishFlies);
					break;
				case 3:// log+logs+cache+tmp
					if (szArray == null)
						return;
					pIntent.putExtra(Define.RAM_STR, m_szApkFiles);
					break;
				case 4:// 空文件夹
					Debug.i(TAG, "空文件夹:" + m_szBlackDir.size());
					pIntent.putExtra(Define.RAM_STR, m_szBlackDir);
					break;
				}
				pIntent.setClass(m_pContext, SceneOfShowRubbish.class);
				m_pContext.startActivity(pIntent);
			}
		};
	}
}
