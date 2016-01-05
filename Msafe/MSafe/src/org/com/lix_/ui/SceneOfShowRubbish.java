package org.com.lix_.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.com.lix_.Define;
import org.com.lix_.enable.EnableOfShowRubbish;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.FileInfo;
import org.com.lix_.plugin.AListView;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SceneOfShowRubbish extends BaseActivity {

	private String TAG = "SceneOfShowRubbish";
	private AListView m_pGridView;
	private PackageManager m_pPckManager;
	private int m_nIndex = -1;
	private LayoutInflater m_pInflater;

	private HashMap<String, AppInfo> m_szTargetHashMap0;

	private HashMap<String, ArrayList<FileInfo>> m_szTargetHashMap1;

	private ArrayList<FileInfo> m_szArrayList1;

	private ArrayList<FileInfo> m_szArrayList0;

	private EnableOfShowRubbish m_pEnable;

	private TextView m_pTotalShowView;

	public Map<String, ViewHolder> m_szListCache = new HashMap<String, ViewHolder>();

	String[] m_szTitls = new String[] { "内存加速", "系统及应用缓存", "垃圾文件", "多余安装包",
			"应用卸载残余" };

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		m_pEnable.onViewClick(v.getId());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rubbish_show_itemlayout);
		init();
	}

	@Override
	public void init() {
		m_pTotalShowView = (TextView) findViewById(R.id.total_rubbish_clickitems);
		m_pEnable = new EnableOfShowRubbish(this);
		m_pInflater = LayoutInflater.from(this);
		Intent pIntent = getIntent();
		m_pPckManager = getPackageManager();
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
		Debug.i(TAG, "m_nIndex" + m_nIndex);
		// 根据index获取容器
		switch (m_nIndex) {
		case 0:
		case 1:
			m_szTargetHashMap0 = (HashMap<String, AppInfo>) pIntent
					.getSerializableExtra(Define.RAM_STR);
			Debug.i(TAG, "m_szList == null :" + (m_szTargetHashMap0 == null));
			break;
		case 2:
		case 3:
			m_szTargetHashMap1 = (HashMap<String, ArrayList<FileInfo>>) pIntent
					.getSerializableExtra(Define.RAM_STR);
			Debug.i(TAG, "m_szList == null :" + (m_szTargetHashMap1 == null));
			break;
		case 4:
			m_szArrayList0 = (ArrayList<FileInfo>) pIntent
					.getSerializableExtra(Define.RAM_STR);
			Debug.i(TAG, "m_szList == null :" + (m_szArrayList0 == null));
			break;
		}
		if (m_nIndex == 2) {
			ArrayList<FileInfo> szFile = null;
			m_szArrayList1 = new ArrayList<FileInfo>();
			for (int i = 0; i < 4; i++) {
				szFile = m_szTargetHashMap1.get(getResources().getStringArray(
						R.array.type_rubbishclear_array)[i]);
				if (szFile == null)
					continue;
				for (int j = 0; j < szFile.size(); j++) {
					szFile.get(j).m_nFileType = i;
					m_szArrayList1.add(szFile.get(j));
				}
			}
		}
		boolean nFlag = false;
		switch (m_nIndex) {
		case 0:
		case 1:
			if (m_szTargetHashMap0 == null || m_szTargetHashMap0.size() == 0) {
			} else {
				Debug.i(TAG, m_szTargetHashMap0.size());
				nFlag = true;
			}

			break;
		case 2:
		case 3:
			if (m_szTargetHashMap1 == null || m_szTargetHashMap1.size() == 0) {

			} else
				nFlag = true;
			break;
		case 4:
			if (m_szArrayList0 == null || m_szArrayList0.size() == 0) {

			} else
				nFlag = true;
			break;
		}
		if (nFlag) {
			findViewById(R.id.no_rubbish_txt).setVisibility(View.INVISIBLE);
			findViewById(R.id.grid_view).setVisibility(View.VISIBLE);
			m_pGridView = (AListView) findViewById(R.id.grid_view);
			m_pGridView.setAutoScroll();
			m_pGridView.setAdapter(new Adapter());
		} else {
			findViewById(R.id.no_rubbish_txt).setVisibility(View.VISIBLE);
			findViewById(R.id.grid_view).setVisibility(View.INVISIBLE);
		}
	}

	private int getListCount() {
		int nTag = 0;
		switch (m_nIndex) {
		case 0:
		case 1:
			nTag = m_szTargetHashMap0.size();
			break;
		case 2:
			nTag = m_szArrayList1.size();
			break;
		case 3:
			nTag = m_szTargetHashMap1.size();
			break;
		case 4:
			nTag = m_szArrayList0.size();
			break;
		default:
			break;
		}
		return nTag;
	}

	class Adapter extends BaseAdapter {
		private int m_nCount;

		public Adapter() {
			m_nCount = getListCount();
		}

		@Override
		public int getCount() {
			return m_nCount;
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
			ViewHolder pHolder = m_szListCache.get(TAG + position);
			switch (m_nIndex) {
			case 0:
				return getRubbishPropView(position, convertView, parent,
						pHolder);
			case 1:
				return getApkCacheView(position, convertView, parent, pHolder);
			case 2:
				return getRubbishPropView(position, convertView, parent,
						pHolder);
			case 3:
				return getApkCacheView(position, convertView, parent, pHolder);
			case 4:
				return getBlankFileView(position, convertView, parent, pHolder);
			}
			return null;
		}
	}

	public View getApkFileView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = m_pInflater.inflate(
					R.layout.grid_item_rubbishactivity, null);
		}
		if (convertView != null) {
			convertView.findViewById(R.id.grid_rubbish_checkbox)
					.setOnTouchListener(new TouchListener(position));
		}
		ImageView pImageView = (ImageView) convertView
				.findViewById(R.id.grid_item_image);
		pImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.rubbish_clear_t3));
		TextView pTextName = (TextView) convertView
				.findViewById(R.id.item_name);
		TextView pRamText = (TextView) convertView.findViewById(R.id.item_desc);
		ArrayList<FileInfo> szTmp = m_szTargetHashMap1.get(getResources()
				.getStringArray(R.array.type_rubbishclear_array)[4]);
		FileInfo sName = szTmp.get(position);
		pTextName.setText(sName.m_sFileName);
		pRamText.setText("apk安装包大小  " + sName.getSize());
		return convertView;
	}

	public View getRubbishFileView(int position, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = m_pInflater.inflate(
					R.layout.grid_item_rubbishactivity, null);
		}
		if (convertView != null) {
			convertView.findViewById(R.id.grid_rubbish_checkbox)
					.setOnTouchListener(new TouchListener(position));
		}
		ImageView pImageView = (ImageView) convertView
				.findViewById(R.id.grid_item_image);
		pImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.rubbish_clear_t2));
		TextView pTextName = (TextView) convertView
				.findViewById(R.id.item_name);
		TextView pRamText = (TextView) convertView.findViewById(R.id.item_desc);
		FileInfo sName = m_szArrayList1.get(position);
		pTextName.setText(sName.m_sFileName);
		pRamText.setText("垃圾文件  " + sName.getSize());
		return convertView;
	}

	public View getApkCacheView(int position, View convertView,
			ViewGroup parent, ViewHolder pHolder) {
		if (convertView == null) {
			convertView = m_pInflater.inflate(
					R.layout.grid_item_rubbishactivity, null);
		}
		ImageView pImageView = (ImageView) convertView
				.findViewById(R.id.grid_item_image);
		TextView pTextName = (TextView) convertView
				.findViewById(R.id.item_name);
		TextView pRamText = (TextView) convertView.findViewById(R.id.item_desc);
		TouchListener pListener = null ;
		String sPckName = null ;
		String sDes = "apk缓存信息大小";
		long nRam = 0;
		String sName = "";
		if(pHolder == null)
		{
			AppInfo pInfo = (AppInfo) m_szTargetHashMap0.values().toArray()[position];
			sPckName = pInfo.getPackageName() ;
			nRam = pInfo.getmCache();
			sName = pInfo.getAppName();
			sDes += " " + UiUtils.getCacheSize(nRam);
			pListener = new TouchListener(position) ;
			pHolder = new ViewHolder();
			pHolder.m_sItemContent = sDes;
			pHolder.m_sItemName = sName;
			pListener = new TouchListener(position);
			pHolder.m_pListener = pListener;
			pHolder.m_nTitleImagePckName = sPckName;
		}else
		{
			check(convertView.findViewById(R.id.grid_rubbish_checkbox),
					pHolder.m_bChecked);
		}
		
		pTextName.setText(pHolder.m_sItemName);
		if (convertView != null) {
			convertView.findViewById(R.id.grid_rubbish_checkbox)
					.setOnTouchListener(pHolder.m_pListener);
		}
		try {
			Drawable pDraw = m_pPckManager.getApplicationIcon(pHolder.m_nTitleImagePckName);
			pImageView.setImageDrawable(pDraw);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			pImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.rubbish_clear_t1));
		}
		pRamText.setText(pHolder.m_sItemContent);
		return convertView;

	}

	private View getRubbishPropView(int position, View convertView,
			ViewGroup parent, ViewHolder pHolder) {
		if (convertView == null) {
			convertView = m_pInflater.inflate(
					R.layout.grid_item_rubbishactivity, null);
		}
		ImageView pImageView = (ImageView) convertView
				.findViewById(R.id.grid_item_image);
		TextView pTextName = (TextView) convertView
				.findViewById(R.id.item_name);
		TextView pRamText = (TextView) convertView.findViewById(R.id.item_desc);
		String sName = null;
		String sPckName = null;
		String sDes = "一个进程";
		long nRam = 0;
		TouchListener pListener = null;
		if (pHolder == null) {
			AppInfo pInfo = (AppInfo) m_szTargetHashMap0.values().toArray()[position];
			sPckName = pInfo.getPackageName();
			sName = pInfo.getAppName();
			int nServiceCount = pInfo.getmServiceCount();
			nRam = pInfo.getM_nRam();
			if (nServiceCount != 0) {
				sDes += "包含" + nServiceCount + "个服务";
			}
			sDes += " " + UiUtils.getCacheSize(nRam);
			pHolder = new ViewHolder();
			pHolder.m_sItemContent = sDes;
			pHolder.m_sItemName = sName;
			pListener = new TouchListener(position);
			pHolder.m_pListener = pListener;
			pHolder.m_nTitleImagePckName = sPckName;
		} else {
			check(convertView.findViewById(R.id.grid_rubbish_checkbox),
					pHolder.m_bChecked);
		}
		pTextName.setText(pHolder.m_sItemName);
		try {
			Drawable pDraw = m_pPckManager.getApplicationIcon(pHolder.m_nTitleImagePckName);
			pImageView.setImageDrawable(pDraw);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			pImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.rubbish_clear_t0));
		}

		convertView.findViewById(R.id.grid_rubbish_checkbox)
				.setOnTouchListener(pHolder.m_pListener);
		pRamText.setText(pHolder.m_sItemContent);
		return convertView;
	}

	public View getBlankFileView(int position, View convertView,
			ViewGroup parent, ViewHolder pHolder) {
		if (convertView == null) {
			convertView = m_pInflater.inflate(
					R.layout.grid_item_rubbishactivity, null);
		}
		ImageView pImageView = (ImageView) convertView
				.findViewById(R.id.grid_item_image);
		TextView pTextName = (TextView) convertView
				.findViewById(R.id.item_name);
		TextView pRamText = (TextView) convertView.findViewById(R.id.item_desc);
		TouchListener pListener = null ;
		String sName = "" ;
		String sContent = "" ;
		if(pHolder == null)
		{
			pHolder = new ViewHolder() ;
			FileInfo sInfo = m_szArrayList0.get(position);
			pListener = new TouchListener(position) ;
			pHolder.m_pListener = pListener ;
			sName = "空白文件夹" ;
			sContent = sInfo.m_sFileName ;
			pHolder.m_sItemName = sName ;
			pHolder.m_sItemContent = sContent ;
			m_szListCache.put(TAG+position, pHolder) ;
		}else
		{
			check(convertView.findViewById(R.id.grid_rubbish_checkbox),
					pHolder.m_bChecked);
		}
		pTextName.setText(pHolder.m_sItemName );
		pRamText.setText(pHolder.m_sItemContent);
		pImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.rubbish_clear_t4));
		if (convertView != null) {
			convertView.findViewById(R.id.grid_rubbish_checkbox)
					.setOnTouchListener(pListener);
		}
		return convertView;
	}

	class ViewHolder {
		public TouchListener m_pListener;
		public String m_nTitleImagePckName;
		public String m_sItemName;
		public String m_sItemContent;
		public boolean m_bChecked = true;
	}

	private void check(View v, boolean bCheck) {
		int nId = 0;
		if (bCheck) {
			((TextView) v).setText(getResources().getString(R.string.duihao));
			nId = R.drawable.checked;
		} else {
			nId = R.drawable.uncheck;
			((TextView) v).setText("");
		}
		v.setBackgroundDrawable(getResources().getDrawable(nId));
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
				m_pGridView.m_bCanMove = false;
				if (m_pGridView.m_bCheckValue == 0) {
					m_pGridView.m_bCheckValue = bChecked ? 1 : -1;
				}
				if (m_pGridView.m_bCheckValue == 1) {
					check(v, false);
				} else {
					check(v, true);
				}
				break;
			}
			return false;
		}
	}
}
