package com.steins.flare;

import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

public class LocationDataAsync extends AsyncTask{
	
	MainActivity mActivity;
	
	LatLng coords;
	
	public LocationDataAsync(MainActivity m, LatLng l) {
		
		coords = l;
		
		mActivity = m;
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		
		String address = getAddress(coords.latitude, coords.longitude);
		
		mActivity.setCoordinates(coords, address);		
		
		return null;
	}
	
    protected void onPostExecute(Long result) {

    }
    
	public String getAddress(double latitude, double longitude){
		
		try {
		    Geocoder geo = new Geocoder(mActivity.getApplicationContext(), Locale.getDefault());
		    List<Address> addresses = geo.getFromLocation((latitude), (longitude), 1);
		    if (addresses.isEmpty()) {

		    	Toast.makeText(mActivity.getApplicationContext(), "Geocoding error", Toast.LENGTH_LONG).show();
		    	return null;
		    }
		    else {
		        if (addresses.size() > 0) {
		        	
		        	
		        	//Reverse Geocoding on client, so ensuring super scalable application
		        	//google "reverse geocoding IP address" limit for details
		        	
		        	JSONObject address = new JSONObject();
		        	
		        	address.put("Feature", addresses.get(0).getFeatureName());
		        	
		        	address.put("Locality", addresses.get(0).getLocality());
		        	
		        	address.put("Administrative Area", addresses.get(0).getAdminArea());
		        	
		        	address.put("Country", addresses.get(0).getCountryName());
		        	
		        	return address.toString();
		        	
		           // return (addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
		        }
		    }
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
		
		return null;
		
		
	}

}
