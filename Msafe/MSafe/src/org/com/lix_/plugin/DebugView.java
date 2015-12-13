package org.com.lix_.plugin;

import java.util.ArrayList;

import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

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
		m_pPaint.setColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawString(canvas);
	}

	private ArrayList<String> plamformString(String sInfos) {
		String sTmp = sInfos + "";
		int nSize = (int) (UiUtils.px2dip(getContext(), getWidth()) / m_pPaint.getTextSize() ) * 3;
		ArrayList<String> szContents = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		int nCount = 0;
		for (int i = 0; i < sTmp.length(); i++) {
			if (nCount == nSize) {
				szContents.add(sb.toString());
				nCount = 0 ;
				sb = new StringBuffer();
			}
			sb.append(sTmp.charAt(i) + "");
			nCount++;
		}
		return szContents;
	}
	
	private String TAG = "DebugView" ;

	private void drawString(Canvas pCanvas) {

		ArrayList<String> m_szInfos = plamformString(Debug.DEBUG_STR);
		if (m_szInfos == null || m_szInfos.size() == 0)
			pCanvas.drawText(Debug.DEBUG_STR, 20, 20, m_pPaint);
		else {
			for (int i = 0; i < m_szInfos.size(); i++) {
				pCanvas.drawText(m_szInfos.get(i), 20, 10 * i + 20, m_pPaint);
			}
		}
		postInvalidateDelayed(1000);
	}

}
