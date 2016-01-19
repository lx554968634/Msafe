package org.com.lix_.enable;

import java.util.List;

import org.com.lix_.db.dao.VirusDaoImpl;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.AppInfoEngine;

import android.content.Context;
import android.os.Message;

public class EnableOfVirusAdmin extends Enable {

	/**
	 * ��ʼ������ɨ������
	 */
	public static final int INIT_VIRVUS_ENGINE = 1;
	/**
	 * ����̫�š�����128λ���������.
	 */
	public static final int VIRVUS_SHIT_HAPPEN = 2;

	/**
	 * ɨ��ĳ������
	 */
	public static final int SCAN_ITEM = 3;

	@Override
	protected void doSynchrWork(Message pMsg) {
		super.doSynchrWork(pMsg);
		int nTag = pMsg.what;
		switch (nTag) {
		case INIT_VIRVUS_ENGINE:
			m_pCallback.callback(nTag);
			break;
		case VIRVUS_SHIT_HAPPEN:
			m_pCallback.callback(nTag, pMsg.arg1);
			break;
		}
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		Message msg = Message.obtain();
		msg.what = INIT_VIRVUS_ENGINE;
		sendMessage(msg);
		List<AppInfo> pList = m_pAppInfoEngine.getVirvusCheckApp();
		// ����һ����Ҫɨ���ļ���ϵͳ.��ʱ����
		scanVirvusAppInfo(pList);
	}

	private void scanVirvusAppInfo(List<AppInfo> pList) {
		if (pList == null) {
			Message msg = Message.obtain();
			msg.what = VIRVUS_SHIT_HAPPEN;
			msg.arg1 = 0;
			sendMessage(msg);
			return;
		}
		boolean bFlag = false ;
		for (AppInfo pInfo : pList) {
			Message msg = Message.obtain();
			msg.what = SCAN_ITEM;
			msg.obj = pInfo.getAppName();
			sendMessage(msg);
			bFlag = m_pDao.queryApp(pInfo) ;
		}
	}

	private AppInfoEngine m_pAppInfoEngine;
	private VirusDaoImpl m_pDao;

	public EnableOfVirusAdmin(Context pContext, EnableCallback pCallback) {
		super(pContext);
		setCallback(pCallback);
		m_pAppInfoEngine = new AppInfoEngine(pContext);
		m_pDao = new VirusDaoImpl(m_pContext);
	}

	@Override
	public void finish() {

	}

	@Override
	public void onViewClick(int nId) {
	}

	/**
	 * ɨ�財����ʼ
	 */
	public void startScanVirvus() {
		doAsyWork();
	}

}
