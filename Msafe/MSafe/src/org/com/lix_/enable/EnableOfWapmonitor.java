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
	 * 对比权限
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
	 * 检测并重置 wap_status_table
	 */
	private List<TmpStatusEntity> checkWapStatusTableRegural(
			TmpStatusDaoImpl pStatusDao) {
		List<TmpStatusEntity> pStatuResult = pStatusDao.queryAll();
		int nSize = pStatuResult == null ? -1 : pStatuResult.size();
		if (nSize != 2 && nSize > 0) {
			// 删除表重新寄存数据
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
		List<TmpRecordWapEntity> pTmpRecordResult = pTmpRecordDao.queryByModel(pTmp);
		if (pTmpRecordResult == null || pTmpRecordResult.size() == 0) {
			IDao.finish();
			Debug.i(TAG,
					"临时表没有记录 uid:" + pInfo.getUid() + ": pckName:"
							+ pInfo.getPackageName());
			return null;
		}else if(pTmpRecordResult.size() == 1){
			if(pTmpRecordResult.get(0).getStatus() > 1)
			{
				Debug.i(TAG, "tmprecord 数据只有一条，但是格式不对:");
			}
		} else if (pTmpRecordResult.size() != 2) {
			// 出现了无效数据，这里面数据最多就是三条，即关于wifi使用临时记录，gprs使用临时记录
			pTmpRecordDao.deleteModel(pTmp);
			Debug.i(TAG,
					"多余记录删除关于uid:" + pInfo.getUid() + ": pckName:"
							+ pInfo.getPackageName());
			IDao.finish();
			return null;
		}else
		{
			boolean nFlag = pTmpRecordResult.get(0).getStatus() == 0 && pTmpRecordResult.get(1).getStatus() == 1 ;
			boolean nFlag2 = pTmpRecordResult.get(0).getStatus() == 1 && pTmpRecordResult.get(1).getStatus() == 0 ;
			if(!(nFlag || nFlag2))
			{
				pTmpRecordDao.deleteModel(pTmp);
				Debug.i(TAG, "tmprecord表关于pInfo.pckName = "+pInfo.getPackageName() +" 数据有误");
				return null ;
			}
		}
		return pTmpRecordResult;
	}

	private int checkWifiOrGprsWithTmpRecord(TmpRecordWapDaoImpl pTmpRecordDao,
			WapRecordDaoImpl pWapRecorddao, TmpStatusDaoImpl pStatusDao,
			TmpStatusEntity pWifiStatus,
			List<TmpRecordWapEntity> pTmpRecordResult, AppInfo pInfo,
			int nenableStatus, int unablestatus) {
		WapuselogDaoImpl pWapuseLog = new WapuselogDaoImpl(m_pContext);
		TmpRecordWapEntity pTmpRecord = null;
		if (pWifiStatus.getStatus() == nenableStatus) {
			// 找到tmpRecord表中记录，并且同步到WapRecord表中
			if (pTmpRecordResult == null || pTmpRecordResult.size() == 0) {
			} else {
				Debug.i(TAG, "此时wifiEnalbe开启，并且中间表中存在数据!");
				// 需要把数据添加到wap_record_table , wapuselogentity ;
				long rxData = TrafficStats.getUidRxBytes(pInfo.getUid());
				long tXData = TrafficStats.getUidTxBytes(pInfo.getUid());
				long max = rxData + tXData;
				Debug.i(TAG,
						"pTmpRecordResult == null :"
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
						pWapuseLog.insert(nTime, nWapdata, pTmpRecord);
						pTmpRecord.setNwapdata((int) nWapdata);
						pWapRecorddao.insertOrUpdateOtherModel(pTmpRecord);
					} else {
						// 无效数据，继续下一个扫描
					}
				} else {
					Debug.i(TAG, "在临时表中不存在uid=" + pInfo.getUid()
							+ ": sPckName = " + pInfo.getPackageName()
							+ " wifi开启的记录");
				}
			}
			Debug.i(TAG, "tmp表中wifi开启记录还原");
			pWifiStatus.setStatus(unablestatus);
			boolean nFlag = pStatusDao.insertOrUpdate(pWifiStatus,
					TmpStatusEntity.getAllComlumn()[2]);
		} else if (pWifiStatus.getStatus() == unablestatus) {
		} else {
			pStatusDao.insertData();
			pTmpRecordDao.deleteAll();
			IDao.finish();
			return -1;
		}
		if (pWifiStatus.getStatus() == unablestatus) {
			if (pTmpRecord == null) {
				pTmpRecord = new TmpRecordWapEntity();
				pTmpRecord.setUid(pInfo.getUid());
				pTmpRecord.setsPckname(pInfo.getPackageName());
			}
			pTmpRecord.setStatus((unablestatus - 1) < 0 ? 0 : (unablestatus - 1));
			Debug.i(TAG, "wifi 临时表记录也已关闭 ,清除多余的tmpRecord记录:" + pInfo.getUid()
					+ ":" + pInfo.getPackageName());
			pTmpRecordDao.deleteModelByStatus(pTmpRecord);
		}
		return 0;
	}

	/**
	 * 核心方法
	 */
	public void saveAndCheckRecord(AppInfo pInfo) {
		String[] szPermission = pInfo.getSzPermission();
		boolean bFlag = checkPermission(szPermission,
				Manifest.permission.INTERNET);
		List<TmpStatusEntity> pStatuResult = null;
		List<TmpRecordWapEntity> pTmpRecordResult = null;
		if (bFlag) {
			Debug.i(TAG,
					"使用 net权限:" + pInfo.getPackageName() + ":" + pInfo.getUid());
			TmpStatusDaoImpl pStatusDao = new TmpStatusDaoImpl(m_pContext);
			TmpRecordWapDaoImpl pTmpRecordDao = new TmpRecordWapDaoImpl(
					m_pContext);
			pStatuResult = checkWapStatusTableRegural(pStatusDao);
			int nSize = pStatuResult == null ? -1 : pStatuResult.size();
			if (nSize != 2) {
				Debug.i(TAG, "pStatuResult 数据不符合格式 : 直接关闭");
				return;
			} else {
				pTmpRecordResult = checkTmpRecord(pTmpRecordDao, pInfo);
				if (pTmpRecordResult == null) {
					Debug.i(TAG, "tmpRecord 数据格式不符:直接关闭");
					return;
				}
				TmpStatusEntity pWifiStatus = pStatuResult.get(0);
				TmpStatusEntity pGprsStatus = pStatuResult.get(1);
				WapRecordDaoImpl pWapRecorddao = new WapRecordDaoImpl(
						m_pContext);
				// wifi gprs 同时enable记录是不共存的
				int nTag = checkWifiOrGprsWithTmpRecord(pTmpRecordDao,
						pWapRecorddao, pStatusDao, pWifiStatus,
						pTmpRecordResult, pInfo, TmpStatusEntity.WIFI_ENABLE,
						TmpStatusEntity.WIFI_UNABLE);
				if (nTag != 0)
					return;
				nTag = checkWifiOrGprsWithTmpRecord(pTmpRecordDao,
						pWapRecorddao, pStatusDao, pGprsStatus,
						pTmpRecordResult, pInfo, TmpStatusEntity.GPRS_ENABLE,
						TmpStatusEntity.GPRS_UNABLE);
				if (nTag != 0)
					return;
			}
			IDao.finish();
		} else {
		}
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onViewClick(int nId) {
		// TODO Auto-generated method stub

	}

}
