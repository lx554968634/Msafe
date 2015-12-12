package org.com.lix_.enable;

import org.com.lix_.ui.R;
import org.com.lix_.ui.SceneOfApkAdmin;
import org.com.lix_.ui.SceneOfExtraFuction;
import org.com.lix_.ui.SceneOfFileAdmin;
import org.com.lix_.ui.SceneOfPowerAdmin;
import org.com.lix_.ui.SceneOfRootAdmin;
import org.com.lix_.ui.SceneOfRubbishClear;
import org.com.lix_.ui.SceneOfVirusAdmin;
import org.com.lix_.ui.SceneOfWapAdmin;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

public class EnableOfMainActivity extends Enable {

	public EnableOfMainActivity(Context pContext) {
		super(pContext);
	}

	@Override
	public void onViewClick(int nId) {

		Intent pIntent = new Intent();
		Class pJump2Class = null;

		switch (nId) {
		case R.id.title_btn_mainlayout:
			pIntent = null;
			break;
		case R.id.btn_apkadmin_mainacitivity:
			pJump2Class = SceneOfApkAdmin.class;
			break;
		case R.id.btn_extrafuction_mainacitivity:
			pJump2Class = SceneOfExtraFuction.class;
			break;
		case R.id.btn_fileadmin_mainacitivity:
			pJump2Class = SceneOfFileAdmin.class;
			break;
		case R.id.btn_poweradmin_mainacitivity:
			pJump2Class = SceneOfPowerAdmin.class;
			break;
		case R.id.btn_rootadmin_mainacitivity:
			pJump2Class = SceneOfRootAdmin.class;
			break;
		case R.id.btn_rubbishclear_mainacitivity:
			pJump2Class = SceneOfRubbishClear.class;
			break;
		case R.id.btn_virusadmin_mainacitivity:
			pJump2Class = SceneOfVirusAdmin.class;
			break;
		case R.id.btn_wapadmin_mainacitivity:
			pJump2Class = SceneOfWapAdmin.class;
			break;
		default:
			break;
		}
		if (pIntent != null && pJump2Class != null) {
			// 跳转到子页面
			pIntent.setClass(m_pContext, pJump2Class) ;
			m_pContext.startActivity(pIntent);
		} else {
			// 本页全屏扫描
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
