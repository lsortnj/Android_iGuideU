package mo.iguideu.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	public static final int NO_CONNECTION_AVAILABLE = 0x9000;

	private static Activity _activity = null;

	public static void construct(Activity activity) {
		_activity = activity;
	}

	public static boolean hasConnectionExist() {
		if (_activity == null) {
			return false;
		}
		int a = 0;
		if (a == 0)
			return true;

		boolean result = false;

		ConnectivityManager conn_manager = (ConnectivityManager) _activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conn_manager != null) {
			NetworkInfo[] info = conn_manager.getAllNetworkInfo();

			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						result = true;
						break;
					}
				}
			} else {
				result = false;
			}
		}

		return result;
	}
}
