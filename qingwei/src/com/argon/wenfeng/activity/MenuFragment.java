package com.argon.wenfeng.activity;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.argon.wenfeng.R;
import com.argon.wenfeng.data.GoodsItemManager;
import com.argon.wenfeng.data.GoodsItemManager.OnSellercatsLoadListener;
import com.taobao.top.android.api.ApiError;
import com.umeng.fb.UMFeedbackService;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
@SuppressLint("ValidFragment")
public class MenuFragment extends ListFragment {

	private Context mContext;
	
	private Handler mHandler = new Handler();
	
	private JSONArray mSellercats;
	private int mLength=3;
	private ActionBar mActionBar;
	
	private ArrayList<Long> mCids=new ArrayList<Long>();;
	ArrayAdapter<String> listAdapter;
	ArrayAdapter<String> childAdapter;
	ArrayList<String> menuList=new ArrayList<String>();
	ArrayList<String> childList=new ArrayList<String>();
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	@SuppressLint("ValidFragment")
	public MenuFragment(Context context) {
		mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Collections.addAll(menuList, getResources().getStringArray(R.array.menu_items)); 
		listAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, menuList);
		setListAdapter(listAdapter);
		
		childAdapter= new ArrayAdapter<String>(getActivity(), 
				R.layout.sherlock_spinner_dropdown_item, childList);
		
		mActionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
		GoodsItemManager.instance().loadSellercats(new OnSellercatsLoadListener(){
				@Override
				public void onComplete(final JSONArray array) {
					// TODO Auto-generated method stub
					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							mSellercats=array;

							try {
									for (int i = 0; i < array.length(); i++) {
										if (array.getJSONObject(i).getLong("parent_cid")==0) {
											menuList.add(array.getJSONObject(i).getString("name"));
											mCids.add(array.getJSONObject(i).getLong("cid"));
										}
								}
									menuList.add("¹ØÓÚ");
									menuList.add("·´À¡");
									mLength=menuList.size();
									listAdapter.notifyDataSetChanged();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
					}, 500);
				}

				@Override
				public void onError(ApiError error) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onException(Exception e) {
					// TODO Auto-generated method stub
					
				}
    			
    		});
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		if (position==0) {
			newContent = new MainFragment(getActivity());
			mActionBar.setDisplayShowTitleEnabled(true);
			mActionBar.setTitle(R.string.app_name);
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}else if (position==mLength-1) {
			UMFeedbackService.openUmengFeedbackSDK(mContext);
		}else if (position==mLength-2) {
	        Intent intent = new Intent();
	        intent.setClass(getActivity(), AboutActivity.class);
	        intent.putExtra("index", position);
	        startActivity(intent);
		}else {
			newContent = new MainFragment(getActivity());
    		childList.clear();
    		childList.add(menuList.get(position));
    		if (getChildList(mCids.get(position-1))) {
    			mActionBar.setDisplayShowTitleEnabled(false);
                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        		mActionBar.setListNavigationCallbacks(childAdapter,
                        new OnNavigationListener() {
                            public boolean onNavigationItemSelected(int itemPosition,
                                    long itemId) {
                                // FIXME add proper implementation
                                return false;
                            }
                        });
			}else {
				mActionBar.setDisplayShowTitleEnabled(true);
				mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				mActionBar.setTitle(menuList.get(position));
			}
//            getChildList(mCids.get(position-1));

		}
		if (newContent != null)
			switchFragment(newContent);
	}
	private Boolean getChildList(Long cid) {

		try {
			for (int i = 0; i < mSellercats.length(); i++) {
				if (mSellercats.getJSONObject(i).getLong("parent_cid")==cid) {
					childList.add(mSellercats.getJSONObject(i).getString("name"));
				}
		}
			if (childList.size()==1) {
				return false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity) {
			MainActivity mainAcitivity = (MainActivity) getActivity();
			mainAcitivity.switchContent(fragment);
		}	
	}
}
