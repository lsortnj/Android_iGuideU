package mo.iguideu.ui.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.koushikdutta.ion.Ion;

import net.sf.json.JSONObject;
import mo.iguideu.R;
import mo.iguideu.guide.TranspotationType;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.ui.joinGuide.ActivityJoinGuide;
import mo.iguideu.user.User;
import mo.iguideu.user.UserManager;
import mo.iguideu.util.FetchImageUtil;
import mo.iguideu.util.GoogleMapsUtil;
import mo.iguideu.util.GuideUtil;
import mo.iguideu.util.MoAnimationUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityGuideDetail extends ActionBarActivity {

	private ImageView imgViewStaticMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_detail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(GuideUtil.getTargetGuide()!=null){
			imgViewStaticMap = (ImageView) findViewById(R.id.activiry_guide_detail_img_static_map);
			
			TextView tvSchedule = (TextView) findViewById(R.id.activiry_guide_detail_txt_schedule);
			TextView tvFee 	  	= (TextView) findViewById(R.id.partial_guide_info_txt_fee);
			TextView tvPeople 	= (TextView) findViewById(R.id.partial_guide_info_txt_people);
			ImageView btnCalendar = (ImageView) findViewById(R.id.partial_guide_info_img_calendar);
			TextView tvGuideType = (TextView) findViewById(R.id.partial_guide_info_txt_guide_type);
			TextView tvGuideTypeLabel = (TextView) findViewById(R.id.partial_guide_info_label_guide_type);
			Button   btnJoin   	= (Button) findViewById(R.id.activiry_guide_detail_btn_join);
			
			tvGuideTypeLabel.setText(GuideUtil.getTargetGuide().isAllDayGuide()?
					getResources().getString(R.string.label_all_day_guide):
						getResources().getString(R.string.label_guide_by_session));
			
			tvGuideType.setText(GuideUtil.getTargetGuide().isAllDayGuide()?
					getDurationText():
						getResources().getString(R.string.label_how_many_guide_session,
								GuideUtil.getTargetGuide().getGuideSessions().size()));
			
			ViewPager 	 photoViewPager 	  = (ViewPager) findViewById(R.id.activiry_guide_detail_pager_photos);
			LinearLayout transScrollContainer = (LinearLayout) findViewById(R.id.partial_guide_info_layout_trans_scroll_container);
			LinearLayout langScrollContainer  = (LinearLayout) findViewById(R.id.partial_guide_info_layout_lang_scroll_container);
			
			PhotoPagerAdapter pagerAdapter = new PhotoPagerAdapter(GuideUtil.getTargetGuide().getPhotos());
			photoViewPager.setAdapter(pagerAdapter);
			
			for(String lang : GuideUtil.getTargetGuide().getSupportLanguage()){
				langScrollContainer.addView(getLanguageIcon(lang));
			}
			
			for(String trans : GuideUtil.getTargetGuide().getOfferTransportationType()){
				transScrollContainer.addView(getTransportationIcon(trans));
			}
			
			//Guide text info 
			tvPeople.setText(""+GuideUtil.getTargetGuide().getMaxPeople());
			tvFee.setText(GuideUtil.getTargetGuide().getFee()==0?
							getResources().getString(R.string.free):
								""+GuideUtil.getTargetGuide().getFee());
			
			
			btnCalendar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showAvailableCalendar();
				}
			});
			
			imgViewStaticMap.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putDouble("lat", GuideUtil.getTargetGuide().getMeetLocation().getLatLng().latitude);
					bundle.putDouble("longi", GuideUtil.getTargetGuide().getMeetLocation().getLatLng().longitude);
					bundle.putString("guideName", GuideUtil.getTargetGuide().getGuideName());
					
					Intent intent = new Intent();
					intent.setClass(ActivityGuideDetail.this, ActivityMapView.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			
			tvSchedule.setText(GuideUtil.getTargetGuide().getSchedule());
			
			btnJoin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), ActivityJoinGuide.class);
					startActivity(intent);
				}
			});
			
			getActionBar().setTitle(GuideUtil.getTargetGuide().getGuideName());
			
			fetchGuiderInfo();
			
			//Can't join self guide
			if(UserManager.getUser().getId()==GuideUtil.getTargetGuide().getGuiderId())
				btnJoin.setVisibility(View.GONE);
		}
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
	
	private void showAvailableCalendar(){
		
	}
	
	private String getDurationText(){
		int durationMinuts = GuideUtil.getTargetGuide().getDuration();
		int hour = 0;
		int min  = 0;
		
		while(durationMinuts>=60){
			hour++;
			durationMinuts -= 60;
		}
		
		min = durationMinuts;
		
		String durationStr = hour>0
					?hour+" "+(hour==1?getResources().getString(R.string.hour):getResources().getString(R.string.hours))+" "+
					 min+getResources().getString(R.string.minutes)
					:min+getResources().getString(R.string.minutes);
		
		return durationStr;
	}
	
	private Handler guiderInfoHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case ServerInfo.GET_GUIDER_INFO_DONE:
				resotreGuiderInfo((String)msg.obj);
				break;
			}
		}
	};
	
	private void resotreGuiderInfo(String guiderJSON){
		if(guiderJSON!=null && guiderJSON.startsWith("{")){
			JSONObject guiderObj = JSONObject.fromObject(guiderJSON);
			CircleImageView guiderPhoto  = (CircleImageView) findViewById(R.id.guider_info_img_photo);
			TextView guiderName   		 = (TextView) findViewById(R.id.guider_info_txt_name);
			TextView guiderIntro   		 = (TextView) findViewById(R.id.guider_info_txt_intro);
			TextView guiderIntroMore   	 = (TextView) findViewById(R.id.guider_info_txt_more);
			
			guiderIntroMore.setVisibility(View.GONE);
			
			guiderName.setText(guiderObj.getString("name"));
			
			String guiderIntroStr = guiderObj.getString("intro");
			int    guiderId       = Integer.parseInt(guiderObj.getString("id"));
			
			if(guiderIntroStr.length() > 60){
				guiderIntroStr = guiderIntroStr.substring(0, 59)+"......";
				guiderIntroMore.setVisibility(View.VISIBLE);
			}
			
			guiderIntro.setText(guiderIntroStr);
			
			FetchImageUtil task = new FetchImageUtil(guiderPhoto);
			task.execute(User.getUserPhotoURL(guiderId, User.PHOTO_SIZE_SMALL));
		}
	}
	
	private void fetchGuiderInfo(){
		ServerCore.instance().getGuiderSimpleInfo(GuideUtil.getTargetGuide().getGuiderId(), guiderInfoHandler);
	}
	
	private ImageView getLanguageIcon(String language){
		ImageView languageIcon = new ImageView(this);
		Locale locale = new Locale(language.split("_")[0], language.split("_")[1]);
		languageIcon.setTag(locale);
		languageIcon.setImageResource(R.drawable.default_language_icon);
		languageIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), 
						((Locale)v.getTag()).getDisplayLanguage()+"-"+((Locale)v.getTag()).getDisplayCountry(), 
						Toast.LENGTH_SHORT).show();
			}
		});
		return languageIcon;
	}
	
	private ImageView getTransportationIcon(String transType){
		ImageView transIcon = new ImageView(this);
		
		int resourceId = R.drawable.trans_else;
		
		if(transType.equals(TranspotationType.TYPE_BIKE)){
			resourceId = R.drawable.trans_bike;
		}else if(transType.equals(TranspotationType.TYPE_BOAT)){
			resourceId = R.drawable.trans_boat;
		}else if(transType.equals(TranspotationType.TYPE_BUS)){
			resourceId = R.drawable.trans_bus;
		}else if(transType.equals(TranspotationType.TYPE_CAR)){
			resourceId = R.drawable.trans_car;
		}else if(transType.equals(TranspotationType.TYPE_ELSE)){
			resourceId = R.drawable.trans_else;
		}else if(transType.equals(TranspotationType.TYPE_MOTO)){
			resourceId = R.drawable.trans_moto;
		}else if(transType.equals(TranspotationType.TYPE_PLANE)){
			resourceId = R.drawable.trans_plane;
		}else if(transType.equals(TranspotationType.TYPE_TAXI)){
			resourceId = R.drawable.trans_taxi;
		}else if(transType.equals(TranspotationType.TYPE_TRAIN)){
			resourceId = R.drawable.trans_train;
		}
		
		transIcon.setImageResource(resourceId);
		
		return transIcon;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		
		imgViewStaticMap.getLayoutParams().height = imgViewStaticMap.getRootView().getHeight()/5;
		
		String staticMapUrl = GoogleMapsUtil.getStaticMapUrl(
				GuideUtil.getTargetGuide().getMeetLocation().getLatLng().latitude, 
				GuideUtil.getTargetGuide().getMeetLocation().getLatLng().longitude, 
				17, imgViewStaticMap.getRootView().getWidth(),imgViewStaticMap.getRootView().getHeight()/5
				);
		
		try{
	    	Ion.with(imgViewStaticMap)
	        .placeholder(R.drawable.placeholder)
	        .error(R.drawable.error)
	        .animateIn(MoAnimationUtil.getFadeInAnimation(500))
	        .load(staticMapUrl);
	    }catch(Exception e){}
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	class PhotoPagerAdapter extends PagerAdapter{

		private List<String> photos = new ArrayList<String>();
		private List<ImageView> imgPhotos = new ArrayList<ImageView>();
		
		public PhotoPagerAdapter(List<String> photoPaths){
			photos = photoPaths;
			
			startFetchImageBackground();
		}
		
		private void startFetchImageBackground(){
			for(String url : photos){
				ImageView photoImg = new ImageView(ActivityGuideDetail.this); 
		        photoImg.setScaleType(ScaleType.CENTER_CROP);
		        photoImg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		        photoImg.setImageBitmap(null);
		        photoImg.setTag(url);	
		       
		        try{
			    	Ion.with(photoImg)
			        .placeholder(R.drawable.placeholder)
			        .error(R.drawable.error)
			        .animateIn(MoAnimationUtil.getFadeInAnimation(500))
			        .load(url);
			    }catch(Exception e){}
		        
			    imgPhotos.add(photoImg);
			}
		}
		
		@Override  
	    public Object instantiateItem (ViewGroup container, int position) {  
			Log.e("position", ""+position);
			View v = imgPhotos.get(position);
			
			if(v!=null)
				container.addView(v);  
	        
	        return v;  
	    }  
		
		@Override
		public int getCount() {
			return photos.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}
		
		@Override  
	    public void destroyItem (ViewGroup container, int position, Object object) {  
	        container.removeView((View)object);  
	    }  
		
	}
}
