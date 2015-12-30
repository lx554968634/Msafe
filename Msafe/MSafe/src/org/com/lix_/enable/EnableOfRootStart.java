package org.com.lix_.enable;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.util.Debug;

import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Message;

public class EnableOfRootStart extends Enable {

	private String TAG = "EnableOfRootStart";

	private EnableCallback m_pCallback;

	private List<RunningServiceInfo> m_szRunningAutoStartService;

	private List<RunningServiceInfo> m_szRunningCloseAutoStartService;

	private List<RunningServiceInfo> m_szTmpList;

	public static final int FINISH_DIVIDE_LIST = 1;

	public EnableOfRootStart(Context pContext,EnableCallback pCallback,
			List<RunningServiceInfo> szList) {
		m_pContext = pContext ;
		m_pCallback = pCallback;
		m_szTmpList = szList;
		init();
	}

	private void init() {
		doAsyWork();
	}

	@Override
	protected void doSynchrWork(Message pMsg) {
		super.doSynchrWork(pMsg);
		int nWhat = pMsg.what;
		switch (nWhat) {
		case FINISH_DIVIDE_LIST:
			if (m_szTmpList != null || m_szTmpList.size() == 0) {
				m_szTmpList.clear();
				m_szTmpList = null;
				System.gc();
			} else {
				Debug.e(TAG, "root start 基础list为null");
			}
			m_pCallback.callback(nWhat);
			break;
		}
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		divideList(m_szTmpList);
		Message pMsg = Message.obtain();
		pMsg.what = FINISH_DIVIDE_LIST;
		m_pAsyHandler.sendMessage(pMsg);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onViewClick(int nId) {
		// TODO Auto-generated method stub

	}

	private void divideList(List<RunningServiceInfo> szList) {
		int nStatue = 0;
		if (szList == null || szList.size() == 0) {
			Debug.e(TAG, "麻痹,没有正在运行的服务");
		} else {
			m_szRunningAutoStartService = new ArrayList<RunningServiceInfo>();
			m_szRunningCloseAutoStartService = new ArrayList<RunningServiceInfo>();
			for (RunningServiceInfo pInfo : szList) {
				nStatue = m_pContext.getPackageManager()
						.getComponentEnabledSetting(pInfo.service);
				switch (nStatue) {
				case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT:
					m_szRunningAutoStartService.add(pInfo);
					break;
				case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
					m_szRunningCloseAutoStartService.add(pInfo);
					break;
				case PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER:
					m_szRunningCloseAutoStartService.add(pInfo);
					break;
				case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
			 		m_szRunningAutoStartService.add(pInfo);
					break;
				default:
					break;
				}
				Debug.i(TAG, "pInfo:" + pInfo.service.getPackageName() + ":"
						+ nStatue);
			}
		}
	}

	public List<RunningServiceInfo> getAutoCloseStartList() {
		// TODO Auto-generated method stub
		return m_szRunningCloseAutoStartService;
	}

	public List<RunningServiceInfo> getAutoStartList() {
		// TODO Auto-generated method stub
		return m_szRunningAutoStartService;
	}
}
