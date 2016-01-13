package org.com.lix_.enable.receiver;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.Define;
import org.com.lix_.db.dao.IDao;
import org.com.lix_.db.dao.TmpRecordWapDaoImpl;
import org.com.lix_.db.dao.TmpStatusDaoImpl;
import org.com.lix_.db.dao.WapRecordDaoImpl;
import org.com.lix_.db.dao.WapuselogDaoImpl;
import org.com.lix_.db.entity.TmpRecordWapEntity;
import org.com.lix_.db.entity.TmpStatusEntity;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.AppInfoEngine;
import org.com.lix_.service.BootService;
import org.com.lix_.util.Debug;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ShutdownReceiver extends BroadcastReceiver {

	private String TAG = "ShutdownReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		boolean bFlag = false;
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals(Define.WAPSERVICE_PATH)) {
				Debug.i(TAG, "�ҵ�ָ��service:" + Define.WAPSERVICE_PATH);
				bFlag = true;
			} else {
				Debug.i(TAG,
						"�ж�service:"
								+ runningService.get(i).service.getClassName());
			}
		}
		if (bFlag) {
			stoneWapInfo(context);
		}
	}

	private void stoneWapInfo(final Context pContext) {
		final InnerHandler pHandler = new InnerHandler();
		final AppInfoEngine pEngine = new AppInfoEngine(pContext);
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<AppInfo> pList = pEngine.getInstalledAppWithPermiss();
				stoneInfo(pList, pContext);
				Message msg = Message.obtain();
				msg.what = SCAN_OVER;
				msg.obj = pContext;
				pHandler.sendMessage(msg);
			}
		}).start();
	}

	private final int SCAN_OVER = 2;

	class InnerHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int nTag = msg.what;
			switch (nTag) {
			case SCAN_OVER:
				Debug.i(TAG, "ɨ�����");
				Intent pIntent = new Intent();
				pIntent.setClass(((Context) msg.obj), BootService.class);
				((Context) msg.obj).stopService(pIntent);
				return;
			}
		}
	}

	private void stoneInfo(Object pObj, Context pContext) {
		List<AppInfo> pList = (List<AppInfo>) pObj;
		for (AppInfo pInfo : pList) {
			String[] szPermission = pInfo.getSzPermission();
			if (szPermission == null)
				continue;
			boolean bFlag = false;
			for (String sPermiss : szPermission) {
				if (sPermiss.equals(Manifest.permission.INTERNET)) {
					bFlag = true;
					break;
				}
			}
			List<TmpStatusEntity> pStatuResult = null;
			List<TmpRecordWapEntity> pTmpRecordResult = null;
			if (bFlag) {
				Debug.i(TAG,
						"ʹ�� netȨ��:" + pInfo.getPackageName() + ":"
								+ pInfo.getUid());
				TmpStatusDaoImpl pStatusDao = new TmpStatusDaoImpl(pContext);
				pStatuResult = pStatusDao.queryAll();
				int nSize = pStatuResult == null ? -1 : pStatuResult.size();
				if (nSize != 2 && nSize > 0) {
					// ɾ�������¼Ĵ�����
					pStatusDao.deleteAll();
					pStatusDao.insertData();
				}
				if (nSize == 0) {
					// ��Ҫ������� ,tag����û���������Բ���Ҫ��¼�����ˣ�ֱ��return
					Debug.i(TAG, "nSize == null : ֱ�ӹر�");
					pStatusDao.insertData();
					IDao.finish();
					// ��ȡ
					return;
				} else {
					TmpRecordWapDaoImpl pTmpRecordDao = new TmpRecordWapDaoImpl(
							pContext);
					TmpRecordWapEntity pTmp = new TmpRecordWapEntity(
							pInfo.getUid(), pInfo.getPackageName());
					pTmpRecordResult = pTmpRecordDao.queryByModel(pTmp);
					if (pTmpRecordResult.size() > 2) {
						// ��������Ч���ݣ�����������������������������wifiʹ����ʱ��¼��gprsʹ����ʱ��¼
						pTmpRecordDao.deleteModel(pTmp);
						Debug.i(TAG, "�����¼ɾ������uid:" + pInfo.getUid()
								+ ": pckName:" + pInfo.getPackageName());
						IDao.finish();
						continue;
					}
					TmpStatusEntity pWifiStatus = pStatuResult.get(0);
					TmpStatusEntity pGprsStatus = pStatuResult.get(1);
					WapRecordDaoImpl pWapRecorddao = new WapRecordDaoImpl(
							pContext);
					WapuselogDaoImpl pWapuseLog = new WapuselogDaoImpl(pContext);
					// wifi gprs ͬʱenable��¼�ǲ������
					if (pWifiStatus.getStatus() == TmpStatusEntity.WIFI_ENABLE) {
						// �ҵ�tmpRecord���м�¼������ͬ����WapRecord����
						if (pTmpRecordResult == null
								|| pTmpRecordResult.size() == 0) {
						} else {
							// ��Ҫ��������ӵ�wap_record_table , wapuselogentity ;
							long rxData = TrafficStats.getUidRxBytes(pInfo
									.getUid());
							long tXData = TrafficStats.getUidTxBytes(pInfo
									.getUid());
							long max = rxData + tXData;
							TmpRecordWapEntity pTmpRecord = getWifiTmpRecord(pTmpRecordResult);
							if (pTmpRecord != null) {
								long nWapdata = pTmpRecord.getNwapdata() - max;
								if (nWapdata > 0) {
									// ��Ч����
									// ���¼�¼ waprecorddao ��log��¼
									long nTime = System.currentTimeMillis();
									pWapuseLog.insert(nTime, nWapdata,
											pTmpRecord);
									pTmpRecord.setNwapdata((int) nWapdata);
									pWapRecorddao
											.insertOrUpdateOtherModel(pTmpRecord);
								} else {
									// ��Ч���ݣ�������һ��ɨ��
								}
							}
						}
						pWifiStatus.setStatus(TmpStatusEntity.WIFI_UNABLE);
						boolean nFlag = pStatusDao.insertOrUpdate(pWifiStatus,
								TmpStatusEntity.getAllComlumn()[2]);
					} else if (pGprsStatus.getStatus() == TmpStatusEntity.GPRS_ENABLE) {

						// �ҵ�tmpRecord���м�¼������ͬ����WapRecord����
						if (pTmpRecordResult == null
								|| pTmpRecordResult.size() == 0) {
						} else {

						}
						pGprsStatus.setStatus(TmpStatusEntity.GPRS_UNABLE);
						boolean nFlag = pStatusDao.insertOrUpdate(pGprsStatus,
								TmpStatusEntity.getAllComlumn()[2]);
					} else {
						if (pWifiStatus.getStatus() == TmpStatusEntity.WIFI_UNABLE) {
							Debug.i(TAG, "wifi ��ʱ���¼Ҳ�ѹر�!");
						}
						if (pGprsStatus.getStatus() == TmpStatusEntity.GPRS_UNABLE) {
							Debug.i(TAG, "gprs ��ʱ��¼��Ҳ�ѹر�");
						}
					}
				}
				IDao.finish();
			} else {
			}
		}
	}

	private TmpRecordWapEntity getWifiTmpRecord(
			List<TmpRecordWapEntity> pTmpRecordResult) {
		return getTargetTmpRecord(pTmpRecordResult, 0);
	}

	private TmpRecordWapEntity getTargetTmpRecord(
			List<TmpRecordWapEntity> pTmpRecordResult, int nValue) {
		TmpRecordWapEntity pEntity = null;
		if (pTmpRecordResult == null || pTmpRecordResult.size() == 0)
			return null;
		for (TmpRecordWapEntity pTmp : pTmpRecordResult) {
			if (pTmp.getStatus() == nValue) {
				return pTmp;
			}
		}
		return null;
	}

	private TmpRecordWapEntity getGprsTmpRecrod(
			List<TmpRecordWapEntity> pTmpRecordResult) {
		return getTargetTmpRecord(pTmpRecordResult, 1);
	}
}
