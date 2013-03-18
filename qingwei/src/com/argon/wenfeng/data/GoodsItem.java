package com.argon.wenfeng.data;

import com.argon.wenfeng.data.GoodsItemDetail.GoodsLocation;

public class GoodsItem {

	private Long mNumberId; // Id of the goods
	private String mTitle;  // Title of the goods, 60 bytes long
	
	private String mNick; // Nick of the seller
	
	private String mDescription; // Description of the goods
	private String mPicUrl; // Main picture url of the goods
	
	private GoodsLocation mLocation; // Location of the goods
	
	private String mPrice; // Price of the goods
	private String mPromotionPrice; // PromotionPrice price of the goods
	
	private String mClickUrl;
	private String mShopClickUrl;
	
	public Long getNumberId() {
		return mNumberId;
	}
	public void setNumberId(Long numberId) {
		this.mNumberId = numberId;
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getNick() {
		return mNick;
	}
	public void setNick(String mNick) {
		this.mNick = mNick;
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
	public String getPromotionPrice() {
		return mPromotionPrice;
	}
	public void setPromotionPrice(String currentPrice) {
		this.mPromotionPrice = currentPrice;
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
	public String getPrice() {
		return mPrice;
	}
	public void setPrice(String mPrice) {
		this.mPrice = mPrice;
	}
	public boolean isPromotion() {
		if(mPrice != null && mPromotionPrice != null && this.mPrice.equals(mPromotionPrice)) {
			return false;
		} else {
			return true;
		}
	}
	

}
