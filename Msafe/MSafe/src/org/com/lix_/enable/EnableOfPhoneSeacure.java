package org.com.lix_.enable;

import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Message;

public class EnableOfPhoneSeacure extends Enable {

	private String TAG = "EnableOfPhoneSeacure";

	public static final int LIST_NONE = 1;

	public static final int START_SCAN = 2;

	public static final int START_ITEM = 3;

	public static final int SCAN_OVER = 4;

	private List<AppInfo> m_szList;

	public EnableOfPhoneSeacure(Context pContext, List<AppInfo> pList,
			EnableCallback pCallback) {
		super(pContext);
		m_szList = pList;
		setCallback(pCallback);
	}

	protected void doAsyWorkInTask(Object... szObj) {
		divideList();
	}

	@Override
	protected void doSynchrWork(Message pMsg) {
		super.doSynchrWork(pMsg);
		int nTag = pMsg.what;
		switch (nTag) {
		case LIST_NONE:
			Debug.i(TAG, "list is none ");
			break;
		case START_SCAN:
			Debug.i(TAG, "开始扫描下一个");
			break;
		case START_ITEM:
			Debug.i(TAG, "START_ITEM:" + pMsg.obj.toString());
			break;
		case SCAN_OVER:
			Debug.i(TAG, "扫描结束");
			break;
		}
		m_pCallback.callback(nTag);
	}

	private HashMap<String, ArrayList<AppInfo>> m_szTargetList = new HashMap<String, ArrayList<AppInfo>>();

	private void divideList() {
		Message msg = Message.obtain();
		Debug.i(TAG, "divider list ");
		if (m_szList == null || m_szList.size() == 0) {
			msg.what = LIST_NONE;
			m_pAsyHandler.sendMessage(msg);
		} else {
			Debug.i(TAG, "divider list start : " + m_szList.size());
			for (AppInfo pInfo : m_szList) {
				String[] szPermission = pInfo.getSzPermission();
				if (szPermission == null || szPermission.length == 0) {
					continue;
				} else {
					msg = Message.obtain();
					msg.what = START_SCAN;
					m_pAsyHandler.sendMessage(msg);
					msg = Message.obtain();
					msg.what = START_ITEM;
					msg.obj = pInfo.getAppName();
					m_pAsyHandler.sendMessage(msg);
					for (String sInfo : szPermission) {
						if (sInfo.equals(permission.CALL_PHONE)) // 电话
						{
							addItem(permission.CALL_PHONE, pInfo);
						}
						if (sInfo.equals(permission.RECEIVE_MMS))// 彩信
						{
							addItem(permission.RECEIVE_MMS, pInfo);
						}
						if (sInfo.equals(permission.RECEIVE_SMS)
								|| sInfo.equals(permission.SEND_SMS)) // 短信
						{
							addItem(permission.RECEIVE_SMS, pInfo);
						}
						if (sInfo.equals(permission.BLUETOOTH)
								|| sInfo.equals(permission.BLUETOOTH_ADMIN)) // 蓝牙
						{
							addItem(permission.BLUETOOTH, pInfo);
						}
						if (sInfo.equals(permission.ACCESS_NETWORK_STATE)) // wap
						{
							addItem(permission.ACCESS_NETWORK_STATE, pInfo);
						}
						if (sInfo.equals(permission.ACCESS_WIFI_STATE)
								|| sInfo.equals(permission.CHANGE_WIFI_STATE)
								|| sInfo.equals(permission.CHANGE_WIFI_MULTICAST_STATE)) // wifi
						{
							addItem(permission.ACCESS_WIFI_STATE, pInfo);
						}
					}
				}
			}
			msg = Message.obtain();
			msg.what = SCAN_OVER;
			sendMessage(msg);
		}
	}

	private void addItem(String sPermission, AppInfo pInfo) {
		if (m_szTargetList.get(sPermission) == null) {
			m_szTargetList.put(sPermission, new ArrayList<AppInfo>());
		}
		m_szTargetList.get(sPermission).add(pInfo);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onViewClick(int nId) {
		// TODO Auto-generated method stub

	}

	public void init() {
		doRestartWork();
	}

	public int getSize() {

		if (m_szTargetList == null) {
			Debug.i(TAG, "m_szTargetList : " + (m_szTargetList == null));
		} else if (m_szTargetList.size() == 0) {
			Debug.i(TAG, "m_szTargetSize : 0 ");
		} else {
			UiUtils.log(m_szTargetList);
		}

		return m_szTargetList.keySet().size();
	}
}
