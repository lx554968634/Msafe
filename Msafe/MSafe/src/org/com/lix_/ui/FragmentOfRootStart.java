package org.com.lix_.ui;

import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
<<<<<<< HEAD
=======
import org.com.lix_.ui.enable.EnableOfRootStart;
>>>>>>> a9147bdfa2b7bea17a5183d6be2f57b87e86549b

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOfRootStart extends Fragment {

	private List<AppInfo> m_szList;

<<<<<<< HEAD
	private org.com.lix_.enable.EnableOfRootStart m_pEnable;
=======
	private EnableOfRootStart m_pEnable;
>>>>>>> a9147bdfa2b7bea17a5183d6be2f57b87e86549b

	public FragmentOfRootStart(List<AppInfo> szList) {
		super();
		m_szList = szList;
		init();
	}

	private void init() {
<<<<<<< HEAD
		m_pEnable = new org.com.lix_.enable.EnableOfRootStart();
=======
		m_pEnable = new EnableOfRootStart();
>>>>>>> a9147bdfa2b7bea17a5183d6be2f57b87e86549b
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tabchild_rootstart, null);
		return v;
	}

}
