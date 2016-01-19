package org.com.lix_.db.dao;

import org.com.lix_.db.engine.DB_define;
import org.com.lix_.db.entity.VirvusEntity;
import org.com.lix_.enable.engine.AppInfo;

import android.content.Context;

public class VirusDaoImpl extends IDao{

	public VirusDaoImpl(Context pContext) {
		super(pContext, VirvusEntity.class, DB_define.DB_VIRVUS_NAME);
	}

	public boolean queryApp(AppInfo pInfo) {
//		VirvusEntity
		return false;
	}
	
	

}
