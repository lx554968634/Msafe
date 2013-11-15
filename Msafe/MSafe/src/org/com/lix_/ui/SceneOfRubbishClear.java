package org.com.lix_.ui;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfRubbishClear;
import org.com.lix_.plugin.AListView;
import org.com.lix_.plugin.AutodrawCircleView;
import org.com.lix_.util.Debug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SceneOfRubbishClear extends BaseActivity {

	private int COUNT_GRID_ITEMS;

	private String[] m_szTitles;

	private AListView m_pGridView;

	private AutodrawCircleView m_pTargetCircle;
	
	private TextView m_pCacheTextView ;

	private EnableOfRubbishClear m_pEnable;

	private EnableCallback pCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_runbishclear);
		init();
	}

	@Override
	public void init() {
		moveQuick();
		m_szTitles = getResources().getStringArray(
				R.array.titles_rubbishclear_array);
		COUNT_GRID_ITEMS = m_szTitles.length;
		m_pGridView = (AListView) findViewById(R.id.list_rubbishactivity);
		m_pGridView.setScrollenable(false);
		m_pGridView.setAdapter(new Adapter());
		m_pTargetCircle = (AutodrawCircleView) findViewById(R.id.circle_target);
		m_pEnable = new EnableOfRubbishClear(this);
		pCallback = new CallbackImpl();
		m_pEnable.setCallback(pCallback);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (m_pGridView != null && hasFocus) {
			m_pGridView.setVisibility(View.VISIBLE);
		}
	}

	class CallbackImpl implements EnableCallback {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.com.lix_.enable.EnableCallback#callback(java.lang.Object[])
		 * 第一个完成项目的tag。后面跟着数据信息
		 */
		@Override
		public void callback(Object... obj) {
			Integer pNum = new Integer(obj[0].toString());
			switch (pNum) {
			case EnableOfRubbishClear.RAM_SHOW:
				pNum = new Integer(obj[1].toString()); //内存增加
				break ;
			case EnableOfRubbishClear.FINSH_SD_RUBBISH:
				if(m_pTargetCircle != null)
					m_pTargetCircle.m_nTargetNum = 2 ;
				Debug.i(TAG, "callback : FINSH_SD_RUBBISH ");
				break;
			case EnableOfRubbishClear.FINSH_INNER_PROP:
				if(m_pTargetCircle != null)
					m_pTargetCircle.m_nTargetNum = 11 ;
				Debug.i(TAG, "callback : FINSH_INNER_PROP ");
				break ;
			case EnableOfRubbishClear.PREPARE_FINISH:
				if(m_pTargetCircle != null)
					m_pTargetCircle.m_nTargetNum = 2 ;
				Debug.i(TAG, "callback : PREPARE_FINISH ");
				break ;
			default:
				break;
			}
		}

	}

	class Adapter extends BaseAdapter {

		public Adapter() {
			super();
			m_pInflater = LayoutInflater.from(SceneOfRubbishClear.this);
		}

		private LayoutInflater m_pInflater;

		@Override
		public int getCount() {
			return COUNT_GRID_ITEMS;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder pHolder;
			if (convertView == null) {
				convertView = m_pInflater.inflate(
						R.layout.grid_item_other_main, null);
				pHolder = new ViewHolder();
				pHolder.m_pTextView = (TextView) convertView
						.findViewById(R.id.itemImage);
				pHolder.m_pImageView = (ImageView) convertView
						.findViewById(R.id.grid_item_image);
				convertView.setTag(pHolder);
			} else {
				pHolder = (ViewHolder) convertView.getTag();
			}
			if (position == COUNT_GRID_ITEMS - 1) {
				convertView.findViewById(R.id.grid_item_div).setVisibility(
						View.GONE);
			} else
				convertView.findViewById(R.id.grid_item_div).setVisibility(
						View.VISIBLE);
			pHolder.m_pTextView.setText(m_szTitles[position]);
			return convertView;
		}

		class ViewHolder {
			public TextView m_pTextView;
			public ImageView m_pImageView;
		}
	}

}
