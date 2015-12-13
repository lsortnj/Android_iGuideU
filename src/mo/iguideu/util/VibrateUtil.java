package mo.iguideu.util;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

public class VibrateUtil 
{
	private static Vibrator _vibrator 	= null;
	private static Activity	_activity	= null;
	
	public static void construct(Activity activity)
	{
		_activity = activity;
		_vibrator = (Vibrator) _activity.getApplication().getSystemService(Service.VIBRATOR_SERVICE);
	}
	
	public static void doVibrate(long milliseconds)
	{
		if(_vibrator != null)
		{
			_vibrator.vibrate(milliseconds);
		}
	}
	
	public static void doShortVibrate(int times, int gap)
	{
		if(_vibrator != null)
		{
			long[] vibrateScript = new long[times*2];
			
			for(int i=0; i<vibrateScript.length; i++)
			{
				if(i==0)
				{
					//Rest time gap
					vibrateScript[i] = 10;
					continue;
				}
				if(i%2==0)
				{
					//Do vibrate time
					vibrateScript[i] = 100;
				}
				else
				{
					//Rest time gap
					vibrateScript[i] = 10;
				}
			}
			
			_vibrator.vibrate(vibrateScript, -1);
		}
	}
}
