package org.com.lix_.ui;

import org.com.lix_.Define;
import org.com.lix_.enable.EnableCallback;
import org.com.lix_.util.Debug;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity implements
		View.OnClickListener {

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		backPress();
	}

	protected void backPress() {

	}

	protected String TAG = "BaseActivity";

	public abstract void init();

	protected void addOnClickListener(int nId) {
		View pView = findViewById(nId);
		if (pView == null) {
			Debug.e(TAG, "布局资源异常 不能找到ID:" + nId);
		} else {
			pView.setOnClickListener(this);
		}
	}

	/**
	 * 开启硬件加速
	 */
	protected void moveQuick() {
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	protected void callback(Object... obj) {

	}
	
	protected void onCallback(Object... obj)
	{
		
	}

	EnableCallback m_pCallback = new EnableCallback() {
		@Override
		public void callback(Object... obj) {
			onCallback(obj) ;
		}
	};
}
