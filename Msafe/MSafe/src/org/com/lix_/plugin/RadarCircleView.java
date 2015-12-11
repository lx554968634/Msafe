package org.com.lix_.plugin;

import org.com.lix_.Define;
import org.com.lix_.ui.R;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class RadarCircleView extends View {

	private Paint m_pInnerCircle;

	private Paint m_pOutCircle;

	private Paint m_pRadarCircle;

	private Paint m_pOutterCircle;
	private int m_nWidth;

	private int m_nHeight;

	private int m_nRotate = 360;

	private Bitmap mScaledBitmap;
	private Bitmap mBitmap;

	private int m_nRadio;

	public RadarCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		
		m_pInnerCircle = UiUtils.decoratePaint(
				getResources().getColor(R.color.radar_inner_color), true, null,
				m_pInnerCircle, null);
		m_pOutCircle = UiUtils.decoratePaint(
				getResources().getColor(R.color.radar_outcircle_color), true,
				null, m_pOutCircle, null);
		m_nWidth = Define.WIDTH / 2;
		m_nHeight = Define.HEIGHT / 4 / 100 * 65;
		m_nRadio = m_nWidth > m_nHeight ? m_nHeight : m_nWidth;
		m_pRadarCircle = new Paint();
		m_pRadarCircle.setColor(getResources().getColor(
				R.color.radar_outcircle_color));

		m_pOutterCircle = new Paint();
		m_pOutterCircle.setColor(getResources().getColor(
				R.color.radar_inner_color));
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (mScaledBitmap == null) {
			mBitmap = BitmapFactory.decodeResource(getContext().getResources(),
					R.drawable.radar);
			if (mBitmap != null) {
				mScaledBitmap = Bitmap.createScaledBitmap(mBitmap,
						m_nRadio * 2, m_nRadio * 2, false);
				mBitmap.recycle();
			}
		}
		canvas.drawCircle(m_nWidth, m_nHeight, m_nRadio, m_pOutterCircle);
		canvas.drawCircle(m_nWidth, m_nHeight, m_nRadio / 2, m_pRadarCircle);
		canvas.save();
		canvas.rotate(m_nRotate -= 4, m_nWidth, m_nHeight);
		canvas.drawBitmap(mScaledBitmap, m_nWidth - m_nRadio, m_nHeight
				- m_nRadio, m_pRadarCircle);
		canvas.restore();
		if (m_nRotate == 0) {
			m_nRotate = 360;
		}
		postInvalidateDelayed(20);
	}

}
