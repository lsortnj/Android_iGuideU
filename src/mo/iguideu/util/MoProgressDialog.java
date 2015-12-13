package mo.iguideu.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class MoProgressDialog {

	private static Activity			activity = null;
	private static ProgressDialog 	dialog   = null;
	
	public static void construct(Activity aActivity){
		activity = aActivity;
	}
	
	//Pass Context to ensure progress dialog in front of others
	public static void showProgress(Context context, String title, String message, boolean isCancelable){
		dialog = ProgressDialog.show((context==null)?activity:context, title, message, true, isCancelable);
	}
	
	public static void dismissProgress(){
		if(dialog != null && dialog.isShowing())
			dialog.dismiss();
	}
}
