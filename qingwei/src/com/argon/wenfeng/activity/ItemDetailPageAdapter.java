package com.argon.wenfeng.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.argon.wenfeng.R;
import com.argon.wenfeng.data.GoodsItem;
import com.argon.wenfeng.data.GoodsItemDetail;
import com.argon.wenfeng.data.GoodsItemDetail.ItemImg;
import com.argon.wenfeng.data.GoodsItemManager;
import com.argon.wenfeng.data.GoodsItemManager.OnGoodsItemDetailLoadListener;
import com.argon.wenfeng.loader.DetailImageLoader;
import com.argon.wenfeng.loader.ImageLoader;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.taobao.top.android.api.ApiError;
import com.umeng.analytics.MobclickAgent;

public class ItemDetailPageAdapter extends FragmentStatePagerAdapter {

	private final DetailImageLoader mLoader;
	private final ImageLoader mItemImageLoader;
	private final Context mContext;
	
	private Tracker mGaTracker;
	private GoogleAnalytics mGaInstance;
	
	public ItemDetailPageAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
		
		mGaInstance = GoogleAnalytics.getInstance(mContext);
        mGaTracker = mGaInstance.getTracker("UA-39513550-1");
		
		mLoader = new DetailImageLoader(context);
		mItemImageLoader = new ImageLoader(context);
	}

	@Override
	public SherlockFragment getItem(int postition) {
		SherlockFragment fragment = new ItemDetailPageFragment();
        Bundle args = new Bundle();
//        args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1); // Our object is just an integer :-P
        args.putInt("ITEM_POSITION", postition);
        fragment.setArguments(args);
        return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return GoodsItemManager.instance().getGoodsItems().size();
	}
	
	@SuppressLint("ValidFragment")
	public class ItemDetailPageFragment extends SherlockFragment {
		
		private int mPosition;
		private Context mContext;
		
		private ItemImageAdapter mItemImageAdapter;
		
		private ArrayList<ItemImg> mItemImgs = new ArrayList<ItemImg>();
		
		private Handler mHandler = new Handler();
		private ListView mItemImgsView;
		private TabHost mTabHost;
		private TextView mNick;
		private WebView mDetailView;
		private TextView mDetailTextView;
		private TextView mDelist;
		
		@Override
	    public void onAttach (Activity activity) {
			super.onAttach(activity);
	    	mContext = activity;
	    }
		
		/**
	     * When creating, retrieve this instance's number from its arguments.
	     */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mPosition = getArguments() != null ? getArguments().getInt("ITEM_POSITION") : 1;
	        GoodsItem item = GoodsItemManager.instance().getGoodsItems().get(mPosition);
	        Long numId = item.getNumberId();
	        
	        setHasOptionsMenu(true);
	    }

		
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.item_detail_page_fragment, container, false);
            Bundle args = getArguments();
            int position = mPosition;
            final GoodsItem item = GoodsItemManager.instance().getGoodsItems().get(position);
            Log.d("SD_TRACE", "num_id: " + item.getNumberId());
            Log.d("SD_TRACE", "title: " + item.getTitle());
            Log.d("SD_TRACE", "nick: " + item.getNick());
            
