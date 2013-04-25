package com.argon.wenfeng.data;

public class Sellercat {

	private long mCid; // Id of the Sellercat
	private String mName;
	private long mParentCid;
	
	public long getCid() {
		return mCid;
	}
	public void setCid(long cid) {
		this.mCid = cid;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public long getParentCid() {
		return mParentCid;
	}
	public void setParentCid(long parentCid) {
		this.mParentCid = parentCid;
	}
	public boolean isParent() {
		if(mParentCid == 0L) {
			return true;
		} else {
			return false;
		}
	}
}
