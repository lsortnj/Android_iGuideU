package mo.iguideu.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mo.iguideu.serverHandler.ServerInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.json.JSONException;

import android.util.Log;

import com.facebook.model.GraphUser;
import com.google.gson.JsonObject;

public class User {

	public static final String PHOTO_SIZE_ORIGIN = "origin";
	public static final String PHOTO_SIZE_MEDIUM = "medium";
	public static final String PHOTO_SIZE_SMALL  = "samll";
	
	//if FB use facebook id
	private String username		= "";
	private String email 		= "";
	private String name  		= "";
	private String firstName    = "";
	private String middleName	= "";
	private String lastName		= "";
	private String birthday 	= "";
	private String authFrom 	= "";
	private String country		= "";
	private String locale		= "";
	private String location 	= "";
	private String fbLink		= "";
	private String timezone		= "";
	private String city			= "";
	private boolean gender		= true;//male=>true, female=>false
	private double latitude 	= -1l;
	private double longitude 	= -1l;
	private Date   registerDate = null;
	private Date   latestUse	= null;
	private int	   id			= -1;
	
	private String photoUrl		= "";
	private String intro		= "";
	
	
	//Guider data
	private boolean isInitGuiderProfile = false;
	private boolean isGotLicense 		= false;
	private double guiderRanking = 0;
	private String	licenseCode	 		= "";
	private int		guiderExpYear 		= 0;
	private List<String> supportLanguages = new ArrayList<String>();
	private List<String> offerTransportationType = new ArrayList<String>();
	
	//Traveler data
	private boolean isInitTravelerProfile = false;
	private String	travelerIntro  = "";
	//JOSN {"09xxxxxxxx","09xxxxxxxx"}
	private String  travelerPhone  = "";
	private double travelerRanking = 0;
	
	//Phone number is JSON, {"phone1":"09xxxxxxx","phone2":"09xxxxxxxx"}
	private String phone		= "";
	
	public User(){}
	
