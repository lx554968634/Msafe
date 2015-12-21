package org.com.lix_.ui;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfApkAdmin;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.plugin.AListView;
import org.com.lix_.util.Debug;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SceneOfApkAdmin extends BaseActivity {

	private AListView m_pListView;
	private EnableOfApkAdmin m_pEnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apkadmin);
		init();
	}

	protected String TAG = "SceneOfApkAdmin";

	private int m_nTotalProgress = 0;

	private int m_nProgressIndex = 0;

	private ProgressBar m_pProssBar;

	@Override
	public void init() {
		m_pProssBar = (ProgressBar) findViewById(R.id.progress);
		m_pEnable = new EnableOfApkAdmin(this);
		m_pCallback = new ApkCallback();
		m_pEnable.init(m_pCallback);

	}

	private ApkCallback m_pCallback;

	class ApkCallback implements EnableCallback {

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
			case EnableOfApkAdmin.STARTSCAN:
				// 开启扫描
				try {
					m_nTotalProgress = Integer.parseInt(obj[1].toString());
					m_pProssBar.setMax(m_nTotalProgress);
				} catch (Exception e) {
					e.printStackTrace();
					Debug.e(TAG, "反馈开启扫描中obj1出错! ");
				}
				m_nProgressIndex = 0;
				m_pProssBar.setProgress(m_nProgressIndex);
				Debug.e(TAG, "开始扫描所有应用:" + m_nTotalProgress);
				break;
			case EnableOfApkAdmin.SCAN_ITEM:
				m_nProgressIndex++;
				m_pProssBar.setProgress(m_nProgressIndex);
				Debug.i(TAG, "扫描到:" + obj[1].toString());
				break;
			case EnableOfApkAdmin.SCAN_OVER:
				m_pProssBar.setProgress(m_nTotalProgress);
				((TextView) findViewById(R.id.fileadmin_filesizetotalnum))
						.setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.fileadmin_filesizetotalnum))
						.setText("扫描结束，共" + m_pEnable.getDataCount() + "个应用");
				Debug.i(TAG, "扫描结束");
				scanListOver();
				break;
			}
		}
	};

	class Adapter extends BaseAdapter {

		private LayoutInflater m_pInflater;

		public Adapter() {
			m_pInflater = LayoutInflater.from(SceneOfApkAdmin.this);
		}

		@Override
		public int getCount() {
			return m_pEnable.getDataCount();
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
				convertView = m_pInflater.inflate(R.layout.item_apkadmin, null);
			}
			AppInfo pAppInfo = m_pEnable.getDataInfo(position);
			TextView pName = (TextView) convertView
					.findViewById(R.id.item_name);
			pName.setText(pAppInfo.getAppName());
			((TextView) convertView.findViewById(R.id.item_desc))
					.setText(pAppInfo.isSystemApp() ? "系统应用" : "用户应用");
			try {
				((ImageView) convertView.findViewById(R.id.grid_item_image))
						.setImageDrawable(SceneOfApkAdmin.this
								.getPackageManager()
								.getApplicationIcon(
										SceneOfApkAdmin.this
												.getPackageManager()
												.getApplicationInfo(
														pAppInfo.getPackageName(),
														PackageManager.GET_META_DATA)));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				Debug.i(TAG, ":图片找不到" + pAppInfo.getPackageName());
			}
			return convertView;
		}

		class ViewHolder {

		}

	}

	protected void scanListOver() {
		findViewById(R.id.tip_0).setVisibility(View.INVISIBLE);
		findViewById(R.id.tip_pro).setVisibility(View.INVISIBLE);
		m_pListView = (AListView) findViewById(R.id.fileadmin_detail_list);

		m_pListView.setAutoScroll();
		m_pListView.setAdapter(new Adapter());
	}
}
