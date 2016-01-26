package org.com.lix_.enable;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.util.Debug;

import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;

public class EnableOfRootStart extends Enable {

	private String TAG = "EnableOfRootStart";

	private EnableCallback m_pCallback;

	private List<AppInfo> m_szRunningAutoStartService;

	private List<AppInfo> m_szRunningCloseAutoStartService;

	private List<RunningServiceInfo> m_szTmpList;

	private List<AppInfo> m_szTmpList2;

	public static final int FINISH_DIVIDE_LIST = 1;

	public EnableOfRootStart(Context pContext, EnableCallback pCallback,
			List szList, List szList2) {
		m_pContext = pContext;
		m_pCallback = pCallback;
		m_szTmpList = szList2;
		m_szTmpList2 = szList;
	}

	public void init() {
		doRestartWork();
	}

	@Override
	protected void doSynchrWork(Message pMsg) {
		super.doSynchrWork(pMsg);
		int nWhat = pMsg.what;
		switch (nWhat) {
		case FINISH_DIVIDE_LIST:
			m_pCallback.callback(nWhat);
			break;
		}
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		divideList();
		Message pMsg = Message.obtain();
		pMsg.what = FINISH_DIVIDE_LIST;
		m_pAsyHandler.sendMessage(pMsg);
	}

	@Override
	public void finish() {
	}

	@Override
	public void onViewClick(int nId) {
	}

	private void divideList() {
		if (m_szTmpList == null || m_szTmpList.size() == 0
				|| m_szTmpList2 == null || m_szTmpList2.size() == 0) {
			Debug.e(TAG, "麻痹,没有正在运行的服务");
		} else {
			m_szRunningAutoStartService = new ArrayList<AppInfo>();
			m_szRunningCloseAutoStartService = new ArrayList<AppInfo>();
			for (AppInfo pInfo : m_szTmpList2) {
				boolean bFlag = false;
				for (RunningServiceInfo running : m_szTmpList) {
					if (pInfo.getPackageName().equals(
							running.service.getPackageName())) {
						// 这两者拥有同样的包名
						m_szRunningAutoStartService.add(pInfo);
						bFlag = true;
						break;
					}
				}
				if (!bFlag) {
					m_szRunningCloseAutoStartService.add(pInfo);
				}
			}
		}
	}

	public List<AppInfo> getAutoStartList() {
		return m_szRunningAutoStartService;
	}

	public List<AppInfo> getAutoCloseStartList() {
		return m_szRunningCloseAutoStartService;
	}

	public int getTotalSize() {
		return getListSize(m_szRunningAutoStartService)
				+ getListSize(m_szRunningCloseAutoStartService);
	}

	private int getListSize(List pList) {
		int nX = 0;
		if (pList == null || pList.size() == 0) {
			nX = 0;
		} else {
			nX = 1 + pList.size();
		}
		return nX;
	}
}
