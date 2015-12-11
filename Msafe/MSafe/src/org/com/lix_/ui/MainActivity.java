package org.com.lix_.ui;

import org.com.lix_.Define;
import org.com.lix_.enable.Enable;
import org.com.lix_.enable.EnableOfMainActivity;
import org.com.lix_.util.Debug;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * ģ������ ��ȫ����
 * 
 * @ClassName: MainActivity
 * @Description: �������
 * @author lix_
 * @created? true
 * @date 2015-12-3 ����9:59:20
 * 
 */
public class MainActivity extends BaseActivity implements AnimationListener {

	private String[] m_szGridTxt0 = new String[] { "���ڼ��", "��������", "���ļ����",
			"����Ӧ�ü��", "�ֻ���ȫ���", "��������", "����ʹ�ü��", "��������Ӧ�ü��" };
	private String[] m_szGridTxt1 = new String[] { "������", "�����������", "������ļ����",
			"����ɳ���Ӧ�ü��", "�ֻ���ȫ", "������������Ż�", "��⵽��������������", "�������������е�Ӧ��" };

	protected String TAG = "MainActivity";

	private int m_nBtnType;

	private GridView m_pGridView;

	private Enable m_pEnable;
	private View m_pBtns;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public void init() {
		m_pEnable = new EnableOfMainActivity(this);
		m_pBtns = findViewById(R.id.btns_mainactivity);
		btnsAddListener();
		moveQuick() ;
		WindowManager wm = this.getWindowManager();
		Define.WIDTH = wm.getDefaultDisplay().getWidth();
		Define.HEIGHT = wm.getDefaultDisplay().getHeight();

	}

	private void btnsAddListener() {
		addOnClickListener(R.id.title_btn_mainlayout);
		addOnClickListener(R.id.btn_apkadmin_mainacitivity);
		addOnClickListener(R.id.btn_extrafuction_mainacitivity);
		addOnClickListener(R.id.btn_fileadmin_mainacitivity);
		addOnClickListener(R.id.btn_poweradmin_mainacitivity);
		addOnClickListener(R.id.btn_rootadmin_mainacitivity);
		addOnClickListener(R.id.btn_rubbishclear_mainacitivity);
		addOnClickListener(R.id.btn_virusadmin_mainacitivity);
		addOnClickListener(R.id.btn_wapadmin_mainacitivity);
	}

	private LayoutAnimationController m_pLayAnControl;

	private void initCheckList() {
		if (m_pLayAnControl == null) {
			Animation pAnimLayout = AnimationUtils.loadAnimation(this,
					R.anim.slide_down_1);
			m_pLayAnControl = new LayoutAnimationController(
					pAnimLayout);
			m_pLayAnControl.setOrder(LayoutAnimationController.ORDER_NORMAL);
			m_pLayAnControl.setDelay(1);
			m_pGridView = (GridView) findViewById(R.id.list_mainactivity);
			initGridView();
		}
		m_pGridView.setVisibility(View.VISIBLE);
		m_pGridView.setLayoutAnimation(m_pLayAnControl);
	}

	private void initGridView() {
		m_pGridView.setAdapter(new Adapter());
	}

	private final int COUNT_GRID_ITEMS = 8;

	class Adapter extends BaseAdapter {

		public Adapter() {
			super();
			m_pInflater = LayoutInflater.from(MainActivity.this);
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
				if (position == 0) {
					convertView = m_pInflater.inflate(
							R.layout.grid_item_first_main, null);
					pHolder = new ViewHolder();
					pHolder.m_pTextView = (TextView) convertView
							.findViewById(R.id.itemImage);
					convertView.setTag(pHolder);
				} else {
					convertView = m_pInflater.inflate(
							R.layout.grid_item_other_main, null);
					pHolder = new ViewHolder();
					pHolder.m_pTextView = (TextView) convertView
							.findViewById(R.id.itemImage);
					pHolder.m_pImageView = (ImageView) convertView
							.findViewById(R.id.grid_item_image);
					convertView.setTag(pHolder);
				}
			} else {
				pHolder = (ViewHolder) convertView.getTag();
			}
			pHolder.m_pTextView.setText(m_szGridTxt0[position]);
			return convertView;
		}

