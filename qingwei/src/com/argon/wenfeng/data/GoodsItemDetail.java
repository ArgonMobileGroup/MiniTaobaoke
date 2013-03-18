package com.argon.wenfeng.data;

import java.util.ArrayList;

public class GoodsItemDetail {
	
	public GoodsItemDetail(Long numberId, String title, String nick,
			String description, String picUrl, GoodsLocation location,
			String currentPrice, ArrayList<ItemImg> itemImgs) {
		this.mNumberId = numberId;
		this.mTitle = title;
		this.mNick = nick;
		this.mDescription = description;
		this.mPicUrl = picUrl;
		this.mLocation = location;
		this.mCurrentPrice = currentPrice;
		this.mItemImgs = itemImgs;
	}

	public GoodsItemDetail() {
	}

	private Long mNumberId; // Id of the goods
	private String mTitle;  // Title of the goods, 60 bytes long
	private String mClickUrl;
	private String mShopClickUrl;
	
	private String mNick; // Nick of the seller
	
	private String mDescription; // Description of the goods
	private String mPicUrl; // Main picture url of the goods
	
	private GoodsLocation mLocation; // Location of the goods
	
	private String mCurrentPrice; // Current price of the goods
	
	private String mDelistTime; // Delist Time of the goods
	
	private boolean mBeenDelisted = false;
	
	public final class GoodsLocation {
		public String mCity;
		public String mState;
	}
	
	// Pictures list of the goods, include the main picture
	private ArrayList<ItemImg> mItemImgs = new ArrayList<ItemImg>(); 
	
	

	public Long getmNumberId() {
		return mNumberId;
	}

	public void setNumberId(Long numberId) {
		this.mNumberId = numberId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getClickUrl() {
		return mClickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.mClickUrl = clickUrl;
	}

	public String getShopClickUrl() {
		return mShopClickUrl;
	}

	public void setShopClickUrl(String shopClickUrl) {
		this.mShopClickUrl = shopClickUrl;
	}

	public String getNick() {
		return mNick;
	}

	public void setNick(String nick) {
		this.mNick = nick;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getPicUrl() {
		return mPicUrl;
	}

	public void setPicUrl(String picUrl) {
		this.mPicUrl = picUrl;
	}

	public GoodsLocation getLocation() {
		return mLocation;
	}

	public void setLocation(GoodsLocation location) {
		this.mLocation = location;
	}

	public String getCurrentPrice() {
		return mCurrentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.mCurrentPrice = currentPrice;
	}

	public ArrayList<ItemImg> getItemImgs() {
		return mItemImgs;
	}

	public void setItemImgs(ArrayList<ItemImg> itemImgs) {
		this.mItemImgs = itemImgs;
	}
	
	public String getDelistTime() {
		return mDelistTime;
	}

	public void setDelistTime(String delistTime) {
		this.mDelistTime = delistTime;
	}

	public boolean isBeenDelisted() {
		return mBeenDelisted;
	}

	public void setBeenDelisted(boolean beenDelisted) {
		this.mBeenDelisted = beenDelisted;
	}

	public static class ItemImg {
		public Long mNumberId;
		public String mImgUrl;
		public int mPosition;
	}
	
}


