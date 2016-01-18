package org.com.lix_.enable;

import java.util.List;

import org.com.lix_.db.dao.IDao;
import org.com.lix_.db.dao.TmpRecordWapDaoImpl;
import org.com.lix_.db.dao.TmpStatusDaoImpl;
import org.com.lix_.db.dao.WapRecordDaoImpl;
import org.com.lix_.db.dao.WapuselogDaoImpl;
import org.com.lix_.db.entity.TmpRecordWapEntity;
import org.com.lix_.db.entity.TmpStatusEntity;
import org.com.lix_.db.entity.WapRecordEntity;
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
		if (nSize == 1) {
			int nStatus = pStatuResult.get(0).getType();
			switch (nStatus) {
			case 0:
				pStatusDao.insertGprsData();
				pStatuResult.add(TmpStatusEntity.getGprsEntity());
				return pStatuResult;
			case 1:
				pStatusDao.insertWifiData();
				pStatuResult.add(TmpStatusEntity.getWifiEntity());
				return pStatuResult;
			default:
				pStatusDao.refresh();
				TmpRecordWapDaoImpl pTmpRecordDao = new TmpRecordWapDaoImpl(
						m_pContext);
				pTmpRecordDao.deleteAll();
				IDao.finish();
				return null;
			}
		} else if (nSize > 2 || nSize == 0) {
			// 删除表重新寄存数据
			pStatusDao.refresh();
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
					"临时表没有记录 uid:" + pInfo.getUid() + ": pckName:"
							+ pInfo.getPackageName());
			return null;
		} else if (pTmpRecordResult.size() == 1) {
			if (pTmpRecordResult.get(0).getStatus() > 1) {
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
		} else {
			boolean nFlag = pTmpRecordResult.get(0).getStatus() == 0
					&& pTmpRecordResult.get(1).getStatus() == 1;
			boolean nFlag2 = pTmpRecordResult.get(0).getStatus() == 1
					&& pTmpRecordResult.get(1).getStatus() == 0;
			if (!(nFlag || nFlag2)) {
				pTmpRecordDao.deleteModel(pTmp);
				Debug.i(TAG,
						"tmprecord表关于pInfo.pckName = " + pInfo.getPackageName()
								+ " 数据有误");
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
		return max < 0 ? 0 : max;
	}

	private int checkWifiOrGprsWithTmpRecord(TmpRecordWapDaoImpl pTmpRecordDao,
			TmpStatusDaoImpl pStatusDao, TmpStatusEntity pTmpStatus,
			List<TmpRecordWapEntity> pTmpRecordResult, AppInfo pInfo,
			int nStatus) {
		WapRecordDaoImpl pWapRecorddao = new WapRecordDaoImpl(m_pContext);
		Debug.e(TAG, "checkWifiOrGprsWithTmpRecord :" + pInfo.getAppName());
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
			// 找到tmpRecord表中记录，并且同步到WapRecord表中
			if (pTmpRecordResult == null || pTmpRecordResult.size() == 0) {
			} else {
				Debug.i(TAG, "此时" + (nStatus == 0 ? "wifi" : "gprs")
						+ "开启，并且中间表中存在数据!");
				// 需要把数据添加到wap_record_table , wapuselogentity ;
				long max = getWapdataByUid(pInfo);
				Debug.i(TAG,
						"pTmpRecordResult == null :"
								+ (pTmpRecordResult == null ? -1
										: pTmpRecordResult.size()));
				if (nStatus == 0)
					pTmpRecord = getWifiTmpRecord(pTmpRecordResult);
				else
					pTmpRecord = getGprsTmpRecrod(pTmpRecordResult);
				if (pTmpRecord != null) {
					long nWapdata = pTmpRecord.getNwapdata() - max;
					Debug.i(TAG, "相差流量:" + nWapdata);
					if (nWapdata >= 0) {
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
			// pTmpStatus.setStatus(unablestatus);
			// pStatusDao.insertOrUpdate(pTmpStatus,
			// TmpStatusEntity.getAllComlumn()[2]);
		} else if (pTmpStatus.getStatus() == unablestatus) {
		} else {
			pStatusDao.insertData();
			pTmpRecordDao.deleteAll();
			IDao.finish();
			return -1;
		}
		if (pTmpStatus.getStatus() == nenableStatus) {
			if (pTmpRecord == null) {
				pTmpRecord = new TmpRecordWapEntity();
				pTmpRecord.setUid(pInfo.getUid());
				pTmpRecord.setsPckname(pInfo.getPackageName());
			}
			pTmpRecord.setStatus(nStatus);
			Debug.i(TAG, "wifi 临时表记录也已关闭 ,清除多余的tmpRecord记录:" + nStatus + ":"
					+ pInfo.getUid() + ":" + pInfo.getPackageName());
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

	public void updateWifiTmpUidwapdata() {
		updateTmpUidwapdata(0);
	}

	public void updateGprsTmpUiswapdata() {
		updateTmpUidwapdata(1);
	}

	/*
	 * 根据uid插入临时数据 1、扫描数据
	 */
	public void updateTmpUidwapdata(int nStatus) {
		List<AppInfo> szList = getAppInfoListWithPermission();
		Debug.i(TAG, "获取所有应用的信息:" + szList.size());
		boolean bTag = false;
		for (AppInfo pInfo : szList) {
			String[] szPermission = pInfo.getSzPermission();
			boolean bFlag = checkPermission(szPermission,
					Manifest.permission.INTERNET);
			if (!bFlag) {
				continue;
			} else {
				bTag = true;
				Debug.i(TAG,
						"记录临时表信息:" + pInfo.getUid() + ":"
								+ pInfo.getPackageName());
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
		if (bTag) {
			TmpStatusEntity pEntity = TmpStatusEntity.getStatusEntity(nStatus);
			Debug.i(TAG, "改变tmpstatus数据");
			updateStatusWap(pEntity);
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
	 * 核心函数里面有储存log，record，清空tmprecord，复位tmpStatus表的功能
	 * 
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
					Debug.i(TAG, "tmpRecord 数据格式不符:直接关闭 对比wapRecord里面看是否具有数据，如果没有就加入一条初始化数据");
					WapRecordDaoImpl pDao = new WapRecordDaoImpl(m_pContext) ;
					TmpRecordWapEntity pTmp = new TmpRecordWapEntity() ;
					pTmp.setUid(pInfo.getUid());
					pTmp.setsPckname(pInfo.getPackageName());
					pTmp.setNwapdata(0);
					switch(nStatus)
					{
					case 0:
						pTmp.setStatus(TmpStatusEntity.WIFI_ENABLE);
						break ;
					case 1:
						pTmp.setStatus(TmpStatusEntity.GPRS_ENABLE);
						break ;
					}
					pDao.insertOrUpdateOtherModel(pTmp);
					return;
				}
				TmpStatusEntity pStatusEntity = getTargetWapStatus(
						pStatuResult, nStatus);
				// wifi gprs 同时enable记录是不共存的
				int nTag = checkWifiOrGprsWithTmpRecord(pTmpRecordDao,
						pStatusDao, pStatusEntity, pTmpRecordResult, pInfo,
						nStatus);
			}
			IDao.finish();
		}
	}

	private TmpStatusEntity getTargetWapStatus(List<TmpStatusEntity> szList,
			int nStatus) {
		TmpStatusEntity pmp = null;
		for (TmpStatusEntity pc : szList) {
			if (pc.getType() == nStatus) {
				return pc;
			}
		}
		return pmp;
	}

	private void stoneAllInfoWhenWapLinkDisconnected(int nStatus) {
		List<AppInfo> szList = getAppInfoListWithPermission();
		for (AppInfo pInfo : szList) {
			String[] szPermission = pInfo.getSzPermission();
			boolean bFlag = checkPermission(szPermission,
					Manifest.permission.INTERNET);
			if (!bFlag) {
				continue;
			} else {
				saveAndCheckRecordWhenWapLinkDisconnected(pInfo, nStatus);
			}
		}
	}

	public void stoneInfoWhenGprsDisconnected() {
		stoneAllInfoWhenWapLinkDisconnected(1);
	}

	public void refreshTmpStatus(int nStatue) {
		TmpStatusDaoImpl pStatusDao = new TmpStatusDaoImpl(m_pContext);
		pStatusDao.refreshStatus(nStatue);
	}

	public void stoneInfoWhenShutdown() {
		Debug.i(TAG, "stoneInfoWhenShutdown");
		stoneInfoWhenGprsDisconnected();
		stoneInfoWhenWifiDisconnected();
		Debug.i(TAG, "refreshTmpStatus:" + 0);
		refreshTmpStatus(0);
		Debug.i(TAG, "refreshTmpStatus:" + 1);
		refreshTmpStatus(1);
		IDao.finish();
	}
	
	public List<WapRecordEntity> getWapDetail(int m_nType)
	{
		WapRecordDaoImpl pDao = new WapRecordDaoImpl(m_pContext) ;
		return pDao.getWapdetailInfo(m_nType) ;
	}
	
}
