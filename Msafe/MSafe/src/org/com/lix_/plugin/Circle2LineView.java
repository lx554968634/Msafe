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
		m_nHeight = Define.HEIGHT / 4 / 100 * 65;
		m_nRadio = m_nWidth > m_nHeight ? m_nHeight : m_nWidth;
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

	final int TOTAL = 360;

	private void drawCavas(Canvas pCanvas) {
		float nX1, nX2, nY1, nY2, nRadio;
		int nTag = 0;
		for (int i = 0; i < TOTAL; i++) {
			if (nTag == 6)
				nTag = 0;
			if (nTag == 0) {
				if(i > 230)
					m_pInnerPaint.setAlpha(60);
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

}
