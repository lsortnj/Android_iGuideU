package mo.iguideu.util;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class AppUtil
{
	public static boolean isNeedToUpdate(Activity activity,
			String versionNameFromServer)
	{
		if (!NetworkUtil.hasConnectionExist())
		{
			return false;
		}

		boolean result = false;

		String version = "";

		try
		{
			PackageInfo pinfo = activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0);

			version = pinfo.versionName;

			if (!version.equals(versionNameFromServer)
					&& !versionNameFromServer.equals(""))
			{
				result = true;

				Log.i("App Version", "Server has newer version , do update! "
						+ pinfo.packageName);
			}

		} catch (NameNotFoundException e)
		{
			e.printStackTrace();

			Log.i("isNeedToUpdate", e.toString());

			return false;
		}

		return result;
	}
}
