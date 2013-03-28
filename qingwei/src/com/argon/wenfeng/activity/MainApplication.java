package com.argon.wenfeng.activity;

import com.taobao.top.android.TopAndroidClient;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class MainApplication extends Application {
	
	//Callback  
	final BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("SD_TRACE", "checkInternet: " + checkInternet());
			if(checkInternet() == false) {
				Toast.makeText(MainApplication.this, "Network error!", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		TopAndroidClient.registerAndroidClient(getApplicationContext(), 
				"21400902",       
				"78cbf3e688d01e369c771ec511aed926", 
				"com.dande.activity://authresult");
		
		Log.d("SD_TRACE", "register android client");
		
		

		//registering recievr in actibity or in service
		IntentFilter mNetworkStateFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetworkStateReceiver , mNetworkStateFilter);
		
	}
	
	public boolean checkInternet() {
	    ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	    // Here if condition check for wifi and mobile network is available or not.
	    // If anyone of them is available or connected then it will return true, otherwise false;

	    if (wifi != null && wifi.isConnected()) {
	        return true;
	    } else if (mobile != null && mobile.isConnected()) {
	        return true;
	    }
	    return false;
	}
}
