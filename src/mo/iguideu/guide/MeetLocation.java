package mo.iguideu.guide;

import com.google.android.gms.maps.model.LatLng;

public class MeetLocation {
	
	private String		locationName 	= "";
	private String		address			= "";
	private String 		countryName		= "";
	private String		countryCode		= "";
	private String 		city			= "";
	private LatLng		latLng			= new LatLng(-37.817732,144.953402);
	
	
	public MeetLocation(String name,String address, LatLng latLng){
		setLocationName(name);
		setAddress(address);
		setLatLng(latLng);
	}
	
	public MeetLocation(){}
	public String getCountryName() {
		return countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	public LatLng getLatLng() {
		return latLng;
	}
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
