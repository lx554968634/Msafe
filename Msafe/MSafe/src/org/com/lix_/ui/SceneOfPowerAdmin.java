package org.com.lix_.ui;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfPowerAdmin;
import org.com.lix_.plugin.Circle2LineView;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class SceneOfPowerAdmin extends BaseActivity {

	private EnableOfPowerAdmin m_pEnable;

	private Circle2LineView m_pCircle;

	private TextView m_pTimeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.poweradmin);
		m_pCircle = (Circle2LineView) findViewById(R.id.circle);
		m_pTimeView = (TextView) findViewById(R.id.time);
		m_pEnable = new EnableOfPowerAdmin(this, m_pCallback);
		init();
	}

	private EnableCallback m_pCallback = new EnableCallback() {

		@Override
		public void callback(Object... obj) {
			int nTag = Integer.parseInt(obj[0].toString());
			switch (nTag) {
			case EnableOfPowerAdmin.GET_POWERSTATE:
				Debug.i(TAG, "获取电量时间:" + obj[1].toString());
				m_pTimeView.setText(UiUtils.getTime(
						getResources().getString(R.string.total_time),
						obj[1].toString()));
				m_pCircle.m_nPercent = Integer.parseInt(obj[1].toString());
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int nId = v.getId();
		Intent pIntent = new Intent();
		switch (nId) {
		case R.id.ui_tag1:
			pIntent.setClass(this, SceneOfSettingPoweradmin.class);
			startActivity(pIntent);
			break;
		}

	}

	@Override
	protected void onDestroy() {
		m_pEnable.finish();
		super.onDestroy();
	}

	@Override
	public void init() {
		findViewById(R.id.ui_tag1).setClickable(true);
		findViewById(R.id.ui_tag1).setOnClickListener(this);
	}
}
