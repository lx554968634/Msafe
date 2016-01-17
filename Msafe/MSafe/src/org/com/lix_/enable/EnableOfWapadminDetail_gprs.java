package org.com.lix_.enable;

import android.content.Context;

/**
 * 管理wapadmin ，详细流量信息逻辑
 * 
 * @author punsher
 *
 */
public class EnableOfWapadminDetail_gprs extends Enable {

	private int m_nStatus = -1;

	public EnableOfWapadminDetail_gprs(int nType, Context pContext) {
		super(pContext);
		m_nStatus = nType;
	}

	@Override
	public void finish() {

	}

	@Override
	public void onViewClick(int nId) {

	}

	public void startLoading(int m_nNetType) {
		
	}

}
