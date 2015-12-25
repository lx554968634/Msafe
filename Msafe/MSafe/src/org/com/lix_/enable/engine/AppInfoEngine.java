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
	 * ��ΪȨ���Ǻܳ����ֶΣ�������ר�šƳ�һ����������
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
			Debug.i(TAG, "��ʼɨɨ��" + packinfos.size());
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
						Debug.i(TAG, name+"����Ҫʲô����Ȩ��");
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				Debug.i(TAG, "����:" + name);
				// Ӧ�ó����������־�� �����������־�����
				int flags = packinfo.applicationInfo.flags;// Ӧ�ý��Ĵ��⿨
				if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// �û�Ӧ��
					appInfo.setSystemApp(true);
				} else {
					// ϵͳӦ��
					appInfo.setSystemApp(false);
				}
				if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
					// �ֻ��ڴ�
					appInfo.setInRom(true);
				} else {
					// �ⲿ�洢
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