//            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                    Integer.toString(args.getInt(ARG_OBJECT)));
            mLoader.DisplayImage(item.getPicUrl() + "_400x400.jpg", (ImageView)rootView.findViewById(R.id.imageView1));
            TextView title = (TextView) rootView.findViewById(R.id.title);
            title.setText(Html.fromHtml(item.getTitle()));
            
            mNick = (TextView) rootView.findViewById(R.id.nick);
            mNick.setText(item.getNick());

            mDelist = (TextView) rootView.findViewById(R.id.delist);
            
            TextView promotionPrice = (TextView) rootView.findViewById(R.id.promotion_price);
            promotionPrice.setText("гд" + item.getPromotionPrice());

        	TextView price = (TextView) rootView.findViewById(R.id.price);
            if(item.isPromotion()) {
                price.setText("гд" + item.getPrice());
                //price.getPaint().setFlags(TextPaint.UNDERLINE_TEXT_FLAG);
                price.setPaintFlags(Paint. STRIKE_THRU_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);
                price.setVisibility(View.VISIBLE);
            } else {
            	price.setVisibility(View.INVISIBLE);
            }
            
            mItemImgsView = (ListView) rootView.findViewById(R.id.item_imgs);
            mItemImageAdapter = new ItemImageAdapter(mContext, R.layout.item_img, mItemImgs, mItemImageLoader);
            mItemImgsView.setAdapter(mItemImageAdapter);
            mItemImgsView.setTextFilterEnabled(true);
            mItemImgsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mItemImgsView.setItemChecked(0, true);
            
            mItemImgsView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long id) {
					mItemImgsView.setItemChecked(position, true);
					
					mLoader.DisplayImage(mItemImageAdapter.getItem(position).mImgUrl + "_400x400.jpg", 
							(ImageView)rootView.findViewById(R.id.imageView1));
				}
            	
            });
            
            updateItemImgs(item.getNumberId());
            
            Button buyButton = (Button) rootView.findViewById(R.id.buy_button);
            buyButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					HashMap<String, String> purchase= new HashMap<String, String>();
					purchase.put("price", item.getPromotionPrice());
					MobclickAgent.onEvent(mContext, "purchase", purchase);
					
					mGaTracker.sendEvent("ui_action", "button_press", "buy_button", Long.getLong(item.getPromotionPrice(), 0));
					Uri clickUri = Uri.parse(item.getClickUrl());
					mContext.startActivity(new Intent(Intent.ACTION_VIEW, clickUri));
				}
            	
            });
            
            //mDetailView = (WebView) rootView.findViewById(R.id.wv);
           // mDetailTextView = (TextView) rootView.findViewById(R.id.desc);
//            mTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
//            mTabHost.setup();
//            
//            mTabHost.addTab(mTabHost.newTabSpec("tab1")
//                    .setIndicator("tab1")
//                    .setContent(R.id.tabOneContentView));
//            mTabHost.addTab(mTabHost.newTabSpec("tab2")
//                    .setIndicator("tab2")
//                    .setContent(R.id.tabTwoContentView));
            return rootView;
        }

		private ShareActionProvider mShareActionProvider; 

  @Override 
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { 
	    MenuItem menuItem = menu.findItem(R.id.share); 
	    // Get the provider and hold onto it to set/change the share intent. 
	    mShareActionProvider = (ShareActionProvider) menuItem.getActionProvider(); 
	    GoodsItem item = GoodsItemManager.instance().getGoodsItems().get(mPosition); 

	    Intent shareIntent = new Intent(Intent.ACTION_SEND); 
	    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject)); 
	    shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(item.getTitle())+"\n"+item.getClickUrl()); 
	    shareIntent.setType("text/plain"); 
	    mShareActionProvider.setShareIntent(shareIntent);   
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

		
		private void updateItemImgs(Long numId) {
			
			GoodsItemManager.instance().loadGoodsItemsDetail(numId, new OnGoodsItemDetailLoadListener() {

				@Override
				public void onComplete(GoodsItemDetail goodsItemDetail) {
					final GoodsItemDetail item = goodsItemDetail;
					mItemImgs = item.getItemImgs();
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if(item.isBeenDelisted() == true) {
								mDelist.setVisibility(View.VISIBLE);
							} else {
								mItemImageAdapter = new ItemImageAdapter(mContext, R.layout.item_img, mItemImgs, mItemImageLoader);
								mItemImgsView.setAdapter(mItemImageAdapter);
								mItemImageAdapter.notifyDataSetChanged();
								mItemImgsView.setItemChecked(0, true);
								
								//mNick.setText(item.getNick());
								//mDetailTextView.setText(Html.fromHtml(item.getDescription()));
								//mDetailView.loadData(item.getDescription(), "text/html", "utf-8");
								Log.d("SD_TRACE", "notify data set changed !!!! " + mItemImgs.size());
								mDelist.setVisibility(View.INVISIBLE);
							}
						}
					});
				}

				@Override
				public void onError(ApiError error) {
					// TODO Auto-generated method stub
					loadError(error);
				}

				private void loadError(ApiError error) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mContext, "Network error!", Toast.LENGTH_SHORT).show();
						}
					});
				}

				@Override
				public void onException(Exception e) {
					loadException(e);
				}

				private void loadException(Exception e) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mContext, "Network error!", Toast.LENGTH_SHORT).show();
						}
					});
				}
	        	
	        });
		}

		protected void loadFinished() {
			// TODO Auto-generated method stub
			
		}
	}

}
