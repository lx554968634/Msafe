package org.com.lix_.ui;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfWapadminDetail_gprs;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOfWapadmin_net extends Fragment {

	private String TAG = "FragmentOfWapadmin_net";

	private int m_nNetType = -1;

	private Context m_pContext;

	private View m_pLayoutView;

	private boolean m_pFirstLoad = true;

	//tab切换时回调，但是总体ui即首页与详细页切换不回调，后续查看
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		Debug.i(TAG, "setUserVisibleHint");
		super.setUserVisibleHint(isVisibleToUser);
		if(m_pFirstLoad)
			return ;
		if (isVisibleToUser) {// onResume
			onresume();
		} else {// onPause
			onpause();
		}
	}

	private void onpause() {
		UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_scan_none));
		UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_total));
		UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_divider));
		UiUtils.inVisiable(findViewById(R.id.wapadmin_netdetail_list));
	}

	private void onresume() {
		findViewById(R.id.wapadmin_netdetail_loading).setVisibility(
				View.VISIBLE);
		decorateTitle();
		m_pEnable.startLoading(m_nNetType);
	}

	private void decorateTitle() {
		String sTxt = "";
		switch (m_nNetType) {
		case 0:
			sTxt = getTxt(R.string.wapadmin_title0);
			break;
		case 1:
			sTxt = getTxt(R.string.wapadmin_title1);
			break;
		}
		UiUtils.setText(findViewById(R.id.wapadmin_list_title), sTxt);
	}

	private String getTxt(int wapadminTitle0) {
		return m_pContext.getResources().getString(wapadminTitle0);
	}

	private EnableOfWapadminDetail_gprs m_pEnable;

	public FragmentOfWapadmin_net(int nType, Context pContext) {
		m_nNetType = nType;
		m_pContext = pContext;
		m_pEnable = new EnableOfWapadminDetail_gprs(nType, m_pContext);
		m_pEnable.setCallback(m_pCallback);
	}

	private EnableCallback m_pCallback = new EnableCallback() {

		@Override
		public void callback(Object... obj) {

		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Debug.i(TAG, "onCreateView");
		m_pLayoutView = inflater.inflate(R.layout.wapadmin_netdetail, null);
		if(m_pFirstLoad)
		{
			m_pFirstLoad = false ;
			onresume();
		}
		return m_pLayoutView;
	}

	private void init() {
		switch (m_nNetType) {
		case 0: // wifi
			break;
		case 1:// gprs
			break;
		}
	}

	private View findViewById(int nId) {
		return m_pLayoutView.findViewById(nId);
	}

	private void startLoading() {
	}

}
