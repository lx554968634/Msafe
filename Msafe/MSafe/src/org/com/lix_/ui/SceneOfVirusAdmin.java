package org.com.lix_.ui;

import org.com.lix_.enable.EnableOfVirusAdmin;
import org.com.lix_.plugin.RadarCircleView;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

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
					.getString(R.string.virvus_engine_shithapped)
					+ ":"
					+ obj[1]);
			break;
		case EnableOfVirusAdmin.SCAN_ITEM:
			UiUtils.setText(findViewById(R.id.virvus_des_pro),
					obj[1].toString());
			break;
		case EnableOfVirusAdmin.SCAN_ONE_VIRVUS:
			int nType = Integer.parseInt(obj[1].toString());
			notifyVirvusHappens(nType);
			break;
		case EnableOfVirusAdmin.SCAN_OVER:
			// …®√ËÕÍ±œ
			showResult();
			break;
		}
	}

	private void showResult() {
		Debug.i(TAG, "…®√ËÕÍ±œ£°");
		findViewById(R.id.virvus_scan_result_txt).setVisibility(View.VISIBLE);
		UiUtils.setText(findViewById(R.id.virvus_des_pro), getResources()
				.getString(R.string.virvus_scan_over));
		String sTxt = getResources().getString(R.string.duihao);
		int nSize0 = m_pEnable.getVirvusTypeSize(1);
		if (nSize0 == 0)
			decorateSetRedTxt(findViewById(R.id.virvus_scan_type0), sTxt,
					R.color.checkbox_green_checked);
		nSize0 = m_pEnable.getVirvusTypeSize(2);
		if (nSize0 == 0)
			decorateSetRedTxt(findViewById(R.id.virvus_scan_type1), sTxt,
					R.color.checkbox_green_checked);
		nSize0 = m_pEnable.getVirvusTypeSize(3);
		if (nSize0 == 0)
			decorateSetRedTxt(findViewById(R.id.virvus_scan_type2), sTxt,
					R.color.checkbox_green_checked);
		nSize0 = m_pEnable.getVirvusTypeSize(4);
		if (nSize0 == 0)
			decorateSetRedTxt(findViewById(R.id.virvus_scan_type3), sTxt,
					R.color.checkbox_green_checked);
		m_pRadar.showResult();
	}

	private void decorateSetRedTxt(View pView, String sTxt, int nId) {
		if (pView instanceof TextView) {
			TextView pTxtView = (TextView) pView;
			((TextView) pView).setTextColor(getResources().getColor(nId));
			pTxtView.setText(sTxt);
		}
	}

	private void notifyVirvusHappens(int nType) {
		String sTanhao = getResources().getString(R.string.tanhao);
		switch (nType) {
		case EnableOfVirusAdmin.VIRVUS_TYPE1:
			decorateSetRedTxt(findViewById(R.id.virvus_scan_type0), sTanhao,
					R.color.red);
			break;
		case EnableOfVirusAdmin.VIRVUS_TYPE2:
			decorateSetRedTxt(findViewById(R.id.virvus_scan_type1), sTanhao,
					R.color.red);
			break;
		case EnableOfVirusAdmin.VIRVUS_TYPE3:
			decorateSetRedTxt(findViewById(R.id.virvus_scan_type2), sTanhao,
					R.color.red);
			break;
		case EnableOfVirusAdmin.VIRVUS_TYPE4:
			decorateSetRedTxt(findViewById(R.id.virvus_scan_type3), sTanhao,
					R.color.red);
			break;
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
		m_pRadar = (RadarCircleView) findViewById(R.id.virvus_radar);
		m_pEnable.startScanVirvus();
	}

	private RadarCircleView m_pRadar;

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
