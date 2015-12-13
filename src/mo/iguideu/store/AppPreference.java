package mo.iguideu.store;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mo.iguideu.util.SDCardUtil;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Environment;

public class AppPreference
{
	public static String APP_HOME			= "iGuideU";
	public static String USER_FOLDER		= "user";
	public static String GUIDE_FOLDER		= "guide";
	public static String USER_PHOTO_FILENAE	= "photo.jpg";

	public static String DEFAULT_APP_ROOT_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath()+ File.separator+ APP_HOME + File.separator;
	
	private static Activity _activity;
	private static AppPreference _instance = null;

	private static SharedPreferences _preferences;
	
	private static SimpleDateFormat datetimeFomat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");

	public static void construct(Activity activity)
	{
		_activity = activity;
		
		_preferences = _activity.getSharedPreferences("APP_SETTING",Context.MODE_PRIVATE);

		if (SDCardUtil.isSDCardExistAndUseful())
		{
			DEFAULT_APP_ROOT_PATH = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ APP_HOME
					+ File.separator;
			
			new File(DEFAULT_APP_ROOT_PATH).mkdirs();
			new File(DEFAULT_APP_ROOT_PATH+USER_FOLDER).mkdirs();
			new File(DEFAULT_APP_ROOT_PATH+GUIDE_FOLDER).mkdirs();
		} 
		else
		{
			DEFAULT_APP_ROOT_PATH = APP_HOME;

			try
			{
				_activity.openFileOutput("temp.txt",Activity.MODE_WORLD_READABLE);

				ContextWrapper cw = new ContextWrapper(_activity);
				
				File temp_folder = cw.getDir("unzip_temp", Context.MODE_WORLD_READABLE);
				
				int file_count = temp_folder.listFiles().length;

				for (int i = file_count - 1; i >= 0; i--)
				{
					temp_folder.listFiles()[i].delete();
				}
				
			} 
			catch ( FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public static AppPreference instance()
	{
		if (_instance == null)
		{
			_instance = new AppPreference();

			return _instance;
		} else
		{
			return _instance;
		}
	}
	
	public String getAppHomeFolder(){
		String home = DEFAULT_APP_ROOT_PATH+File.separator;

		return home;
	}
	
	public String getTempCameraPhotoSavePath(){
		String filename = datetimeFomat.format(new Date())+".jpg";
		String photoPath = DEFAULT_APP_ROOT_PATH+File.separator+
							GUIDE_FOLDER+File.separator+filename;
		
		return photoPath;
	}
	
	public String getUserPhotoPath(){
		_preferences = _activity.getSharedPreferences("APP_SETTING",
				Context.MODE_PRIVATE);
		
		String photoPath = DEFAULT_APP_ROOT_PATH+File.separator+
							USER_FOLDER+File.separator+
								USER_PHOTO_FILENAE;

		return _preferences.getString("UserPhotoPath", photoPath);
	}

	public String getLastUsername()
	{
		_preferences = _activity.getSharedPreferences("APP_SETTING",
				Context.MODE_PRIVATE);

		return _preferences.getString("LastUsername", "");
	}
	
	public void setLastUsername(String username)
	{
		SharedPreferences.Editor editor = _preferences.edit();

		editor.putString("LastUsername", username);

		editor.commit();
	}
	
	public String getUserShowName()
	{
		_preferences = _activity.getSharedPreferences("APP_SETTING",
				Context.MODE_PRIVATE);

		return _preferences.getString("UserShowName", "");
	}
	
	public void setUserShowName(String showName)
	{
		SharedPreferences.Editor editor = _preferences.edit();

		editor.putString("UserShowName", showName);

		editor.commit();
	}
	
	public boolean isInitGuideProfile()
	{
		_preferences = _activity.getSharedPreferences("APP_SETTING",
				Context.MODE_PRIVATE);

		return _preferences.getBoolean("IsInitGuideProfile", false);
	}
	
	public void isInitGuideProfile(boolean isInitGuideProfile)
	{
		SharedPreferences.Editor editor = _preferences.edit();

		editor.putBoolean("isInitGuideProfile", isInitGuideProfile);

		editor.commit();
	}
}
