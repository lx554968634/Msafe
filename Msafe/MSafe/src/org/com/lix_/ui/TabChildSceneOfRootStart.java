package org.com.lix_.ui;

import android.os.Bundle;
import android.view.Window;

public class TabChildSceneOfRootStart extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE) ;
		setContentView(R.layout.tabchild_rootstart);
	}

	@Override
	public void init() {
		
	}

}
