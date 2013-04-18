package com.argon.wenfeng.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.argon.wenfeng.R;
import com.fima.cardsui.objects.Card;

public class ContentCard extends Card {

	public ContentCard(String title,String desc,int image){
		super(title,desc,image);
	}

	@Override
	public View getCardContent(Context context) {
		View view= LayoutInflater.from(context).inflate(R.layout.card_ex, null);
		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.description)).setText(desc);
		return view;
	}

}
