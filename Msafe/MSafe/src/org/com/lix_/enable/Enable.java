package org.com.lix_.enable;

import android.content.Context;

/**
 * 逻辑处理 不处理UI变化，顾及使用线程回引起UI变化
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
