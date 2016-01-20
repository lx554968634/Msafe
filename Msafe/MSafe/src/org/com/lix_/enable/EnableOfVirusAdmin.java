package org.com.lix_.enable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.com.lix_.db.dao.VirusDaoImpl;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.AppInfoEngine;

import android.content.Context;
import android.os.Message;

public class EnableOfVirusAdmin extends Enable {

	private String TAG = "EnableOfVirusAdmin";

	/**
	 * 初始化病毒扫描引擎
	 */
	public static final int INIT_VIRVUS_ENGINE = 1;
	/**
	 * 病毒太屌。老子128位引擎崩溃了.
	 */
	public static final int VIRVUS_SHIT_HAPPEN = 2;

	/**
	 * 扫描到病毒
	 */
	public static final int SCAN_ONE_VIRVUS = 3;
	/**
	 * 扫描完毕
	 */
	public static final int SCAN_OVER = 4;

	/**
	 * 扫描某个程序
	 */
	public static final int SCAN_ITEM = 5;

	/**
	 * 恶意应用类型
	 */
	public static final int VIRVUS_TYPE1 = 1;
	/**
	 * 隐患应用类型
	 */
	public static final int VIRVUS_TYPE2 = 2;
	/**
	 * 支付风险类型
	 */
	public static final int VIRVUS_TYPE3 = 3;
	/**
	 * 盗号风险类型
	 */
	public static final int VIRVUS_TYPE4 = 4;
	private HashMap<String, ArrayList<AppInfo>> m_szVirvusList = new HashMap();

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
		case SCAN_ONE_VIRVUS:
			AppInfo pInfo = (AppInfo) (pMsg.obj);
			addItem(pInfo);
			m_pCallback.callback(nTag, pInfo.getM_nVirvusType());
			break;
		case SCAN_OVER:
			m_pCallback.callback(nTag);
			break;
		case SCAN_ITEM:
			m_pCallback.callback(nTag, pMsg.obj);
			break;
		}
	}

	private void addItem(AppInfo pInfo) {
		ArrayList<AppInfo> list = m_szVirvusList.get(TAG
				+ pInfo.getM_nVirvusType());
		if (list == null) {
			m_szVirvusList.put(TAG + pInfo.getM_nVirvusType(), list);
		}
		list.add(pInfo);
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		Message msg = Message.obtain();
		msg.what = INIT_VIRVUS_ENGINE;
		sendMessage(msg);
		List<AppInfo> pList = m_pAppInfoEngine.getVirvusCheckApp();
		// 还有一个需要扫描文件夹系统.暂时不做
		scanVirvusAppInfo(pList);
	}

	private void scanVirvusAppInfo(List<AppInfo> pList) {
		if (pList == null) {
			Message msg = Message.obtain();
			msg.what = VIRVUS_SHIT_HAPPEN;
			msg.arg1 = 0;
			sendMessage(msg);
			// return;
		}
		boolean bFlag = false;
		Message msg = null;
		if (pList != null)
			for (AppInfo pInfo : pList) {
				msg = Message.obtain();
				msg.what = SCAN_ITEM;
				msg.obj = pInfo.getAppName();
				sendMessage(msg);
				int nType = -1;
				nType = m_pDao.queryApp(pInfo);
				if (nType != -1) {
					// 存在，这个是病毒
					msg = Message.obtain();
					msg.what = SCAN_ONE_VIRVUS;
					pInfo.setM_nVirvusType(nType);
					msg.obj = pInfo;
					sendMessage(msg);
				} else {
					// 不存在。这个不是病毒
					continue;
				}
			}
		msg = Message.obtain();
		msg.what = SCAN_OVER;
		sendMessage(msg);
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
	 * 扫描病毒开始
	 */
	public void startScanVirvus() {
		doAsyWork();
	}
	public int getVirvusTypeSize(int i) {
		ArrayList pList = m_szVirvusList.get(TAG + i);
		if (pList != null)
			return pList.size();
		return 0;
	}

}
