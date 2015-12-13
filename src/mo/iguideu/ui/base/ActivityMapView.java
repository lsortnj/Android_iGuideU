package mo.iguideu.ui.base;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import mo.iguideu.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class ActivityMapView  extends ActionBarActivity{

	private GoogleMap map;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle mapInfo = getIntent().getExtras();
		
		if(mapInfo!=null){
			
			map =  ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map_view)).getMap();
			
			LatLng center = new LatLng(mapInfo.getDouble("lat"),mapInfo.getDouble("longi"));
			
			Marker nkut = map.addMarker(new MarkerOptions().position(center).title(mapInfo.getString("guideName")));
			nkut.setDraggable(true);
			
			map.setMyLocationEnabled(true);
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 16));
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
	    case android.R.id.home:
	    	finish();
	        return true;
	    }
		return super.onOptionsItemSelected(item);
	}
}
