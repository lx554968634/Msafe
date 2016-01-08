package org.com.lix_.enable.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.com.lix_.enable.EnableOfApkAdmin;
import org.com.lix_.enable.EnableOfRootAdmin;
import org.com.lix_.util.Debug;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;

public class AppInfoEngine {

	private Context m_pContext;

	private Handler m_pHandler;

	public AppInfoEngine(Context pContext, Handler pHandler) {
		m_pContext = pContext;
		m_pHandler = pHandler;
	}

	public AppInfoEngine(Context pContext) {
		m_pContext = pContext;
	}

	public void init(Handler pHandler) {
		m_pHandler = pHandler;
	}

	private String TAG = "AppInfoEngine";

	/*
	 * ��ΪȨ���Ǻܳ����ֶΣ�������ר�šƳ�һ����������
	 */
	public List<AppInfo> getInstalledAppWithPermiss() {
		return getInstalledApp(true, EnableOfRootAdmin.SCAN_START,
				EnableOfRootAdmin.SCAN_ITEM);
	}

	/*
	 * ��װ����Ĵ�С���� 1:���������Ĵ�С+��������˾ʹ������г����С
	 */
	private List<AppInfo> getInstalledApp(boolean nFlag, int nScanStart,
			int nScanItem) {
		if (m_pContext == null) {
			Debug.e(TAG, m_pContext == null);
		} else {
			PackageManager pm = m_pContext.getPackageManager();
			Message msg = Message.obtain();
			List<AppInfo> appinfos = new ArrayList<AppInfo>();
			ActivityManager am = (ActivityManager) m_pContext
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> totalApps = am.getRunningAppProcesses();
			List<ApplicationInfo> installedAppList = pm
					.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
			if (installedAppList == null) {
				return null;
			}
			msg.what = nScanStart;
			msg.arg1 = installedAppList.size();
			m_pHandler.sendMessage(msg);
			for (ApplicationInfo application : installedAppList) {
				AppInfo appInfo = new AppInfo();
				int flags = application.flags;
				if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// �û�Ӧ��
					appInfo.setSystemApp(true);
				} else {
					// ϵͳӦ��
					appInfo.setSystemApp(false);
					continue;
				}
				if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
					// �ֻ��ڴ�
					appInfo.setInRom(true);
				} else {
					// �ⲿ�洢
					appInfo.setInRom(false);
				}
				String packname = application.packageName;
				RunningAppProcessInfo tmpRunningInfo = null;
				/*
				 * ���ֱ��ɨ���Լ����Ῠ������
				 */
				if (!packname.equals("org.com.lix_.ui")) 
					for (RunningAppProcessInfo pRunningInfo : totalApps) {
						for (String sPck : pRunningInfo.pkgList) {
							if (sPck.equals(packname)) {
								tmpRunningInfo = pRunningInfo;
							}
						}
					}
				String dir = application.publicSourceDir;
				appInfo.m_nFirstInstalledTime = new File(dir).lastModified();
				appInfo.setAppName(application.loadLabel(pm).toString());
				long nExtRam = 0;
				Debug.i(TAG, "");
				if (tmpRunningInfo != null) {
					Debug.i(TAG, "am == null :" + (am == null));
					Debug.i(TAG,
							"getMem == null:"
									+ am.getProcessMemoryInfo(new int[] { tmpRunningInfo.pid }) == null);
					Debug.i(TAG,
							"[0] == null:"
									+ am.getProcessMemoryInfo(new int[] { tmpRunningInfo.pid })[0] == null);
					try {
						nExtRam = 1024 * am
								.getProcessMemoryInfo(new int[tmpRunningInfo.pid])[0]
								.getTotalPrivateDirty();
					} catch (Exception e) {
						e.printStackTrace();
						nExtRam = 0;
					}
				}
				appInfo.setM_nRam(new File(dir).length() + nExtRam);
				try {
					if (nFlag
							&& pm.getPackageInfo(packname,
									PackageManager.GET_PERMISSIONS).requestedPermissions != null) {
						;
						appInfo.setSzPermission(pm.getPackageInfo(packname,
								PackageManager.GET_PERMISSIONS).requestedPermissions);
					} else {
						Debug.i(TAG, packname + "����Ҫʲô����Ȩ��");
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				msg = Message.obtain();
				msg.what = nScanItem;
				msg.obj = packname;
				m_pHandler.sendMessage(msg);
				appInfo.setPackageName(packname);
				appinfos.add(appInfo);
			}
			return appinfos;
		}
		return null;
	}

	public List<AppInfo> getInstalledApp() {
		return getInstalledApp(false, EnableOfApkAdmin.STARTSCAN,
				EnableOfApkAdmin.SCAN_ITEM);
	}
}
