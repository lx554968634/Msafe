package org.com.lix_.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOfWapadmin_net extends Fragment {

	private int m_nNetType = -1;

	public FragmentOfWapadmin_net(int i) {
		m_nNetType = i;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wapadmin_netdetail, null);
		decorate(v);
		return v;
	}

	private void decorate(View v) {
		startLoading(v);
	}

	private void startLoading(View pView) {
		pView.findViewById(R.id.wapadmin_netdetail_loading).setVisibility(
				View.VISIBLE);
	}

}
