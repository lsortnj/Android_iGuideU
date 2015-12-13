package mo.iguideu.ui.upcoming;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wt.calendarcard.CalendarCardPager;
import com.wt.calendarcard.CardGridItem;
import com.wt.calendarcard.OnCellItemClick;

import mo.iguideu.R;
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
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityTravelerUpcoming extends ActionBarActivity  implements OnCellItemClick{

	private static final int FETCH_DATA_FINISHED = 0x500;
	
	private CalendarCardPager calendarPager  = null;
	private LinearLayout	  eventContianer = null;
	private ProgressBar		  progressbar	 = null;
	private TextView		  title			 = null;
	private List<TravelerUpcomingGuide> guides = new ArrayList<TravelerUpcomingGuide>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upcoming_guide);
		setTitle(getResources().getString(R.string.title_traveler_upcoming));
		
		calendarPager   = (CalendarCardPager) findViewById(R.id.activity_upcoming_calendar);
		eventContianer  = (LinearLayout) findViewById(R.id.activity_upcoming_event_container);
		progressbar     = (ProgressBar) findViewById(R.id.activity_upcoming_progressbar);
		title     		= (TextView) findViewById(R.id.activity_upcoming_txt_title);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		calendarPager.setOnCellItemClick(this);
		calendarPager.isBorwseOnly(true);
		
		title.setVisibility(View.GONE);
		
		refresh();
	}
	
	private void addGuideInfoToContainer(TravelerUpcomingGuide guide){
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		};
		
		String moreInfo = guide.getSessionStart().equals("")?
					getResources().getString(R.string.all_day_guide):
						guide.getSessionStart()+"~"+guide.getSessionEnd();
					
		View guideView = ViewFactory.getGuideSimpleInfoView(
				getApplicationContext(), guide, listener, moreInfo);
	    
		eventContianer.addView(guideView);
	}
	
	@Override
	public void onCellClick(CardGridItem item, boolean isSelect) {
		eventContianer.removeAllViews();
		//Show this date event
		for(TravelerUpcomingGuide guide : guides){
			if(guide.getTargetDate().equals(item.getDateString())){
				addGuideInfoToContainer(guide);
			}
		}
	}
	
	@Override
	public void onCellSelect(View v, CardGridItem item) {
		
	}

	@Override
	public void onCellUnselect(View v, CardGridItem item) {
		
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
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == ServerInfo.GET_TRAVELER_UPCOMING_GUIDE_DONE){
				restoreJSON((String) msg.obj);
			}if(msg.what == FETCH_DATA_FINISHED){
				progressbar.setVisibility(View.GONE);
				if(guides.size()==0){
					title.setVisibility(View.VISIBLE);
					title.setText(getResources().getString(R.string.title_traveler_no_guide_upcoming));
				}else
					title.setVisibility(View.GONE);
			}
		}
	};
	
	private void restoreJSON(final String jsonArrString){
		new Thread(new Runnable(){
			public void run(){
				if(jsonArrString!=null && jsonArrString.startsWith("[")){
					JSONArray jsonArr = JSONArray.fromObject(jsonArrString);
					for(int i=0; i<jsonArr.size(); i++){
						JSONObject jsonObj = jsonArr.getJSONObject(i);
						TravelerUpcomingGuide guide = new TravelerUpcomingGuide();
						guide.setId(jsonObj.getInt("guide_id"));
						guide.setGuiderId(jsonObj.getJSONObject("guide").getJSONObject("user").getInt("id"));
						guide.setGuiderName(jsonObj.getJSONObject("guide").getJSONObject("user").getString("name"));
						guide.setTargetDate(jsonObj.getString("target_date").split(" ")[0]);
						guide.setGuideName(jsonObj.getJSONObject("guide").getString("name"));
						guide.setCountryCode(jsonObj.getJSONObject("guide").getString("country_code"));
						guide.setCity(jsonObj.getJSONObject("guide").getString("city"));
						try{
							//Throws exception when all day guide
							guide.setSessionStart(jsonObj.getJSONObject("session").getString("start"));
							guide.setSessionEnd(jsonObj.getJSONObject("session").getString("end"));
						}catch(Exception e){}
						
						calendarPager.getSelectedDate().add(guide.getTargetDate());
						 
						guides.add(guide);
					}
					calendarPager.notifyDataChanged();
					handler.sendEmptyMessage(FETCH_DATA_FINISHED);
				}
			}
		}).start();
	}
	
	private void refresh(){
		progressbar.setVisibility(View.VISIBLE);
		
		eventContianer.removeAllViews();
		guides.clear();
		calendarPager.getSelectedDate().clear();
		
		ServerCore.instance().getTravelerUpcomingGuide(handler);
	}
}
