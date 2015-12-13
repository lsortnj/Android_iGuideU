package mo.iguideu.util;

import android.app.Activity;

public class ProjectResourcesUtil {

	private static Activity activity = null;
	
	public static void construct(Activity aActivity){
		activity = aActivity;
	}
	
	public static Activity getActivity(){
		return activity;
	}
	
	public static String getString(int resourceId){
		return activity.getResources().getString(resourceId);
	}
}
