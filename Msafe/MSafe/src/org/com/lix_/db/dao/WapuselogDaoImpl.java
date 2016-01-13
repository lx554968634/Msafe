package org.com.lix_.db.dao;

import org.com.lix_.db.entity.TmpRecordWapEntity;
import org.com.lix_.db.entity.TmpStatusEntity;
import org.com.lix_.db.entity.WapuselogEntity;

import android.content.Context;

public class WapuselogDaoImpl extends IDao {

	public WapuselogDaoImpl(Context pContext) {
		super(pContext, WapuselogEntity.class);
	}

	public void insert(
			long nTime, long nWapdata, TmpRecordWapEntity pTmpRecord) {
		WapuselogEntity pObj = new WapuselogEntity() ;
		pObj.setStarttimesmt(pTmpRecord.getTimesmt());
		pObj.setEndtimesmt(nTime);
		pObj.setStatus(pTmpRecord.getStatus());
		pObj.setStartwapdata((long)pTmpRecord.getNwapdata());
		pObj.setEndwapdata(nTime);
		pObj.setUid(pTmpRecord.getUid());
		pObj.setsPckname(pTmpRecord.getsPckname());
		insert(pObj) ;
	}

}
