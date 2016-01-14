package org.com.lix_.enable;

import java.util.List;

import org.com.lix_.db.dao.IDao;
import org.com.lix_.db.dao.TmpRecordWapDaoImpl;
import org.com.lix_.db.dao.TmpStatusDaoImpl;
import org.com.lix_.db.dao.WapRecordDaoImpl;
import org.com.lix_.db.dao.WapuselogDaoImpl;
import org.com.lix_.db.entity.TmpRecordWapEntity;
import org.com.lix_.db.entity.TmpStatusEntity;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.AppInfoEngine;
import org.com.lix_.util.Debug;

import android.Manifest;
import android.content.Context;
import android.net.TrafficStats;

public class EnableOfWapmonitor extends Enable {

	private String TAG = "EnableOfWapmonitor";

	public EnableOfWapmonitor(Context pContext) {
		super(pContext);
	}

	/**
	 * �Ա�Ȩ��
	 * 
	 * @param szPermission
	 * @param sTargetPermission
	 * @return
	 */
	private boolean checkPermission(String[] szPermission,
			String sTargetPermission) {
		if (szPermission == null)
			return false;
		boolean bFlag = false;
		for (String sPermiss : szPermission) {
			if (sPermiss.equals(sTargetPermission)) {
				bFlag = true;
				break;
			}
		}
		return bFlag;
	}

	/*
	 * ��Ⲣ���� wap_status_table
	 */
	private List<TmpStatusEntity> checkWapStatusTableRegural(
			TmpStatusDaoImpl pStatusDao) {
		List<TmpStatusEntity> pStatuResult = pStatusDao.queryAll();
		int nSize = pStatuResult == null ? -1 : pStatuResult.size();
		if (nSize != 2 && nSize > 0) {
			// ɾ�������¼Ĵ�����
			pStatusDao.refresh();
			return null;
		} else if (nSize == 0) {
			pStatusDao.insertData();
			TmpRecordWapDaoImpl pTmpRecordDao = new TmpRecordWapDaoImpl(
					m_pContext);
			pTmpRecordDao.deleteAll();
			IDao.finish();
			return null;
		} else
			return pStatuResult;
	}

	private List<TmpRecordWapEntity> checkTmpRecord(
			TmpRecordWapDaoImpl pTmpRecordDao, AppInfo pInfo) {
		TmpRecordWapEntity pTmp = new TmpRecordWapEntity(pInfo.getUid(),
				pInfo.getPackageName());
		List<TmpRecordWapEntity> pTmpRecordResult = pTmpRecordDao
				.queryByModel(pTmp);
		if (pTmpRecordResult == null || pTmpRecordResult.size() == 0) {
			IDao.finish();
			Debug.i(TAG,
					"��ʱ��û�м�¼ uid:" + pInfo.getUid() + ": pckName:"
							+ pInfo.getPackageName());
			return null;
		} else if (pTmpRecordResult.size() == 1) {
			if (pTmpRecordResult.get(0).getStatus() > 1) {
				Debug.i(TAG, "tmprecord ����ֻ��һ�������Ǹ�ʽ����:");
			}
		} else if (pTmpRecordResult.size() != 2) {
			// ��������Ч���ݣ�����������������������������wifiʹ����ʱ��¼��gprsʹ����ʱ��¼
			pTmpRecordDao.deleteModel(pTmp);
			Debug.i(TAG,
					"�����¼ɾ������uid:" + pInfo.getUid() + ": pckName:"
							+ pInfo.getPackageName());
			IDao.finish();
			return null;
		} else {
			boolean nFlag = pTmpRecordResult.get(0).getStatus() == 0
					&& pTmpRecordResult.get(1).getStatus() == 1;
			boolean nFlag2 = pTmpRecordResult.get(0).getStatus() == 1
					&& pTmpRecordResult.get(1).getStatus() == 0;
			if (!(nFlag || nFlag2)) {
				pTmpRecordDao.deleteModel(pTmp);
				Debug.i(TAG,
						"tmprecord�����pInfo.pckName = " + pInfo.getPackageName()
								+ " ��������");
				IDao.finish();
				return null;
			}
		}
		return pTmpRecordResult;
	}

	private long getWapdataByUid(AppInfo pInfo) {
		long rxData = TrafficStats.getUidRxBytes(pInfo.getUid());
		long tXData = TrafficStats.getUidTxBytes(pInfo.getUid());
		long max = rxData + tXData;
		return max;
	}

