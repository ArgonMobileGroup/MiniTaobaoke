package com.argon.wenfeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.argon.wenfeng.R;
import com.fima.cardsui.views.CardUI;
import com.umeng.fb.UMFeedbackService;

public class AboutActivity extends SherlockActivity {

	private CardUI mCardView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		// Set up action bar.
        final ActionBar actionBar = getSupportActionBar();
        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // init CardView
     	mCardView = (CardUI) findViewById(R.id.cardsview);
     	mCardView.setSwipeable(false);
     	
    	// add AndroidViews Cards
     	ContentCard card1 = new ContentCard(getResources().getString(R.string.title_card1),getResources().getString(R.string.description_card1),0);
     	ContentCard card2 = new ContentCard(getResources().getString(R.string.title_card2),getResources().getString(R.string.description_card2),0);
     	ContentCard card3 = new ContentCard(getResources().getString(R.string.title_card3),getResources().getString(R.string.description_card3),0);
     	mCardView.addCard(card1);
     	mCardView.addCard(card2);
     	mCardView.addCard(card3);
    	
    	// draw cards
    	mCardView.refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	@SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                	finish();
                }
                return true;
                
            case R.id.action_feedback:
            	UMFeedbackService.openUmengFeedbackSDK(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
