package mo.iguideu.guide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import mo.iguideu.serverHandler.ServerInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GuideEvent {
	
	public static final String AVAILABLE_TYPE_ANYTIME 			= "anytime";
	public static final String AVAILABLE_TYPE_EVERY_WEEK 		= "everyWeek";
	public static final String AVAILABLE_TYPE_EVERY_MONTH 		= "everyMonth";
	public static final String AVAILABLE_TYPE_SPECIFIC_DATETIME = "specificDatetime";
	public static final String AVAILABLE_TYPE_ASK_ME		 	= "askMe";
	
	public static final String IMAGE_QUALITY_ORIGIN		 	= "origin";
	public static final String IMAGE_QUALITY_MEDIUM		 	= "medium";
	public static final String IMAGE_QUALITY_SMALL		 	= "small";
	
	public static final int	MAX_PHOTO_COUNT = 5;
	public static final int	MAX_PEOPLE		= 5;
	
	private int  			id						= -1;
	private String 			guideName 				= "";
	private String			schedule				= "";
	private String			countryCode				= "";
	private String			city					= "";
	private List<String> 	supportLanguages 		= new ArrayList<String>();
	private List<String> 	offerTransportationType = new ArrayList<String>();
	private List<String> 	availableDateArray      = new ArrayList<String>();
	private List<GuideSession> 	guideSessions       = new ArrayList<GuideSession>();
	private boolean 		isAllDayGuide			= false;
	private int				maxPeople				= MAX_PEOPLE;
	private int				currentPeople			= 0;
	private double			fee						= 0;
	private int				joinCount				= 0;
	private int				guiderId				= 0;
	private String			guiderName				= "";
	private String			guiderPhotoFilename		= "";
	private MeetLocation 	meetLocation 			= new MeetLocation();
	private int				duration				= 0;//Unit min
	private double			distance				= 0l;
	
	
	private String 				availableType		= AVAILABLE_TYPE_ANYTIME;
	private AvailableDatetime 	availableDatetime 	= new AvailableDatetime();
	private List<GuidePhoto>	guidePhotos			= new ArrayList<GuidePhoto>();
	private List<String>		photos				= new ArrayList<String>();
	
	private int reviewCount = 0;
	
	public GuideEvent(String guideName, String availableType){
		setGuideName(guideName);
		setAvailableType(availableType);
	}
	
	public GuideEvent(){}
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getGuiderPhotoFilename() {
		return guiderPhotoFilename;
	}

	public void setGuiderPhotoFilename(String guiderPhotoFilename) {
		this.guiderPhotoFilename = guiderPhotoFilename;
	}

	public String getGuiderName() {
		return guiderName;
	}

	public void setGuiderName(String guiderName) {
		this.guiderName = guiderName;
	}

	public List<GuideSession> getGuideSessions() {
		return guideSessions;
	}

	public void setGuideSessions(List<GuideSession> guideSessions) {
		this.guideSessions = guideSessions;
	}

	public boolean isAllDayGuide() {
		return isAllDayGuide;
	}

	public void setAllDayGuide(boolean isAllDayGuide) {
		this.isAllDayGuide = isAllDayGuide;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getGuiderId() {
		return guiderId;
	}

	public void setGuiderId(int guiderId) {
		this.guiderId = guiderId;
	} 

	public int getReviewCount() {
		return reviewCount;
	}
 
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public int getDuration() {
		return duration;
	}

	public List<String> getAvailableDateArray() {
		return availableDateArray;
	}

	public void setAvailableDateArray(List<String> availableDateArray) {
		this.availableDateArray = availableDateArray;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public int getJoinCount() {
		return joinCount;
	}

	public void setJoinCount(int joinCount) {
		this.joinCount = joinCount;
	}

	public void addPhoto(List<String> photos){
		photos.addAll(photos);
	}
	
	public void addPhoto(String filePath){
		photos.add(filePath);
	}
	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public int getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(int maxPeople) {
		this.maxPeople = maxPeople;
	}

	public void addGuidePhoto(GuidePhoto guidePhoto){
		guidePhotos.add(guidePhoto);
	}
	public void addSupportLanguage(String language){
		supportLanguages.add(language);
	}
	public List<String> getSupportLanguage() {
		return supportLanguages;
	}

	public void setSupportLanguage(List<String> supportLanguage) {
		this.supportLanguages = supportLanguage;
	}

	public List<GuidePhoto> getGuidePhotos() {
		return guidePhotos;
	}

	public void setGuidePhotos(List<GuidePhoto> guidePhotos) {
		this.guidePhotos = guidePhotos;
	}

	public String getGuideName() {
		return guideName;
	}
	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public void addOfferTransportationType(String transportationType){
		offerTransportationType.add(transportationType);
	}
	
	public List<String> getOfferTransportationType() {
		return offerTransportationType;
	}

	public void setOfferTransportationType(
			List<String> availableTransportationType) {
		this.offerTransportationType = availableTransportationType;
	}
	
	public MeetLocation getMeetLocation() {
		return meetLocation;
	}
	public void setMeetLocation(MeetLocation meetLocation) {
		this.meetLocation = meetLocation;
	}
	public String getAvailableType() {
		return availableType;
	}
	public void setAvailableType(String availableType) {
		this.availableType = availableType;
	}
	public AvailableDatetime getAvailableDatetime() {
		return availableDatetime;
	}
	public void setAvailableDatetime(AvailableDatetime availableDatetime) {
		this.availableDatetime = availableDatetime;
	}

	public int getCurrentPeople() {
		return currentPeople;
	}

	public void setCurrentPeople(int currentPeople) {
		this.currentPeople = currentPeople;
	}
	
	public String getGuideSessionJSONArrayString(){
		JSONArray jsonArr = new JSONArray();
		for(GuideSession guideSession : getGuideSessions()){
			JSONObject jsonObj = new JSONObject();
			jsonObj.put(GuideSession.JSON_KEY_START_TIME, guideSession.getStartTime());
			jsonObj.put(GuideSession.JSON_KEY_END_TIME, guideSession.getEndTime());
			jsonArr.add(jsonObj);
		}
		return jsonArr.toString();
	}
	
	public void restoreFromJSONObject(JSONObject jsonObj){
	
		if(jsonObj!=null){
			try{
				SimpleDateFormat datetimeFomat 	= new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
				
				setId(jsonObj.getInt("id"));
				setGuideName(jsonObj.getString("name"));
				setCountryCode(jsonObj.getString("country_code"));
				setCity(jsonObj.getString("city"));
				
				getMeetLocation().setAddress(jsonObj.getString("meet_address"));
				getMeetLocation().setLocationName(jsonObj.getString("meet_location_name"));
				getMeetLocation().setCountryName(jsonObj.getString("country_name"));
				getMeetLocation().setCountryCode(jsonObj.getString("country_code"));
				getMeetLocation().setCity(jsonObj.getString("city"));
				getMeetLocation().setLatLng(
					new LatLng( jsonObj.getDouble("meet_lat"),
								jsonObj.getDouble("meet_lng")));
				
				setFee(jsonObj.getDouble("fee"));
				setMaxPeople(jsonObj.getInt("max_people"));
				setSchedule(jsonObj.getString("schedule"));
				setReviewCount(jsonObj.getInt("review_count"));
				setGuiderId(jsonObj.getInt("user_id"));
				setDuration(jsonObj.getInt("duration"));
				setAllDayGuide(jsonObj.getBoolean("is_all_day_guide"));
				
				
				String offerTrnas = jsonObj.getString("offer_transportation");
				getOfferTransportationType().clear();
				if(offerTrnas!=null && offerTrnas.startsWith("[")){
					Object[] transArr = JSONArray.fromObject(offerTrnas).toArray();
					for(Object trans : transArr){
						addOfferTransportationType((String)trans);
					}
				}
				
				String supportLang = jsonObj.getString("support_langs");
				getSupportLanguage().clear();
				if(supportLang!=null && supportLang.startsWith("[")){
					Object[] langsArr = JSONArray.fromObject(supportLang).toArray();
					for(Object lang : langsArr){
						addSupportLanguage((String)lang);
					}
				} 
				
				String available_dates = jsonObj.getString("available_date_array");
				getAvailableDateArray().clear();
				if(available_dates!=null && available_dates.startsWith("[")){
					Object[] dateArr = JSONArray.fromObject(available_dates).toArray();
					for(Object date : dateArr){
						getAvailableDateArray().add((String)date);
					}
				} 
				
				getPhotos().clear();
				if(jsonObj.getString("photo1")!=null && !jsonObj.getString("photo1").contains("{\"url\":null"))
					addPhoto(getPhotoUrlFromFilename(jsonObj.getString("photo1"),getId()));
				if(jsonObj.getString("photo2")!=null && !jsonObj.getString("photo2").contains("{\"url\":null"))
					addPhoto(getPhotoUrlFromFilename(jsonObj.getString("photo2"),getId()));
				if(jsonObj.getString("photo3")!=null && !jsonObj.getString("photo3").contains("{\"url\":null"))
					addPhoto(getPhotoUrlFromFilename(jsonObj.getString("photo3"),getId()));
				if(jsonObj.getString("photo4")!=null && !jsonObj.getString("photo4").contains("{\"url\":null"))
					addPhoto(getPhotoUrlFromFilename(jsonObj.getString("photo4"),getId()));
				
				setAvailableType(jsonObj.getString("available_type"));
				getAvailableDatetime().setDayOfMonth(jsonObj.getInt("available_monthday"));
				getAvailableDatetime().setDayOfWeek(jsonObj.getInt("available_weekday"));
				getAvailableDatetime().setStartDateTime(datetimeFomat.parse(jsonObj.getString("available_start_datetime")));
				getAvailableDatetime().setEndDateTime(datetimeFomat.parse(jsonObj.getString("available_end_datetime")));
				getAvailableDatetime().setSpecificDate(datetimeFomat.parse(jsonObj.getString("available_specific_datetime")));
				
				String guideSessions = jsonObj.getString("guide_session_json");
				getGuideSessions().clear();
				JSONObject guideSessionJsonObj = null;
				GuideSession guideSession = null;
				if(guideSessions!=null && guideSessions.startsWith("[")){
					JSONArray jsonArr = JSONArray.fromObject(guideSessions);
					for(int i=0; i<jsonArr.size(); i++){
						guideSessionJsonObj = JSONObject.fromObject(jsonArr.get(i));
						guideSession = new GuideSession();
						guideSession.setStartTime(guideSessionJsonObj.get(GuideSession.JSON_KEY_START_TIME).toString());
						guideSession.setEndTime(guideSessionJsonObj.get(GuideSession.JSON_KEY_END_TIME).toString());
						getGuideSessions().add(guideSession);
					}
				} 
				
				setDistance(jsonObj.getDouble("distance"));
			}catch(Exception e){
				Log.e("GuideEvent", "RestoreFromJSON:"+e.toString());
			}
		}
	}
	
	
	public static String getGuideCoverURL(String imageQuality, int guideID){
		//get_guide_cover/:guide_id(/:version)
		String fullFetchUrl = ServerInfo.HOST+ServerInfo.C_GUIDE+ServerInfo.A_FETCH_GUIDE_COVER+guideID;
			
		if(imageQuality != null && 
				(imageQuality.equals(IMAGE_QUALITY_MEDIUM) || 
					imageQuality.equals(IMAGE_QUALITY_ORIGIN) || 
						imageQuality.equals(IMAGE_QUALITY_SMALL))){
			fullFetchUrl += "/"+imageQuality;
		}
		
		return fullFetchUrl;
	}
	
	public static String getPhotoUrlFromFilename(String filenameOnServer, int guideID){
		String fullFetchUrl = "";
				
		if(filenameOnServer!=null && !filenameOnServer.equals("") && !filenameOnServer.startsWith("http"))
			fullFetchUrl = ServerInfo.HOST+ServerInfo.C_GUIDE+ServerInfo.A_FETCH_PHOTO+
						"?"+ServerInfo.P_FILE_NAME+"="+filenameOnServer+"&"+ServerInfo.P_ID+"="+guideID;
		
		return fullFetchUrl;
	}
	
}
