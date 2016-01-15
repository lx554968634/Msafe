package org.com.lix_.db.dao;

import java.util.List;

import org.com.lix_.db.entity.TmpStatusEntity;

import android.content.Context;

public class TmpStatusDaoImpl extends IDao {

	public TmpStatusDaoImpl(Context pContext) {
		super(pContext, TmpStatusEntity.class);
	}

	public List<TmpStatusEntity> queryAll() {
		List<TmpStatusEntity> pList = queryByCondition(
				TmpStatusEntity.getAllComlumn(), null, null, null);
		return pList;
	}

	public void refresh() {
		deleteAll();
		insertData();
	}

	public void insertWifiData() {
		TmpStatusEntity p1 = new TmpStatusEntity();
		p1.setStatus(TmpStatusEntity.WIFI_UNABLE);
		p1.setType(0);
		p1.setTimesmt(System.currentTimeMillis());
		insert(p1);
	}

	public void insertGprsData() {
		TmpStatusEntity p1 = null;
		p1 = new TmpStatusEntity();
		p1.setStatus(TmpStatusEntity.GPRS_UNABLE);
		p1.setTimesmt(System.currentTimeMillis());
		p1.setType(1);
		insert(p1);
	}

	public void insertData() {
		insertWifiData();
		insertGprsData();
	}

	public void update(TmpStatusEntity pEntity) {
		insertOrUpdate(pEntity, pEntity.getAllComlumn()[2]);
	}

	public void refreshStatus(int nStatus) {
		TmpStatusEntity pEntity = TmpStatusEntity.getStatusEntity(nStatus);
		pEntity.setStatus(nStatus == 0 ? TmpStatusEntity.WIFI_UNABLE
				: TmpStatusEntity.GPRS_UNABLE);
		insertOrUpdate(pEntity, pEntity.getAllComlumn()[2]);
	}

}
