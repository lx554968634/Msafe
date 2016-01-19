package org.com.lix_.plugin;

import org.com.lix_.Define;
import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RadarCircleView extends View {

	private String TAG = "RadarCircleView";

	private Paint m_pInnerCircle;

	private Paint m_pOutCircle;

	private Paint m_pRadarCircle;

	private int m_nWidth;

	private int m_nHeight;

	private int m_nRotate = 360;

	private Paint m_pWaterPaint;

	private int m_nRadio;

	private Paint m_pSmallPaint;

	public RadarCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		m_pSmallPaint = new Paint();
		m_pSmallPaint.setColor(getResources().getColor(R.color.white));
		m_pSmallPaint.setAntiAlias(true);
		m_pInnerCircle = new Paint();
		m_pInnerCircle.setColor(getResources().getColor(
				R.color.radar_inner_color));
		m_pInnerCircle.setAntiAlias(true);
		m_pInnerCircle.setAlpha(80);
		m_pOutCircle = new Paint();
		m_pOutCircle.setColor(getResources().getColor(
				R.color.radar_outcircle_color));
		m_pOutCircle.setAntiAlias(true);
		m_pOutCircle.setAlpha(40);
		m_nWidth = Define.WIDTH / 2;
		m_nHeight = Define.HEIGHT / 4 / 100 * 65;
		m_nRadio = m_nWidth > m_nHeight ? m_nHeight : m_nWidth;
		m_nRadio -= 1;
		m_nHeight += 30;
		m_pRadarCircle = new Paint();
		m_pRadarCircle.setColor(getResources().getColor(
				R.color.radar_outcircle_color));
		m_pRadarCircle.setAntiAlias(true);
	}

	private Bitmap mScaledBitmap;
	private Bitmap mBitmap;

	private void drawSector(Canvas pCanvas, float radius, float nStart,
			float nEnd) {
		RectF pRect = new RectF(m_nWidth - radius, m_nHeight - radius, m_nWidth
				+ radius, m_nHeight + radius);
		m_nRotate += 10;
		pCanvas.drawArc(pRect, nStart, nEnd, true, m_pWaterPaint);
	}

	private void drawMap(Canvas canvas) {
		if (mScaledBitmap == null) {
			mBitmap = BitmapFactory.decodeResource(getContext().getResources(),
					R.drawable.radar);
			if (mBitmap != null) {
				mScaledBitmap = Bitmap.createScaledBitmap(mBitmap,
						m_nRadio * 2, m_nRadio * 2, false);
//				 mBitmap.recycle();
			}
		}
		Debug.i(TAG, "draw map");
		canvas.drawBitmap(mScaledBitmap, m_nWidth - m_nRadio, m_nHeight
				- m_nRadio, m_pRadarCircle); 
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawCircle(m_nWidth, m_nHeight, m_nRadio, m_pOutCircle);
		canvas.drawCircle(m_nWidth, m_nHeight, m_nRadio / 2, m_pInnerCircle);
		canvas.drawCircle(m_nWidth, m_nHeight, m_nRadio / 10, m_pSmallPaint);
		canvas.save();
		canvas.rotate(m_nRotate -= 2, m_nWidth, m_nHeight);
		drawMap(canvas);
		canvas.restore();
		if (m_nRotate == 0) {
			m_nRotate = 360;
		}
		postInvalidateDelayed(0);
	}

}
