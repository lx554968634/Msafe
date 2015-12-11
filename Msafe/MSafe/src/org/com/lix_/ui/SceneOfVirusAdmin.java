package org.com.lix_.ui;

import android.os.Bundle;
import android.view.Window;

public class SceneOfVirusAdmin extends BaseActivity {

	protected String TAG = "SceneOfVirusAdmin";

	@Override
	public void init() {
		moveQuick() ;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.virvusadmin);
		init() ;
	}

}
