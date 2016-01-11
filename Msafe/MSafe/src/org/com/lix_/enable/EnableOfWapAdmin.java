package org.com.lix_.enable;

import org.com.lix_.ui.R;
import org.com.lix_.ui.SceneOfSettingWapAdmin;

import android.content.Context;
import android.content.Intent;

public class EnableOfWapAdmin extends Enable {

	public EnableOfWapAdmin(Context pContext, EnableCallback pCallback) {
		super(pContext);
		setCallback(pCallback);
	}

	@Override
	public void finish() {

	}

	@Override
	public void onViewClick(int nId) {
		Intent pIntent = new Intent();
		switch (nId) {
		case R.id.wap_show_title:
			pIntent.setClass(m_pContext, SceneOfSettingWapAdmin.class);
			m_pContext.startActivity(pIntent);
			break;
		}
	}
}
