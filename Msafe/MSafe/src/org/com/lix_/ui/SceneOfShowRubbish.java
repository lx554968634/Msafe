package org.com.lix_.ui;

import org.com.lix_.plugin.AListView;
import org.com.lix_.util.Debug;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class SceneOfShowRubbish extends BaseActivity {
	private AListView m_pGridView;
	private int m_nListCount;
	private String[] m_szArrString = new String[] { "内存加速", "系统以应用清理", "垃圾文件",
			"多余安装包", "应用参与卸载", "内存加速", "系统以应用清理", "垃圾文件", "多余安装包", "应用参与卸载",
			"内存加速", "系统以应用清理", "垃圾文件", "多余安装包", "应用参与卸载", "内存加速", "系统以应用清理",
			"垃圾文件", "多余安装包", "应用参与卸载", "内存加速", "系统以应用清理", "垃圾文件", "多余安装包",
			"应用参与卸载" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showsth_layout);
		init();
	}

	@Override
	public void init() {
		m_pGridView = (AListView) findViewById(R.id.grid_view);
		m_nListCount = m_szArrString.length;
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