	private int checkWifiOrGprsWithTmpRecord(TmpRecordWapDaoImpl pTmpRecordDao,
			WapRecordDaoImpl pWapRecorddao, TmpStatusDaoImpl pStatusDao,
			TmpStatusEntity pTmpStatus,
			List<TmpRecordWapEntity> pTmpRecordResult, AppInfo pInfo,
			int nStatus) {
		WapuselogDaoImpl pWapuseLog = new WapuselogDaoImpl(m_pContext);
		TmpRecordWapEntity pTmpRecord = null;
		int nenableStatus = -1;
		int unablestatus = -1;
		switch (nStatus) {
		case 0:// wifi
			nenableStatus = TmpStatusEntity.WIFI_ENABLE;
			unablestatus = TmpStatusEntity.WIFI_UNABLE;
			break;
		case 1:// gprs
			nenableStatus = TmpStatusEntity.GPRS_ENABLE;
			unablestatus = TmpStatusEntity.GPRS_UNABLE;
			break;
		}
		if (pTmpStatus.getStatus() == nenableStatus) {
			// �ҵ�tmpRecord���м�¼������ͬ����WapRecord����
			if (pTmpRecordResult == null || pTmpRecordResult.size() == 0) {
			} else {
				Debug.i(TAG, "��ʱwifiEnalbe�����������м���д�������!");
				// ��Ҫ��������ӵ�wap_record_table , wapuselogentity ;
				long max = getWapdataByUid(pInfo);
				Debug.i(TAG,
						"pTmpRecordResult == null :"
								+ (pTmpRecordResult == null ? -1
										: pTmpRecordResult.size()));
				pTmpRecord = getWifiTmpRecord(pTmpRecordResult);
				if (pTmpRecord != null) {
					long nWapdata = pTmpRecord.getNwapdata() - max;
					Debug.i(TAG, "�������:" + nWapdata);
					if (nWapdata > 0) {
						// ��Ч����
						// ���¼�¼ waprecorddao ��log��¼
						long nTime = System.currentTimeMillis();
						pWapuseLog.insert(nTime, nWapdata, pTmpRecord);
						pTmpRecord.setNwapdata((int) nWapdata);
						pWapRecorddao.insertOrUpdateOtherModel(pTmpRecord);
					} else {
						// ��Ч���ݣ�������һ��ɨ��
					}
				} else {
					Debug.i(TAG, "����ʱ���в�����uid=" + pInfo.getUid()
							+ ": sPckName = " + pInfo.getPackageName()
							+ " wifi�����ļ�¼");
				}
			}
			Debug.i(TAG, "tmp����wifi������¼��ԭ");
			pTmpStatus.setStatus(unablestatus);
			boolean nFlag = pStatusDao.insertOrUpdate(pTmpStatus,
					TmpStatusEntity.getAllComlumn()[2]);
		} else if (pTmpStatus.getStatus() == unablestatus) {
		} else {
			pStatusDao.insertData();
			pTmpRecordDao.deleteAll();
			IDao.finish();
			return -1;
		}
		if (pTmpStatus.getStatus() == unablestatus) {
			if (pTmpRecord == null) {
				pTmpRecord = new TmpRecordWapEntity();
				pTmpRecord.setUid(pInfo.getUid());
				pTmpRecord.setsPckname(pInfo.getPackageName());
			}
			pTmpRecord.setStatus(nStatus);
			Debug.i(TAG, "wifi ��ʱ���¼Ҳ�ѹر� ,��������tmpRecord��¼:" + pInfo.getUid()
					+ ":" + pInfo.getPackageName());
			pTmpRecordDao.deleteModelByStatus(pTmpRecord);
		}
		return 0;
	}

	private TmpRecordWapEntity getWifiTmpRecord(
			List<TmpRecordWapEntity> pTmpRecordResult) {
		return getTargetTmpRecord(pTmpRecordResult, 0);
	}

