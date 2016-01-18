package org.com.lix_.ui;

import org.com.lix_.db.entity.WapRecordEntity;
import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfWapadminDetail_gprs;
import org.com.lix_.plugin.AListView;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FragmentOfWapadmin_net extends Fragment {

	private int m_nNetType = -1;

	private String TAG = (m_nNetType == 0 ? "wifi:" : "gprs")
			+ "FragmentOfWapadmin_net";

	private Context m_pContext;

	private View m_pLayoutView;

	private LayoutInflater m_pInfter;
	/**
	 * 0 初始化 1初始第一次显示 2正常显示 3预备销毁（之后回归0）
	 */
	private int m_nVisiable = 0;

	private boolean m_bVisiable = false;

	// tab切换时回调，但是总体ui即首页与详细页切换不回调，后续查看
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		Debug.i(TAG, "setUserVisibleHint:" + isVisibleToUser);
		super.setUserVisibleHint(isVisibleToUser);
		m_bVisiable = isVisibleToUser;
		if (m_nVisiable == 1 && m_bVisiable) {
			initList();
			return;
		}
	}

	private void decorateTitle() {
		String sTxt = "";
		switch (m_nNetType) {
		case 0:
			sTxt = getTxt(R.string.wapadmin_title1);
			break;
		case 1:
			sTxt = getTxt(R.string.wapadmin_title0);
			break;
		}
		UiUtils.setText(findViewById(R.id.wapadmin_list_title), sTxt);
	}

	private BaseAdapter m_pAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = m_pInfter.inflate(R.layout.item_wapdetail, null);
			}
			decorateItem(convertView, position);
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return m_pEnable.getDataSize();
		}
	};

	private String getTxt(int wapadminTitle0) {
		return m_pContext.getResources().getString(wapadminTitle0);
	}

	protected void decorateItem(View convertView, int position) {
		WapRecordEntity pEntity = m_pEnable.getRecordList().get(position);
		UiUtils.setText(
				convertView.findViewById(R.id.wapadmin_detailnetitem_name),
				pEntity.getsPckname());
		try {
			((ImageView) convertView
					.findViewById(R.id.wapadmin_detailnet_item_image))
					.setImageDrawable(m_pContext
							.getPackageManager()
							.getApplicationIcon(
									m_pContext
											.getPackageManager()
											.getApplicationInfo(
													pEntity.getsPckname(),
													PackageManager.GET_META_DATA)));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Debug.i(TAG, ":图片找不到" + pEntity.getsPckname());
		}
		UiUtils.setText(
				convertView.findViewById(R.id.wapadmin_detailnetitem_desc),
				UiUtils.getCacheSize(pEntity.getNwapdata()));
	}

	private EnableOfWapadminDetail_gprs m_pEnable;

	public FragmentOfWapadmin_net(int nType, Context pContext) {
		m_nNetType = nType;
		TAG = (m_nNetType == 0 ? "wifi:" : "gprs") + "FragmentOfWapadmin_net";
		m_pContext = pContext;
		m_pEnable = new EnableOfWapadminDetail_gprs(nType, m_pContext);
		m_pInfter = LayoutInflater.from(pContext);
		m_pEnable.setCallback(m_pCallback);
	}

	private EnableCallback m_pCallback = new EnableCallback() {

		@Override
		public void callback(Object... obj) {
			int nTag = Integer.parseInt(obj[0].toString());
			switch (nTag) {
			case EnableOfWapadminDetail_gprs.GETLIST:
				if (m_nVisiable == 0) {
					m_bgetInfo = false;
					Debug.i(TAG, "数据返回，但是界面不是改界面");
					return;
				}
				m_nVisiable = 2;
				m_bgetInfo = false;
				Debug.i(TAG, "数据返回了");
				onViewVisiable(m_nVisiable);
				break;
			}
		}

	};

	private AListView m_pList = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Debug.i(TAG, "onCreateView");
		m_pLayoutView = inflater.inflate(R.layout.wapadmin_netdetail, null);
		decorateTitle();
		m_bgetInfo = false;
		setVisiable();
		return m_pLayoutView;
	}

	private View findViewById(int nId) {
		return m_pLayoutView.findViewById(nId);
	}

	private void onViewVisiable(int b) {
		switch (b) {
		case 1:
			UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_scan_none));
			UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_total));
			UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_divider));
			UiUtils.visiable(findViewById(R.id.wapadmin_netdetail_loading));
			UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_list));
			break;
		case 2:
			UiUtils.visiable(findViewById(R.id.wapadmin_netdetail_total));
			UiUtils.visiable(findViewById(R.id.wapadmin_netdetail_divider));
			UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_loading));
			if (m_pEnable.getDataSize() == 0) {
				UiUtils.visiable(findViewById(R.id.wapadmin_netdetail_scan_none));
				UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_list));
			} else {
				m_pAdapter.notifyDataSetChanged();
				UiUtils.visiable(findViewById(R.id.wapadmin_netdetail_list));
			}
			break;
		case 0:
			UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_list));
			UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_scan_none));
			UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_total));
			UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_divider));
			UiUtils.visiable(findViewById(R.id.wapadmin_netdetail_loading));
			break;
		case 3:
			break;
		}
	}

	public void setVisiable() {
		Debug.i(TAG, "setVisiable : 1");
		m_nVisiable = 1;
		initList();
	}

	@Override
	public void onResume() {
		super.onResume();
		Debug.i(TAG, "onResume:" + m_bVisiable + ":" + m_nVisiable);
		if (!m_bLoadOk) {
			m_bLoadOk = true;
		}
		if (m_bVisiable) {
			if (m_nVisiable == 1) {
				onViewVisiable(m_nVisiable);
				initList();
			}
		}
	}

	/**
	 * 初始化第一次标记
	 */
	private boolean m_bLoadOk = false;

	private void startLoaddata() {
		m_pEnable.startLoading(m_nNetType);
	}

	private boolean m_bgetInfo = false;

	private void initList() {
		if (!m_bLoadOk)
			return;
		if (m_pList != null) {
			if (m_nVisiable == 1 && m_bVisiable && !m_bgetInfo) {
				m_bgetInfo = true;
				startLoaddata();
			} else {
				if (m_bgetInfo) {
					Debug.i(TAG, "重复请求数据");
				}
			}
			return;
		}
		m_pList = (AListView) findViewById(R.id.wapadmin_netdetail_list);
		m_pList.setAdapter(m_pAdapter);
		if (m_nVisiable == 1 && m_bVisiable && !m_bgetInfo) {
			m_bgetInfo = true;
			startLoaddata();
		} else {
			if (m_bgetInfo) {
				Debug.i(TAG, "重复请求数据");
			}
		}
	}

	public void setDisVisiable() {
		Debug.i(TAG, "setDisVisiable");
		m_nVisiable = 3;
		m_pEnable.initData();
		m_pAdapter.notifyDataSetChanged();
		m_nVisiable = 0;
		onViewVisiable(0);
	}

}
