package com.argon.wenfeng.activity;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.argon.wenfeng.R;
import com.argon.wenfeng.data.GoodsItem;
import com.argon.wenfeng.data.GoodsItemManager;
import com.argon.wenfeng.data.GoodsItemManager.OnGoodsItemLoadListener;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;
import com.taobao.top.android.api.TopTqlListener;



import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.StaggeredGridView;
import android.support.v4.widget.StaggeredGridView.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends SherlockActivity {

	private StaggeredGridView mSGV;
	
	private GoodsItemAdapter mGoodsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mGoodsAdapter = new GoodsItemAdapter(this, 
				R.id.imageView1, 
				GoodsItemManager.instance().getGoodsItems());
		
		
		//mAdapter = new SGVAdapter(this);
        mSGV = (StaggeredGridView) findViewById(R.id.grid);
        //mSGV.setColumnCount(-1);
        //mSGV.setAdapter(mAdapter);
        mSGV.setAdapter(mGoodsAdapter);
        //mSGV.setAdapter(new EndlessGoodsItemAdapter(this, mGoodsAdapter, R.id.textView1));
        mSGV.setItemMargin(10);
        mGoodsAdapter.notifyDataSetChanged();
        
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(GoodsItemManager.instance().getGoodsItems().size() == 0) {
			refreshGoodsItems();
		}
		//refreshGoodsItems();
	}

	private MenuItem mRefreshItem;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.refresh:
            mRefreshItem = item;
            refresh();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	
	/**
     * Refresh the fragment's list
     */
    public void refresh() {
        
            /* Attach a rotating ImageView to the refresh item as an ActionView */
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);

            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.clockwise_refresh);
            rotation.setRepeatCount(Animation.INFINITE);
            iv.startAnimation(rotation);

            mSGV.setVisibility(View.INVISIBLE);
            
            mRefreshItem.setActionView(iv);
            refreshGoodsItems();
        
    }
	
    public void onLoadFinished() {
    	if(mLoadSuccess) {
	    	mGoodsAdapter = new GoodsItemAdapter(this, 
					R.id.imageView1, 
					GoodsItemManager.instance().getGoodsItems());
	    	mSGV.setAdapter(mGoodsAdapter);
	    	
    	} else {
    		Toast.makeText(this, "Network error!", Toast.LENGTH_SHORT).show();
    	}

        if (mRefreshItem != null && mRefreshItem.getActionView() != null) {
        	mRefreshItem.getActionView().clearAnimation();
        	mRefreshItem.setActionView(null);
        }
        mSGV.setVisibility(View.VISIBLE);
    }
    
	Handler mHandler = new Handler();

	protected boolean mLoadSuccess = true;

	private void refreshGoodsItems() {
		
		if(checkInternet()) {
			
			GoodsItemManager.instance().refresh(new OnGoodsItemLoadListener() {
	
				@Override
				public void onComplete(ArrayList<GoodsItem> mGoodsItems) {
					// TODO Auto-generated method stub
					mHandler.postDelayed(new Runnable() {
	
						@Override
						public void run() {
							onLoadFinished();
						}
						
					}, 1000);
					mLoadSuccess = true;
				}
	
				@Override
				public void onError(ApiError error) {
					// TODO Auto-generated method stub
					Log.d("SD_TRACE", "load api error" + error.toString());
					mLoadSuccess = false;
				}
	
				@Override
				public void onException(Exception e) {
					// TODO Auto-generated method stub
					Log.d("SD_TRACE", "load api exception" + e.toString());
					mLoadSuccess  = false;
				}
				
			});
		} else {
			mLoadSuccess = false;
			onLoadFinished();
		}
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
