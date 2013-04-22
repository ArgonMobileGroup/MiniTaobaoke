package com.argon.wenfeng.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.argon.wenfeng.data.GoodsItemDetail.ItemImg;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;

public class GoodsItemManager {
	
	public interface OnGoodsItemLoadListener {

		void onComplete(ArrayList<GoodsItem> goodsItems);

		void onError(ApiError error);

		void onException(Exception e);

	}
	
	public interface OnGoodsItemDetailLoadListener {

		void onComplete(GoodsItemDetail goodsItemDetail);

		void onError(ApiError error);

		void onException(Exception e);

	}
	
	public interface OnSellercatsLoadListener {

		void onComplete(JSONArray array);

		void onError(ApiError error);

		void onException(Exception e);

	}

	private static final int UPDATE_STATUS_FINISHED = 0;
	private static final int UPDATE_STATUS_ONGOING = 1;

	private static GoodsItemManager mInstance;
	
	private TopAndroidClient mTopClient = TopAndroidClient.getAndroidClientByAppKey("21398750");
	
	private ArrayList<GoodsItem> mGoodsItems = new ArrayList<GoodsItem>();
	private int mCurrentPage = 1;
	
	private int mCurrentPosition = 0;
	
	private int mUpdateStatus = UPDATE_STATUS_FINISHED;
	
	private JSONArray mArray;
	
	public static GoodsItemManager instance() {
		if(mInstance == null) {
			mInstance = new GoodsItemManager();
		}
		return mInstance;
	}
	
	private GoodsItemManager() {
		
	}
	
	public void setCurrentPosition(int position) {
		mCurrentPosition = position;
	}
	
