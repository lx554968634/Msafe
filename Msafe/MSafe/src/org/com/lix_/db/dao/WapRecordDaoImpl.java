package org.com.lix_.db.dao;

import org.com.lix_.db.entity.TmpRecordWapEntity;
import org.com.lix_.db.entity.WapRecordEntity;

import android.content.Context;

public class WapRecordDaoImpl extends IDao {

	public WapRecordDaoImpl(Context pContext) {
		super(pContext, WapRecordEntity.class);
	}
	public void insertOrUpdateOtherModel(TmpRecordWapEntity pTmpRecord) {
		WapRecordEntity pObj = new WapRecordEntity();
		pObj.setNwapdata(pTmpRecord.getNwapdata());
		pObj.setsPckname(pTmpRecord.getsPckname());
		pObj.setStatus(pTmpRecord.getStatus());
		pObj.setUid(pTmpRecord.getUid());
		insertOrUpdate(pObj, pObj.getAllComlumn()[0], pObj.getAllComlumn()[1]);
	}
}
