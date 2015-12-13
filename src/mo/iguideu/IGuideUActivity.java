package mo.iguideu;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONArray;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.store.AppPreference;
import mo.iguideu.ui.base.DialogPositiveNegative;
import mo.iguideu.ui.base.DialogSingleButton;
import mo.iguideu.ui.initGuideProfile.GuideInitDataActivity;
import mo.iguideu.ui.main.FragmentPopular;
import mo.iguideu.ui.myGuide.FragmentMyGuide;
import mo.iguideu.ui.nearby.FragmentNearby;
import mo.iguideu.ui.rating.ActivityRatingGuide;
import mo.iguideu.ui.rating.ActivityRatingTraveler;
import mo.iguideu.ui.upcoming.ActivityGuiderUpcoming;
import mo.iguideu.ui.upcoming.ActivityTravelerUpcoming;
import mo.iguideu.user.User;
import mo.iguideu.util.CalendarUtil;
import mo.iguideu.util.DeviceInfoUtil;
import mo.iguideu.util.FileUtil;
import mo.iguideu.util.GPSTracker;
import mo.iguideu.util.ImageUtil;
import mo.iguideu.util.LogUtil;
import mo.iguideu.util.MoProgressDialog;
import mo.iguideu.util.NetworkUtil;
import mo.iguideu.util.ProjectResourcesUtil;
import mo.iguideu.util.VibrateUtil;
import mo.iguideu.user.UserManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class IGuideUActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	public static final int REQUEST_CODE_INIT_GUIDE_DATA 			= 0x1000;
	public static final int RESULT_CODE_INIT_GUIDE_CANCEL 			= 0x1001;
	public static final int RESULT_CODE_INIT_GUIDE_UPDATE_SUCCESS 	= 0x1002;
	
	public static final int REQUEST_CODE_SIGN_UP		 			= 0x2000;
	public static final int RESULT_CODE_SIGN_UP_CANCEL			 	= 0x2001;
	
	
	private UiLifecycleHelper 			uiHelper;
	private NavigationDrawerFragment 	mNavigationDrawerFragment;
	private TextView 					tvUserName;
	
	private FragmentPopular fragmentPopular = new FragmentPopular();
	private FragmentNearby  fragmentNearby  = new FragmentNearby();
	private FragmentMyGuide fragmentMyGuide = new FragmentMyGuide();
	
	private GraphUser	fbGraphUser = null;
	
	private DialogPositiveNegative dialogLogout = null;
	
	private String currentActionBarTitle = "";
	
	private boolean isFirstInit = true;
	
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	    	
	        if(fbGraphUser==null)
	        	onSessionStateChange(session, state, exception);
	    }
	}; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_i_guide_u);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
																	.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
										(DrawerLayout) findViewById(R.id.drawer_layout));
		
		tvUserName = (TextView) findViewById(R.id.side_menu_txt_user_show_name);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
		
	    initResources();
	    
	    tvUserName.setText(AppPreference.instance().getUserShowName());
	    
	    getSupportFragmentManager()
		.beginTransaction()
			.replace(R.id.container,fragmentPopular).commitAllowingStateLoss();
	}
	
	private void initResources(){
		AppPreference.construct(this);
		ServerCore.construct(this);
		ServerCore.construct(this);
		DeviceInfoUtil.construct(this);
		FileUtil.construce(this);
		LogUtil.initFileWriter(this);
		NetworkUtil.construct(this);
		VibrateUtil.construct(this);
		ProjectResourcesUtil.construct(this);
		GPSTracker.construct(this);
		GPSTracker.instance();
		CalendarUtil.construct(this);
		MoProgressDialog.construct(this);
	}
	
	//更新側邊功能表的使用者資訊
	private Handler handlerUserInfo = new Handler(){
		public void handleMessage(Message msg){
			
			if(msg.what == ServerInfo.RES_CODE_AFTER_SYNC_USER_DATA){
				doFetchUserProfilePhoto();
			}else{
				if(msg.obj!=null){
					ImageView userPicture;
					userPicture = (ImageView) findViewById(R.id.side_menu_img_profile_img);
					userPicture.setImageBitmap((Bitmap) msg.obj);
				}
			}
			
			tvUserName.setText(UserManager.getUser().getName());

			ServerCore.instance().getGuideCanRate(refreshRatingCountHandler);
			ServerCore.instance().getTravelerCanRate(refreshRatingCountHandler);
		}
	};
	
	//檢查是否有已完成的導覽，可評價導遊/遊客的通知
	private Handler refreshRatingCountHandler = new Handler(){
		public void handleMessage(Message msg){
			try{
				JSONArray arr = JSONArray.fromObject(msg.obj);
				switch(msg.what){
					case ServerInfo.GET_GUIDE_CAN_RATE_DONE:
						mNavigationDrawerFragment.setTravelerWaitingRatingCount(arr.size());
						break;
					case ServerInfo.GET_TRAVELER_CAN_RATE_DONE:
						mNavigationDrawerFragment.setGuiderWaitingRatingCount(arr.size());	
						break;
				}
			}catch(Exception e){Log.e("RefreshRatingCount", e.toString());}
		}
	};
	
	//註冊檢查
	private Handler signupCheckHandler = new Handler(){
		public void handleMessage(Message msg){
			
			switch(msg.what){
				case ServerInfo.RES_CODE_USER_SIGNED_UP:
					ServerCore.instance().syncUserData(handlerUserInfo);
					break;
				case ServerInfo.RES_CODE_USER_SIGN_UP_YET:
					User guser = new User(fbGraphUser);
	                guser.setAuthFrom("Facebook");
	                UserManager.setUser(guser);
	                AppPreference.instance().setUserShowName(guser.getName());
	                doFetchFacebookProfilePhoto(false);
					break;
			}
			
			if(isFirstInit){
				currentActionBarTitle = getResources().getString(R.string.action_bat_title_popular);
				getSupportFragmentManager()
					.beginTransaction()
						.replace(R.id.container,fragmentPopular).commitAllowingStateLoss();
			}
			
			isFirstInit = false;
		}
	};
	
	private void doFetchFacebookProfilePhoto(final boolean isUpdateToServer){
		new Thread(new Runnable(){
			public void run(){
				URL url; 
				try {
					url = new URL("https://graph.facebook.com/"+ 
									UserManager.getUser().getUsername() + 
										"/picture?type=large");
					
					Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					
					ImageUtil.saveImage(bmp, AppPreference.instance().getUserPhotoPath());
					
					ServerCore.instance().signUpUser(UserManager.getUser());
					
					//Server端沒有使用者大頭，此時Android端有實體檔案，故上傳到Server
					if(isUpdateToServer)
						ServerCore.instance().updateUserPhoto();
					
					Message msg = new Message();
					msg.obj = bmp;
					  
					handlerUserInfo.sendMessage(msg);
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void doFetchUserProfilePhoto(){
		new Thread(new Runnable(){
			public void run(){
				URL url; 
				try {
					url = new URL(User.getUserPhotoURL(UserManager.getUser().getId(),User.PHOTO_SIZE_SMALL));
					
					Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					
					ImageUtil.saveImage(bmp, AppPreference.instance().getUserPhotoPath());
					
					Message msg = new Message();
					msg.obj = bmp;
					  
					handlerUserInfo.sendMessage(msg);
					
				} catch (Exception e) {
					e.printStackTrace();
					doFetchFacebookProfilePhoto(true);
				}
			}
		}).start();
	}
	 
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		 
		if (state.isOpened()) {
	    	
	        Request.newMeRequest(session, new Request.GraphUserCallback() {
	        	@Override
				public void onCompleted(GraphUser user,Response response) {
					if(NetworkUtil.hasConnectionExist()){
						fbGraphUser= user;
						if(user!=null)
							ServerCore.instance().checkUserSignUp(user.getId(), signupCheckHandler);
						else
							showUserNullDialog();
					}
	            }
	        }).executeAsync();
	       
	    } else{
	        //No user login, show login/sign up activity
	    	toLoginPage();
	    }
	}
	
	public void showUserNullDialog(){
		DialogSingleButton dialog = new DialogSingleButton(this, 
				getResources().getString(R.string.error_cant_get_fb_user_info));
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				finish();
			}
		});
		dialog.show();
	}
	
	private void toLoginPage(){
		Intent intent = new Intent();
		intent.setClass(IGuideUActivity.this, SignUpActivity.class);
		startActivityForResult(intent,REQUEST_CODE_SIGN_UP);
	}
	
	//側邊功能表選單
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();

		switch(position){
			case R.id.side_menu_layout_popular:
				currentActionBarTitle = getResources().getString(R.string.action_bat_title_popular);
				fragmentManager
					.beginTransaction()
						.replace(R.id.container,fragmentPopular).commit();
				break;
			case R.id.side_menu_layout_nearby_guide:
				currentActionBarTitle = getResources().getString(R.string.action_bat_title_nearby);
				fragmentManager
					.beginTransaction()
						.replace(R.id.container,fragmentNearby).commit();
				break;
			case R.id.side_menu_layout_nice_guider:
				currentActionBarTitle = getResources().getString(R.string.action_bat_title_nice_guider);
				break;
			case R.id.side_menu_layout_user_info:
				currentActionBarTitle = getResources().getString(R.string.action_bat_title_profile);
				break;
			case R.id.side_menu_layout_my_guide:
				
				if(UserManager.getUser()==null)
					return;
				currentActionBarTitle = getResources().getString(R.string.action_bat_title_my_guides);
				
				if(AppPreference.instance().isInitGuideProfile()){
					fragmentManager
						.beginTransaction()
							.replace(R.id.container,fragmentMyGuide).commit();
				}
				
				if(!UserManager.getUser().isInitGuiderProfile()){
					Intent intent = new Intent();
					intent.setClass(this, GuideInitDataActivity.class);
					startActivityForResult(intent, REQUEST_CODE_INIT_GUIDE_DATA);
				}else{
					fragmentManager
						.beginTransaction()
							.replace(R.id.container,fragmentMyGuide).commit();
				}
				
				break;
			case R.id.side_menu_layout_traveler_upcoming:
				toTravlerUpcoming();
				break;
			case R.id.side_menu_layout_setting:
				currentActionBarTitle = getResources().getString(R.string.action_bat_title_setting);
				break;
			case R.id.side_menu_layout_traveler_waiting_rating:
				toRatingGuide();
				break;
			case R.id.side_menu_btn_logout:
				showLogoutDialog();
				break;
				
			case R.id.side_menu_layout_guider_upcoming:
				toGuiderUpcoming();
				break;
			case R.id.side_menu_layout_guider_waiting_rate:
				toRatingTraveler();
				break;
		}
	}
	
	private void toTravlerUpcoming(){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), ActivityTravelerUpcoming.class);
		startActivity(intent);
	}
	
	private void toGuiderUpcoming(){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), ActivityGuiderUpcoming.class);
		startActivity(intent);
	}
	
	private void toRatingTraveler(){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), ActivityRatingTraveler.class);
		startActivity(intent);
	}
	
	private void toRatingGuide(){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), ActivityRatingGuide.class);
		startActivity(intent);
	}
	
	private void showLogoutDialog(){
		if(dialogLogout == null)
			dialogLogout = new DialogPositiveNegative(
					this,
					"",
					getResources().getString(R.string.tip_logout_confirm),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							doLogout();
							dialogLogout.dismiss();
						}
				});
			
		dialogLogout.show();
	}
	
	private void doLogout(){
		UserManager.setUser(new User());
		AppPreference.instance().setLastUsername("");
		AppPreference.instance().setUserShowName("");
		AppPreference.instance().isInitGuideProfile(false);
		
		mNavigationDrawerFragment.scrollToTop();
		
		//Delete User Profile Photo
		File userPhto = new File(AppPreference.instance().getUserPhotoPath());
		if(userPhto.exists())
			userPhto.delete();
		
		//FB Logout
		if (Session.getActiveSession() != null) {
		    Session.getActiveSession().closeAndClearTokenInformation();
		}
		Session.setActiveSession(null);
		
		toLoginPage();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		 
	    Session session = Session.getActiveSession();
	    if (session != null && 
	           (session.isOpened() || session.isClosed())) {
	        onSessionStateChange(session, session.getState(), null);
	    }else{
	    	toLoginPage();
	    }

	    uiHelper.onResume();
	    
//	    if(!GPSTracker.instance().canGetLocation())
//	    	GPSTracker.instance().showSettingsAlert();
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	    
	    //Return from sign up page
	    if(requestCode == REQUEST_CODE_SIGN_UP){
	    	if(resultCode == RESULT_CODE_SIGN_UP_CANCEL)
		    	finish();
	    }
	    
	    //Return from initial guide data page
	    if(requestCode == REQUEST_CODE_INIT_GUIDE_DATA){
	    	switch(resultCode){
	    		case RESULT_CODE_INIT_GUIDE_CANCEL:
	    			
	    			break;
	    		case RESULT_CODE_INIT_GUIDE_UPDATE_SUCCESS:
	    			getSupportFragmentManager()
						.beginTransaction()
							.replace(R.id.container,fragmentMyGuide).commit();
	    			break;
	    	}
	    }
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}


	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		if(currentActionBarTitle.equals(""))
			actionBar.setTitle(mTitle);
		else
			actionBar.setTitle(currentActionBarTitle);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		if (!mNavigationDrawerFragment.isDrawerOpen()) {
////			getMenuInflater().inflate(R.menu.travel_now, menu);
//			restoreActionBar();
////			return true;
//		}
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		
//		return super.onOptionsItemSelected(item);
//	}

}
