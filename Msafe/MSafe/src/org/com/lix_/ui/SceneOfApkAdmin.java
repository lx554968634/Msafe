package org.com.lix_.ui;

import org.com.lix_.plugin.AListView;
import org.com.lix_.ui.SceneOfShowRubbish.Adapter;
import org.com.lix_.util.Debug;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;

public class SceneOfApkAdmin extends BaseActivity {
	private AListView m_pListView;

	private String[] m_szList;

	private int m_nListCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apkadmin);
		init();
	}

	protected String TAG = "SceneOfApkAdmin";

	@Override
	public void init() {
		m_pListView = (AListView) findViewById(R.id.fileadmin_detail_list);

		m_szList = getResources().getStringArray(
				R.array.titles_rubbishclear_array);
		m_nListCount = m_szList.length;

		Debug.e(TAG, "m_szList.size:" + m_szList.length);

		m_pListView.setAutoScroll();
		m_pListView.setAdapter(new Adapter());
	}

	class Adapter extends BaseAdapter {

		private LayoutInflater m_pInflater;

		public Adapter() {
			m_pInflater = LayoutInflater.from(SceneOfApkAdmin.this);
		}

		@Override
		public int getCount() {
			return m_nListCount;
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
			return convertView;
		}

		class ViewHolder {

		}

	}
}
