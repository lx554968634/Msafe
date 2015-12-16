package org.com.lix_.ui;

import org.com.lix_.Define;
import org.com.lix_.enable.EnableOfRubbishClear;
import org.com.lix_.plugin.AListView;
import org.com.lix_.util.Debug;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SceneOfShowRubbish extends BaseActivity {
	private AListView m_pGridView;
	private int m_nListCount;
	private int m_nIndex = -1;
	String[] m_szTitls = new String[] { "内存加速", "系统及应用缓存", "垃圾文件", "多余安装包",
			"应用卸载残余" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rubbish_show_itemlayout);
		init();
	}

	@Override
	public void init() {
		Intent pIntent = getIntent();
		String sNum = pIntent.getStringExtra(Define.INTENT_TAG0);
		if (sNum == null) {
			Debug.e(TAG, "出错了--获取m_nIndex 或pRubbish: ");
			onBackPressed();
			return;
		}
		try {
			m_nIndex = Integer.parseInt(sNum);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Debug.e(TAG, "出错了--NumberFormatException: " + sNum);
			onBackPressed();
			return;
		}
		((TextView) findViewById(R.id.show_title)).setText(m_szTitls[m_nIndex]);
		m_pGridView = (AListView) findViewById(R.id.grid_view);
		m_pGridView.setAutoScroll();
		m_pGridView.setAdapter(new Adapter());
	}

	class Adapter extends BaseAdapter {

		private LayoutInflater m_pInflater;

		public Adapter() {
			m_pInflater = LayoutInflater.from(SceneOfShowRubbish.this);
		}

		@Override
		public int getCount() {
			return m_nListCount;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = m_pInflater.inflate(
						R.layout.grid_item_rubbishactivity, parent, false);
				final CheckBox pBox = (CheckBox) convertView
						.findViewById(R.id.grid_rubbish_checkbox);
				pBox.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						int nEventCode = event.getAction();
						switch (nEventCode) {
						case MotionEvent.ACTION_DOWN:
							m_pGridView.m_bCanMove = false;
							if (m_pGridView.m_bCheckValue == 0) {
								m_pGridView.m_bCheckValue = pBox.isChecked() ? 1
										: -1;
							}
							if (m_pGridView.m_bCheckValue == 1)
								pBox.setChecked(false);
							else
								pBox.setChecked(true);
							break;
						}
						return false;
					}
				});
			} else {

			}
			return convertView;
		}

		class ViewHolder {

		}
	}
}
