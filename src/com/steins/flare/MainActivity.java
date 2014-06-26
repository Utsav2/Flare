package com.steins.flare;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MainActivity extends SherlockFragmentActivity {

	private String uploadPhoneNumber = "";

	private String TYPE_OF_USER = "typeOfUsage";

	private String typeOfFlare = "";

	SharedPreferences typeOfUser;

	private String imagePath;

	private LatLng mineLocation = null;

	private String descriptionText = "";

	public void setDescriptionText(String text) {

		descriptionText = text;
	}

	public String getDescriptionText() {

		return descriptionText;
	}

	public void setTypeOfUser(Fragment fragment) {

		String type = "REPORT";

		if (fragment instanceof VolunteerFragment) {

			type = "VOLUNTEER";

		}

		Editor editor = typeOfUser.edit();

		editor.putString(TYPE_OF_USER, type);

		editor.commit();

	}

	public String getUploadPhoneNumber() {

		return uploadPhoneNumber;

	}

	public void setPhoneNumber(String number) {

		this.uploadPhoneNumber = number;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getSupportActionBar().setTitle("Flare");

		typeOfUser = this.getSharedPreferences("com.steins.flare",
				Context.MODE_PRIVATE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_layout);

		// This obtains the usual job of the user. Therefore, it sets the user
		// to a reporter or volunteer based on
		// past usage of the application

		// if(!typeOfUser.contains(TYPE_OF_USER)){ TODO: UNCOMMENT

		StartFragment sFragment = new StartFragment();

		setFragment(sFragment);

		// }
		/*
		 * else{
		 * 
		 * String type = typeOfUser.getString(TYPE_OF_USER, "REPORT");
		 * 
		 * if(type.equals("REPORT")){
		 * 
		 * ReportFragment rFragment = new ReportFragment();
		 * 
		 * setFragment(rFragment); } else{
		 * 
		 * VolunteerFragment vFragment = new VolunteerFragment();
		 * 
		 * setFragment(vFragment);
		 * 
		 * }
		 * 
		 * }
		 */

	}

	public void setFragment(Fragment fragment) {

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.content_frame, fragment);
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	// This removes previous fragment from the Activity

	public void setFragment(Fragment fragment, Fragment previous) {

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.remove(previous);
		transaction.replace(R.id.content_frame, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void setImage(String path) {

		Log.e("SET PATH", "YOLO");
		
		imagePath = path;

	}

	public void setCoordinates(LatLng l) {

		Log.i("HI", "REACHED SET");

		mineLocation = l;

	}

	public String getImage() {

		return imagePath;

	}

	public LatLng getCoordinates() {

		return mineLocation;

	}

	public boolean areCoordinatesSaved() {

		return mineLocation != null;
	}

	public void setTypeOfFlare(String type) {

		typeOfFlare = type;

	}

	public void setUpFlare() {

		if (typeOfFlare.equals("INTERNET")) {

			setUpInternetFlare();
		} else if (typeOfFlare.equals("SMS")) {

			setUpSMSFlare();
		}

	}

	public void setUpInternetFlare(){
		
		String lat = Double.valueOf(getCoordinates().latitude).toString();
		
		String lng = Double.valueOf(getCoordinates().longitude).toString();
		
		InternetClient client = new InternetClient();
		
		client.postData(getApplicationContext(), getImage(), getDescriptionText(), lat, lng);
		
			
	}

	public void setUpSMSFlare() {

	} 

	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

}
