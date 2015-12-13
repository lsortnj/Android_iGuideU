package mo.iguideu.ui.base;

import com.koushikdutta.ion.Ion;

import mo.iguideu.R;
import mo.iguideu.guide.Guest;
import mo.iguideu.guide.GuideEvent;
import mo.iguideu.ui.upcoming.GuiderUpcomingGuide;
import mo.iguideu.user.User;
import mo.iguideu.util.FetchImageUtil;
import mo.iguideu.util.MoAnimationUtil;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * 用來產生一些常用的View
 * @author lsortnj
 *
 */
public class ViewFactory {

	public static View getGuestView(
			Context context, 
			Guest guest, 
			String moreInfo,
			boolean isCheckbox,
			OnClickListener clickListener, 
			OnCheckedChangeListener changeListener){
		
		View guestInfoView = LayoutInflater.from(context).inflate(R.layout.item_wait_rating_traveler, null, false);
		
		TextView  guestName  = (TextView) guestInfoView.findViewById(R.id.list_item_rating_traveler_txt_traveler_name);
		TextView  country    = (TextView) guestInfoView.findViewById(R.id.list_item_rating_traveler_txt_country);
		ImageView photo		 = (ImageView)guestInfoView.findViewById(R.id.list_item_rating_traveler_img_profile_img);
		CheckBox  checkcox   = (CheckBox)guestInfoView.findViewById(R.id.list_item_rating_traveler_checkbox);
		
		country.setText(moreInfo);
		checkcox.setVisibility(View.GONE);
		
		if(isCheckbox){
			checkcox.setVisibility(View.VISIBLE);
			if(changeListener!=null)
				checkcox.setOnCheckedChangeListener(changeListener);
			checkcox.setTag(guest.getId());
		}
		
		guestInfoView.setTag(guest.getId());
		
		guestName.setText(guest.getName());
		
		FetchImageUtil task = new FetchImageUtil(photo);
		task.execute(User.getUserPhotoURL(guest.getId(), User.PHOTO_SIZE_SMALL));
		
		
		if(clickListener!=null)
			guestInfoView.setOnClickListener(clickListener);
		
		return guestInfoView;
	}
	
	public static View getGuideSimpleEnrollStateView(Context context, GuiderUpcomingGuide guide, OnClickListener listener){
		View guideView = LayoutInflater.from(context).inflate(R.layout.item_guide_info_simple, null, false);
		TextView guideName   = (TextView) guideView.findViewById(R.id.item_guide_info_simple_txt_guide_name);
		TextView guiderName  = (TextView) guideView.findViewById(R.id.item_guide_info_simple_txt_guider_name);
		TextView moreInfo    = (TextView) guideView.findViewById(R.id.item_guide_info_simple_txt_more);
		ImageView guiderIcon = (ImageView) guideView.findViewById(R.id.item_guide_info_simple_img_guider);
		ImageView guidePhoto = (ImageView) guideView.findViewById(R.id.item_guide_info_simple_img_guide_cover);
		
		guiderIcon.setImageResource(R.drawable.clock);
		
		guideName.setText(guide.getGuideName());
		
		String guideTime = guide.isAllDayGuide()
								?context.getResources().getString(R.string.all_day_guide)
										:guide.getSessionStart()+"~"+guide.getSessionEnd();
		
		guiderName.setText(guideTime);
		moreInfo.setText(context.getResources().getString(R.string.guests)+":"+guide.getCurrentPeople());
		
		try{
	    	Ion.with(guidePhoto)
	        .placeholder(R.drawable.placeholder)
	        .error(R.drawable.error)
	        .animateIn(MoAnimationUtil.getFadeInAnimation(500))
	        .load(GuideEvent.getGuideCoverURL(GuideEvent.IMAGE_QUALITY_SMALL, guide.getId()));
	    }catch(Exception e){Log.e("FetchGuidePhoto",e.toString());}
		
//		FetchImageUtil task = new FetchImageUtil(guidePhoto);
//		task.execute(User.getUserPhotoURL(guide.getGuiderId(), User.PHOTO_SIZE_SMALL));
//	    guideView.setTag(guide);
	    
	    if(listener != null)
	    	guideView.setOnClickListener(listener);
	    
	    return guideView;
	}
	
	public static View getGuideSimpleInfoView(Context context, GuideEvent guide, OnClickListener listener, String moreInfoString){
		
		View guideView = LayoutInflater.from(context).inflate(R.layout.item_guide_info_simple, null, false);
		TextView guideName   = (TextView) guideView.findViewById(R.id.item_guide_info_simple_txt_guide_name);
		TextView guiderName  = (TextView) guideView.findViewById(R.id.item_guide_info_simple_txt_guider_name);
		TextView moreInfo    = (TextView) guideView.findViewById(R.id.item_guide_info_simple_txt_more);
		ImageView guiderIcon = (ImageView) guideView.findViewById(R.id.item_guide_info_simple_img_guider);
		ImageView guidePhoto = (ImageView) guideView.findViewById(R.id.item_guide_info_simple_img_guide_cover);
		
		guideName.setText(guide.getGuideName());
		guiderName.setText(guide.getGuiderName());
		moreInfo.setText(moreInfoString);
		
		try{
	    	Ion.with(guidePhoto)
	        .placeholder(R.drawable.placeholder)
	        .error(R.drawable.error)
	        .animateIn(MoAnimationUtil.getFadeInAnimation(500))
	        .load(GuideEvent.getGuideCoverURL(GuideEvent.IMAGE_QUALITY_SMALL,guide.getId()));
	    	
	    	FetchImageUtil task = new FetchImageUtil(guiderIcon);
			task.execute(User.getUserPhotoURL(guide.getGuiderId(), User.PHOTO_SIZE_SMALL));
	    }catch(Exception e){}
		
	    guideView.setTag(guide);
	    if(listener != null)
	    	guideView.setOnClickListener(listener);
	    
	    return guideView;
	}
}
