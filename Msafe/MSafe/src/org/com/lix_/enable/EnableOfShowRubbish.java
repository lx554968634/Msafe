package org.com.lix_.enable;

import org.com.lix_.ui.R;

import android.content.Context;

public class EnableOfShowRubbish extends Enable {
	
	private static final long serialVersionUID = -1021586470764052651L;

	public EnableOfShowRubbish(Context pContext) {
		super(pContext);
	}
	
	@Override
	public void finish() {

	}

	@Override
	public void onViewClick(int nId) {
		switch(nId)
		{
		case R.id.clear_itemlayout:
			clearRubbish() ;
			break ;
		}
	}

	private void clearRubbish() {
		
	}

}
