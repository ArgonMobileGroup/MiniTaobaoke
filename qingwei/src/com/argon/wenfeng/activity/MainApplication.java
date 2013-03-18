package com.argon.wenfeng.activity;

import com.taobao.top.android.TopAndroidClient;

import android.app.Application;
import android.util.Log;

public class MainApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		TopAndroidClient.registerAndroidClient(getApplicationContext(), 
				"21398750",       
				"50fec5dc44d55a862f514c62af3212fa", 
				"com.argon.wenfeng.activity://authresult");
		
		Log.d("SD_TRACE", "register android client");
	}
}
