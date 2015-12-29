package org.com.lix_.plugin;

import org.com.lix_.Define;
import org.com.lix_.ui.R;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Circle2LineView extends View {

	private int m_nHeight;

	private int m_nOffset;

	private int m_nRadio;

	private int m_nWidth;

	private Paint m_pInnerPaint;

	public Circle2LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private int m_nBorder;

	private void init() {
		m_nWidth = Define.WIDTH / 2;
		m_nHeight = Define.HEIGHT / 4 - 25;
		m_nRadio = m_nWidth > m_nHeight / 4 * 3 ? m_nHeight / 4 * 3 : m_nWidth;
		m_nOffset = (int) getResources().getDimension(R.dimen.padding_list);
		m_pInnerPaint = new Paint();
		m_pInnerPaint.setAntiAlias(true);
		m_pInnerPaint.setColor(getResources().getColor(R.color.white));
		m_pInnerPaint.setStrokeWidth(getResources().getDimension(
				R.dimen.circle2line_stroke));
		m_nBorder = (int) (getResources().getDimension(
				R.dimen.circle2line_stroke) * 3);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCavas(canvas);
	}

	public int m_nPercent = 0;
	
	private int m_nTarget = 0 ;

	private void drawCanvas(Canvas pCanvas,int nAlpha, int nPercent) {
		float nX1, nX2, nY1, nY2, nRadio;
		int nTag = 0;
		int nExt = 180;
		for (int i = TOTAL + nExt; i >= nExt; i--) {
			if (nTag == 6)
				nTag = 0;
			if (nTag == 0) {
				if (TOTAL + nExt - i < nPercent) {
					m_pInnerPaint.setAlpha(nAlpha);
				} else {
					return ;
				}
				nRadio = m_nRadio;
				nX1 = m_nWidth + UiUtils.sin(i) * nRadio;
				nY1 = m_nHeight + UiUtils.cos(i) * nRadio;
				nRadio -= m_nBorder;
				nX2 = m_nWidth + UiUtils.sin(i) * nRadio;
				nY2 = m_nHeight + UiUtils.cos(i) * nRadio;
				pCanvas.drawLine(nX1, nY1, nX2, nY2, m_pInnerPaint);
			}
			nTag++;
		}
	}

	final int TOTAL = 360;

	private void drawCavas(Canvas pCanvas) {
		if(m_nPercent != m_nTarget)
		{
			m_nTarget += m_nPercent /50 ;
		}
		if(m_nTarget >= m_nPercent)
			m_nTarget = m_nPercent ;
		drawCanvas(pCanvas,60 ,200000);
		drawCanvas(pCanvas,255 ,m_nTarget * 360 / 100);
		postInvalidateDelayed(20);
	}

}
