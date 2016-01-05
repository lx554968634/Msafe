package org.com.lix_.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfFileAdmin;
import org.com.lix_.enable.engine.FileInfo;
import org.com.lix_.plugin.AListView;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SceneOfFileAdmin extends BaseActivity {

	private AListView m_pListView;

	private EnableOfFileAdmin m_pEnable;

	private Callback m_pCallback;

	private ProgressBar m_pScanProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fileadmin);
		init();
	}

	protected String TAG = "SceneOfFileAdmin";

	@Override
	public void init() {
		m_pScanProgress = (ProgressBar) findViewById(R.id.fileadmin_progress);
		m_pEnable = new EnableOfFileAdmin(this);
		m_pCallback = new Callback();
		findViewById(R.id.line_view).setVisibility(View.VISIBLE);
		findViewById(R.id.fileadmin_list).setVisibility(View.VISIBLE);
		m_pListView = (AListView) findViewById(R.id.fileadmin_list);
		m_pListView.setAutoScroll();
		m_pAdapter = new Adapter() ;
		m_pListView.setAdapter(m_pAdapter);
		m_pEnable.init(m_pCallback);
	}

	class Callback implements EnableCallback {

		@Override
		public void callback(Object... obj) {
			Integer pInteger = null;
			try {
				pInteger = Integer.parseInt(obj[0].toString());
			} catch (RuntimeException ee) {
				ee.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == pInteger) {
				Debug.e(TAG, "无法获得 callback tag !");
				return;
			}
			switch (pInteger.intValue()) {
			case EnableOfFileAdmin.FINISH_SIMFILE_SCAN:
				m_pAdapter.notifyDataSetChanged(); 
				break;
			case EnableOfFileAdmin.REFRESH_PROGRESS:
				setProgressBar(Integer.parseInt(obj[1].toString()));
				break;
			case EnableOfFileAdmin.NONE_SD:
				finishScan(EnableOfFileAdmin.NONE_SD);
				break;
			case EnableOfFileAdmin.FINISH_SCAN:
				finishScan(EnableOfFileAdmin.FINISH_SCAN);
				break;
			}
		}
	}

	private void setProgressBar(int nValue) {
		Debug.i(TAG, nValue + "更新进度:" + (m_pScanProgress == null));
		if (m_pScanProgress != null) {
			m_pScanProgress.setProgress(nValue);
		}
	}

	private void finishScan(int nStatus) {
		findViewById(R.id.filadmin_tip_pro).setVisibility(View.INVISIBLE);
		switch (nStatus) {
		case EnableOfFileAdmin.NONE_SD:
			UiUtils.setText(findViewById(R.id.fileadmin_tip_0), getResources()
					.getString(R.string.fileadmin_none_sdcard));
			break;
		case EnableOfFileAdmin.FINISH_SCAN:
			findViewById(R.id.total_rubbish_clickitems).setVisibility(View.VISIBLE);
			UiUtils.setText(findViewById(R.id.fileadmin_tip_0),
					m_pEnable.m_nTotalSize + "个大文件");
			findViewById(R.id.fileadmin_list_type).setVisibility(View.VISIBLE);
			findViewById(R.id.filadmin_tip_des).setVisibility(View.VISIBLE);
			UiUtils.setText(findViewById(R.id.filadmin_tip_des),
					"共" + UiUtils.getCacheSize(m_pEnable.m_nTotalCache));
			m_pAdapter.notifyDataSetChanged(); 
			break;
		}
	}
	
	private Adapter m_pAdapter ;

	private void initList() {
		findViewById(R.id.fileadmin_btn_clear).setVisibility(View.VISIBLE);
	}
//ebebeb    f5f5f5  fafafa
	class Adapter extends BaseAdapter {

		private LayoutInflater m_pInflater;

		public Adapter() {
			m_pInflater = LayoutInflater.from(SceneOfFileAdmin.this);
		}

		@Override
		public int getCount() {
			return m_pEnable.m_nTotalSize;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = m_pInflater
						.inflate(R.layout.item_fileadmin, null);
			}
			FileInfo pFile = m_pEnable.getData().get(position);
			((TextView) convertView.findViewById(R.id.fileadminitem_name))
					.setText(pFile.m_sFileName);
			((TextView) convertView.findViewById(R.id.fileadminitem_cache))
					.setText(pFile.getSize());
			((TextView) convertView.findViewById(R.id.fileadminitem_desc))
					.setText(pFile.m_sType);
			return convertView;
		}

		class ViewHolder {

		}

	}

}