	private TmpRecordWapEntity getTargetTmpRecord(
			List<TmpRecordWapEntity> pTmpRecordResult, int nValue) {
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

	@Override
	public void finish() {

	}

	@Override
	public void onViewClick(int nId) {
		// TODO Auto-generated method stub

	}

	public void updateStatusWap(TmpStatusEntity pEntity) {
		TmpStatusDaoImpl pDao = new TmpStatusDaoImpl(m_pContext);
		pDao.update(pEntity);
	}

	/*
	 * ����uid������ʱ���� 1��ɨ������
	 */
	public void updateTmpUidwapdata(int nStatus) {
		List<AppInfo> szList = getAppInfoListWithPermission();
		for (AppInfo pInfo : szList) {
			String[] szPermission = pInfo.getSzPermission();
			boolean bFlag = checkPermission(szPermission,
					Manifest.permission.INTERNET);
			if (!bFlag) {
				return;
			} else {
				TmpRecordWapDaoImpl pTmpRecordDao = new TmpRecordWapDaoImpl(
						m_pContext);
				TmpRecordWapEntity pModel = new TmpRecordWapEntity();
				pModel.setUid(pInfo.getUid());
				pModel.setsPckname(pInfo.getPackageName());
				pModel.setStatus(nStatus);
				long max = getWapdataByUid(pInfo);
				pModel.setNwapdata((int) max);
				pModel.setTimesmt(System.currentTimeMillis());
				pTmpRecordDao.insertModel(pModel);
			}
		}
		IDao.finish();
	}

	private List<AppInfo> getAppInfoListWithPermission() {
		AppInfoEngine pEngine1 = new AppInfoEngine(m_pContext);
		List<AppInfo> pList = pEngine1.getInstalledAppWithPermiss();
		return pList;
	}

	public void stoneInfoWhenWifiDisconnected() {
		stoneAllInfoWhenWapLinkDisconnected(0);
	}

	/**
	 * ���ĺ��������д���log��record�����tmprecord����λtmpStatus��Ĺ���
	 * @param pInfo
	 * @param nStatus
	 */
	private void saveAndCheckRecordWhenWapLinkDisconnected(AppInfo pInfo,
			int nStatus) {
		String[] szPermission = pInfo.getSzPermission();
		boolean bFlag = checkPermission(szPermission,
				Manifest.permission.INTERNET);
		List<TmpStatusEntity> pStatuResult = null;
		List<TmpRecordWapEntity> pTmpRecordResult = null;
		if (bFlag) {
			Debug.i(TAG,
					"ʹ�� netȨ��:" + pInfo.getPackageName() + ":" + pInfo.getUid());
			TmpStatusDaoImpl pStatusDao = new TmpStatusDaoImpl(m_pContext);
			TmpRecordWapDaoImpl pTmpRecordDao = new TmpRecordWapDaoImpl(
					m_pContext);
			pStatuResult = checkWapStatusTableRegural(pStatusDao);
			int nSize = pStatuResult == null ? -1 : pStatuResult.size();
			if (nSize != 2) {
				Debug.i(TAG, "pStatuResult ���ݲ����ϸ�ʽ : ֱ�ӹر�");
				return;
			} else {
				pTmpRecordResult = checkTmpRecord(pTmpRecordDao, pInfo);
				if (pTmpRecordResult == null) {
					Debug.i(TAG, "tmpRecord ���ݸ�ʽ����:ֱ�ӹر�");
					return;
				}
				TmpStatusEntity pStatusEntity = pStatuResult.get(nStatus);
				WapRecordDaoImpl pWapRecorddao = new WapRecordDaoImpl(
						m_pContext);
				// wifi gprs ͬʱenable��¼�ǲ������
				int nTag = checkWifiOrGprsWithTmpRecord(pTmpRecordDao,
						pWapRecorddao, pStatusDao, pStatusEntity,
						pTmpRecordResult, pInfo, nStatus);
			}
			IDao.finish();
		}
	}

	private void stoneAllInfoWhenWapLinkDisconnected(int nStatus) {
		List<AppInfo> szList = getAppInfoListWithPermission();
		for (AppInfo pInfo : szList) {
			String[] szPermission = pInfo.getSzPermission();
			boolean bFlag = checkPermission(szPermission,
					Manifest.permission.INTERNET);
			if (!bFlag) {
				return;
			} else {
				saveAndCheckRecordWhenWapLinkDisconnected(pInfo, nStatus);
			}
		}
	}

	public void stoneInfoWhenGprsDisconnected() {
		stoneAllInfoWhenWapLinkDisconnected(1);
	}

}
