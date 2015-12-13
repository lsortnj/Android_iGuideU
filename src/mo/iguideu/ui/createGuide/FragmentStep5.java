package mo.iguideu.ui.createGuide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import mo.iguideu.R;
import mo.iguideu.guide.MeetLocation;
import mo.iguideu.ui.base.IStepValidateCheck;
import mo.iguideu.util.DeviceInfoUtil;
import mo.iguideu.util.GPSTracker;
import mo.iguideu.util.GoogleMapsUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * Create new Guide step5: Choose meet location
 * @author lsortnj
 *
 */
public class FragmentStep5 extends Fragment implements IStepValidateCheck, OnMapLongClickListener{
	
	private GoogleMap 				map					= null;
	private AutoCompleteTextView 	editLocationName 	= null;
	
	private Geocoder 	geocoder 		= null;
	private LatLng      locationLatLng 	= null;
	private Address 	address			= null;
	private Marker 		lastMark		= null;
	
	private String 		locationName	= "";
	private String		countryCode 	= "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_creat_step5, container, false);
		
		geocoder = new Geocoder(getActivity());
		
		editLocationName = (AutoCompleteTextView) v.findViewById(R.id.create_guide_step5_edit_location_name);
		editLocationName.setAdapter(new PlacesAutoCompleteAdapter(getActivity(),R.layout.google_place_list_item));
		editLocationName.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				String str = (String) adapterView.getItemAtPosition(position);
				editLocationName.setText(str);
				moveMapviewToUserTypeLocation();
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editLocationName.getWindowToken(), 0);
			}
		});
		editLocationName.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					moveMapviewToUserTypeLocation();             
			    }
				return false;
			}
		});
		if(!locationName.equals(""))
			editLocationName.setText(locationName);
		
		map = ((MapFragment) getActivity().getFragmentManager()
				.findFragmentById(R.id.create_guide_step5_map_view)).getMap();
		map.setOnMapLongClickListener(this);
		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom
				(
					new LatLng(GPSTracker.instance().getLatitude(),GPSTracker.instance().getLongitude()),
					(float) 15.0)
				);
		
		if(address!=null){
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(),address.getLongitude()),(float) 15.0));
			
			lastMark = map.addMarker(new MarkerOptions()
			                            .position(new LatLng(address.getLatitude(),address.getLongitude()))
			                            .title(locationName)
			                            .snippet(address.getLocality())
			                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
		}
		
		if(GPSTracker.instance().isLocationAvailable()){
			try {
				List<Address> addrs = geocoder.getFromLocation(GPSTracker.instance().getLatitude(), GPSTracker.instance().getLongitude(), 1);
				if(addrs!=null && addrs.size()>0)
					countryCode = addrs.get(0).getCountryCode();
				Log.e("COUNTRY_CODE", countryCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	    return v;
	 }
	
	private void moveMapviewToUserTypeLocation(){
		
		String userTypeLocation = editLocationName.getText().toString();
		
		try {
			List<Address> searchResult = geocoder.getFromLocationName(userTypeLocation, 1);
			
			if(searchResult.size()>0){
				address = searchResult.get(0);
				if(address.hasLatitude() && address.hasLongitude()){
					locationLatLng = new LatLng(address.getLatitude(),address.getLongitude());
					
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng,(float) 15.0));
					map.clear();
					lastMark = map.addMarker(new MarkerOptions()
					                            .position(new LatLng(address.getLatitude(),address.getLongitude()))
					                            .title(userTypeLocation)
					                            .snippet(address.getLocality())
					                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MeetLocation getMeetLocation(){
		MeetLocation location = new MeetLocation();
		if(address!=null){
			location.setLocationName(editLocationName.getText().toString());
			location.setLatLng(locationLatLng);
			location.setCountryName(address.getCountryName());
			location.setCountryCode(address.getCountryCode());
			if(address.getFeatureName().toLowerCase().equals("null")){
				if(!address.getLocality().toLowerCase().equals("null"))
					location.setCity(address.getLocality());
			}else
				location.setCity(address.getFeatureName());
			if(address.getMaxAddressLineIndex()!= -1){
				location.setAddress(getFullAddress(address));
			}
		}
		
		return location;
	}

	@Override
	public boolean isDataValid() {
		
		if(locationLatLng!=null && !editLocationName.getText().toString().equals("")){
			return true;
		}
		
		return false;
	}
	
	
	@Override
	public String getDataInValidString() {
		String invalidString = "";
		
		if(editLocationName.getText().toString().equals(""))
			invalidString = getActivity().getString(R.string.tip_create_guide_location_name_empty);
		if(locationLatLng==null)
			invalidString = getActivity().getString(R.string.tip_create_guide_location_choose_yet);
		
		return invalidString;
	}
	
	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	    private ArrayList<String> resultList;
	    
	    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
	        super(context, textViewResourceId);
	    }
	    
	    @Override
	    public int getCount() {
	        return resultList.size();
	    }

	    @Override
	    public String getItem(int index) {
	        return resultList.get(index);
	    }

	    @Override
	    public Filter getFilter() {
	        Filter filter = new Filter() {
	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	                FilterResults filterResults = new FilterResults();
	                if (constraint != null) {
	                    // Retrieve the autocomplete results.
	                    resultList = GoogleMapsUtil.getGooglePlaceAutocomplete(
	                    				constraint.toString(), 
	                    				DeviceInfoUtil.instance().getCurrentLocale().getCountry());
	                    
	                    // Assign the data to the FilterResults
	                    filterResults.values = resultList;
	                    filterResults.count = resultList.size();
	                }
	                return filterResults;
	            }

	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	                if (results != null && results.count > 0) {
	                    notifyDataSetChanged();
	                }
	                else {
	                    notifyDataSetInvalidated();
	                }
	            }};
	        return filter;
	    }
	}
	
	private String getFullAddress(Address aAddress){
		String detailedAddress = "";
		
		if(aAddress!=null && aAddress.getMaxAddressLineIndex()!= -1){
			for(int i=0; i<=address.getMaxAddressLineIndex(); i++)
				detailedAddress+=address.getAddressLine(i);
		}
		
		return detailedAddress;
	}

	@Override
	public void onMapLongClick(LatLng point) {
		locationLatLng = point;
		List<Address> addr = null;
		
		try {
			addr = geocoder.getFromLocation(point.latitude, point.longitude, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(addr!=null && addr.size()>0){
			address = addr.get(0);
			editLocationName.setText(getFullAddress(address));
			map.clear();
			lastMark = map.addMarker(new MarkerOptions()
	        .position(new LatLng(address.getLatitude(),address.getLongitude()))
	        .title(getFullAddress(address))
	        .snippet(address.getLocality())
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
		}
		
	}
}
