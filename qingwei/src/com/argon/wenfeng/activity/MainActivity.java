package com.argon.wenfeng.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.view.MenuItem;
import com.argon.wenfeng.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;

@SuppressLint("NewApi")
public class MainActivity extends SlidingFragmentActivity {

	private Tracker mGaTracker;
	private GoogleAnalytics mGaInstance;

	private Fragment mContent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MobclickAgent.onError(this);
		UMFeedbackService.enableNewReplyNotification(this, NotificationType.AlertDialog);
		
		// set the Above View
        setContentView(R.layout.content_frame);
		
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		
		if (mContent == null) {
			mContent = new MainFragment(this);
		}
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();

        mGaInstance = GoogleAnalytics.getInstance(this);
        mGaTracker = mGaInstance.getTracker("UA-39513550-1");

        // set the Behind View
     	setBehindContentView(R.layout.menu_frame);
     	getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MenuFragment(this))
		.commit();
        
        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
//        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        
        setSlidingActionBarEnabled(false);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case android.R.id.home:
        toggle();
        return true;
    }
    return super.onOptionsItemSelected(item);
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
	
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}

}
