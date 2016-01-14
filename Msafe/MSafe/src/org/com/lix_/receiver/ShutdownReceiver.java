package org.com.lix_.receiver;

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
import org.com.lix_.enable.EnableOfWapmonitor;
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
	EnableOfWapmonitor m_pEnable ;

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
				Debug.i(TAG, "找到指定service:" + Define.WAPSERVICE_PATH);
				bFlag = true;
			} else {
				Debug.i(TAG,
						"判断service:"
								+ runningService.get(i).service.getClassName());
			}
		}
		if (bFlag) {
			m_pEnable = new EnableOfWapmonitor(context) ;
			stoneWapInfo(context);
		}
	}

	private void stoneWapInfo(final Context pContext) {
		final InnerHandler pHandler = new InnerHandler();
		final AppInfoEngine pEngine1 = new AppInfoEngine(pContext);
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<AppInfo> pList = pEngine1.getInstalledAppWithPermiss();
				stoneInfo(pList);
				Message msg = Message.obtain();
				msg.what = SCAN_OVER;
				msg.obj = pContext;
				pHandler.sendMessage(msg);
			}
		}).start();
	}
	
	private void stoneInfo(List<AppInfo> pList)
	{
		if(pList == null || pList.size() == 0)
			return ;
		for(AppInfo pInfo : pList)
		{
			m_pEnable.saveAndCheckRecord(pInfo);
		}
	}

	private final int SCAN_OVER = 2;

	class InnerHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int nTag = msg.what;
			switch (nTag) {
			case SCAN_OVER:
				Debug.i(TAG, "扫描结束");
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
						"使用 net权限:" + pInfo.getPackageName() + ":"
								+ pInfo.getUid());
				TmpStatusDaoImpl pStatusDao = new TmpStatusDaoImpl(pContext);
				pStatuResult = pStatusDao.queryAll();
				int nSize = pStatuResult == null ? -1 : pStatuResult.size();
				if (nSize != 2 && nSize > 0) {
					// 删除表重新寄存数据
					pStatusDao.deleteAll();
					pStatusDao.insertData();
				}
				TmpRecordWapDaoImpl pTmpRecordDao = new TmpRecordWapDaoImpl(
						pContext);
				if (nSize == 0) {
					// 需要填充数据 ,tag表中没有数据所以不需要记录数据了，直接return
					Debug.i(TAG, "nSize == null : 直接关闭");
					pStatusDao.insertData();
					pTmpRecordDao.deleteAll();
					IDao.finish();
					// 读取
					return;
				} else {
					TmpRecordWapEntity pTmp = new TmpRecordWapEntity(
							pInfo.getUid(), pInfo.getPackageName());
					pTmpRecordResult = pTmpRecordDao.queryByModel(pTmp);
					if (pTmpRecordResult == null
							|| pTmpRecordResult.size() == 0) {
						IDao.finish();
						Debug.i(TAG, "临时表没有记录 uid:" + pInfo.getUid()
								+ ": pckName:" + pInfo.getPackageName());
						continue;
					} else if (pTmpRecordResult.size() > 2) {
						// 出现了无效数据，这里面数据最多就是三条，即关于wifi使用临时记录，gprs使用临时记录
						pTmpRecordDao.deleteModel(pTmp);
						Debug.i(TAG, "多余记录删除关于uid:" + pInfo.getUid()
								+ ": pckName:" + pInfo.getPackageName());
						IDao.finish();
						continue;
					}
					TmpStatusEntity pWifiStatus = pStatuResult.get(0);
					TmpStatusEntity pGprsStatus = pStatuResult.get(1);
					WapRecordDaoImpl pWapRecorddao = new WapRecordDaoImpl(
							pContext);
					WapuselogDaoImpl pWapuseLog = new WapuselogDaoImpl(pContext);
					// wifi gprs 同时enable记录是不共存的
					TmpRecordWapEntity pTmpRecord = null;
					if (pWifiStatus.getStatus() == TmpStatusEntity.WIFI_ENABLE) {
						// 找到tmpRecord表中记录，并且同步到WapRecord表中
						if (pTmpRecordResult == null
								|| pTmpRecordResult.size() == 0) {
						} else {
							Debug.i(TAG, "此时wifiEnalbe开启，并且中间表中存在数据!");
							// 需要把数据添加到wap_record_table , wapuselogentity ;
							long rxData = TrafficStats.getUidRxBytes(pInfo
									.getUid());
							long tXData = TrafficStats.getUidTxBytes(pInfo
									.getUid());
							long max = rxData + tXData;
							Debug.i(TAG, "pTmpRecordResult == null :"
									+ (pTmpRecordResult == null ? -1
											: pTmpRecordResult.size()));
							pTmpRecord = getWifiTmpRecord(pTmpRecordResult);
							if (pTmpRecord != null) {
								long nWapdata = pTmpRecord.getNwapdata() - max;
								Debug.i(TAG, "相差流量:" + nWapdata);
								if (nWapdata > 0) {
									// 有效数据
									// 更新记录 waprecorddao 和log记录
									long nTime = System.currentTimeMillis();
									pWapuseLog.insert(nTime, nWapdata,
											pTmpRecord);
									pTmpRecord.setNwapdata((int) nWapdata);
									pWapRecorddao
											.insertOrUpdateOtherModel(pTmpRecord);
								} else {
									// 无效数据，继续下一个扫描
								}
							} else {
								Debug.i(TAG,
										"在临时表中不存在uid=" + pInfo.getUid()
												+ ": sPckName = "
												+ pInfo.getPackageName()
												+ " wifi开启的记录");
							}
						}
						Debug.i(TAG, "tmp表中wifi开启记录还原");
						pWifiStatus.setStatus(TmpStatusEntity.WIFI_UNABLE);
						boolean nFlag = pStatusDao.insertOrUpdate(pWifiStatus,
								TmpStatusEntity.getAllComlumn()[2]);
					} else if (pWifiStatus.getStatus() == TmpStatusEntity.WIFI_UNABLE) {
					} else {
						pStatusDao.insertData();
						pTmpRecordDao.deleteAll();
						IDao.finish();
						// 读取
						return;
					}
					if (pWifiStatus.getStatus() == TmpStatusEntity.WIFI_UNABLE) {
						if (pTmpRecord == null) {
							pTmpRecord = new TmpRecordWapEntity();
							pTmpRecord.setUid(pInfo.getUid());
							pTmpRecord.setsPckname(pInfo.getPackageName());
						}
						pTmpRecord.setStatus(0);
						Debug.i(TAG, "wifi 临时表记录也已关闭 ,清除多余的tmpRecord记录:"
								+ pInfo.getUid() + ":" + pInfo.getPackageName());
						pTmpRecordDao.deleteModelByStatus(pTmpRecord);
					}
					if (pGprsStatus.getStatus() == TmpStatusEntity.GPRS_ENABLE) {

						// 找到tmpRecord表中记录，并且同步到WapRecord表中
						if (pTmpRecordResult == null
								|| pTmpRecordResult.size() == 0) {
						} else {
							Debug.i(TAG, "此时GPRS开启，并且中间表中存在数据!");
							long rxData = TrafficStats.getUidRxBytes(pInfo
									.getUid());
							long tXData = TrafficStats.getUidTxBytes(pInfo
									.getUid());
							long max = rxData + tXData;
							Debug.i(TAG, "pTmpRecordResult == null :"
									+ (pTmpRecordResult == null ? -1
											: pTmpRecordResult.size()));
							pTmpRecord = getGprsTmpRecrod(pTmpRecordResult);
							if (pTmpRecord != null) {
								long nWapdata = pTmpRecord.getNwapdata() - max;
								Debug.i(TAG, "相差流量:" + nWapdata);
								if (nWapdata > 0) {
									// 有效数据
									// 更新记录 waprecorddao 和log记录
									long nTime = System.currentTimeMillis();
									pWapuseLog.insert(nTime, nWapdata,
											pTmpRecord);
									pTmpRecord.setNwapdata((int) nWapdata);
									pWapRecorddao
											.insertOrUpdateOtherModel(pTmpRecord);
								} else {
									// 无效数据，继续下一个扫描
								}
							} else {
								Debug.i(TAG,
										"在临时表中不存在uid=" + pInfo.getUid()
												+ ": sPckName = "
												+ pInfo.getPackageName()
												+ " wifi开启的记录");
							}
						}
						Debug.i(TAG, "tmp表中GPRS开启记录还原");
						pGprsStatus.setStatus(TmpStatusEntity.GPRS_UNABLE);
						boolean nFlag = pStatusDao.insertOrUpdate(pGprsStatus,
								TmpStatusEntity.getAllComlumn()[2]);
					} else if (pGprsStatus.getStatus() == TmpStatusEntity.GPRS_UNABLE) {
					}else
					{
						pStatusDao.insertData();
						pTmpRecordDao.deleteAll();
						IDao.finish();
						// 读取
						return;
					}
					if (pGprsStatus.getStatus() == TmpStatusEntity.GPRS_UNABLE) {
						if (pTmpRecord == null) {
							pTmpRecord = new TmpRecordWapEntity();
							pTmpRecord.setUid(pInfo.getUid());
							pTmpRecord.setsPckname(pInfo.getPackageName());
						}
						pTmpRecord.setStatus(1);
						Debug.i(TAG, "gprs 临时记录表也已关闭 ,清除多余的tmpRecord记录:"
								+ pInfo.getUid() + ":" + pInfo.getPackageName());
						pTmpRecordDao.deleteModelByStatus(pTmpRecord);
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
