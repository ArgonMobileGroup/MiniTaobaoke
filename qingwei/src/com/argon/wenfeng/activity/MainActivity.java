package com.argon.wenfeng.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.StaggeredGridView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;

@SuppressLint("NewApi")
public class MainActivity extends SherlockFragmentActivity {

	private Tracker mGaTracker;
	private GoogleAnalytics mGaInstance;

	private Fragment mContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MobclickAgent.onError(this);
		UMFeedbackService.enableNewReplyNotification(this, NotificationType.AlertDialog);
		
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		
		if (mContent == null) {
			mContent = new MainFragment(this);
		}
		
		getSupportFragmentManager()
		.beginTransaction()
		.add(android.R.id.content, mContent)
		.commit();

        mGaInstance = GoogleAnalytics.getInstance(this);
        mGaTracker = mGaInstance.getTracker("UA-39513550-1");

        getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	@Override
	public void onStop() {
		super.onStop();
	    EasyTracker.getInstance().activityStop(this);
	}

}
