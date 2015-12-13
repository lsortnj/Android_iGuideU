package mo.iguideu.guide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AvailableDatetime {
	
	private int 	dayOfWeek 		= Calendar.SUNDAY;
	private int 	dayOfMonth 		= 1;
	private Date 	startDateTime 	= new Date();
	private Date 	endDateTime 	= new Date();
	private Date	specificDate	= new Date();
	private boolean isAllDay		= false;
	
	private SimpleDateFormat datetimeFomat 	= new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat dateFomat 		= new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFomat 		= new SimpleDateFormat("HH:mm");
	
	
	public AvailableDatetime(){
	}
	
	public AvailableDatetime(Date specificDate, Date startDatetime, Date endDatetime){
		this.specificDate 	= specificDate;
		this.startDateTime 	= startDatetime;
		this.endDateTime 	= endDatetime;
	}
	
	public void setAllDayAvailable(Date targetDate){
		isAllDay(true);
		setSpecificDate(targetDate);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(targetDate);
		
		//Start from target data 00:00
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		setStartDateTime(cal.getTime());
		
		//End at target data 23:59
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		setEndDateTime(cal.getTime());
	}
	
	public void setEveryWeekDay(int dayOfWeek, Date startDatetime, Date endDatetime){
		setDayOfWeek(dayOfWeek);
		setStartDateTime(startDatetime);
		setEndDateTime(endDatetime);
		isAllDay(false);
	}
	
	public void setEveryMonthDay(int dayOfMonth, Date startDatetime, Date endDatetime){
		setDayOfMonth(dayOfMonth);
		setStartDateTime(startDatetime);
		setEndDateTime(endDatetime);
		isAllDay(false);
	}
	
	
	
	/***
	 * yyyy-MM-dd HH:mm
	 * @return
	 */
	public String getStartDatetimeString(){
		return getStartDateTime()!=null?datetimeFomat.format(getStartDateTime()):"not set";
	}
	
	/***
	 * yyyy-MM-dd
	 * @return
	 */
	public String getStartDateString(){
		return getStartDateTime()!=null?dateFomat.format(getStartDateTime()):"not set";
	}
	
	/***
	 * HH:mm
	 * @return
	 */
	public String getStartTimeString(){
		return getStartDateTime()!=null?timeFomat.format(getStartDateTime()):"not set";
	}
	
	/***
	 * yyyy-MM-dd HH:mm
	 * @return
	 */
	public String getEndDatetimeString(){
		return getEndDateTime()!=null?datetimeFomat.format(getEndDateTime()):"not set";
	}
	
	/***
	 * yyyy-MM-dd
	 * @return
	 */
	public String getEndDateString(){
		return getEndDateTime()!=null?dateFomat.format(getEndDateTime()):"not set";
	}
	
	/***
	 * HH:mm
	 * @return
	 */
	public String getEndTimeString(){
		return getEndDateTime()!=null?timeFomat.format(getEndDateTime()):"not set";
	}
	
	/***
	 * yyyy-MM-dd HH:mm
	 * @return
	 */
	public String getSpecificDateString(){
		return getSpecificDate()!=null?datetimeFomat.format(getSpecificDate()):"not set";
	}
	
	public Date getSpecificDate() {
		return specificDate;
	}

	public void setSpecificDate(Date specificDate) {
		this.specificDate = specificDate;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public Date getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Date getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}
	public boolean isAllDay() {
		return isAllDay;
	}
	public void isAllDay(boolean isAllDay) {
		this.isAllDay = isAllDay;
	}
	
	
	
	
}
