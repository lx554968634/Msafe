package org.com.lix_.enable.engine;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.enable.EnableOfApkAdmin;
import org.com.lix_.enable.EnableOfRootAdmin;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
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
	 * 因为权限是很长的字段，所以我专门∑出一个函数处理
	 */
	public List<AppInfo> getInstalledAppWithPermiss() {
		return getInstalledApp(true, EnableOfRootAdmin.SCAN_START,
				EnableOfRootAdmin.SCAN_ITEM);
	}

	private List<AppInfo> getInstalledApp(boolean nFlag, int nScanStart,
			int nScanItem) {
		if (m_pContext == null) {
			Debug.e(TAG, m_pContext == null);
		} else {
			PackageManager pm = m_pContext.getPackageManager();
			Message msg = Message.obtain();
			List<AppInfo> appinfos = new ArrayList<AppInfo>();
			List<PackageInfo> packinfos = pm.getInstalledPackages(0);
			msg.what = nScanStart;
			msg.arg1 = packinfos.size();
			m_pHandler.sendMessage(msg);
			Debug.i(TAG, "开始扫扫描" + packinfos.size());
			for (PackageInfo packinfo : packinfos) {
				String packname = packinfo.packageName;
				AppInfo appInfo = new AppInfo();
				String name = packinfo.applicationInfo.loadLabel(pm).toString();
				try {
					if (nFlag && pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS).requestedPermissions != null) {
						 ;
						appInfo.setSzPermission(pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS).requestedPermissions);
					}else
					{
						Debug.i(TAG, name+"不需要什么特殊权限");
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				Debug.i(TAG, "名称:" + name);
				// 应用程序的特征标志。 可以是任意标志的组合
				int flags = packinfo.applicationInfo.flags;// 应用交的答题卡
				if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// 用户应用
					appInfo.setSystemApp(true);
				} else {
					// 系统应用
					appInfo.setSystemApp(false);
				}
				if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
					// 手机内存
					appInfo.setInRom(true);
				} else {
					// 外部存储
					appInfo.setInRom(false);
				}
				msg = Message.obtain();
				msg.what = nScanItem;
				msg.obj = packname;
				m_pHandler.sendMessage(msg);
				appInfo.setPackageName(packname);
				appInfo.setAppName(name);
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
