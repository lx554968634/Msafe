package org.com.lix_.ui;

import org.com.lix_.enable.EnableOfTabChildSceneOfNotification;

import android.app.Fragment;
import android.view.Window;

/*
 * notification ֪ͨ
 */
public class TabChildSceneOfNotification extends BaseActivity{
	
	private EnableOfTabChildSceneOfNotification m_pEnable;
	
	public  TabChildSceneOfNotification() {
		requestWindowFeature(Window.FEATURE_NO_TITLE) ;
		setContentView(R.layout.tabchild_root_notification);
	}

	@Override
	public void init() {
	}

}
