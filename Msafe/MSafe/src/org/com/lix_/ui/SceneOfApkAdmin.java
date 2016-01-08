package org.com.lix_.ui;

import javax.crypto.spec.PSource;

import org.com.lix_.Define;
import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfApkAdmin;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.FileInfo;
import org.com.lix_.plugin.AListView;
import org.com.lix_.ui.SceneOfFileAdmin.ViewHolder;
import org.com.lix_.ui.dialog.DialogOfApkTypes;
import org.com.lix_.ui.dialog.DialogOfFileItemClick;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.R.color;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SceneOfApkAdmin extends BaseActivity {

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(m_pApkTypeDialog != null)
		{
			m_pApkTypeDialog.dismiss(); 
			m_pApkTypeDialog = null ;
			System.gc(); 
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int nId = v.getId();
		switch (nId) {
		case R.id.apkadmin_total_rubbish_clickitems:
			dialogShowFileItem() ;
			break;
		}
	}
	
	private DialogOfApkTypes m_pApkTypeDialog;

	/*
	 * 加载图片不急弄
	 */
	private void dialogShowFileItem() {
		if (m_pApkTypeDialog != null)
			m_pApkTypeDialog.show();
		else {
			m_pApkTypeDialog = new DialogOfApkTypes(this,
					R.style.class_dialog, R.layout.dialog_apktype);
			Window pWin = m_pApkTypeDialog.getWindow();
			LayoutParams pParam = pWin.getAttributes();
			pParam.x = Define.WIDTH / 5 * 2;
			pParam.width = Define.WIDTH / 5 * 3;
			pParam.height = Define.HEIGHT / 7;
			pParam.y = (int) (m_nDialogHeight - getResources().getDimension(R.dimen.padding_list_helf));
			m_pApkTypeDialog.getWindow().setAttributes(pParam);
			m_pApkTypeDialog.show();
		}
	}

	private AListView m_pListView;
	private EnableOfApkAdmin m_pEnable;
	private Adapter m_pListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apkadmin);
		init();
	}

	protected String TAG = "SceneOfApkAdmin";

	private int m_nTotalProgress = 0;

	private int m_nProgressIndex = 0;

	private ProgressBar m_pProssBar;
	
	int m_nDialogHeight = -1 ;

	@Override
	public void init() {
		m_pProssBar = (ProgressBar) findViewById(R.id.apkadmin_progress);
		m_pEnable = new EnableOfApkAdmin(this);
		m_pCallback = new ApkCallback();
		findViewById(R.id.apkadmin_total_rubbish_clickitems)
				.setOnClickListener(this);
		m_pEnable.init(m_pCallback);
		m_pListView = (AListView) findViewById(R.id.apkadmin_detail_list);
		final View pView = findViewById(R.id.apkadmin_tag0) ;
		pView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				m_nDialogHeight = pView.getMeasuredHeight() ;
			}
		});
		m_pListView.setAutoScroll();
		m_pListView.setAdapter(m_pListAdapter = new Adapter());
		m_pListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Debug.i(TAG, "那个:" + arg2 + ":" + arg3);
			}
		});
	}

	private ApkCallback m_pCallback;

	class ApkCallback implements EnableCallback {

		@Override
		public void callback(Object... obj) {
			Integer pInteger = null;
			try {
				pInteger = Integer.parseInt(obj[0].toString());
			} catch (RuntimeException ee) {
				ee.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == pInteger) {
				Debug.e(TAG, "无法获得 callback tag !");
				return;
			}
			switch (pInteger.intValue()) {
			case EnableOfApkAdmin.STARTSCAN:
				// 开启扫描
				try {
					m_nTotalProgress = Integer.parseInt(obj[1].toString());
					m_pProssBar.setMax(m_nTotalProgress);
				} catch (Exception e) {
					e.printStackTrace();
					Debug.e(TAG, "反馈开启扫描中obj1出错! ");
				}
				m_nProgressIndex = 0;
				m_pProssBar.setProgress(m_nProgressIndex);
				Debug.e(TAG, "开始扫描所有应用:" + m_nTotalProgress);
				break;
			case EnableOfApkAdmin.SCAN_ITEM:
				m_nProgressIndex++;
				m_pProssBar.setProgress(m_nProgressIndex);
				Debug.i(TAG, "扫描到:" + obj[1].toString());
				break;
			case EnableOfApkAdmin.SCAN_OVER:
				m_pProssBar.setProgress(m_nTotalProgress);
				scanListOver();
				break;
			}
		}
	};

	class Adapter extends BaseAdapter {

		private LayoutInflater m_pInflater;

		public Adapter() {
			m_pInflater = LayoutInflater.from(SceneOfApkAdmin.this);
		}

		@Override
		public int getCount() {
			return m_pEnable.getDataCount();
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
				convertView = m_pInflater.inflate(R.layout.item_apkadmin, null);
			}
			Debug.i(TAG, "getView:" + position + ":" + m_nTargetViewIndex);
			if (position == m_nTargetViewIndex) {
				check(convertView
						.findViewById(R.id.apkadmin_item_rubbish_checkbox));
			} else {
				removeCheckRadio(convertView
						.findViewById(R.id.apkadmin_item_rubbish_checkbox));
			}
			AppInfo pAppInfo = m_pEnable.getDataInfo(position);
			TextView pName = (TextView) convertView
					.findViewById(R.id.apkadmin_itemitem_name);
			pName.setText(pAppInfo.getAppName() == null ? "未知应用" : pAppInfo
					.getAppName());
			((TextView) convertView.findViewById(R.id.apkadmin_itemitem_cache))
					.setText(getResources().getString(R.string.zhanyong)
							+ UiUtils.getCacheSize(pAppInfo.getM_nRam()));
			UiUtils.setText(convertView
					.findViewById(R.id.apkadmin_itemitem_installedtime),
					pAppInfo.getInstalledTime());
			convertView.findViewById(R.id.apkadmin_item_rubbish_checkbox)
					.setTag(TAG + ":" + position);
			convertView.findViewById(R.id.apkadmin_item_rubbish_checkbox)
					.setOnClickListener(
							new OnItemCheckBoxClickListener(position));
			try {
				((ImageView) convertView
						.findViewById(R.id.apkadmin_item_item_image))
						.setImageDrawable(SceneOfApkAdmin.this
								.getPackageManager()
								.getApplicationIcon(
										SceneOfApkAdmin.this
												.getPackageManager()
												.getApplicationInfo(
														pAppInfo.getPackageName(),
														PackageManager.GET_META_DATA)));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				Debug.i(TAG, ":图片找不到" + pAppInfo.getPackageName());
			}
			return convertView;
		}

	}

	class OnItemCheckBoxClickListener implements View.OnClickListener {
		private int m_nPos = -1;

		public OnItemCheckBoxClickListener(int nPos) {
			m_nPos = nPos;
		}

		@Override
		public void onClick(View v) {
			Debug.i(TAG, "onClick" + v.getTag() + ":" + m_nPos);
			onapkadminItemClick(m_nPos);
		}
	}

	private void showAnimOff(int nIndex) {
		Debug.i(TAG, "关闭AnimOff:" + nIndex);
		if (m_pListView.getChildAtC(nIndex) == null) {
			return;
		}
		final View pView = m_pListView.getChildAtC(nIndex).findViewById(
				R.id.apkadmin_item_rubbish_checkbox);
		int nId = R.anim.normal2small;
		Animation pAnim = AnimationUtils.loadAnimation(this, nId);
		pAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				removeCheckRadio(pView);
			}
		});
		pView.startAnimation(pAnim);
	}

	private void removeCheckRadio(View v) {
		if (v == null)
			return;
		((TextView) v).setTextColor(getResources().getColor(color.transparent));
		((TextView) v).setText("");
		v.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.removecheck));
	}

	private void check(int nIndex) {
		Debug.i(TAG, "check:" + nIndex);
		View v = m_pListView.getChildAtC(nIndex);
		if (v == null) {
			Debug.e(TAG, "出现了null的返回getChildAt(" + nIndex + ")");
			return;
		}
		check(v.findViewById(R.id.apkadmin_item_rubbish_checkbox));
	}

	private void check(View v) {
		int nId = R.drawable.checked;
		((TextView) v).setTextColor(getResources().getColor(R.color.white));
		((TextView) v).setText(getResources().getString(R.string.duihao));
		v.setBackgroundDrawable(getResources().getDrawable(nId));
	}

	private int m_nTargetViewIndex = -1;

	private void onapkadminItemClick(int nPos) {
		Debug.i(TAG, "onClick:" + nPos + ":" + m_nTargetViewIndex);
		if (m_nTargetViewIndex == -1) {
			m_nTargetViewIndex = nPos;
			check(nPos);
			UiUtils.setText(
					findViewById(R.id.apkadmin_tip_des),
					"已选"
							+ UiUtils.getCacheSize(m_pEnable.getDataInfo(nPos)
									.getM_nRam()));
			return;
		}
		if (nPos == m_nTargetViewIndex) {
			showAnimOff(m_nTargetViewIndex);
			UiUtils.setText(findViewById(R.id.apkadmin_tip_des),
					"共" + UiUtils.getCacheSize(m_pEnable.m_nTotalSize));
			m_nTargetViewIndex = -1;
			return;
		} else {
			showAnimOff(m_nTargetViewIndex);
			check(nPos);
			UiUtils.setText(
					findViewById(R.id.apkadmin_tip_des),
					"已选"
							+ UiUtils.getCacheSize(m_pEnable.getDataInfo(nPos)
									.getM_nRam()));
			m_nTargetViewIndex = nPos;
		}
	}

	protected void scanListOver() {
		findViewById(R.id.apkadmin_tip_pro).setVisibility(View.INVISIBLE);
		findViewById(R.id.apkadmin_total_rubbish_clickitems).setVisibility(View.VISIBLE);
		UiUtils.setText(findViewById(R.id.apkadmin_tip_0),
				"扫描到" + m_pEnable.getDataCount() + "个应用");
		findViewById(R.id.apkadmin_tip_des).setVisibility(View.VISIBLE);
		UiUtils.setText(findViewById(R.id.apkadmin_tip_des),
				"共" + UiUtils.getCacheSize(m_pEnable.m_nTotalSize));
		if (m_pListAdapter != null)
			m_pListAdapter.notifyDataSetChanged();
	}
}
