package org.com.lix_.ui;

import org.com.lix_.plugin.AListView;
import org.com.lix_.ui.MainActivity.Adapter.ViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SceneOfRubbishClear extends BaseActivity {

	private int COUNT_GRID_ITEMS;

	private String[] m_szTitles;

	private ListView m_pGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_runbishclear);
		init();
	}

	@Override
	public void init() {
		moveQuick() ;
		m_szTitles = getResources().getStringArray(
				R.array.titles_rubbishclear_array);
		COUNT_GRID_ITEMS = m_szTitles.length;
		m_pGridView = (ListView) findViewById(R.id.list_rubbishactivity);
		m_pGridView.setAdapter(new Adapter());
		m_pGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(SceneOfRubbishClear.this,SceneOfShowRubbish.class));
			}
		});
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
			pHolder.m_pTextView.setText(m_szTitles[position]);
			return convertView;
		}

		class ViewHolder {
			public TextView m_pTextView;
			public ImageView m_pImageView;
		}
	}

}
