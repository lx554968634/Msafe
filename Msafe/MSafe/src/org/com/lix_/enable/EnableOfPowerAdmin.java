package org.com.lix_.enable;

import org.com.lix_.enable.receiver.PowerReceiver;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;

public class EnableOfPowerAdmin extends Enable {

	public static final int GET_POWERSTATE = 1;
	
	private String TAG = "EnableOfPowerAdmin" ;
	
	private PowerReceiver m_pReceiver ;

	public EnableOfPowerAdmin(Context pContext,EnableCallback pCallback) {
		super(pContext);
		setCallback(pCallback);
		init() ;
	}
	
	@Override
	protected void doSynchrWork(Message pMsg) {
		// TODO Auto-generated method stub
		super.doSynchrWork(pMsg);
		int nTag = pMsg.what ;
		switch(nTag)
		{
		case GET_POWERSTATE:
			m_pCallback.callback(nTag,pMsg.obj );
			break ;
		}
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		// TODO Auto-generated method stub
		super.doAsyWorkInTask(szObj);
	}

	private void init()
	{
		regPowerChangeState() ;
		doAsyWork(); 
	}

	private void regPowerChangeState() {
		if(m_pReceiver == null)
			m_pReceiver = new PowerReceiver(m_pAsyHandler) ;
		 IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
         m_pContext.registerReceiver(m_pReceiver, filter);//×¢²áBroadcastReceiver
	}

	@Override
	public void finish() {
		if(m_pReceiver != null)
			if(m_pContext != null)
			{
				Debug.i(TAG, "ÊÍ·Å m_pReceiver");
				m_pContext.unregisterReceiver(m_pReceiver);
			}
	}
	
	@Override
	public void onViewClick(int nId) {

	}

}
