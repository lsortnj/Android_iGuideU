package mo.iguideu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mo.iguideu.R;
import mo.iguideu.guide.GuideEvent;
import mo.iguideu.guide.GuideSession;
import mo.iguideu.user.User;
import android.app.Activity;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;

public class CalendarUtil {

	private static Activity activity = null;
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd",Locale.getDefault());
	
	
	public static void construct(Activity aActivity){
		activity = aActivity;
	}
	
	public static void addJoinGuideEventToCalendar(
			GuideEvent guide, 
			User guider,
			String targetDate, 
			GuideSession session){
		try{
			Calendar beginTime = Calendar.getInstance();
			Calendar endTime = Calendar.getInstance();
			Date date = dateFormat.parse(targetDate);
			beginTime.setTime(date);
			endTime.setTime(date);
			
			if(guide.isAllDayGuide()){
				beginTime.set(Calendar.HOUR_OF_DAY, 8);
				beginTime.set(Calendar.MINUTE, 0);
				
				endTime.set(Calendar.HOUR_OF_DAY, 20);
				endTime.set(Calendar.MINUTE, 0);
			}else{
				beginTime.set(Calendar.HOUR_OF_DAY, session.getStartTimeCalendar().get(Calendar.HOUR_OF_DAY));
				beginTime.set(Calendar.MINUTE, session.getStartTimeCalendar().get(Calendar.MINUTE));
				
				endTime.set(Calendar.HOUR_OF_DAY, session.getEndTimeCalendar().get(Calendar.HOUR_OF_DAY));
				endTime.set(Calendar.MINUTE, session.getEndTimeCalendar().get(Calendar.MINUTE));
			}
			
			Intent intent = new Intent(Intent.ACTION_INSERT)
			        .setData(CalendarContract.Events.CONTENT_URI)
			        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
			        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
			        .putExtra(Events.TITLE, activity.getResources().getString(R.string.title_calendar_event)+guide.getGuideName())
			        .putExtra(Events.DESCRIPTION, activity.getResources().getString(R.string.content_calendar_event,guider.getName(), guide.getMeetLocation().getLocationName()))
			        .putExtra(Events.EVENT_LOCATION, guide.getMeetLocation().getLocationName())
			        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
			        .putExtra(Events.ALL_DAY, guide.isAllDayGuide())
			        .putExtra(Intent.ACTION_CALL, guider.getPhone1());
			activity.startActivity(intent);
		}catch(Exception e){
			Log.e("addJoinGuideEventToCalendar", e.toString());
		}
	}
	
}
