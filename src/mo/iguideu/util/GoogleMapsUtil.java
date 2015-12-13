package mo.iguideu.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import mo.iguideu.serverHandler.ServerCore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class GoogleMapsUtil {

	private static final String LOG_TAG = "ExampleApp";
    
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private static final String SERVER_API_KEY = "AIzaSyBIlp8qFbiL8_l907Twl1IWlbtf8Grj8b0";

	public static net.sf.json.JSONArray searchCityOrCountry(String input, String locale){
		String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json?address="+input+
						 "&sensor=true&language="+locale;
		
		String result = ServerCore.instance().sendHttpGet(baseUrl);
		
		net.sf.json.JSONArray jsonArr = null;
		
		try{ jsonArr = net.sf.json.JSONArray.fromObject(result); }catch(Exception e){}
		
		return jsonArr;
	}
	
	public static ArrayList<String> getGooglePlaceAutocomplete(String input, String country) {
	    ArrayList<String> resultList = null;
	    
	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	        sb.append("?sensor=false&key=" + SERVER_API_KEY);
//	        sb.append("&components=country:"+country.toLowerCase());
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));
	        
	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        
	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
	        
	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	        }
	    } catch (JSONException e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }
	    
	    return resultList;
	}
	
	public static String getStaticMapUrl(
			double lat, 
			double longi, 
			int zoomLevel,
			int width,
			int height){
		
		String url = "http://maps.googleapis.com/maps/api/staticmap?";
		
		if(zoomLevel < 2)
			zoomLevel = 2;
		if(zoomLevel > 20)
			zoomLevel = 18;
		
		url += "center="+lat+","+longi+"&markers=color:green%7Clabel:G%7C"+lat+","+longi+"&zoom="+zoomLevel+"&";
		url += "size="+width+"x"+height+"&maptype=roadmap&sensor=true_or_false&markers=color:red";
		
		return url;
	}
	
}
