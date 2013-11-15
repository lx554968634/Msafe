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
 * ��ȡ���������Ϣ
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
		Debug.i(TAG, "ɨ���¼���еĽ���");
		ActivityManager am = (ActivityManager) m_pContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
		return run;
	}

	public HashMap<String, AppInfo> getAllAppInfo() {
		Debug.i(TAG, "ɨ�貢��¼���е�Ӧ��");
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

	public ArrayList<RunningServiceInfo> getRubbishServices(
			Map<String, AppInfo> szAppInfos, List<RunningServiceInfo> szServices) {
		Debug.i(TAG, "�Ա�ɨ��������������");
		ArrayList<RunningServiceInfo> szRubbishProp = new ArrayList<RunningServiceInfo>();
		int nCount = szServices.size();
		String sPckName;
		AppInfo pInfo = null;
		for (int i = 0; i < nCount; i++) {
			sPckName = szServices.get(i).service.getPackageName();
			pInfo = szAppInfos.get(sPckName);
			if (pInfo == null) {
				Debug.e(TAG, "��֪Ϊ�� ��������" + sPckName + " �İ������а�װ�����в�����!");
				continue;
			} else {
				if (pInfo.isSystemApp()) {
					Debug.i(TAG, "���Ӧ����ϵͳӦ�û��Ǳ��˰�:" + sPckName);
				} else {
					Debug.i(TAG, "���Լ�����������service:" + sPckName);
					szRubbishProp.add(szServices.get(i));
				}
			}

		}
		Debug.i(TAG, "����������:" + szRubbishProp.size());
		return szRubbishProp;
	}

	public Map<String, AppInfo> getRunningRubbishInfo(
			Map<String, AppInfo> szAppInfos, List<RunningAppProcessInfo> szTotal) {
		Debug.i(TAG, "��ʼ�Ա�������������");
		Map<String, AppInfo> szRubbishProp = new HashMap<String, AppInfo>();

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
		msg.arg1 = nTotal;
		m_pHandler.sendMessage(msg);
		for (int i = 0; i < nCount; i++) {
			pInfo = szTotal.get(i);
			szPck = pInfo.pkgList;
			for (String sTmp : szPck) {
				if (sTmp != null) {
					pAppInfo = szAppInfos.get(sTmp);
					if (pAppInfo != null) {
						if (pAppInfo.isSystemApp()) {
							Debug.i(TAG, "���������ϵͳ����:" + sTmp);
							msg = Message.obtain();
							msg.what = EnableOfRubbishClear.FIND_CACHE_SYSTEMPCK;
							msg.obj = pAppInfo.getPackageName();
							m_pHandler.sendMessage(msg);
						} else {
							Debug.i(TAG, "����������û����̣��ɸɵ�:" + sTmp);
							szPid = new int[] { pInfo.pid };
							pAppInfo.setM_nRam(am.getProcessMemoryInfo(szPid)[0].dalvikPrivateDirty);
							szRubbishProp.put(pAppInfo.getPackageName(),
									pAppInfo);
							String packname = pAppInfo.getPackageName();
							try {
								Debug.i(TAG, "�����۲���ȥ�鿴�ڴ棺" + packname);
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
					Debug.i(TAG, "obserse:" + pStats.packageName + ":" + cache);
					m_pHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Debug.e(TAG,
							"���ڲ�����������ô�����:" + pStats.packageName + ":"
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
		Debug.i(TAG, "ִ��һ��ǿ�������������:" + sPackName);
		ActivityManager am = (ActivityManager) m_pContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		try {
			Method forceStopPackage = am.getClass().getDeclaredMethod(
					"forceStopPackage", String.class);
			forceStopPackage.setAccessible(true);
			forceStopPackage.invoke(am, sPackName);
		} catch (Exception e) {
			Debug.e(TAG, "forceStopPackage ��������ʹ�ã���ʹ��killBackgroundProcesses��"
					+ sPackName);
			am.killBackgroundProcesses(sPackName);
		}
	}

	public List<RunningServiceInfo> getRunningService() {
		Debug.i(TAG, "��ȡȫ���������е���������");
		ActivityManager am = (ActivityManager) m_pContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> szServices = am.getRunningServices(50);
		return szServices;
	}

	public ArrayList<AppInfo> checkRubbish(Map<String, AppInfo> szRubbishProp,
			ArrayList<RunningServiceInfo> szRubbishService) {
		Debug.i(TAG, "checkRubbish-�Ա�ɨ�����е���������:"
				+ (szRubbishProp == null));
		ArrayList<AppInfo> szArr = new ArrayList<AppInfo>();
		int nCount = szRubbishService.size();
		String sPckName = null;
		AppInfo pAppInfo = null;
		Debug.i(TAG, "nCount:" + nCount);
		for (int i = 0; i < nCount; i++) {
			sPckName = szRubbishService.get(i).service.getPackageName();
			pAppInfo = szRubbishProp.get(sPckName);
			if (pAppInfo != null)
				pAppInfo.setmServiceCount(pAppInfo.getmServiceCount() + 1);
		}
		Debug.i(TAG, "szRubbishProp.keySet():" + szRubbishProp.keySet().size());
		for (String sKey : szRubbishProp.keySet()) {
			szArr.add(szRubbishProp.get(sKey));
		}
		Debug.i(TAG, "szRubbishProp.keySet():voer");
		return szArr;
	}

}
