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
			Debug.e(TAG, "������Դ�쳣 �����ҵ�ID:"+nId);
		}else
		{
			pView.setOnClickListener(this);
		}
	}
	/**
	 * ����Ӳ������
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
