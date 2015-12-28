package org.com.lix_.ui;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.util.Debug;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

public abstract class BaseFragActivity extends Fragment {

	protected String TAG = "BaseActivity";

	protected EnableCallback m_pCallback = new EnableCallback() {

		@Override
		public void callback(Object... obj) {
			doCallback(obj);
		}
	};

	public abstract void doCallback(Object... szObj);

}
