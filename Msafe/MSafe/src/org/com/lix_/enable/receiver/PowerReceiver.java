package org.com.lix_.enable.receiver;

import org.com.lix_.enable.EnableOfPowerAdmin;
import org.com.lix_.util.Debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class PowerReceiver extends BroadcastReceiver{
	
	private String TAG = "PowerReceiver" ;
	
	private Handler m_pHandler ;

	public PowerReceiver(Handler pHandler) {
		super();
		m_pHandler = pHandler ;
	}
  
	@Override
	public void onReceive(Context context, Intent intent) {
		 int current=intent.getExtras().getInt("level");//��õ�ǰ����
         int total=intent.getExtras().getInt("scale");//����ܵ���
         int percent=current*100/total;
         Debug.i(TAG, current+":"+percent+":PowerReceiver onreceiver : "+System.currentTimeMillis());
         Message msg = Message.obtain() ;
         msg.what = EnableOfPowerAdmin.GET_POWERSTATE ;
         msg.obj = percent+"" ;
         m_pHandler.sendMessage(msg) ;
    //     tv.setText("���ڵĵ�����"+percent+"%��");
	}

}