	public synchronized void refresh(OnGoodsItemLoadListener onGoodsItemLoadListener) {
		Log.d("SD_TRACE", "refresh");
		if(mUpdateStatus == UPDATE_STATUS_ONGOING) {
			synchronized(mGoodsItems) {
				try {
					mGoodsItems.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		mGoodsItems.clear();
		mCurrentPage = 1;
		loadGoodsItems(onGoodsItemLoadListener);
	}
	
	public synchronized void loadMore(OnGoodsItemLoadListener onGoodsItemLoadListener) {
		if(mUpdateStatus == UPDATE_STATUS_ONGOING) {
			synchronized(mGoodsItems) {
				try {
					mGoodsItems.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(mCurrentPage < 10 ) {
			mCurrentPage = mCurrentPage + 1;
			loadGoodsItems(onGoodsItemLoadListener);
		} else {
			// Stub should use the call back to notify the UI
		}
		
	}
	

	private void loadGoodsItems(final OnGoodsItemLoadListener onGoodsItemLoadListener) {
		Log.d("SD_TRACE", "loadGoodsItems");
		//mGoodsItems.clear();
		//mCurrentPage = 1;
		
		// Search for Bioliving
		TopParameters params = new TopParameters();
		params.setMethod("taobao.taobaoke.items.get");
		params.addParam("fields", "num_iid,nick,title,price,click_url,shop_click_url,pic_url,promotion_price");
		params.addParam("nick", "¶ÅÅôÏö");
		params.addParam("keyword", "°ÙÎäÎ÷");
		params.addParam("start_credit", "4crown");
		params.addParam("end_credit", "5goldencrown");
		params.addParam("sort", "commissionNum_desc");
		params.addParam("mall_item", "true");
		params.addParam("page_no", "" + mCurrentPage);
		params.addParam("page_size", "30");
		params.addParam("is_mobile", "true");
		
		// Search for Souline
//		TopParameters params = new TopParameters();
//		params.setMethod("taobao.taobaoke.items.get");
//		params.addParam("fields", "num_iid,nick,title,price,click_url,shop_click_url,pic_url,promotion_price");
//		params.addParam("keyword", "ËØÂÆSouline");
//		params.addParam("start_credit", "4crown");
//		params.addParam("end_credit", "5goldencrown");
//		params.addParam("mall_item", "false");
//		params.addParam("page_no", "" + mCurrentPage);
//		params.addParam("page_size", "30");
//		params.addParam("is_mobile", "true");
		
		// Search for Liebo
//		TopParameters params = new TopParameters();
//		params.setMethod("taobao.taobaoke.items.get");
//		params.addParam("fields", "num_iid,nick,title,price,click_url,shop_click_url,pic_url,promotion_price");
//		params.addParam("keyword", "ÁÑ²¯");
//		params.addParam("nick", "¶ÅÅôÏö");
//		params.addParam("start_credit", "3goldencrown");
//		params.addParam("end_credit", "5goldencrown");
//		params.addParam("mall_item", "true");
//		params.addParam("sort", "commissionVolume_desc");
//		params.addParam("page_no", "" + mCurrentPage);
//		params.addParam("page_size", "30");
//		params.addParam("is_mobile", "true");
		
		
		mTopClient.api(params, null, new TopApiListener() {

			@Override
			public void onComplete(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					JSONObject response = json.getJSONObject("taobaoke_items_get_response");
					JSONArray items = response.getJSONObject("taobaoke_items").getJSONArray("taobaoke_item");
					for (int i = 0; i < items.length(); i++) {
						JSONObject item = items.getJSONObject(i);
						GoodsItem goodsItem = new GoodsItem();
						
						String title = item.getString("title");
						goodsItem.setTitle(title);
						Log.d("SD_TRACE", "get item: " + title);
						
						String picUrl = item.getString("pic_url");
						goodsItem.setPicUrl(picUrl);
						Log.d("SD_TRACE", "get pic url: " + picUrl);
						
						String nick = item.getString("nick");
						goodsItem.setNick(nick);
						
						Long numId = item.getLong("num_iid");
						goodsItem.setNumberId(numId);
						Log.d("SD_TRACE", "get item id: " + numId);
						
						String promotionPrice = item.getString("promotion_price");
						goodsItem.setPromotionPrice(promotionPrice);
						
						String price = item.getString("price");
						goodsItem.setPrice(price);
						
						String clickUrl = item.getString("click_url");
						goodsItem.setClickUrl(clickUrl);
						
						mGoodsItems.add(goodsItem);
					}
					onGoodsItemLoadListener.onComplete(mGoodsItems);
					//mGoodsAdapter.notifyDataSetChanged();
					int totalResults = json.getJSONObject("taobaoke_items_get_response").getInt("total_results");
					Log.d("SD_TRACE", "get items count: " + items.length());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mUpdateStatus = UPDATE_STATUS_FINISHED;
				synchronized(mGoodsItems) {
					mGoodsItems.notify();
				}
			}

			@Override
			public void onError(ApiError error) {
				// TODO Auto-generated method stub
				Log.d("SD_TRACE", "load api error" + error.toString());
				onGoodsItemLoadListener.onError(error);
				mUpdateStatus = UPDATE_STATUS_FINISHED;
				synchronized(mGoodsItems) {
					mGoodsItems.notify();
				}
			}

			@Override
			public void onException(Exception e) {
				// TODO Auto-generated method stub
				Log.d("SD_TRACE", "load api exception" + e.toString());
				onGoodsItemLoadListener.onException(e);
				mUpdateStatus = UPDATE_STATUS_FINISHED;
				synchronized(mGoodsItems) {
					mGoodsItems.notify();
				}
			}
			
		}, true);
	}
	
	public synchronized void loadGoodsItemsDetail(Long numId, final OnGoodsItemDetailLoadListener onGoodsItemLoadListener) {
		TopParameters params = new TopParameters();
		params.setMethod("taobao.taobaoke.items.detail.get");
		params.addParam("fields", "num_iid, item_img.url, delist_time");
		params.addParam("is_mobile", "true");

		params.addParam("num_iids", numId.toString());
		
		mTopClient.api(params, null, new TopApiListener() {

			@Override
			public void onComplete(JSONObject json) {
				GoodsItemDetail goodsItemDetail = new GoodsItemDetail();
				try {
					JSONObject response = json.getJSONObject("taobaoke_items_detail_get_response");
					int totalResults = response.getInt("total_results");
					if(totalResults == 0) {
						goodsItemDetail.setBeenDelisted(true);
					} else {
						JSONArray items = response.getJSONObject("taobaoke_item_details").getJSONArray("taobaoke_item_detail");
						if(items.length() > 0) {
							goodsItemDetail.setBeenDelisted(false);
							
							JSONObject item = items.getJSONObject(0).getJSONObject("item");
							
							JSONArray itemImgs = item.getJSONObject("item_imgs").getJSONArray("item_img");
							for(int i = 0; i < itemImgs.length(); i++) {
								
								ItemImg itemImg = new ItemImg();
								itemImg.mImgUrl = itemImgs.getJSONObject(i).getString("url");
								//itemImg.mImgUrl = itemImgs.getJSONObject(i).getString("url");
								
								goodsItemDetail.getItemImgs().add(itemImg);
							}
							//goodsItemDetail.setDescription(item.getString("desc"));
							//goodsItemDetail.set
						} else {
							goodsItemDetail.setBeenDelisted(true);
						}
					}
					
					onGoodsItemLoadListener.onComplete(goodsItemDetail);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mUpdateStatus = UPDATE_STATUS_FINISHED;
				synchronized(mGoodsItems) {
					mGoodsItems.notify();
				}
			}

			@Override
			public void onError(ApiError error) {
				// TODO Auto-generated method stub
				Log.d("SD_TRACE", "load api error" + error.toString());
				mUpdateStatus = UPDATE_STATUS_FINISHED;
				synchronized(mGoodsItems) {
					mGoodsItems.notify();
				}
			}

			@Override
			public void onException(Exception e) {
				// TODO Auto-generated method stub
				Log.d("SD_TRACE", "load api exception" + e.toString());
				mUpdateStatus = UPDATE_STATUS_FINISHED;
				synchronized(mGoodsItems) {
					mGoodsItems.notify();
				}
			}
			
		}, true);
		
	}
	
	public synchronized ArrayList<GoodsItem> getGoodsItems() {
		return mGoodsItems;
	}
	
	public void loadSellercats(final OnSellercatsLoadListener onSellercatsLoadListener) {
		Log.d("SD_TRACE", "loadSellercats");
		//mGoodsItems.clear();
		//mCurrentPage = 1;
		
		// Search for Bioliving
		TopParameters params = new TopParameters();
		params.setMethod("taobao.sellercats.list.get");
		params.addParam("nick", "°ÙÎäÎ÷Æì½¢µê");
				
		mTopClient.api(params, null, new TopApiListener() {

			@Override
			public void onComplete(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					mArray = json.getJSONObject("sellercats_list_get_response").getJSONObject("seller_cats").getJSONArray("seller_cat");
				
					onSellercatsLoadListener.onComplete(mArray);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mUpdateStatus = UPDATE_STATUS_FINISHED;
				synchronized(mGoodsItems) {
					mGoodsItems.notify();
				}
			}

			@Override
			public void onError(ApiError error) {
				// TODO Auto-generated method stub
				Log.d("SD_TRACE", "load api error" + error.toString());
				onSellercatsLoadListener.onError(error);
				mUpdateStatus = UPDATE_STATUS_FINISHED;
				synchronized(mGoodsItems) {
					mGoodsItems.notify();
				}
			}

			@Override
			public void onException(Exception e) {
				// TODO Auto-generated method stub
				Log.d("SD_TRACE", "load api exception" + e.toString());
				onSellercatsLoadListener.onException(e);
				mUpdateStatus = UPDATE_STATUS_FINISHED;
				synchronized(mGoodsItems) {
					mGoodsItems.notify();
				}
			}
			
		}, true);
	}
}
