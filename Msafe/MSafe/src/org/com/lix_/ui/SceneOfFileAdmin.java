package org.com.lix_.ui;

import java.util.HashMap;
import java.util.Map;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfFileAdmin;
import org.com.lix_.enable.engine.FileInfo;
import org.com.lix_.plugin.AListView;
import org.com.lix_.plugin.Rect;
import org.com.lix_.ui.dialog.DialogOfFileAdminTypes;
import org.com.lix_.ui.dialog.DialogOfFileItemClick;
import org.com.lix_.util.Debug;
import org.com.lix_.util.MediaUtils;
import org.com.lix_.util.UiUtils;

import android.R.color;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SceneOfFileAdmin extends BaseActivity {

	private AListView m_pListView;

	private EnableOfFileAdmin m_pEnable;

	private Callback m_pCallback;

	private ProgressBar m_pScanProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fileadmin);
		init();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.fileadmin_tip_0:
		case R.id.fileadmin_list_type:
			Debug.i(TAG, "监听 了 信息弹出对话框");
			if (findViewById(R.id.fileadmin_list_type).getVisibility() == View.VISIBLE) {
				dialogShowOK();
				// dialogShowFileItem();
			}
			break;
		}
	}

	protected String TAG = "SceneOfFileAdmin";
	DialogOfFileAdminTypes m_pPopFileTypeDialog;
	DialogOfFileItemClick m_pFileItemClickDialog;

	/*
	 * 弹出文件类型选项
	 */
	private void dialogShowOK() {
		if (m_pPopFileTypeDialog != null)
			m_pPopFileTypeDialog.show();
		else {
			m_pPopFileTypeDialog = new DialogOfFileAdminTypes(this,
					R.style.noshade_dialog, R.layout.dialog_fileadmin_filetypes);
			Window pWin = m_pPopFileTypeDialog.getWindow();
			LayoutParams pParam = pWin.getAttributes();
			pParam.x = (int) m_pFileTypePopRect.m_nX;
			pParam.width = m_pFileTypePopRect.m_nWidth / 2;
			pParam.height = m_pFileTypePopRect.m_nHeight;
			pParam.y = (int) (m_pFileTypePopRect.m_nY
					- getResources().getDimension(R.dimen.padding_list) + m_pFileTypePopRect.m_nHeight);
			m_pPopFileTypeDialog.getWindow().setAttributes(pParam);
			m_pPopFileTypeDialog.show();
		}
	}

	/*
	 * 加载图片不急弄
	 */
	private void dialogShowFileItem() {
		if (m_pFileItemClickDialog != null)
			m_pFileItemClickDialog.show();
		else {
			FileInfo pFileInfo = new FileInfo();
			pFileInfo.m_sFileName = "V51031-173625.mp4";
			pFileInfo.m_sType = "录像";
			pFileInfo.m_sAbFilePath = "sdcard/DCIM/Vidio/V51031-173625.mp4";
			pFileInfo.initModifyTime();
			pFileInfo.m_nFileSize = 20000000;
			m_pFileItemClickDialog = new DialogOfFileItemClick(this,
					R.style.class_dialog, R.layout.dialog_fileadmin_item,
					pFileInfo);
			m_pFileItemClickDialog.show();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseDialog(m_pFileItemClickDialog);
		releaseDialog(m_pPopFileTypeDialog);
	}
	private void releaseDialog(Dialog pDialog) {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
	@Override
	public void init() {
		m_pScanProgress = (ProgressBar) findViewById(R.id.fileadmin_progress);
		m_pEnable = new EnableOfFileAdmin(this);
		m_pCallback = new Callback();
		findViewById(R.id.line_view).setVisibility(View.VISIBLE);
		final View pView = findViewById(R.id.fileadmin_pop_tag);
		pView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Debug.i(TAG,
								"获得view 信息：" + pView.getX() + ":"
										+ pView.getY() + ":"
										+ pView.getMeasuredWidth() + ":"
										+ pView.getMeasuredHeight());
						getPopFileTypeRect(pView.getLeft(), pView.getTop(),
								pView.getMeasuredWidth(),
								pView.getMeasuredHeight());
					}
				});
		findViewById(R.id.fileadmin_list_type).setOnClickListener(this);
		findViewById(R.id.fileadmin_tip_0).setOnClickListener(this);
		findViewById(R.id.fileadmin_list).setVisibility(View.VISIBLE);
		m_pListView = (AListView) findViewById(R.id.fileadmin_list);
		m_pListView.setAutoScroll();
		m_pAdapter = new Adapter();
		m_pListView.setAdapter(m_pAdapter);
		m_pEnable.init(m_pCallback);
	}
	class ItemClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			dialogShowFileItem();

		}
	}
	class Callback implements EnableCallback {

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
			case EnableOfFileAdmin.FINISH_SIMFILE_SCAN:
				m_pAdapter.notifyDataSetChanged();
				break;
			case EnableOfFileAdmin.REFRESH_PROGRESS:
				setProgressBar(Integer.parseInt(obj[1].toString()));
				break;
			case EnableOfFileAdmin.NONE_SD:
				finishScan(EnableOfFileAdmin.NONE_SD);
				break;
			case EnableOfFileAdmin.FINISH_SCAN:
				finishScan(EnableOfFileAdmin.FINISH_SCAN);
				break;
			case EnableOfFileAdmin.GET_MEDIA_IMAGE:
				if (obj[1] == null)
					return;
				int nPos = Integer.parseInt(obj[3].toString());
				if (m_pListView.getChildAtC(nPos) == null)
					return;
				if (Integer.parseInt(m_pListView.getChildAtC(nPos).getTag()
						.toString()) == nPos) {
					if (m_szItemHolder.get(TAG + nPos).m_pFileInfo.m_sAbFilePath
							.equals(obj[1].toString())) {
						Bitmap pTmp = (Bitmap) obj[2];
						if (pTmp != null) {
							if (m_pListView.getChildAtC(nPos) == null)
								return;
							((ImageView) m_pListView.getChildAtC(nPos)
									.findViewById(R.id.fileadmin_item_image))
									.setImageBitmap(pTmp);
						}
					}
				}
				break;
			}
		}
	}
	private void setProgressBar(int nValue) {
		Debug.i(TAG, nValue + "更新进度:" + (m_pScanProgress == null));
		if (m_pScanProgress != null) {
			m_pScanProgress.setProgress(nValue);
		}
	}
	private void finishScan(int nStatus) {
		findViewById(R.id.filadmin_tip_pro).setVisibility(View.INVISIBLE);
		switch (nStatus) {
		case EnableOfFileAdmin.NONE_SD:
			UiUtils.setText(findViewById(R.id.fileadmin_tip_0), getResources()
					.getString(R.string.fileadmin_none_sdcard));
			break;
		case EnableOfFileAdmin.FINISH_SCAN:
			findViewById(R.id.total_rubbish_clickitems).setVisibility(
					View.VISIBLE);
			UiUtils.setText(findViewById(R.id.fileadmin_tip_0),
					m_pEnable.m_nTotalSize + "个大文件");
			findViewById(R.id.fileadmin_list_type).setVisibility(View.VISIBLE);
			findViewById(R.id.filadmin_tip_des).setVisibility(View.VISIBLE);
			UiUtils.setText(findViewById(R.id.filadmin_tip_des),
					"共" + UiUtils.getCacheSize(m_pEnable.m_nTotalCache));
			m_pAdapter.notifyDataSetChanged();
			break;
		}
	}
	private Adapter m_pAdapter;
	// ebebeb f5f5f5 fafafa
	class Adapter extends BaseAdapter {

		private LayoutInflater m_pInflater;

		public Adapter() {
			m_pInflater = LayoutInflater.from(SceneOfFileAdmin.this);
		}

		@Override
		public int getCount() {
			return m_pEnable.m_nTotalSize;
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
				convertView = m_pInflater
						.inflate(R.layout.item_fileadmin, null);
			}
			if (convertView.getTag() != null
					&& convertView.getTag().toString().equals(position + "")) {
				return convertView;
			}
			ViewHolder pViewHolder = null;
			convertView.setTag(position + "");
			pViewHolder = m_szItemHolder.get(TAG + position);
			if (pViewHolder == null) {
				pViewHolder = new ViewHolder();
				pViewHolder.m_pFileInfo = m_pEnable.getData().get(position);
				pViewHolder.m_pCheckView = convertView;
				checkRadio(pViewHolder.m_pCheckView);
				m_szItemHolder.put(TAG + convertView.getTag(), pViewHolder);
			}
			final View v = pViewHolder.m_pCheckView;
			final FileInfo pFile = pViewHolder.m_pFileInfo;
			if (MediaUtils.checkMediaName(pFile.m_sAbFilePath)) {
				m_pEnable.getMediaImage(position, pFile.m_sAbFilePath);
			} else {
				((ImageView) convertView
						.findViewById(R.id.fileadmin_item_image))
						.setImageDrawable(getResources().getDrawable(
								R.drawable.fileadmin_item_default));
			}
			ItemClickListener pLis = new ItemClickListener();
			convertView.findViewById(R.id.fileadmin_item_image)
					.setOnClickListener(pLis);
			convertView.findViewById(R.id.filemadin_item_rubbish_des)
					.setOnClickListener(pLis);
			if (v != null) {
				v.findViewById(R.id.fileadmin_rubbish_checkbox)
						.setOnTouchListener(new TouchListener(position));
			}
			((TextView) v.findViewById(R.id.fileadminitem_name))
					.setText(pFile.m_sFileName);
			((TextView) v.findViewById(R.id.fileadminitem_cache)).setText(pFile
					.getSize());
			((TextView) v.findViewById(R.id.fileadminitem_desc))
					.setText(pFile.m_sType);
			return v;
		}
	}

	private void checkAll(boolean nFlag) {
		if (nFlag) {
			for (ViewHolder pHolder : m_szItemHolder.values()) {
				if (pHolder.m_bCheck == -1) {
					pHolder.m_bCheck = 0;
					unCheckRadio(pHolder.m_pCheckView);
				}
			}
		} else {
			boolean bTag = false;
			for (ViewHolder pHolder : m_szItemHolder.values()) {
				if (pHolder.m_bCheck == 1) {
					bTag = true;
					break;
				}
			}
			if (!bTag) {
				Debug.i(TAG, "消除checkbox痕迹");
				for (ViewHolder pHolder : m_szItemHolder.values()) {
					pHolder.m_bCheck = -1;
					showAnimSmall2Normal(pHolder.m_pCheckView, false);
				}
			}
		}
	}

	private void checkRadio(View v) {
		int nId = R.drawable.checked;
		((TextView) v.findViewById(R.id.fileadmin_rubbish_checkbox))
				.setTextColor(getResources().getColor(R.color.white));
		((TextView) v.findViewById(R.id.fileadmin_rubbish_checkbox))
				.setText(getResources().getString(R.string.duihao));
		v.findViewById(R.id.fileadmin_rubbish_checkbox).setBackgroundDrawable(
				getResources().getDrawable(nId));
	}

	private void unCheckRadio(View v) {
		int nId = R.drawable.uncheck;
		((TextView) v.findViewById(R.id.fileadmin_rubbish_checkbox))
				.setTextColor(getResources().getColor(R.color.white));
		((TextView) v.findViewById(R.id.fileadmin_rubbish_checkbox))
				.setText("");
		v.findViewById(R.id.fileadmin_rubbish_checkbox).setBackgroundDrawable(
				getResources().getDrawable(nId));
	}

	private void showAnimSmall2Normal(final View v, final boolean bTag) {
		int nId = -1;
		if (bTag)
			nId = R.anim.small2normal;
		else
			nId = R.anim.normal2small;
		Debug.i(TAG, "放动画:" + bTag);
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
				Debug.i(TAG, "播放结束:" + bTag);
				if (bTag) {
					unCheckRadio(v);
				} else {
					removeCheckRadio(v);
				}
			}
		});
		v.findViewById(R.id.fileadmin_rubbish_checkbox).startAnimation(pAnim);
	}

	private void removeCheckRadio(View v) {
		((TextView) v.findViewById(R.id.fileadmin_rubbish_checkbox))
				.setTextColor(getResources().getColor(color.transparent));
		((TextView) v.findViewById(R.id.fileadmin_rubbish_checkbox))
				.setText("");
		v.findViewById(R.id.fileadmin_rubbish_checkbox).setBackgroundDrawable(
				getResources().getDrawable(R.drawable.removecheck));
	}

	private void check(View v, boolean bCheck) {
		if (bCheck) {
			checkRadio(v);
			checkAll(bCheck);
		} else {
			unCheckRadio(v);
			checkAll(bCheck);
		}
	}

	class TouchListener implements OnTouchListener {

		private int m_nPos;

		public TouchListener(int nPosi) {
			m_nPos = nPosi;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			TextView pTxt = ((TextView) v);
			boolean bChecked = false;
			if (pTxt.getText() == null
					|| !pTxt.getText().toString()
							.equals(getResources().getString(R.string.duihao))) {
			} else {
				pTxt.setText(getResources().getString(R.string.duihao));
				bChecked = true;
			}
			int nEventCode = event.getAction();
			switch (nEventCode) {
			case MotionEvent.ACTION_DOWN:
				m_pListView.m_bCanMove = false;
				ViewHolder pViewHolder = m_szItemHolder.get(TAG + m_nPos);
				if (m_pListView.m_bCheckValue == 0) {
					m_pListView.m_bCheckValue = bChecked ? 1 : -1;
				}
				if (m_pListView.m_bCheckValue == 1) {
					if (pViewHolder.m_bCheck == 0) {
					} else if (pViewHolder.m_bCheck == -1) {
					} else {
						pViewHolder.m_bCheck = 0;
						check(pViewHolder.m_pCheckView, false);
					}
				} else {
					pViewHolder.m_bCheck = 1;
					check(pViewHolder.m_pCheckView, true);
				}
				break;
			}
			return false;
		}
	}

	private Map<String, ViewHolder> m_szItemHolder = new HashMap<String, ViewHolder>();

	class ViewHolder {
		int m_bCheck = 1; // 选中 0 至空，-1都没选
		FileInfo m_pFileInfo;
		View m_pCheckView;
	}

	private Rect m_pFileTypePopRect;

	private void getPopFileTypeRect(float nX, float nY, int nWidth, int nHeight) {
		m_pFileTypePopRect = new Rect(nX, nY, nWidth, nHeight);
	}

}
