package org.com.lix_.ui;

import org.com.lix_.enable.EnableOfWapAdmin;
import org.com.lix_.plugin.SinkingView;
import org.com.lix_.util.Debug;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class SceneOfWapAdmin extends BaseActivity implements
		SensorEventListener {
	@Override
	protected void onResume() {
		super.onResume();
		m_nSettingClock = 0;
	}

	protected String TAG = "SceneOfWapAdmin";
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private TextView m_pTitleTextView;
	private EnableOfWapAdmin m_pEnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wapadmin);
		init();
	}

	private SinkingView mSinkingView;

	private float percent = 0;

	@Override
	public void init() {
		moveQuick();
		m_pEnable = new EnableOfWapAdmin(this, m_pCallback);
		mSinkingView = (SinkingView) findViewById(R.id.wap_sinking);
		percent = 0.01f;
		m_pTitleTextView = (TextView) findViewById(R.id.wap_show_title);
		m_pTitleTextView.setOnClickListener(this);
		Debug.i(TAG, "注册了监听");
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_UI);
		mSinkingView.setPercent(percent);
	}

	private int m_nSettingClock = 0;

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Debug.i(TAG, "点击----");
		int nId = v.getId();
		switch (nId) {
		case R.id.wap_show_title:
			Debug.i(TAG, "执行");
			if (m_nSettingClock == 1)
				return;
			m_nSettingClock = 1;
			m_pEnable.onViewClick(nId);
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			test();
			break;
		}
		return super.onTouchEvent(event);
	}

	private void test() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				percent = 0.25f;
				mSinkingView.setPercent(percent);
			}
		});
		thread.start();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/**
	 * 0 正常 1 》0 -1 《 0
	 */
	private int m_nRotateStatus = 0;

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor == null) {
			return;
		}
		if (mSinkingView != null)
			mSinkingView.onSensorChanged(event);

	}
}
