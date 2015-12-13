package mo.iguideu;

import java.security.MessageDigest;
import java.util.Arrays;

import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.store.AppPreference;
import mo.iguideu.user.User;
import mo.iguideu.util.DeviceInfoUtil;
import mo.iguideu.util.FileUtil;
import mo.iguideu.util.LogUtil;
import mo.iguideu.util.NetworkUtil;
import mo.iguideu.util.VibrateUtil;
import mo.iguideu.user.UserManager;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;

public class SignUpActivity extends Activity{

	private UiLifecycleHelper uiHelper;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	}; 
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
//	    //Remove notification bar
//	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		
		setContentView(R.layout.activity_sign_up);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
		
	    initResources();
	    
	    LoginButton authButton = (LoginButton)findViewById(R.id.authButton);
	    authButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes"));
	    authButton.setApplicationId(getString(R.string.app_id));
	    
	}
		
	public void genHashKey(){
		try{
	    	PackageInfo info = getPackageManager().getPackageInfo("mo.iguideu",  PackageManager.GET_SIGNATURES);

		    for (Signature signature : info.signatures)
		        {
		            MessageDigest md = MessageDigest.getInstance("SHA");
		            md.update(signature.toByteArray());
		            Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		        }
	    }catch(Exception e){}
	}
	
	private void initResources(){
		AppPreference.construct(this);
		ServerCore.construct(this);
		DeviceInfoUtil.construct(this);
		FileUtil.construce(this);
		LogUtil.initFileWriter(this);
		NetworkUtil.construct(this);
		VibrateUtil.construct(this);
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	
		if (state.isOpened()) {
	        Request.newMeRequest(session, new Request.GraphUserCallback() {
	        	@Override
				public void onCompleted(GraphUser user,Response response) {
					
	                User guser = new User(user);
	                guser.setAuthFrom("Facebook");
	                UserManager.setUser(guser);
	                
	                AppPreference.instance().setUserShowName(guser.getName());
	                
	                ServerCore.instance().signUpUser(UserManager.getUser());
	                
	    			SignUpActivity.this.finish();
				}
	        }).executeAsync();
	    } else if (state.isClosed()) {
	        
	    } 
	}  
	
	@Override
	public void onBackPressed() {
		setResult(IGuideUActivity.RESULT_CODE_SIGN_UP_CANCEL,null);
		finish();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed())) {
	        onSessionStateChange(session, session.getState(), null);
	    }  

	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
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
}
