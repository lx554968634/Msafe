package org.com.lix_.enable.engine;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.com.lix_.enable.EnableOfRubbishClear;
import org.com.lix_.util.Debug;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

/**
 * 获取进程相关信息
 * 
 * @author Administrator
 *
 */
public class PropInfoEngine {

	private String TAG = "PropInfoEngine";

	private Context m_pContext;

	private Handler m_pHandler;

	public PropInfoEngine(Context pContext, Handler pHandler) {
		m_pContext = pContext;
		m_pHandler = pHandler;
	}

	public List<RunningAppProcessInfo> getRunningTask() {
		ActivityManager am = (ActivityManager) m_pContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
		return run;
	}

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
			myApp.setSystemApp(!filterApp(appInfo));
			myApps.put(packageName, myApp);
		}
		return myApps;
	}

	/**
	 * 判断某个应用程序是 不是三方的应用程序
	 * 
	 * @param info
	 * @return 如果是第三方应用程序则返回true，如果是系统程序则返回false
	 */
	public boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}

	public ArrayList<RunningServiceInfo> getRubbishServices(
			Map<String, AppInfo> szAppInfos, List<RunningServiceInfo> szServices) {
		ArrayList<RunningServiceInfo> szRubbishProp = new ArrayList<RunningServiceInfo>();
		int nCount = szServices.size();
		String sPckName;
		AppInfo pInfo = null;
		for (int i = 0; i < nCount; i++) {
			sPckName = szServices.get(i).service.getPackageName();
			pInfo = szAppInfos.get(sPckName);
			if (pInfo == null) {
				continue;
			} else {
				if (pInfo.isSystemApp()) {
				} else {
					szRubbishProp.add(szServices.get(i));
				}
			}

		}
		return szRubbishProp;
	}

	public HashMap<String, AppInfo> getRunningRubbishInfo(
			Map<String, AppInfo> szAppInfos, List<RunningAppProcessInfo> szTotal) {
		HashMap<String, AppInfo> szRubbishProp = new HashMap<String, AppInfo>();

		int nCount = szTotal.size();
		String[] szPck;
		AppInfo pAppInfo;
		int[] szPid;
		ActivityManager am = (ActivityManager) m_pContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		RunningAppProcessInfo pInfo;
		Message msg = Message.obtain();
		msg.what = EnableOfRubbishClear.FIND_START_SCANCACHE;
		int nTotal = 0;
		for (int i = 0; i < nCount; i++) {
			pInfo = szTotal.get(i);
			szPck = pInfo.pkgList;
			for (String sTmp : szPck) {
				nTotal++;
			}
		}
		Message pMsg = null ;
		msg.arg1 = nTotal;
		m_pHandler.sendMessage(msg);
		for (int i = 0; i < nCount; i++) {
			pInfo = szTotal.get(i);
			szPck = pInfo.pkgList;
			for (String sTmp : szPck) {
				if (sTmp != null) {
					pAppInfo = szAppInfos.get(sTmp);
					if (pAppInfo != null) {
						pMsg = Message.obtain();
						pMsg.what = EnableOfRubbishClear.TXT_SHOW;
						pMsg.obj = pAppInfo.getAppName();
						m_pHandler.sendMessage(pMsg);
						if (pAppInfo.isSystemApp()) {
							msg = Message.obtain();
							msg.what = EnableOfRubbishClear.FIND_CACHE_SYSTEMPCK;
							msg.obj = pAppInfo.getPackageName();
							m_pHandler.sendMessage(msg);
						} else {
							szPid = new int[] { pInfo.pid };
							pAppInfo.setM_nRam(1024 * am
									.getProcessMemoryInfo(szPid)[0]
									.getTotalPrivateDirty());
							msg = Message.obtain();
							msg.what = EnableOfRubbishClear.RAM_SHOW;
							msg.arg1 = EnableOfRubbishClear.RAM_TYPE_PROCESS;
							msg.obj = 1024
									* am.getProcessMemoryInfo(szPid)[0]
											.getTotalPrivateDirty() + "";
							m_pHandler.sendMessage(msg);
							String packname = pAppInfo.getPackageName();
							szRubbishProp.put(packname,
									pAppInfo);
							try {
								Method method = PackageManager.class.getMethod(
										"getPackageSizeInfo", String.class,
										IPackageStatsObserver.class);
								method.invoke(m_pContext.getPackageManager(),
										packname, new CacheAndCodeObservice());
							} catch (RuntimeException ex) {
								ex.printStackTrace();
								msg = Message.obtain();
								msg.what = EnableOfRubbishClear.FIND_CACHE_NO;
								msg.obj = pAppInfo.getPackageName();
								m_pHandler.sendMessage(msg);
								Debug.e(TAG,
										"err in getpackageSizeInfo invoke:"
												+ ex.getMessage());
							} catch (Exception e) {
								e.printStackTrace();
								msg = Message.obtain();
								msg.what = EnableOfRubbishClear.FIND_CACHE_NO;
								msg.obj = pAppInfo.getPackageName();
								m_pHandler.sendMessage(msg);
								Debug.e(TAG,
										"err in getpackageSizeInfo invoke:"
												+ e.getMessage());
							}
						}
					}
				}
			}
		}
		return szRubbishProp;
	}

	class CacheAndCodeObservice extends IPackageStatsObserver.Stub {
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cache = pStats.cacheSize;
			Message msg = Message.obtain();
			msg.what = EnableOfRubbishClear.FIND_CACHE_USERPCK;
			if (cache > 0) {
				try {
					ActivityManager am = null;
					if (m_pContext != null) {
						am = (ActivityManager) m_pContext
								.getSystemService(Context.ACTIVITY_SERVICE);
					} else {
						return;
					}
					AppInfo pInfo = new AppInfo();
					pInfo.setPackageName(pStats.packageName);
					pInfo.setmCache(cache);
					msg.obj = pInfo;
					m_pHandler.sendMessage(msg);
					msg = Message.obtain();
					msg.what = EnableOfRubbishClear.RAM_SHOW;
					msg.arg1 = EnableOfRubbishClear.RAM_TYPE_APK_CACHE;
					msg.obj = cache + ":"+pStats.packageName;
					m_pHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Debug.e(TAG,
							"是在不明白这里怎么会出错:" + pStats.packageName + ":"
									+ e.toString());
				}
			} else {
				AppInfo pInfo = new AppInfo();
				pInfo.setPackageName(pStats.packageName);
				pInfo.setmCache(cache);
				msg.obj = pInfo;
				Debug.i(TAG, "obserse:" + pStats.packageName + ":" + cache);
				m_pHandler.sendMessage(msg);
			}
		}
	}

	public void deleteTaskOrService(String sPackName) {
		Debug.i(TAG, "执行一次强制清除垃圾进程:" + sPackName);
		ActivityManager am = (ActivityManager) m_pContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		try {
			Method forceStopPackage = am.getClass().getDeclaredMethod(
					"forceStopPackage", String.class);
			forceStopPackage.setAccessible(true);
			forceStopPackage.invoke(am, sPackName);
		} catch (Exception e) {
			Debug.e(TAG, "forceStopPackage 不能正常使用！改使用killBackgroundProcesses："
					+ sPackName);
			am.killBackgroundProcesses(sPackName);
		}
	}

	public List<RunningServiceInfo> getRunningService() {
		ActivityManager am = (ActivityManager) m_pContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> szServices = am.getRunningServices(50);
		return szServices;
	}

	public ArrayList<AppInfo> checkRubbish(Map<String, AppInfo> szRubbishProp,
			ArrayList<RunningServiceInfo> szRubbishService) {
		ArrayList<AppInfo> szArr = new ArrayList<AppInfo>();
		int nCount = szRubbishService.size();
		String sPckName = null;
		AppInfo pAppInfo = null;
		for (int i = 0; i < nCount; i++) {
			sPckName = szRubbishService.get(i).service.getPackageName();
			pAppInfo = szRubbishProp.get(sPckName);
			if (pAppInfo != null)
				pAppInfo.setmServiceCount(pAppInfo.getmServiceCount() + 1);
		}
		for (String sKey : szRubbishProp.keySet()) {
			szArr.add(szRubbishProp.get(sKey));
		}
		return szArr;
	}
}
