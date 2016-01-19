package org.com.lix_.ui;

import org.com.lix_.enable.EnableOfVirusAdmin;
import org.com.lix_.util.UiUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class SceneOfVirusAdmin extends BaseActivity {

	@Override
	protected void onCallback(Object... obj) {
		super.onCallback(obj);
		int nTag = Integer.parseInt(obj[0].toString());
		switch (nTag) {
		case EnableOfVirusAdmin.INIT_VIRVUS_ENGINE:
			UiUtils.setText(findViewById(R.id.virvus_des_pro), getResources()
					.getString(R.string.virvus_init_engine));
			break;
		case EnableOfVirusAdmin.VIRVUS_SHIT_HAPPEN:
			UiUtils.setText(findViewById(R.id.virvus_des_pro), getResources()
					.getString(R.string.virvus_engine_shithapped)+":"+obj[1]);
			break;
		case EnableOfVirusAdmin.SCAN_ITEM:
			UiUtils.setText(findViewById(R.id.virvus_des_pro), obj[1].toString());
			break ;
		case EnableOfVirusAdmin.SCAN_ONE_VIRVUS:
			int nType = Integer.parseInt(obj[1].toString()) ;
			break ;
		case EnableOfVirusAdmin.SCAN_OVER:
			//…®√ËÕÍ±œ
			break ;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent pIntent = new Intent();
		int nId = v.getId();
		switch (nId) {
		case R.id.ui_tag1:
			pIntent.setClass(this, SceneOfSettingVirvus.class);
			startActivity(pIntent);
			break;
		}
	}

	protected String TAG = "SceneOfVirusAdmin";

	private EnableOfVirusAdmin m_pEnable;

	@Override
	public void init() {
		moveQuick();
		initBtn();
		m_pEnable.startScanVirvus();
	}

	private void initBtn() {
		m_pEnable = new EnableOfVirusAdmin(this, m_pCallback);
		findViewById(R.id.ui_tag1).setClickable(true);
		findViewById(R.id.ui_tag1).setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.virvusadmin);
		init();
	}

}
