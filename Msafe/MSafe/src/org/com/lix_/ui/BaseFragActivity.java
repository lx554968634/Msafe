package org.com.lix_.ui;

import org.com.lix_.util.Debug;

import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

public abstract class BaseFragActivity extends FragmentActivity {
	protected String TAG = "BaseActivity";
	/**
	 * ����Ӳ������
	 */
	protected void moveQuick() {
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Debug.e(TAG, "λ��:" + event.getX() + "---" + event.getY());
		return super.onTouchEvent(event);
	}

	public abstract void init();
}
