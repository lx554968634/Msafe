package org.com.lix_.ui;

import org.com.lix_.Define;
import org.com.lix_.util.Debug;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity implements View.OnClickListener{
	
	protected String TAG = "BaseActivity" ;
	
	public abstract void init() ;
	protected void addOnClickListener(int nId)
	{
		View pView = findViewById(nId) ;
		if(pView == null)
		{
			Debug.e(TAG, "布局资源异常 不能找到ID:"+nId);
		}else
		{
			pView.setOnClickListener(this);
		}
	}
	/**
	 * 开启硬件加速
	 */
	protected void moveQuick()
	{
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
	
}
