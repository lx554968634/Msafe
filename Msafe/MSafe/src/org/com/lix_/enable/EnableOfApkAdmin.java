package org.com.lix_.enable;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.AppInfoEngine;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class EnableOfApkAdmin extends Enable {
	private String TAG = "EnableOfApkAdmin" ;

	public static final int STARTSCAN = 1;

	public static final int SCAN_ITEM = 2;

	public static final int SCAN_OVER = 3;

	private Context m_pContext;

	private EnableCallback m_pCallback;

	private AppInfoEngine m_pEngine;

	public EnableOfApkAdmin(Context pContext) {
		super(pContext);
		m_pContext = pContext;
	}

	public void init(EnableCallback pCallback) {
		m_pCallback = pCallback;
		m_pEngine = new AppInfoEngine(m_pContext);
		m_pEngine.init(m_pAsyHandler);
		doAsyWork();
	}

	private void doSynchronizeWork(Message msg) {
		int nTag = msg.what;
		switch (nTag) {
		case STARTSCAN:
			m_pCallback.callback("" + msg.what, "" + msg.arg1);
			break;
		case SCAN_ITEM:
			m_pCallback.callback("" + msg.what, msg.obj);
			break;
		case 3:
			m_pCallback.callback("" + msg.what);
			break;
		}
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		scanUserApk();
	}

	private List<AppInfo> m_pData;
	public long m_nTotalSize ;
	private void getTotalSize()
	{
		if(m_pData != null)
		{
			for(AppInfo pInfo : m_pData)
			{
				m_nTotalSize+= pInfo.getM_nRam() ;
			}
		}
	}

	private void scanUserApk() {
		m_pData = m_pEngine.getInstalledApp();
		if(m_pData != null)
		{
			getTotalSize() ;
		}
		Message msg = Message.obtain();
		msg.what = SCAN_OVER;
		sendMessage(msg);
	}

	@Override
	protected void doSynchrWork(Message pMsg) {
		super.doSynchrWork(pMsg);
		doSynchronizeWork(pMsg);
	}

	@Override
	public void finish() {
	}

	@Override
	public void onViewClick(int nId) {
	}

	public int getDataCount() {
		return m_pData == null ? 0 : m_pData.size();
	}

	public AppInfo getDataInfo(int position) {
		return m_pData == null ? null : m_pData.get(position);
	}

}
