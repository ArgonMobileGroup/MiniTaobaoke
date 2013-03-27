package com.argon.wenfeng.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.argon.wenfeng.R;
import com.argon.wenfeng.R.layout;
import com.argon.wenfeng.R.menu;
import com.argon.wenfeng.data.GoodsItemManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.UMFeedbackService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class ItemDetailActivity extends SherlockFragmentActivity {

	/**
     * The {@link ViewPager} that will display the detail of Item
     */
    ViewPager mViewPager;
    
    private ItemDetailPageAdapter mItemDetailPageAdapter = null;
    
    private Tracker mGaTracker;
	private GoogleAnalytics mGaInstance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MobclickAgent.onError(this);
		
		setContentView(R.layout.activity_item);
		
		// Set up action bar.
        final ActionBar actionBar = getSupportActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        
        mItemDetailPageAdapter = new ItemDetailPageAdapter(this, getSupportFragmentManager());
        
        mViewPager.setAdapter(mItemDetailPageAdapter);
        Log.d("SD_TRACE", "show postion: " + getIntent().getIntExtra("SHOW_POSITION", 0));
        mViewPager.setCurrentItem(getIntent().getIntExtra("SHOW_POSITION", 0));
//        int pos = getIntent().getIntExtra("SHOW_POSITION", 0);
//        mItemDetailPageAdapter.setPrimaryItem(mViewPager, pos, mItemDetailPageAdapter.instantiateItem(mViewPager, pos));
        
        mGaInstance = GoogleAnalytics.getInstance(this);
        mGaTracker = mGaInstance.getTracker("UA-39513550-1");
        
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		//refreshGoodsItems();
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.item, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                	finish();
                }
                return true;
                
            case R.id.action_feedback:
            	UMFeedbackService.openUmengFeedbackSDK(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
