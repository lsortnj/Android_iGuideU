package mo.iguideu.guide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;
import net.sf.json.JSONObject;

public class GuideSession {
	
	public static final String JSON_KEY_START_TIME = "start";
	public static final String JSON_KEY_END_TIME   = "end";
	
	private SimpleDateFormat format = new SimpleDateFormat ("HH:mm",Locale.getDefault());
	private String startTime = "";
	private String endTime = "";
	
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public void setStartTime(Date date){
		setStartTime(format.format(date));
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public void setEndTime(Date date) {
		setEndTime(format.format(date));
	}
	public Calendar getStartTimeCalendar(){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(format.parse(startTime));
		}catch(Exception e){
			Log.e("getStartTimeHour", e.toString());
		}
		return cal;
	}
	public Calendar getEndTimeCalendar(){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(format.parse(endTime));
		}catch(Exception e){
			Log.e("getStartTimeHour", e.toString());
		}
		return cal;
	}
	public String toJSONObjectString(){
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(JSON_KEY_START_TIME, getStartTime());
		jsonObj.put(JSON_KEY_END_TIME, getEndTime());
		
		return jsonObj.toString();
	}
}
