package org.com.lix_.db.dao;

import java.util.List;

import org.com.lix_.db.engine.DB_define;
import org.com.lix_.db.entity.VirvusEntity;
import org.com.lix_.enable.engine.AppInfo;

import android.content.Context;

public class VirusDaoImpl extends IDao {

	public VirusDaoImpl(Context pContext) {
		super(pContext, VirvusEntity.class, DB_define.DB_VIRVUS_NAME);
	}

	public int queryApp(AppInfo pInfo) {
		List pList = queryByCondition(VirvusEntity.getColumns()[0] + " = ? ",
				pInfo.getVirvusMd5());
		if (pList == null)
			return -1;
		if (pList.size() == 0)
			return -1;
		VirvusEntity pEntity = (VirvusEntity) (pList.get(0));
		return pEntity.getType();
	}

}
