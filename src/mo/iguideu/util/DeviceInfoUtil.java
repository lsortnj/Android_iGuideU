package mo.iguideu.util;

import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class DeviceInfoUtil
{
	private static String DEVICE_UUID = "";

	private static DeviceInfoUtil _instance = null;
	private static Activity _activity;
	private static Display _display = null;

	public static void construct(Activity activity)
	{
		_activity = activity;

		WindowManager manager = (WindowManager) _activity
				.getSystemService(Context.WINDOW_SERVICE);

		_display = manager.getDefaultDisplay();
	}

	public static DeviceInfoUtil instance()
	{
		if (_instance == null)
		{
			_instance = new DeviceInfoUtil();

			return _instance;
		} else
		{
			return _instance;
		}
	}
	
	
	public Locale getCurrentLocale(){
		return _activity.getResources().getConfiguration().locale;
	}

	public static boolean isTablet(Context context) 
	{
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static DisplayMetrics getDeviceDisplayMetrics()
	{
		DisplayMetrics dm = new DisplayMetrics();
		_display.getMetrics(dm);
		
		return dm;
	}

	public String getDeviceUUID()
	{
		if (!DEVICE_UUID.equals(""))
		{
			return DEVICE_UUID;
		}

		final TelephonyManager tm = (TelephonyManager) _activity
				.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, androidId;

		tmDevice = "" + tm.getDeviceId();

		WifiManager wm = (WifiManager) _activity
				.getSystemService(Context.WIFI_SERVICE);

		androidId = ""
				+ android.provider.Settings.Secure.getString(
						_activity.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid;

		try
		{
			deviceUuid = new UUID(androidId.hashCode(), (wm.getConnectionInfo()
					.getMacAddress().hashCode()));
		} catch (Exception e)
		{
			deviceUuid = new UUID(androidId.hashCode(), tmDevice.hashCode());
		}

		Log.i("UUID", deviceUuid.toString());

		DEVICE_UUID = deviceUuid.toString();

		return DEVICE_UUID;
	}
}