	public User(GraphUser fbUser){
		if(fbUser != null){
			setName(fbUser.getName());
			setBirthday(fbUser.getBirthday());
			setFirstName(fbUser.getFirstName());
			setLastName(fbUser.getLastName());
			setFbLink(fbUser.getLink());
			setMiddleName(fbUser.getMiddleName());
			setUsername(fbUser.getId());
			
			try {
				if(fbUser.getInnerJSONObject().get("locale")!=null)
					setTimezone(fbUser.getInnerJSONObject().getString("timezone"));
				if(fbUser.getInnerJSONObject().get("locale")!=null)
					setGender(fbUser.getInnerJSONObject().getString("gender").equals("male")?true:false);
				if(fbUser.getInnerJSONObject().get("locale")!=null)
					setLocale((String)fbUser.getInnerJSONObject().get("locale"));
				if(fbUser.getInnerJSONObject().get("location")!=null){
					setCity(fbUser.getLocation().getCity());
					setCountry(fbUser.getLocation().getCountry());
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean isInitTravelerProfile() {
		return isInitTravelerProfile;
	}

	public void isInitTravelerProfile(boolean isInitTravelerProfile) {
		this.isInitTravelerProfile = isInitTravelerProfile;
	}

	public String getTravelerIntro() {
		return travelerIntro;
	}

	public void setTravelerIntro(String travelerIntro) {
		this.travelerIntro = travelerIntro;
	}

	public String getTravelerPhone() {
		return travelerPhone;
	}

	public void setTravelerPhone(String travelerPhone) {
		this.travelerPhone = travelerPhone;
	}

	public double getGuiderRanking() {
		return guiderRanking;
	}

	public void setGuiderRanking(double guiderRanking) {
		this.guiderRanking = guiderRanking;
	}

	public double getTravelerRanking() {
		return travelerRanking;
	}

	public void setTravelerRanking(double travelerRanking) {
		this.travelerRanking = travelerRanking;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getPhoneNumberJSON() {
		return phone;
	}

	public void setPhoneNumberJSON(String phone) {
		this.phone = phone;
	}
	
	public String getPhone1(){
		return JSONObject.fromObject(phone).getString("phone1");
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

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean getGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAuthFrom() {
		return authFrom;
	}
	public void setAuthFrom(String authFrom) {
		this.authFrom = authFrom;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getFbLink() {
		return fbLink;
	}
	public void setFbLink(String fbLink) {
		this.fbLink = fbLink;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Date getLatestUse() {
		return latestUse;
	}
	public void setLatestUse(Date latestUse) {
		this.latestUse = latestUse;
	}
	public boolean isInitGuiderProfile() {
		return isInitGuiderProfile;
	}
	public void isInitGuiderProfile(boolean isInitGuiderProfile) {
		this.isInitGuiderProfile = isInitGuiderProfile;
	}
	public boolean isGotLicense() {
		return isGotLicense;
	}
	public void isGotLicense(boolean isGotLicense) {
		this.isGotLicense = isGotLicense;
	}
	public String getLicenseCode() {
		return licenseCode;
	}
	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}
	public int getGuiderExpYear() {
		return guiderExpYear;
	}
	public void setGuiderExpYear(int guiderExpYear) {
		this.guiderExpYear = guiderExpYear;
	}
	public void addSupportLanguage(String language){
		supportLanguages.add(language);
	}
	public List<String> getSupportLanguage() {
		return supportLanguages;
	}
	public void setSupportLanguage(List<String> availableLanguage) {
		this.supportLanguages = availableLanguage;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
//	public String getUserPhotoMediumUrl(){
//		return getPhotoUrl()+"/"+ServerInfo.VERSION_MEDIUM;
//	}
//	
//	public String getUserPhotoSmallUrl(){
//		return getPhotoUrl()+"/"+ServerInfo.VERSION_SMALL;
//	}
//	

	public static String getUserPhotoUrlFromFilename(String filename, int userId){
		String url = filename;
				
		if(filename!=null && !filename.equals("") && !filename.startsWith("http"))
			url = ServerInfo.HOST+ServerInfo.C_USER+ServerInfo.A_FETCH_USER_PHOTO+userId;
		
		return url;
	}
	
	public static String getUserPhotoURL(int userId, String photoSize){
		
		if(photoSize==null || photoSize.equals("") || 
				(!photoSize.equals(PHOTO_SIZE_ORIGIN)&&!photoSize.equals(PHOTO_SIZE_MEDIUM)&&!photoSize.equals(PHOTO_SIZE_SMALL)))
			photoSize = PHOTO_SIZE_SMALL;
		
		return ServerInfo.HOST+ServerInfo.C_USER+ServerInfo.A_FETCH_USER_PHOTO+userId+"/"+photoSize;
	}
	
	public void restoreFromJSON(String jsonObjString){
		try{
			JSONObject json = JSONObject.fromObject(jsonObjString);
			
			setId(json.getInt("id"));
			setAuthFrom(json.getString("authFrom"));
			setBirthday(json.getString("birthday")==null?"":json.getString("birthday"));
			setCity(json.getString("city"));
			setCountry(json.getString("country"));
			setEmail(json.getString("email"));
			setFbLink(json.getString("fbLink"));
			setFirstName(json.getString("firstName"));
			setGender(json.getBoolean("gender"));
			setGuiderExpYear(json.get("guiderExpYear")==null?0:json.getInt("guiderExpYear"));
			isGotLicense(json.getBoolean("isGotLicense"));
			isInitGuiderProfile(json.getBoolean("isInitGuiderProfile"));
			setLastName(json.getString("lastName"));
			setLatitude(json.getDouble("latitude"));
			setLicenseCode(json.getString("licenseCode"));
			setLocale(json.getString("locale"));
			setLocation(json.getString("location"));
			setLongitude(json.getDouble("longitude"));
			setMiddleName(json.getString("middleName"));
			setName(json.getString("name"));
			setTimezone(json.getString("timezone"));
			setPhotoUrl(getUserPhotoUrlFromFilename(json.getString("photo"), getId()));
			setGuiderRanking(json.getDouble("guider_rating"));
			setTravelerRanking(json.getDouble("traveler_rating"));
			setIntro(json.getString("intro"));
			
			if(json.get("registerDate")!=null)
				setRegisterDate(new SimpleDateFormat("yyyy-MM-dd").parse(json.getString("registerDate")));
			
			//Restore Guider's Data
			restoreGuiderData(json);
			
		}catch(Exception e){
			Log.e("User", "Error occured when restore json data."+e.toString());
		}
	}
	
	public void restoreTravelerData(JSONObject json){
		isInitTravelerProfile(json.getBoolean("isInitTravelerProfile"));
		setTravelerPhone(json.getString("travelerPhone"));
		setTravelerIntro(json.getString("travelerIntro"));
		setTravelerRanking(json.getDouble("traveler_rating"));
	}
	
	public void restoreGuiderData(JSONObject json){
		isInitGuiderProfile(json.getBoolean("isInitGuiderProfile"));
		isGotLicense(json.getBoolean("isGotLicense"));
		setLicenseCode(json.getString("licenseCode"));
		setGuiderExpYear(json.getInt("guiderExpYear"));
		setGender(json.getBoolean("gender"));
		setPhoneNumberJSON(json.getString("phone"));
		setName(json.getString("name"));
		setGuiderRanking(json.getDouble("guider_rating"));
		 
		//Support Language
		getSupportLanguage().clear();
		JSONArray langJsonArr = JSONArray.fromObject(json.get("supportLanguage"));
		for(int i=0; i<langJsonArr.size(); i++){
			addSupportLanguage(langJsonArr.getJSONObject(i).getString("lang"));
		}
		//Offer Transportation
		getOfferTransportationType().clear();
		JSONArray transJsonArr = JSONArray.fromObject(json.get("transportationType"));
		for(int i=0; i<transJsonArr.size(); i++){
			addOfferTransportationType(transJsonArr.getJSONObject(i).getString("trans"));
		}
	}
	
	public JSONObject getJSONObject(){
		JSONObject json = new JSONObject();
		json.put("username", username);
		json.put("email", email);
		json.put("name", name);
		json.put("firstName", firstName);
		json.put("middleName", middleName);
		json.put("lastName", lastName);
		json.put("birthday", birthday);
		json.put("authFrom", authFrom);
		json.put("country", country);
		json.put("locale", locale);
		json.put("location", location);
		json.put("fbLink", fbLink);
		json.put("timezone", timezone);
		json.put("city", city);
		json.put("gender", gender);
		json.put("latitude", latitude);
		json.put("longitude", longitude);
//		json.put("registerDate", registerDate);
		json.put("latestUse", latestUse);
		
		for(Object key : getGuideDataJSONObject().keySet()){
			json.put(key, getGuideDataJSONObject().get(key));
		}
		
		for(Object key : getTravelerJSONObject().keySet()){
			json.put(key, getTravelerJSONObject().get(key));
		}
		
		return json;
	}
	
	public JSONObject getTravelerJSONObject(){
		JSONObject json = new JSONObject();
		json.put("isInitTravelerProfile", isInitTravelerProfile());
		json.put("travelerPhone", getTravelerPhone());
		json.put("travelerIntro", getTravelerIntro());
		json.put("traveler_rating", getTravelerRanking());
		
		return json;
	}
	 
	public JSONObject getGuideDataJSONObject(){
		
		JSONObject json = new JSONObject();
		json.put("isInitGuiderProfile", isInitGuiderProfile);
		json.put("isGotLicense", isGotLicense);
		json.put("licenseCode", licenseCode);
		json.put("guiderExpYear", guiderExpYear);
		json.put("gender", getGender());
		json.put("phone", getPhoneNumberJSON());
		json.put("name", getName());
		json.put("guider_rating", getGuiderRanking());
		json.put("intro", getIntro());
		
		 
		//Support languages
		JSONArray langJSONArr = new JSONArray();
		for(String lang : supportLanguages){
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("lang", lang);
			langJSONArr.add(jsonObj);
		}
		json.put("supportLanguage", langJSONArr.toString());
		
		//Offer transportation type
		JSONArray transJSONArr = new JSONArray();
		for(String trans : offerTransportationType){
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("trans", trans);
			transJSONArr.add(jsonObj);
		}
		json.put("transportationType", transJSONArr.toString());	
		return json;
	}
	
}
