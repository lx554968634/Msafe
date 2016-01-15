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
		pObj.setsPckname(pTmpRecord.getsPckname());
		pObj.setStatus(pTmpRecord.getStatus());
		pObj.setUid(pTmpRecord.getUid());
		
		pObj = queryByModel(pObj) ;
		if(pObj == null)
		{
			pObj = new WapRecordEntity() ;
			pObj.setsPckname(pTmpRecord.getsPckname());
			pObj.setStatus(pTmpRecord.getStatus());
			pObj.setUid(pTmpRecord.getUid());
			pObj.setNwapdata(pTmpRecord.getNwapdata());
		}else
		{
			pObj.setNwapdata(pObj.getNwapdata()+pTmpRecord.getNwapdata());
		}
		insertOrUpdate(pObj, pObj.getAllComlumn()[0], pObj.getAllComlumn()[1],
				pObj.getAllComlumn()[3] + "");
	}

	public WapRecordEntity queryByModel(WapRecordEntity pObj) {
		return (WapRecordEntity) queryUniqueRecord(
				pObj.getAllComlumn()[0] + " = ? AND " + pObj.getAllComlumn()[1]
						+ " = ? AND " + pObj.getAllComlumn()[3] + " = ? ",
				pObj.getUid() + "", "" + pObj.getsPckname(),
				"" + pObj.getStatus());
	}

}
