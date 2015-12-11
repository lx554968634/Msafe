package org.com.lix_.plugin;

import android.content.Context;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AListView extends ListView {

	public boolean m_bCanMove;

	/**
	 * 0 未初始化 1 checked -1 unchecked
	 */
	public int m_bCheckValue = 0;

	public AListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 开启边界自由滚动
	 */
	public void setAutoScroll() {
		AutoScrollHelper autoScrollHelper = new ListViewAutoScrollHelper(
				this);
		setOnTouchListener(autoScrollHelper);
		autoScrollHelper.setEnabled(true); // 这个不要忘了
	}

	public AListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AListView(Context context) {
		super(context);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int nEventCode = ev.getAction();
		switch (nEventCode) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			m_bCanMove = true;
			m_bCheckValue = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!m_bCanMove) {
				ev.setAction(MotionEvent.ACTION_DOWN);
				dispatchTouchEvent(ev);
				return false;
			}
			break;
		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

}
