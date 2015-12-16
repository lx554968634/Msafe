package org.com.lix_.ui;

import java.util.Map;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfRubbishClear;
import org.com.lix_.plugin.AListView;
import org.com.lix_.plugin.AutodrawCircleView;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SceneOfRubbishClear extends BaseActivity {

	private String[] STR_STEP = new String[] { "正在扫描", "扫描完毕" };

	private String[] STR_TIP = new String[] { "请稍候", "建议清理" };

	private int COUNT_GRID_ITEMS;

	private String[] m_szTitles;

	private AListView m_pGridView;

	private AutodrawCircleView m_pTargetCircle;

	private TextView m_pCacheTextView;

	private TextView m_pStepTextView;

	private TextView m_pTipTextView;

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
		m_pTipTextView = (TextView) findViewById(R.id.rubbish_text_1);
		m_pTipTextView.setText(STR_TIP[0]);
		m_pStepTextView = (TextView) findViewById(R.id.rubbish_text_2);
		m_pStepTextView.setText(STR_STEP[0]);
		m_pCacheTextView = (TextView) findViewById(R.id.rubbish_text_0);
		m_pCacheTextView.setText(UiUtils.getCacheSize(0));
		COUNT_GRID_ITEMS = m_szTitles.length;
		m_pGridView = (AListView) findViewById(R.id.list_rubbishactivity);
//		m_pGridView.setScrollenable(false);
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

	private void addCacheViewValue(long nValue) {
		m_pCacheTextView.setText(UiUtils.getCacheSize(nValue));
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
			try {
				Integer pNum = new Integer(obj[0].toString());
				switch (pNum) {
				case EnableOfRubbishClear.TXT_SHOW:
					String sTxt = obj[1].toString();
					if (m_pTipTextView != null) {
						m_pTipTextView.setText(UiUtils.getCurrentTxt(sTxt));
					}
					break;
				case EnableOfRubbishClear.RAM_SHOW:
					pNum = new Integer(obj[1].toString()); // 内存增加
					addCacheViewValue(pNum);
					break;
				case EnableOfRubbishClear.FINSH_SD_RUBBISH:
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = 12;
					Debug.i(TAG, "callback : FINSH_SD_RUBBISH ");
					break;
				case EnableOfRubbishClear.FINSH_INNER_PROP:
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = 28;
					Debug.i(TAG, "callback : FINSH_INNER_PROP ");
					finishScan();
					break;
				case EnableOfRubbishClear.PREPARE_FINISH:
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = 0;
					Debug.i(TAG, "callback : PREPARE_FINISH ");
					break;
				default:
					break;
				}
			} catch (Error e) {
				e.printStackTrace();
				Debug.logFile("错误incallback:" + e.getMessage(), false);
			}
		}

	}

	/*
	 * 扫描接受添加监听
	 */
	private void finishScan() {
		if (m_pTipTextView != null) {
			m_pTipTextView.setText("");
		}
		if (m_pStepTextView != null) {
			m_pStepTextView.setText("扫描结束");
		}
		TextView pTextView;
		for (int i = 0; i < m_pGridView.getChildCount(); i++) {
			if (m_pGridView.getChildAt(i) != null) {
				pTextView = (TextView) m_pGridView.getChildAt(i).findViewById(
						R.id.grid_item_size);
				pTextView.setText(m_pEnable.getRubbishSize(i));
				;
			}
		}
		m_pGridView.setOnItemClickListener(m_pEnable.getOnGridItemListener());
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
