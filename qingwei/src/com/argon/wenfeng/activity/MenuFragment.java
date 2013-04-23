package com.argon.wenfeng.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
public class MenuFragment extends Fragment {

	private Context mContext;
	
	private Handler mHandler = new Handler();
	
	private JSONArray mSellercats;
	private ActionBar mActionBar;
	
	private ArrayList<Long> mCids=new ArrayList<Long>();;
	private ArrayAdapter<String> mSellercatsListAdapter;
	private ArrayAdapter<String> mChildAdapter;
	private ArrayList<String> mSellercatsList=new ArrayList<String>();
	private ArrayList<String> mChildList=new ArrayList<String>();
	
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
		
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), 
				R.layout.list_item_1, R.id.text1, getResources().getStringArray(R.array.menu_items));
		ListView listView =(ListView)getActivity().findViewById(R.id.list);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				Fragment newContent = null;
				switch (position)
				{
				case 0:
					newContent = new MainFragment(getActivity());
					mActionBar.setDisplayShowTitleEnabled(true);
					mActionBar.setTitle(R.string.app_name);
					mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					break;
				case 1:
					UMFeedbackService.openUmengFeedbackSDK(mContext);
					break;
				case 2:
			        Intent intent = new Intent();
			        intent.setClass(getActivity(), AboutActivity.class);
			        intent.putExtra("index", position);
			        startActivity(intent);
					break;
				}
				if (newContent != null)
					switchFragment(newContent);
			}
		});
		
		mChildAdapter= new ArrayAdapter<String>(getActivity(), 
				R.layout.sherlock_spinner_dropdown_item, mChildList);
		
		ListView sellercatsListView =(ListView)getActivity().findViewById(R.id.list_sellercats);
		mSellercatsListAdapter = new ArrayAdapter<String>(getActivity(), 
				R.layout.list_item_1, R.id.text1, mSellercatsList);
		sellercatsListView.setAdapter(mSellercatsListAdapter);
		sellercatsListView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				Fragment newContent = null;
				
				newContent = new MainFragment(getActivity());
	    		mChildList.clear();
	    		mChildList.add(mSellercatsList.get(position));
	    		if (getChildList(mCids.get(position))) {
	    			//禁用标题，设置下拉菜单
	    			mActionBar.setDisplayShowTitleEnabled(false);
	                mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	        		mActionBar.setListNavigationCallbacks(mChildAdapter,
	                        new OnNavigationListener() {
	                            public boolean onNavigationItemSelected(int itemPosition,
	                                    long itemId) {
	                                // FIXME add proper implementation
	                                return false;
	                            }
	                        });
				}else {
					//启用标题，设置成对应分类
					mActionBar.setDisplayShowTitleEnabled(true);
					mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					mActionBar.setTitle(mSellercatsList.get(position));
				}
			
				
				if (newContent != null)
					switchFragment(newContent);
			}
		});
		
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
											mSellercatsList.add(array.getJSONObject(i).getString("name"));
											mCids.add(array.getJSONObject(i).getLong("cid"));
										}
								}
									mSellercatsListAdapter.notifyDataSetChanged();
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

	private Boolean getChildList(Long cid) {

		try {
			for (int i = 0; i < mSellercats.length(); i++) {
				if (mSellercats.getJSONObject(i).getLong("parent_cid")==cid) {
					mChildList.add(mSellercats.getJSONObject(i).getString("name"));
				}
		}
			if (mChildList.size()==1) {
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
