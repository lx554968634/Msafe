package org.com.lix_.ui;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfRubbishClear;
import org.com.lix_.plugin.AListView;
import org.com.lix_.plugin.AutodrawCircleView;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
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

	private View m_pTotalView;

	private boolean m_nViewClickLock = false;

	@Override
	public void onClick(View v) {
		if (m_nViewClickLock)
			return;
		m_nViewClickLock = true;
		m_pEnable.onViewClick(v.getId());
	}

	@Override
	protected void onResume() {
		m_nViewClickLock = false;
		super.onResume();
	}

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
		if (VERSION.SDK_INT >= 19) {
			// 这样会使系统标题栏与应用黏在一起，需要重新定义位置
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		m_pTotalView = findViewById(R.id.total_runbishclear);
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
		// m_pGridView.setScrollenable(false);
		m_pGridView.setAdapter(new Adapter());
		m_pTargetCircle = (AutodrawCircleView) findViewById(R.id.circle_target);
		m_pEnable = new EnableOfRubbishClear(this);
		pCallback = new CallbackImpl();
		m_pEnable.setCallback(pCallback);
		findViewById(R.id.ui_tag1).setOnClickListener(this);
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				m_pEnable.start();
			}
		}.sendEmptyMessage(1);
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
				case EnableOfRubbishClear.START_HAS_CACHE:
					if (m_pEnable.getRubbishCache() != 0) {
						m_pTotalView.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.rubbish_linecolor0));
						reFreshUI();
					}
					break;
				case EnableOfRubbishClear.RAM_SHOW:
					pNum = new Integer(obj[1].toString()); // 内存增加
					addCacheViewValue(pNum);
					break;
				case EnableOfRubbishClear.NONE_SDCARD:
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = 12;
					break;
				case EnableOfRubbishClear.FINSH_SD_RUBBISH:
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = 12;
					Debug.i(TAG, "callback : FINSH_SD_RUBBISH ");
					break;
				case EnableOfRubbishClear.FINSH_INNER_PROP:
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = 28;
					if (m_pEnable.getRubbishCache() != 0) {
						m_pTotalView.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.rubbish_linecolor2));
						reFreshUI();
					}
					SceneOfRubbishClear.this.findViewById(R.id.rubbish_clear_button_divider).setVisibility(View.VISIBLE);
					SceneOfRubbishClear.this.findViewById(R.id.rubbish_clear_button).setVisibility(View.VISIBLE);
					Debug.i(TAG, "callback : FINSH_INNER_PROP ");
					finishScan();
					break;
				case EnableOfRubbishClear.PREPARE_FINISH:
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = 4;
					m_pTotalView.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.rubbish_linecolor0));
					reFreshUI();
					Debug.i(TAG, "callback : PREPARE_FINISH ");
					break;
				case EnableOfRubbishClear.START_SCAN_GETALLAPPINFO:
					if (m_pTipTextView != null) {
						m_pTipTextView.setText("获取所有app信息!");
					}
					if (m_pEnable.getRubbishCache() > 10) {
						if (m_pEnable.getRubbishCache() != 0) {
							m_pTotalView
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.rubbish_linecolor1));
						}
					}
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = pNum + 3;
					break;
				case EnableOfRubbishClear.START_SCAN_GETRUNNINGTASK:
					if (m_pTipTextView != null) {
						m_pTipTextView.setText("获取所有运行进程!");
					}
					if (m_pEnable.getRubbishCache() > 10) {
						if (m_pEnable.getRubbishCache() != 0) {
							m_pTotalView
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.rubbish_linecolor1));
							reFreshUI();
						}
					}
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = pNum + 3;
					break;
				case EnableOfRubbishClear.START_SCAN_GETRUNNINGRUBBISH:
					if (m_pTipTextView != null) {
						m_pTipTextView.setText("获取所有运行垃圾进程!");
					}
					if (m_pEnable.getRubbishCache() > 10) {
						if (m_pEnable.getRubbishCache() != 0) {
							m_pTotalView
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.rubbish_linecolor1));
							reFreshUI();
						}
					}
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = pNum + 3;
					break;
				case EnableOfRubbishClear.START_SCAN_RUNNINGSERVICE:
					if (m_pTipTextView != null) {
						m_pTipTextView.setText("获取所有运行服务!");
					}
					if (m_pEnable.getRubbishCache() > 10) {
						if (m_pEnable.getRubbishCache() != 0) {
							m_pTotalView
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.rubbish_linecolor1));
							reFreshUI();
						}
					}
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = pNum + 3;
					break;
				case EnableOfRubbishClear.START_SCAN_RUBBISHSERVICE:
					if (m_pTipTextView != null) {
						m_pTipTextView.setText("获取所有垃圾服务!");
					}
					if (m_pEnable.getRubbishCache() > 10) {
						if (m_pEnable.getRubbishCache() != 0) {
							m_pTotalView
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.rubbish_linecolor1));
							reFreshUI();
						}
					}
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = pNum + 3;
					break;
				case EnableOfRubbishClear.START_SCAN_CHECKRUBBISH:
					if (m_pEnable.getRubbishCache() > 10) {
						if (m_pEnable.getRubbishCache() != 0) {
							m_pTotalView
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.rubbish_linecolor1));
							reFreshUI();
						}
					}
					if (m_pTargetCircle != null)
						m_pTargetCircle.m_nTargetNum = pNum + 3;
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
						R.id.scan_rubbish_res);
				pTextView.setVisibility(View.VISIBLE);
				pTextView.setText(m_pEnable.getRubbishSize(i));
				;
			}
		}
		m_pGridView.setOnItemClickListener(m_pEnable.getOnGridItemListener());
	}

	public void reFreshUI() {

	}

	class Adapter extends BaseAdapter {

		public Adapter() {
			super();
			m_pInflater = LayoutInflater.from(SceneOfRubbishClear.this);
		}

		private LayoutInflater m_pInflater;

		@Override
		public int getCount() {
			// return 2;
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
			if (convertView == null) {
				convertView = m_pInflater.inflate(R.layout.grid_item_rubbish,
						null);
				initItem(convertView, position);
				if (position == COUNT_GRID_ITEMS - 1) {
					convertView.findViewById(R.id.grid_item_divi)
							.setVisibility(View.INVISIBLE);
					;
				}
			}
			return convertView;
		}

		private void initItem(View pView, int nPos) {
			int nDrawableId = -1;
			switch (nPos) {
			case 0:
				nDrawableId = R.drawable.rubbish_clear_t0;
				break;
			case 1:
				nDrawableId = R.drawable.rubbish_clear_t1;
				break;
			case 2:
				nDrawableId = R.drawable.rubbish_clear_t2;
				break;
			case 3:
				nDrawableId = R.drawable.rubbish_clear_t3;
				break;
			case 4:
				nDrawableId = R.drawable.rubbish_clear_t4;
				break;
			default:
				nDrawableId = R.drawable.ic_launcher;
				break;
			}
			if (pView.findViewById(R.id.scan_rubbish_title_img) != null)
				UiUtils.setImg(pView.findViewById(R.id.scan_rubbish_title_img),
						getResources().getDrawable(nDrawableId));
			if (pView.findViewById(R.id.scan_rubbish_des) != null)
				UiUtils.setText(pView.findViewById(R.id.scan_rubbish_des),
						m_szTitles[nPos]);
		}

	}

}
