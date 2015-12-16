package org.com.lix_.enable;

import java.io.Serializable;

import android.content.Context;

/**
 * �߼����� ������UI�仯���˼�ʹ���̻߳�����UI�仯
 * 
 * @author punsher
 *
 */
public abstract class Enable  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
