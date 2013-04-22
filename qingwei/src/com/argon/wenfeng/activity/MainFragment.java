package com.argon.wenfeng.activity;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.argon.wenfeng.R;
import com.argon.wenfeng.data.GoodsItem;
import com.argon.wenfeng.data.GoodsItemManager;
import com.argon.wenfeng.data.GoodsItemManager.OnGoodsItemLoadListener;
import com.taobao.top.android.api.ApiError;
import com.umeng.fb.UMFeedbackService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.StaggeredGridView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link MainFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
@SuppressLint("ValidFragment")
public class MainFragment extends SherlockFragment {

	private StaggeredGridView mSGV;
	
	private GoodsItemAdapter mGoodsAdapter;
	
	private ProgressBar mProgress;
	
	private Context mContext;
	
	private MenuItem mRefreshItem;
	
	@SuppressLint("ValidFragment")
	public MainFragment(Activity activity) {
		// Required empty public constructor
		mContext = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_main, container, false);
		
		mGoodsAdapter = new GoodsItemAdapter(mContext, 
				R.id.imageView1, 
				GoodsItemManager.instance().getGoodsItems());
		
		
		//mAdapter = new SGVAdapter(this);
        mSGV = (StaggeredGridView) view.findViewById(R.id.grid);
        //mSGV.setColumnCount(-1);
        //mSGV.setAdapter(mAdapter);
        mSGV.setAdapter(mGoodsAdapter);
        //mSGV.setAdapter(new EndlessGoodsItemAdapter(this, mGoodsAdapter, R.id.textView1));
        mSGV.setItemMargin(10);
        mGoodsAdapter.notifyDataSetChanged();
        
        mProgress = (ProgressBar) view.findViewById(R.id.progress);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(GoodsItemManager.instance().getGoodsItems().size() == 0) {
			refreshGoodsItems();
		}
		
	}
	
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main, menu);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.refresh:
            mRefreshItem = item;
            refresh();
            return true;
        case R.id.action_feedback:
        	UMFeedbackService.openUmengFeedbackSDK(mContext);
            return true;
        case R.id.action_about:
        	Intent intent = new Intent();
        	intent.setClass(mContext, AboutActivity.class);  
        	mContext.startActivity(intent);  
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
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);

        Animation rotation = AnimationUtils.loadAnimation(mContext, R.anim.clockwise_refresh);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);

        mSGV.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);
        
        mRefreshItem.setActionView(iv);
        refreshGoodsItems();
        
    }
	
	public void onLoadFinished() {
    	if(mLoadSuccess) {
	    	mGoodsAdapter = new GoodsItemAdapter(mContext, 
					R.id.imageView1, 
					GoodsItemManager.instance().getGoodsItems());
	    	mSGV.setAdapter(mGoodsAdapter);
	    	
    	} else {
    		Toast.makeText(mContext, "Network error!", Toast.LENGTH_SHORT).show();
    	}

        if (mRefreshItem != null && mRefreshItem.getActionView() != null) {
        	mRefreshItem.getActionView().clearAnimation();
        	mRefreshItem.setActionView(null);
        }
        mSGV.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }
    
	Handler mHandler = new Handler();

	protected boolean mLoadSuccess = true;
	
	private void refreshGoodsItems() {
		
		mProgress.setVisibility(View.VISIBLE);
        
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
	    ConnectivityManager connec = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
