package mo.iguideu.ui.rating;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import mo.iguideu.R;
import mo.iguideu.guide.Guest;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityRatingTraveler extends ActionBarActivity {

	private List<GuideToRateTraveler> watingRateGuides = new ArrayList<GuideToRateTraveler>();
	private LinearLayout layoutContainer = null;
	private TextView	 hintTextNoMore  = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating_traveler);
		setTitle(getResources().getString(R.string.title_rating_traveler));
		
		layoutContainer = (LinearLayout) findViewById(R.id.activity_rating_traveler_container);
		hintTextNoMore  = (TextView) findViewById(R.id.activity_rating_traveler_txt_no_traveler_to_rate);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		refresh();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
	    case android.R.id.home:
	    	finish();
	        return true;
	    }
		return super.onOptionsItemSelected(item);
	}
	
	private void addGuideViewToList(){
		for(GuideToRateTraveler guide : watingRateGuides){
			View guideView = LayoutInflater.from(this).inflate(R.layout.item_waiting_rate_traveler_by_guide, null, false);
			TextView guideName   = (TextView) guideView.findViewById(R.id.item_rate_teaveler_by_guide_txt_guide_name);
			TextView targetDate  = (TextView) guideView.findViewById(R.id.item_rate_teaveler_by_guide_txt_date);
			TextView guestCount  = (TextView) guideView.findViewById(R.id.item_rate_teaveler_by_guide_txt_guest_count);
			
			guideName.setText(guide.getGuideName());
			targetDate.setText(guide.getTatgetDate());
			guestCount.setText(""+guide.getGuestCount());
			
		    guideView.setTag(guide);
		    guideView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DialogRateTravelerList dialog = new DialogRateTravelerList(ActivityRatingTraveler.this, (GuideToRateTraveler)v.getTag());
					dialog.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							refresh();
						}
					});
					dialog.show();
				}
			});
		    
			layoutContainer.addView(guideView);
		}
	}
	
	private void restoreGuideInfo(String jsonArrayString){
		if(jsonArrayString!=null && jsonArrayString.startsWith("[")){
			JSONArray jsonArr = JSONArray.fromObject(jsonArrayString);
			watingRateGuides.clear();
			layoutContainer.removeAllViews();
			for(int i=0; i<jsonArr.size(); i++){
				//It's Guide's Simple Info
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				
				GuideToRateTraveler guide = new GuideToRateTraveler();
				guide.setId(jsonObj.getInt("guide_id"));
				guide.setGuideName(jsonObj.getString("guide_name"));
				guide.setTatgetDate(jsonObj.getString("target_date").split(" ")[0]);
				guide.setGuestCount(jsonObj.getInt("guest_count"));
				
				String guestJSON = jsonObj.getString("guests");
				
				if(guestJSON!=null && guestJSON.startsWith("[")){
					//Guests info
					JSONArray guestsArr = JSONArray.fromObject(jsonObj.getString("guests"));
					for(int mo=0; mo<guestsArr.size(); mo++){
						JSONObject guestObj = guestsArr.getJSONObject(mo);
						Guest guest = new Guest();
						guest.setId(guestObj.getInt("id"));
						guest.setName(guestObj.getString("name"));
						guide.getGuests().add(guest);
					}
				}

				watingRateGuides.add(guide);
			}
			addGuideViewToList();
			
			hintTextNoMore.setVisibility(layoutContainer.getChildCount()==0?View.VISIBLE:View.GONE);
		}
	}
	
	private Handler ratingHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case ServerInfo.GET_GUIDE_TO_RATE_TRAVELER_DONE:
				restoreGuideInfo((String)msg.obj);
				break;
			}
		}
	};
	
	private void refresh(){
		ServerCore.instance().getGuideToRateTraveler(ratingHandler);
	}
}

