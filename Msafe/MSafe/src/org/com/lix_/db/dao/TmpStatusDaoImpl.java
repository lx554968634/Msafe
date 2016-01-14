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

	public void insertData() {
		TmpStatusEntity p1 = new TmpStatusEntity();
		p1.setStatus(TmpStatusEntity.WIFI_UNABLE);
		p1.setType(0);
		p1.setTimesmt(System.currentTimeMillis());
		insert(p1);
		p1 = new TmpStatusEntity();
		p1.setStatus(TmpStatusEntity.GPRS_UNABLE);
		p1.setTimesmt(System.currentTimeMillis());
		p1.setType(1);
		insert(p1);
	}

}
