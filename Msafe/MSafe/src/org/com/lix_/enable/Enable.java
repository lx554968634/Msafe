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

	public Enable(Context pContext) {
		m_pContext = pContext;
	}

	public abstract void onViewClick(int nId);
}
