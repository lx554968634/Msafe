package org.com.lix_.plugin;

import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DebugView extends View {
	private Paint m_pPaint;

	public DebugView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		m_pPaint = new Paint();
		m_pPaint.setStrokeWidth(getResources().getDimension(
				R.dimen.circle2line_stroke));
		m_pPaint.setColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawString(canvas);
	}

	private String[] m_szInfos;

	private void drawString(Canvas pCanvas) {
		m_szInfos = Debug.DEBUG_STR.split(Debug.SPLIT_STR);
		if (m_szInfos.length == 0)
			pCanvas.drawText(Debug.DEBUG_STR, 20, 20, m_pPaint);
		else {
			for (int i = 0; i < m_szInfos.length; i++) {
				pCanvas.drawText(m_szInfos[i], 20, 10 * i + 20, m_pPaint);
			}
		}
		postInvalidateDelayed(20);
	}

}
