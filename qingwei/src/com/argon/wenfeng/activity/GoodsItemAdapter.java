package com.argon.wenfeng.activity;

import java.util.ArrayList;

import com.argon.wenfeng.R;
import com.argon.wenfeng.data.GoodsItem;
import com.argon.wenfeng.data.GoodsItemManager;
import com.argon.wenfeng.data.GoodsItemManager.OnGoodsItemLoadListener;
import com.argon.wenfeng.loader.ImageLoader;
import com.taobao.top.android.api.ApiError;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class GoodsItemAdapter extends ArrayAdapter<GoodsItem> {
	
	private ImageLoader mLoader;

	public GoodsItemAdapter(Context context, int viewResourceId,
			ArrayList<GoodsItem> goodsItems) {
		super(context, viewResourceId, goodsItems);
		mLoader = new ImageLoader(context);
		mLoader.setRequiredSize(128);
		
	}
	
	@Override
	public int getCount() {
		// Add the last pending view
		return GoodsItemManager.instance().getGoodsItems().size() + 1;
	}
	
	private Handler mHandler = new Handler();
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutParams lp = null;
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			
			if ( position % 3 == 0 && position != (getCount() - 1) ) {
				convertView = layoutInflator.inflate(R.layout.element_item_large, parent, false);
            } else if (position != (getCount() - 1)) {
            	convertView = layoutInflator.inflate(R.layout.element_item, parent, false);
            } else {
            	convertView = layoutInflator.inflate(R.layout.element_item_large, parent, false);
            }

			holder = new ViewHolder();
			holder.mPicture = (ImageView) convertView .findViewById(R.id.imageView1);
			holder.mTitle = (TextView) convertView.findViewById(R.id.textView1);
			holder.mNick = (TextView) convertView.findViewById(R.id.nick);
			holder.mPrice = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);

		}
		
		if ( position % 5 == 0 ) {
        	lp = new LayoutParams(convertView.getLayoutParams());
        	lp.span = 2;
    		convertView.setLayoutParams(lp);
    		//convertView .findViewById(R.id.imageView1).setBackgroundColor(Color.rgb(0x8a, 0xd5, 0xf0));
        } else {
        	lp = new LayoutParams(convertView.getLayoutParams());
        	lp.span = 1;
    		convertView.setLayoutParams(lp);
        }
		Log.d("SD_TRACE", "position: " + position);
		if( position == (getCount() - 1)) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.element_item_large, parent, false);
			lp = new LayoutParams(convertView.getLayoutParams());
        	lp.span = 2;
    		convertView.setLayoutParams(lp);
    		
    		holder = new ViewHolder();
			holder.mPicture = (ImageView) convertView .findViewById(R.id.imageView1);
			holder.mTitle = (TextView) convertView.findViewById(R.id.textView1);
			holder.mNick = (TextView) convertView.findViewById(R.id.nick);
			holder.mPrice = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
//    		holder = (ViewHolder) convertView.getTag();
//
//    		//mLoader.DisplayImage(getItem(position).getPicUrl(), holder.mPicture);
    		holder.mPicture.setVisibility(View.GONE);
//    		holder.mTitle.setText("I am a pending view");
//    		holder.mNick.setText("ignore me pls");
//    		holder.mPosition = position;
    		
    		GoodsItemManager.instance().loadMore(new OnGoodsItemLoadListener() {

				@Override
				public void onComplete(ArrayList<GoodsItem> mGoodsItems) {
					// TODO Auto-generated method stub
					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							notifyDataSetChanged();
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
    		
    		return convertView;
		} else {

            final int pos = position;
            
			convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ItemDetailActivity.class);
                    
                    intent.putExtra("SHOW_POSITION", ((ViewHolder) view.getTag()).mPosition);
                    getContext().startActivity(intent);
                }
            });
		}

		if(position < getCount()) {
			holder = (ViewHolder) convertView.getTag();
	
			holder.mPicture.setVisibility(View.VISIBLE);
			mLoader.displayImage(getItem(position).getPicUrl(), holder.mPicture);
			holder.mTitle.setText(Html.fromHtml(getItem(position).getTitle()));
			holder.mNick.setText(getItem(position).getNick());
			holder.mPrice.setText("гд" + getItem(position).getPromotionPrice());
			holder.mPosition = position;
		}
		return convertView;
	}
	
	static class ViewHolder {
		ImageView mPicture;
		TextView mTitle;
		TextView mNick;
		TextView mPrice;
		int mPosition;
	}

}
