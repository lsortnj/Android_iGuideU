package mo.iguideu.serverHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.ion.Ion;

import mo.iguideu.guide.GuideEvent;
import mo.iguideu.guide.GuideSession;
import mo.iguideu.store.AppPreference;
import mo.iguideu.ui.rating.GuideRating;
import mo.iguideu.ui.rating.TravelerRating;
import mo.iguideu.user.User;
import mo.iguideu.user.UserManager;
import mo.iguideu.util.GPSTracker;
import net.sf.json.JSONArray;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerCore {

	private static ServerCore instance = null;
	private static Activity		 activity = null;
	
	public static void construct(Activity aActicity){
		activity = aActicity;
	}
	
	public static ServerCore instance(){
		if(instance == null)
			instance = new ServerCore();
		
		return instance;
	}
	
	public void updateGuiderData(final Handler callbackHandler){
		String url = ServerInfo.HOST + ServerInfo.C_USER + ServerInfo.A_UPDATE_GUIDE_DATA;
		
		Ion.with(activity.getApplicationContext()).load(url)
		.setMultipartParameter(ServerInfo.P_USERNAME, UserManager.getUser().getUsername())
		.setMultipartParameter(ServerInfo.P_GUIDE_DATA_JSON, UserManager.getUser().getGuideDataJSONObject().toString())
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				int returnCode = ServerInfo.RES_CODE_UPDATE_GUIDE_DATA_FAILED;
				
				try{
					returnCode = Integer.parseInt(result);
				}catch(Exception e){
					returnCode = ServerInfo.RES_CODE_UPDATE_GUIDE_DATA_FAILED;
				}
				
				if(returnCode == ServerInfo.RES_CODE_UPDATE_GUIDE_DATA_SUCCESS){
					UserManager.getUser().isInitGuiderProfile(true);
					AppPreference.instance().isInitGuideProfile(true);
				}
					
				if(callbackHandler!=null){
					callbackHandler.sendEmptyMessage(returnCode);
				}
			}
		});
	}
	
	public void syncUserData(final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_USER + ServerInfo.A_SYNC_DATA;
		
		Ion.with(activity.getApplicationContext()).load(url)
		.setMultipartParameter(ServerInfo.P_USERNAME, UserManager.getUser().getUsername())
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				UserManager.syncUserData(result);
				AppPreference.instance().setLastUsername(UserManager.getUser().getUsername());
				AppPreference.instance().setUserShowName(UserManager.getUser().getName());
				AppPreference.instance().isInitGuideProfile(UserManager.getUser().isInitGuiderProfile());
				
				if(handler != null){
					handler.sendEmptyMessage(ServerInfo.RES_CODE_AFTER_SYNC_USER_DATA);
				}
			}
		});
	}
	
	public void getMyGuides(final Handler handler, final int offset){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_GET_MY_GUIDE+UserManager.getUser().getId()+"/"+offset;
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_MY_GUIDES_DONE, result);
			}
		});
	}
	
	public void getMyJoinGuides(final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_MY_JOIN_GUIDES+UserManager.getUser().getId();
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_MY_JOIN_GUIDES_DONE, result);
			}
		});
	}
	
	public void getGuiderAllInfo(int guiderId,final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_FETCH_GUIDER_ALL_INFO+guiderId;
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_GUIDER_INFO_DONE, result);
			}
		});
	}
	
	public void getGuiderSimpleInfo(int guiderId,final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_FETCH_GUIDER_SIMPLE_INFO+guiderId;
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_GUIDER_INFO_DONE, result);
			}
		});
	}
	
	public void getTravelerCanRate(final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_GET_TRAVELER_CAN_RATE+UserManager.getUser().getId();
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_TRAVELER_CAN_RATE_DONE, result);
			}
		});
	}
	
	public void getGuideCanRate(final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_GET_GUIDES_CAN_RATE+UserManager.getUser().getId();
				
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_GUIDE_CAN_RATE_DONE, result);
			}
		});
	}
	
	public void doRateTravelers(final TravelerRating rating, final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_RATE_TRAVELER;
		
		Ion.with(activity.getApplicationContext()).load(url)
		.setMultipartParameter(ServerInfo.P_USER_ID, ""+UserManager.getUser().getId())
		.setMultipartParameter(ServerInfo.P_TRAVELER_IDS, rating.getTravelerIDs().toString())
		.setMultipartParameter(ServerInfo.P_RATING, String.valueOf(rating.getRating()))
		.setMultipartParameter(ServerInfo.P_COMMENT, rating.getComment())
		.setMultipartParameter(ServerInfo.P_GUIDE_ID, ""+rating.getGuideId())
		.setMultipartParameter(ServerInfo.P_TARGET_DATE, rating.getTargetDate())
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				try{
					int resultCode = Integer.parseInt(result);
					
					if(handler!=null){
						Message msg = new Message();
						msg.what = resultCode;
						msg.obj  = rating;
						handler.sendMessage(msg);
					}
				}catch(Exception e){Log.e("doRateTravelers", e.toString());}
			}
		});
	}
	
	public void doRateGuide(final GuideRating guideRate, final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_RATE_GUIDE+
						guideRate.getGuideId()+"/"+UserManager.getUser().getId()+"/"+
						guideRate.getRating()+"/"+guideRate.getComment();
				
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.DO_RATE_GUIDE_DONE, result);
			}
		});
	}
	
	public void joinGuide(
			final int userId,
			final int guideId, 
			final int guestCount,
			final String date,
			final GuideSession guideSession,
			final Handler handler){
		try{
			String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_JOIN_GUIDE+userId+"/"+guideId+
						  "/"+date+"/"+guestCount+
							(guideSession!=null?"/"+URLEncoder.encode(guideSession.toJSONObjectString(), "utf-8"):"");
			
			Ion.with(activity.getApplicationContext()).load(url)
			.asString().setCallback(new FutureCallback<String>(){
				@Override
				public void onCompleted(Exception arg0, String result) {
					int msgWhat = ServerInfo.RES_CODE_JOIN_GUIDE_SUCCESS;
					
					try{
						msgWhat = Integer.parseInt(result);
					}catch(Exception e){msgWhat = ServerInfo.RES_CODE_JOIN_GUIDE_FAILED;}
					
					sendToHandler(handler, msgWhat, result);
				}
			});
		}catch(Exception e){Log.e("JoinGuide", e.toString());}
	}
	
	public void getGuideNearby(final Handler handler, final int offset){
		// list_guide_nearby/:lat/:lng/:offset(/:range)
		String lat 		= "";
		String lng 		= "";
		String range    = String.valueOf(ServerInfo.DEFAULT_SEARCH_RANGE);
		
		lat = GPSTracker.instance().isLocationAvailable()?String.valueOf(GPSTracker.instance().getLatitude()):"0.0";
		lng = GPSTracker.instance().isLocationAvailable()?String.valueOf(GPSTracker.instance().getLongitude()):"0.0";
	
		
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_GUIDE_NEARBY+
						lat+"/"+lng+"/"+offset+"/"+range;
		
		Ion.with(activity.getApplicationContext()).load(url)
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.FETCH_GUIDE_DONE, result);
			}
		});
	}
	
	public void getGuideToRateTraveler(final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_GET_GUIDE_TO_RATE_TRAVELER+UserManager.getUser().getId();
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_GUIDE_TO_RATE_TRAVELER_DONE, result);
			}
		});
	}
	
	public void getRollCallGuestList(
			final Handler handler, 
			final int guide_id, 
			final String targetDate, 
			final String sessionJsonString){
		
		try{
			boolean hasSession = (sessionJsonString!=null && sessionJsonString.startsWith("{"));
			String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_ROLL_CALL+
							guide_id+"/"+targetDate+(hasSession?"/"+URLEncoder.encode(sessionJsonString, "utf-8"):"");
			
			Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
			.asString().setCallback(new FutureCallback<String>(){
				@Override
				public void onCompleted(Exception arg0, String result) {
					sendToHandler(handler, ServerInfo.GET_GUEST_LIST_DONE, result);
				}
			});
		}catch(Exception e){Log.e("getRollCallGuestList", e.toString());}
	}
	
	public void getGuideAvailableStatus(final Handler handler, final int guideId){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_GUIDE_AVAILABLE_STATUS+guideId;
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_GUIDE_AVAILABLE_STATUS_DONE, result);
			}
		});
	}
	
	public void getGuiderUpcomingGuide(final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_GUIDER_UPCOMING_GUIDE+UserManager.getUser().getId();
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_GUIDER_UPCOMING_GUIDE_DONE, result);
			}
		});
	}
	
	public void getTravelerUpcomingGuide(final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_TRAVELER_UPCOMING_GUIDE+UserManager.getUser().getId();
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString().setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.GET_TRAVELER_UPCOMING_GUIDE_DONE, result);
			}
		});
	}
	
	public void getGuidePopular(final Handler handler, final int offset){
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_GUIDE_POPULAR+offset;
		
		Ion.with(activity.getApplicationContext()).load(url).setMultipartParameter("foo", "bar")
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				sendToHandler(handler, ServerInfo.FETCH_GUIDE_DONE, result);
			}
		});
	}
	
	public void updateUserPhoto(){
		String url = ServerInfo.HOST + ServerInfo.C_USER + ServerInfo.A_UPDATE_USER_PHOTO;
		
		Ion.with(activity.getApplicationContext()).load(url)
		.setMultipartParameter(ServerInfo.P_USER_ID, ""+UserManager.getUser().getId())
		.setMultipartFile(ServerInfo.P_USER_PHOTO, new File(AppPreference.instance().getUserPhotoPath()))
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				try{
					int resultCode = Integer.parseInt(result);
					Log.i("UpdateUserPhoto","Code:"+resultCode);
				}catch(Exception e){}
			}
		});
	}
	
	public void signUpUser(final User user){
		String url = ServerInfo.HOST + ServerInfo.C_USER + ServerInfo.A_REGISTER;
		
		Ion.with(activity.getApplicationContext()).load(url)
		.setMultipartParameter(ServerInfo.P_USER_JSON, user.getJSONObject().toString())
		.setMultipartFile(ServerInfo.P_USER_PHOTO, new File(AppPreference.instance().getUserPhotoPath()))
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				try{
					int resultCode = Integer.parseInt(result);
					Log.i("SignUpUser","Code:"+resultCode);
				}catch(Exception e){}
			}
		});
	}
	
	public void checkUserSignUp(final String username, final Handler handler){
		String url = ServerInfo.HOST + ServerInfo.C_USER + ServerInfo.A_IS_SIGN_UP;
		
		Ion.with(activity.getApplicationContext()).load(url)
		.setMultipartParameter(ServerInfo.P_USERNAME, username)
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				if(handler!=null){
					try{
						int resultCode = Integer.parseInt(result);
						
						if(resultCode == ServerInfo.RES_CODE_USER_SIGNED_UP)
							UserManager.getUser().setUsername(username);
						
						if(handler!=null){
							handler.sendEmptyMessage(resultCode);
						}
					}catch(Exception e){}
				}
			}
		});
	}
	
	public Future<String> createNewGuide(GuideEvent guide, final Handler handler, ProgressDialog progressDialog){
		
		String url = ServerInfo.HOST + ServerInfo.C_GUIDE + ServerInfo.A_CREATE_GUIDE;
		
		int idx = 1;
		List<FilePart> photos = new ArrayList<FilePart>();
		if(guide.getPhotos()!=null){
			for(String imgUri : guide.getPhotos()){
				FilePart part = new FilePart("g[photo"+idx+"]",new File(Uri.parse(imgUri).getPath()));
				photos.add(part);
				idx++;
			}
		}
		
		Future<String> uploading = Ion.with(activity.getApplicationContext()).load(url)
		.uploadProgressDialog(progressDialog)
		.setTimeout(60 * 1000)
		/*** Base Info ***/
		.setMultipartParameter("g[name]", guide.getGuideName()) 
		.setMultipartParameter("g[max_people]", String.valueOf(guide.getMaxPeople()))
		.setMultipartParameter("g[schedule]", guide.getSchedule())
		.setMultipartParameter("g[offer_transportation]", JSONArray.fromObject(UserManager.getUser().getOfferTransportationType()).toString())
		.setMultipartParameter("g[support_langs]", JSONArray.fromObject(UserManager.getUser().getSupportLanguage()).toString())
		.setMultipartParameter("g[available_date_array]", JSONArray.fromObject(guide.getAvailableDateArray()).toString())
		.setMultipartParameter("g[fee]", ""+guide.getFee())
		/*** DateTime Info ***/
		.setMultipartParameter("g[available_specific_datetime]", guide.getAvailableDatetime().getSpecificDateString())
		.setMultipartParameter("g[available_start_datetime]", guide.getAvailableDatetime().getStartDatetimeString())
		.setMultipartParameter("g[available_end_datetime]", guide.getAvailableDatetime().getEndDatetimeString())
		.setMultipartParameter("g[available_weekday]", ""+guide.getAvailableDatetime().getDayOfWeek())
		.setMultipartParameter("g[available_monthday]", ""+guide.getAvailableDatetime().getDayOfMonth())
		.setMultipartParameter("g[available_type]", guide.getAvailableType())
		/*** Location Info ***/
		.setMultipartParameter("g[meet_location_name]", guide.getMeetLocation().getLocationName())
		.setMultipartParameter("g[meet_address]", guide.getMeetLocation().getAddress())
		.setMultipartParameter("g[meet_lat]", ""+guide.getMeetLocation().getLatLng().latitude)
		.setMultipartParameter("g[meet_lng]", ""+guide.getMeetLocation().getLatLng().longitude)
		.setMultipartParameter("g[country_name]", ""+guide.getMeetLocation().getCountryName())
		.setMultipartParameter("g[country_code]", ""+guide.getMeetLocation().getCountryCode())
		.setMultipartParameter("g[city]", ""+guide.getMeetLocation().getCity())
		.setMultipartParameter("g[duration]", ""+guide.getDuration())
		.setMultipartParameter("g[guide_session_json]", guide.getGuideSessionJSONArrayString())
		.setMultipartParameter("g[is_all_day_guide]", ""+guide.isAllDayGuide())
		/*** User Info ***/
		.setMultipartParameter("username", UserManager.getUser().getUsername())
		/*** File Part ***/
		.addMultipartFile(photos)
		.asString()
		.setCallback(new FutureCallback<String>(){
			@Override
			public void onCompleted(Exception arg0, String result) {
				if(handler!=null){
					Message msg = new Message();
					msg.what = Boolean.parseBoolean(result)?ServerInfo.CREATE_GUIDE_SUCCESS:ServerInfo.CREATE_GUIDE_FAILED;
					handler.sendMessage(msg);
				}
			}
		});
		
		return uploading;
	}
	
	public String sendHttpGet(String url){
		String response = "";
		try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            InputStream inputStream = httpResponse.getEntity().getContent();
 
            if(inputStream != null)
            	response = convertInputStreamToString(inputStream);
 
        } catch (Exception e) {
            Log.d("sendHttpGet", e.getLocalizedMessage());
        }
		
		return response;
	}
	
	private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
	
	private void sendToHandler(Handler handler, int msgWhat, Object msgObj){
		if(handler!=null){
			Message msg = new Message();
			msg.what = msgWhat;
			msg.obj = msgObj;
			handler.sendMessage(msg);
		}
	}
}
