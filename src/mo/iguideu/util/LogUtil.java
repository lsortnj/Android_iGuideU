package mo.iguideu.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

public class LogUtil
{
	private static FileWriter	_file_writer 		= null;
	private static String 		_log_file_path      ="";
	private static String 		_app_name		    ="";
	private static Activity		_activity			= null;
	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
	
	public static void initFileWriter(Activity activity)
	{
		_activity = activity;
		
		_app_name = getApplicationName();
		
		try
		{
			_log_file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ 
							 File.separator + _app_name + "_log.txt";
			
			_file_writer = new FileWriter(_log_file_path,true);
		} 
		 catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static String getApplicationName()
	{
		PackageManager pm = _activity.getApplicationContext().getPackageManager();
		ApplicationInfo ai;
		
		try 
		{
		    ai = pm.getApplicationInfo( _activity.getPackageName(), 0);
		} 
		catch (final NameNotFoundException e) 
		{
		    ai = null;
		}
		
		final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
		
		return applicationName;
	}
	
	public static void writeMessage(String message)
	{
		try
		{
			_file_writer = new FileWriter(_log_file_path,true);
		} 
		 catch (IOException e)
		{
			e.printStackTrace();
		}
		
		BufferedWriter out = new BufferedWriter(_file_writer);
		
		String timestamp = format.format(new Date());
		
		try
		{
			out.write("\n"+timestamp+":\t");
			out.write(message);
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void writeErrorToLogFile(String errorStr)
	{
		BufferedWriter out = new BufferedWriter(_file_writer);
		
		errorStr = "Error--"+errorStr;
		
		String timestamp = format.format(new Date());
		
		try
		{
			out.write("\n"+timestamp+":\t");
			out.write(errorStr);
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
}
