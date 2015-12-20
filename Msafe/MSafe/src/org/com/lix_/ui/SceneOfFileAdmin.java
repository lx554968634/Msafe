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
import android.widget.TextView;

public class SceneOfFileAdmin extends BaseActivity {

	private AListView m_pListView;

	private EnableOfFileAdmin m_pEnable;

	private Callback m_pCallback;

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

		m_pEnable = new EnableOfFileAdmin(this);
		m_pCallback = new Callback();
		m_pEnable.init(m_pCallback);
	}

	class Callback implements EnableCallback {

		@Override
		public void callback(Object... obj) {
			Integer pInteger = null;
			HashMap<String, ArrayList<FileInfo>> szTmpData = null;
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
			case EnableOfFileAdmin.GET_LIST:
				break;
			case EnableOfFileAdmin.FINISH_SIMFILE_SCAN:
				if (obj[1] != null) {
					szTmpData = (HashMap<String, ArrayList<FileInfo>>) obj[1];
					m_pEnable.addData(m_szDatas, szTmpData);
				}
				initList();
				break;
			}
		}
	}

	private ArrayList<FileInfo> m_szDatas = new ArrayList<FileInfo>();

	private void initList() {
		findViewById(R.id.line_view).setVisibility(View.VISIBLE);
		findViewById(R.id.fileadmin_detail_list).setVisibility(View.VISIBLE);
		m_pListView = (AListView) findViewById(R.id.fileadmin_detail_list);
		m_pListView.setAutoScroll();
		m_pListView.setAdapter(new Adapter());
		Debug.i(TAG, "m_szDatas == null :" + m_szDatas.size());
		findViewById(R.id.fileadmin_btn_clear).setVisibility(View.VISIBLE);
	}

	class Adapter extends BaseAdapter {

		private LayoutInflater m_pInflater;

		public Adapter() {
			m_pInflater = LayoutInflater.from(SceneOfFileAdmin.this);
		}

		@Override
		public int getCount() {
			return m_szDatas.size();
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
			FileInfo pFile = m_szDatas.get(position);
			((TextView) convertView.findViewById(R.id.item_name))
					.setText(pFile.m_sFileName);
			((TextView) convertView.findViewById(R.id.item_desc))
					.setText(pFile.m_sType);
			return convertView;
		}

		class ViewHolder {

		}

	}

}
