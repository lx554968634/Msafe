package org.com.lix_.enable.engine;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.enable.EnableOfApkAdmin;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AppInfoEngine {

	private Context m_pContext;
	
	private Handler m_pHandler ;

	public AppInfoEngine(Context pContext) {
		m_pContext = pContext;
	}
	
	public void init(Handler pHandler)
	{
		m_pHandler  = pHandler ;
	}
	

	private String TAG = "AppInfoEngine";

	public List<AppInfo> getInstalledApp() {
		if (m_pContext == null) {
			Debug.e(TAG, m_pContext == null);
		} else {
			PackageManager pm = m_pContext.getPackageManager();
			Message msg  = Message.obtain() ;
			List<AppInfo> appinfos = new ArrayList<AppInfo>();
			List<PackageInfo> packinfos = pm.getInstalledPackages(0);
			msg.what = EnableOfApkAdmin.STARTSCAN ; 
			msg.arg1 = packinfos.size() ;
			m_pHandler.sendMessage(msg) ;
			Debug.i(TAG, "��ʼɨɨ��"+packinfos.size());
			for (PackageInfo packinfo : packinfos) {
				String packname = packinfo.packageName;
				AppInfo appInfo = new AppInfo();
				String name = packinfo.applicationInfo.loadLabel(pm).toString()
					;
				Debug.i(TAG, "����:"+name);
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
				msg.what = EnableOfApkAdmin.SCAN_ITEM ; 
				msg.obj = packname ;
				m_pHandler.sendMessage(msg) ;
				appInfo.setPackageName(packname);
				appInfo.setAppName(name);
				appinfos.add(appInfo);
			}
			return appinfos ;
		}
		return null;
	}
}
