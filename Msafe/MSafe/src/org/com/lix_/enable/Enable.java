package org.com.lix_.enable;

import android.content.Context;

/**
 * �߼����� ������UI�仯���˼�ʹ���̻߳�����UI�仯
 * 
 * @author punsher
 *
 */
public abstract class Enable {
	protected Context m_pContext;
	protected EnableCallback m_pCallback;

	public Enable(Context pContext) {
		m_pContext = pContext;
	}

	public void setCallback(EnableCallback pCallback) {
		m_pCallback = pCallback;
	}

	public abstract void finish();

	public abstract void onViewClick(int nId);
}
