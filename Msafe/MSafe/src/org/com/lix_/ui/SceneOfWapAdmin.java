package org.com.lix_.ui;

import org.com.lix_.plugin.SinkingView;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

public class SceneOfWapAdmin extends BaseActivity implements
		SensorEventListener {
	protected String TAG = "SceneOfWapAdmin";
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private TextView m_pTitleTextView;

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
		mSinkingView = (SinkingView) findViewById(R.id.wap_sinking);
		percent = 0.01f;
		m_pTitleTextView = (TextView) findViewById(R.id.wap_show_title);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_UI);
		mSinkingView.setPercent(percent);
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
	 * 0 Õý³£ 1 ¡·0 -1 ¡¶ 0
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
