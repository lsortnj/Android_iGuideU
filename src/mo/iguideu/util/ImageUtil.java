package mo.iguideu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class ImageUtil
{
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8 ) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}
	
	public static Bitmap getBitmapFromUrl(String aUrl){
		Bitmap bitmap = null;
		InputStream inputStream = null;
		
		try {
	        URL url = new URL(aUrl);
	        URLConnection conn = url.openConnection();

	        HttpURLConnection httpConn = (HttpURLConnection)conn;
	        httpConn.setRequestMethod("GET");
	        httpConn.connect();

	        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	            inputStream = httpConn.getInputStream();

	            bitmap = BitmapFactory.decodeStream(inputStream);
	            inputStream.close();
	        }
	    } catch (MalformedURLException e1) {
	        e1.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		return bitmap;
	}
	
	public static boolean saveImage(Bitmap bitmap, String path)
	{
		boolean result = true;
		try{
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fout = new FileOutputStream(file);

			bitmap.compress(CompressFormat.JPEG, 99, fout);

			fout.flush();
			fout.close();
		} catch (FileNotFoundException e){
			result =false;
			e.printStackTrace();
		} catch (IOException e){
			result =false;
			e.printStackTrace();
		}
		
		return result;
	}

	public static Bitmap scaleImage(Bitmap bitmap, float ratio)
	{
		if (bitmap == null)
		{
			return null;
		}

		Bitmap return_bitmap = null;

		if (ratio != 0.0f)
		{
			Matrix matrix = new Matrix();

			matrix.postScale(ratio, ratio);

			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			return_bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
					true);
		}

		return return_bitmap;
	}

	public static Bitmap getFitSizeImage(Bitmap sourceImg, int width, int height)
	{
		if (sourceImg == null)
		{
			return null;
		}

		float fit_w = width;
		float fit_h = height;
		float source_w = sourceImg.getWidth();
		float source_h = sourceImg.getHeight();
		float ratio = 0f;

		if ((source_w <= fit_w) && (source_h <= fit_h))
		{
			return sourceImg;
		}

		Bitmap resize_img = null;

		NumberFormat num_format = NumberFormat.getInstance();

		num_format.setMaximumFractionDigits(2);

		if ((source_w > fit_w) && (source_h < fit_h))
		{
			ratio = Float.parseFloat(num_format.format(fit_w / source_w));
		} else if ((source_w < fit_w) && (source_h > fit_h))
		{
			ratio = Float.parseFloat(num_format.format(fit_h / source_h));
		} else if ((source_w > fit_w) && (source_h > fit_h))
		{
			ratio = Math.min(
					Float.parseFloat(num_format.format(fit_w / source_w)),
					Float.parseFloat(num_format.format(fit_h / source_h)));
		}

		if (ratio != 0.0f)
		{
			Matrix matrix = new Matrix();

			matrix.postScale(ratio, ratio);

			int w = sourceImg.getWidth();
			int h = sourceImg.getHeight();

			resize_img = Bitmap.createBitmap(sourceImg, 0, 0, w, h, matrix,
					true);
		}

		return resize_img;
	}

	public static Bitmap decodeFromStream(InputStream is)
	{
		Bitmap bitmap = null;

		try
		{
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 60;

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true)
			{
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
				{
					break;
				}
				width_tmp /= 2;

				height_tmp /= 2;

				scale *= 2;
			}

			Log.i("ImageUtil", "inSampleSize " + scale);

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();

			o2.inSampleSize = scale;

			bitmap = BitmapFactory.decodeStream(is, null, o2);

			return bitmap;
		} catch (Exception e)
		{
		}

		return bitmap;
	}
}
