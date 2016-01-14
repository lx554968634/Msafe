package org.com.lix_.db.dao;

import java.util.List;

import org.com.lix_.db.entity.TmpRecordWapEntity;
import org.com.lix_.db.entity.TmpStatusEntity;
import org.com.lix_.util.Debug;

import android.content.Context;

public class TmpRecordWapDaoImpl extends IDao {

	private String TAG = "TmpRecordWapDaoImpl";

	public TmpRecordWapDaoImpl(Context pContext) {
		super(pContext, TmpRecordWapEntity.class);
	}

	public List<TmpRecordWapEntity> queryList() {
		List<TmpRecordWapEntity> pList = queryByCondition(
				TmpRecordWapEntity.getAllComlumn(), null, null, null);
		return pList;
	}

	public List<TmpRecordWapEntity> queryByModel(
			TmpRecordWapEntity tmpRecordWapEntity) {
		Debug.i(TAG, "queryByModel : start");
		List<TmpRecordWapEntity> pList = queryByCondition(
				TmpRecordWapEntity.getAllComlumn(),
				TmpRecordWapEntity.getAllComlumn()[0] + " = ? AND "
						+ TmpRecordWapEntity.getAllComlumn()[1] + " = ?", null,
				null, null, tmpRecordWapEntity.getUid() + "",
				tmpRecordWapEntity.getsPckname());
		Debug.i(TAG, "queryByModel : end" + (pList == null ? 0 : pList.size()));
		return pList;
	}

	public void deleteModel(TmpRecordWapEntity tmpRecordWapEntity) {
		delete(TmpRecordWapEntity.getAllComlumn()[0] + " = ? AND "
				+ TmpRecordWapEntity.getAllComlumn()[1] + " = ?", tmpRecordWapEntity.getUid() + "",
				tmpRecordWapEntity.getsPckname());
	}

	public void deleteModelByStatus(TmpRecordWapEntity tmpRecordWapEntity) {
		delete(TmpRecordWapEntity.getAllComlumn()[0] + " = ? AND "
				+ TmpRecordWapEntity.getAllComlumn()[1] + " = ? AND "
				+ TmpRecordWapEntity.getAllComlumn()[4] + "=?",
				tmpRecordWapEntity.getUid() + "",
				tmpRecordWapEntity.getsPckname(),
				tmpRecordWapEntity.getStatus() + "");
	}

}
