package com.argon.wenfeng.activity;

import java.util.ArrayList;

import com.argon.wenfeng.R;
import com.argon.wenfeng.data.GoodsItem;
import com.argon.wenfeng.data.GoodsItemDetail.ItemImg;
import com.argon.wenfeng.data.GoodsItemManager;
import com.argon.wenfeng.data.GoodsItemManager.OnGoodsItemLoadListener;
import com.argon.wenfeng.loader.ImageLoader;
import com.taobao.top.android.api.ApiError;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.StaggeredGridView.LayoutParams;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemImageAdapter extends ArrayAdapter<ItemImg> {
	
	private ImageLoader mLoader;

	public ItemImageAdapter(Context context, int viewResourceId,
			ArrayList<ItemImg> itemImgs, ImageLoader imageLoader) {
		super(context, viewResourceId, itemImgs);
		mLoader = imageLoader;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			
			convertView = layoutInflator.inflate(R.layout.item_img, parent, false);


			holder = new ViewHolder();
			holder.mPicture = (ImageView) convertView .findViewById(R.id.imageView1);
			convertView.setTag(holder);
            
			convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
		}

		holder = (ViewHolder) convertView.getTag();

		//holder.mPicture.setVisibility(View.VISIBLE);
		mLoader.displayImage(getItem(position).mImgUrl, holder.mPicture);
		return convertView;
	}
	
	static class ViewHolder {
		ImageView mPicture;
	}

}