		class ViewHolder {
			public TextView m_pTextView;
			public ImageView m_pImageView;
		}
	}

	@Override
	public void onClick(View v) {
		int nId = v.getId();
		m_nBtnType = nId;
		switch (nId) {
		case R.id.title_btn_mainlayout:
			onTitleBtnClick();
			// initCheckList();
			break;
		case R.id.btn_apkadmin_mainacitivity:
			break;
		case R.id.btn_extrafuction_mainacitivity:
			break;
		case R.id.btn_fileadmin_mainacitivity:
			break;
		case R.id.btn_poweradmin_mainacitivity:
			break;
		case R.id.btn_rootadmin_mainacitivity:
			break;
		case R.id.btn_rubbishclear_mainacitivity:
			break;
		case R.id.btn_virusadmin_mainacitivity:
			break;
		case R.id.btn_wapadmin_mainacitivity:
			break;
		default:
			break;
		}
		m_pEnable.onViewClick(m_nBtnType);
	}

	private void onTitleBtnClick() {
		if (m_pBtns == null) {
			m_nBtnType = -1;
			Debug.e(TAG, "m_pBtns == null û�취������!");
		} else {
			if (m_pBtns.getVisibility() == View.VISIBLE) {
				m_nBtnType = -2;
				hiddenBtns();
			} else {
				m_nBtnType = -3;
				showBtns();
			}
		}
	}

	private void showBtns() {
		m_pBtns.setVisibility(View.VISIBLE);
		m_pGridView.setVisibility(View.INVISIBLE);
		m_pBtns.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.slide_up));
	}

	private Animation m_pHiddenAnimation;

	/**
	 * �������й���ϸ�ڰ�ť
	 */
	private void hiddenBtns() {
		if (m_pHiddenAnimation == null) {
			m_pHiddenAnimation = AnimationUtils.loadAnimation(this,
					R.anim.slide_down);
			m_pHiddenAnimation.setAnimationListener(this);
		}
		m_pBtns.startAnimation(m_pHiddenAnimation);

	}

	/**
	 * TranslateAnimation �������ĸ����� float fromXDelta ������ʼ�ĵ��뵱ǰView X�����ϵĲ�ֵ float
	 * toXDelta ���������ĵ��뵱ǰView X�����ϵĲ�ֵ float fromYDelta ������ʼ�ĵ��뵱ǰView Y�����ϵĲ�ֵ
	 * float toYDelta ������ʼ�ĵ��뵱ǰView Y�����ϵĲ�ֵ (1)
	 * 
	 * 2015��12��5�� 20:55:55 ����������̫��
	 * 
	 * 2015��12��5�� 21:33:43 ����Ӳ�����ټ��� .�Ż�ֱ��ʹ�������ļ�
	 */
	// private void initHiddenAnim() {
	// m_pTransHiddenAnims = new TranslateAnimation(
	// Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
	// Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
	// m_pTransHiddenAnims.setDuration(TIME_HID_ANIMDURATION);
	// }

	// /*
	// * 2015��12��5�� 20:55:55 ����������̫��
	// */
	// private void initShowAnim() {
	// m_pTransShowAnims = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
	// 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
	// 1f, Animation.RELATIVE_TO_SELF, 0f);
	// m_pTransShowAnims.setDuration(TIME_HID_ANIMDURATION);
	// }

	@Override
	public void onAnimationEnd(Animation animation) {
		switch (m_pBtns.getVisibility()) {
		case View.VISIBLE:
			m_pBtns.setVisibility(View.INVISIBLE);
			initCheckList();
			break;
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

}
