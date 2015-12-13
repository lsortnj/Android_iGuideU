package mo.iguideu.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class FetchImageUtil  extends AsyncTask<String, Void, Bitmap> {

	private static int MaxTextureSize = 2048;

	public ImageView v;

	public FetchImageUtil(ImageView iv) {
	    v = iv;
	}

	@SuppressLint("NewApi")
	protected Bitmap doInBackground(String... params) {
		
		InputStream is 	= null;
		Bitmap bitmap 	= null;
		
		try {
			URL url = new URL(params[0]);
			HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

			is = connection.getInputStream();
			
			BitmapFactory.Options opt = new BitmapFactory.Options();
		    opt.inPurgeable = true;
		    if(android.os.Build.VERSION.SDK_INT >= 10)
		    	opt.inPreferQualityOverSpeed = false;
		    opt.inSampleSize = 1;

		    if(isCancelled() || is==null) {
		        return null;
		    }

		    opt.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(is, null, opt);
		    while(opt.outHeight > MaxTextureSize || opt.outWidth > MaxTextureSize) {
		        opt.inSampleSize++;
		        BitmapFactory.decodeStream(is, null, opt);
		    }
		    opt.inJustDecodeBounds = false;

		    bitmap = BitmapFactory.decodeStream(new URL(params[0]).openConnection().getInputStream(), null, opt);
	    } catch (Exception e) {
	        return null;
	    }
		
	    return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
	    if(v != null) {
	        v.setImageBitmap(result);
	    }
	}
	
}
