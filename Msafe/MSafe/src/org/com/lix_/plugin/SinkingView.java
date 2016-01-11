package org.com.lix_.plugin;

import org.com.lix_.Define;
import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Ë®²¨ÀËÇòÐÎ½ø¶ÈView
 */
public class SinkingView extends View {

	protected String TAG = "SinkingView";

	private static final int DEFAULT_TEXTCOLOT = 0xFFFFFFFF;
	private static final int DEFAULT_TEXTSIZE = 250;
	private float mPercent = .9f;
	private Paint mPaint = new Paint();
	private Bitmap mBitmap;
	private Bitmap mScaledBitmap;
	private float mLeft;
	private int mSpeed = 5;
	private int mRepeatCount = 3;
	private Status mFlag = Status.RUNNING;
	private int mTextColor = DEFAULT_TEXTCOLOT;
	private int mTextSize = DEFAULT_TEXTSIZE;
	private int m_nWidth;

	private int m_nHeight;

	private int m_nOffset;

	private int m_nRadio;

	public SinkingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Debug.e(TAG, "construct in surfaceView");
		mPaint.setAntiAlias(true);
		m_nWidth = Define.WIDTH / 2;
		m_nHeight = Define.HEIGHT / 4 / 100 * 65;
		m_nRadio = m_nWidth > m_nHeight ? m_nHeight : m_nWidth;
		m_nOffset = (int) getResources().getDimension(R.dimen.padding_list);

	}

	public void setTextColor(int color) {
		mTextColor = color;
	}

	public void setTextSize(int size) {
		mTextSize = size;
	}

	public void setPercent(float percent) {
		mFlag = Status.RUNNING;
		mPercent = percent;
		postInvalidate();
	}

	public void setStatus(Status status) {
		mFlag = status;
	}

	public void clear() {
		mFlag = Status.NONE;
		if (mScaledBitmap != null) {
			mScaledBitmap.recycle();
			mScaledBitmap = null;
		}
		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}

	private void drawView1(Canvas pCanvas) {
		// ²Ã¼ô³ÉÔ²ÇøÓò
		Path path = new Path();
		pCanvas.save();
		path.reset();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(getResources().getColor(R.color.wap_circle_background));
		pCanvas.drawCircle((float) m_nWidth, (float) m_nHeight + m_nOffset,
				(float) (m_nRadio + m_nOffset), mPaint);
		pCanvas.clipPath(path);
		path.addCircle(m_nWidth, m_nHeight + m_nOffset, m_nRadio, Direction.CCW);
		pCanvas.clipPath(path, Op.REPLACE);
		if (mFlag == Status.RUNNING) {
			if (mScaledBitmap == null) {
				mBitmap = BitmapFactory.decodeResource(getContext()
						.getResources(), R.drawable.wave2);
				if (mBitmap != null) {
					mScaledBitmap = Bitmap.createScaledBitmap(mBitmap,
							mBitmap.getWidth(), m_nRadio * 2, false);
					mBitmap.recycle();
				}
				mBitmap = null;
				if (mScaledBitmap != null)
					mRepeatCount = (int) Math.ceil(m_nWidth
							/ mScaledBitmap.getWidth() + 0.5) + 1;
			}
			pCanvas.save();
			if (m_nAnimRotate == 0) {
				if (m_nRotate == 0) {
				} else if (m_nRotate > 0) {
					m_nRotate -= m_nRotateExt;
				} else {
					m_nRotate += m_nRotateExt;
				}
			} else if (m_nAnimRotate > 0) {
				if (m_nRotate < 30)
					m_nRotate += m_nRotateExt;
			} else {
				if (m_nRotate > -30) {
					m_nRotate -= m_nRotateExt;
				}
			}
			pCanvas.rotate(m_nRotate, (float) m_nWidth, (float) m_nHeight
					+ m_nOffset);
			for (int idx = 0; idx < mRepeatCount; idx++) {
				if (mScaledBitmap != null) {
					pCanvas.drawBitmap(mScaledBitmap, mLeft + (idx - 1)
							* mScaledBitmap.getWidth() + 30, (1 - mPercent)
							* (m_nHeight + m_nRadio), null);
				}
			}
			pCanvas.restore();
			mPaint.setColor(mTextColor);
			mPaint.setTextSize(mTextSize);
			mPaint.setStyle(Style.FILL);
			mLeft += mSpeed;
			if (mScaledBitmap != null && mLeft >= mScaledBitmap.getWidth())
				mLeft = 0;
		}
		pCanvas.restore();

		postInvalidateDelayed(20);
	}

	public enum Status {
		RUNNING, NONE
	}

	private float m_nRotate = 0;

	private float m_nRotateExt = 0.6f;
	/**
	 * ¼àÌý ×´Ì¬
	 */
	private int m_nAnimRotate = 0;

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x = (int) event.values[0];
			if (x > 4) {
				m_nAnimRotate = 1;
			} else if (x < -4) {
				m_nAnimRotate = -1;
			} else {
				m_nAnimRotate = 0;
			}
		}
	}

	@Override
	public void onDraw(Canvas pCanvas) {
		drawView1(pCanvas);
	}

}