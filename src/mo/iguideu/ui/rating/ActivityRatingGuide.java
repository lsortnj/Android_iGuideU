package mo.iguideu.ui.rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import mo.iguideu.R;
import mo.iguideu.guide.GuideEvent;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.ui.base.ViewFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityRatingGuide extends ActionBarActivity {

	private List<GuideEvent> watingRateGuides = new ArrayList<GuideEvent>();
	private LinearLayout layoutContainer = null;
	private TextView	 hintTextNoMore  = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating_guide);
		setTitle(getResources().getString(R.string.title_rating_guide));
		
		layoutContainer = (LinearLayout) findViewById(R.id.activity_rating_guide_container);
		hintTextNoMore  = (TextView) findViewById(R.id.activity_rating_guide_txt_no_guide_to_rate);
		
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
	
	private Handler ratingGuideHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case ServerInfo.GET_GUIDE_CAN_RATE_DONE:
				restoreGuideInfo((String)msg.obj);
				break;
			}
		}
	};
	
	private void restoreGuideInfo(String jsonArrayString){
		if(jsonArrayString!=null && jsonArrayString.startsWith("[")){
			Locale locale = null;
			JSONArray jsonArr = JSONArray.fromObject(jsonArrayString);
			watingRateGuides.clear();
			layoutContainer.removeAllViews();
			for(int i=0; i<jsonArr.size(); i++){
				//It's Guide's Simple Info
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				
			    GuideEvent guide = new GuideEvent();
				guide.setId(jsonObj.getInt("id"));
				guide.setGuiderId(jsonObj.getInt("guider_id"));
				guide.setGuiderName(jsonObj.getString("guider_name"));
				guide.setGuideName(jsonObj.getString("name"));
				guide.setGuiderPhotoFilename(jsonObj.getString("guider_photo_filename"));
				guide.getMeetLocation().setCountryCode(jsonObj.getString("country_code"));
				guide.getMeetLocation().setCity(jsonObj.getString("city"));
				guide.getPhotos().add(jsonObj.getString("photo1"));
				
				locale = new Locale("",guide.getMeetLocation().getCountryCode());
				guide.getMeetLocation().setCountryName(locale.getDisplayCountry());
				
				watingRateGuides.add(guide);
			}
			addGuideViewToList();
			
			hintTextNoMore.setVisibility(layoutContainer.getChildCount()==0?View.VISIBLE:View.GONE);
		}
	}
	
	private void addGuideViewToList(){
		for(GuideEvent guide : watingRateGuides){
			OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					new DialogRateGuide(ActivityRatingGuide.this, (GuideEvent)v.getTag()).show();
				}
			};
			
			View guideView = ViewFactory.getGuideSimpleInfoView(
					getApplicationContext(), guide, listener,
					guide.getMeetLocation().getCountryName()+","+guide.getMeetLocation().getCity());
		    
			layoutContainer.addView(guideView);
		}
	}
	
	
	private void refresh(){
		ServerCore.instance().getGuideCanRate(ratingGuideHandler);
	}
}
