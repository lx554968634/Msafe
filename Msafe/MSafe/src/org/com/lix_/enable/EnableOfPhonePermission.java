package org.com.lix_.enable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.util.Debug;

import android.Manifest.permission;
import android.content.Context;
import android.os.Message;

public class EnableOfPhonePermission extends Enable {
	private String TAG = "EnableOfPhonePermission";

	public static final int LIST_NONE = 1;
	private List<AppInfo> m_szList;
	public static final int START_SCAN = 2;

	public static final int START_ITEM = 3;

	public static final int SCAN_OVER = 4;

	public EnableOfPhonePermission(Context pContext, List<AppInfo> list,
			EnableCallback pCallback) {
		super(pContext);
		m_szList = list;
		setCallback(pCallback);
	}

	public void init() {
		doRestartWork();
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
			Debug.i(TAG, "开始扫描");
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

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		divideList();
	}

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
						if (sInfo.equals(permission.CHANGE_CONFIGURATION)) // 修改系统设置
						{
							addItem(permission.CHANGE_CONFIGURATION, pInfo);
						}
						if (sInfo.equals(permission.READ_CONTACTS)) // 读取通话记录
						{
							addItem(permission.READ_CONTACTS, pInfo);
						}
						if (sInfo.equals(permission.RECEIVE_MMS))// 读取彩信
						{
							addItem(permission.RECEIVE_MMS, pInfo);
						}
						if (sInfo.equals(permission.READ_SMS)
								|| sInfo.equals(permission.SEND_SMS)) // 短信
						{
							addItem(permission.READ_SMS, pInfo);
						}
						if (sInfo.equals(permission.RECORD_AUDIO)) // 本地录音
						{
							addItem(permission.RECORD_AUDIO, pInfo);
						}
						if (sInfo.equals(permission.ACCESS_FINE_LOCATION)) // 定位位置
						{
							addItem(permission.ACCESS_FINE_LOCATION, pInfo);
						}
						if (sInfo.equals(permission.CAMERA)) // 摄像拍照相关
						{
							addItem(permission.CAMERA, pInfo);
						}
						if (sInfo.equals(permission.READ_PHONE_STATE))// 手机识别码
						{
							addItem(permission.READ_PHONE_STATE, pInfo);
						}
					}
				}
				msg = Message.obtain();
				msg.what = SCAN_OVER;
				sendMessage(msg);
			}
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

	}

	@Override
	public void onViewClick(int nId) {
	}

}
