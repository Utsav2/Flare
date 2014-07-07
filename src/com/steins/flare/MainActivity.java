package com.steins.flare;

import java.util.ArrayList;

import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.google.android.gms.maps.model.LatLng;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends SherlockFragmentActivity {

	private String typeOfFlare = "";

	SharedPreferences sharedPrefs;

	private String imagePath;

	private LatLng mineLocation = null;

	private String descriptionText = "";

	private String address;

	private String NUMBER = "100";

	private String TYPE_OF_USER = "101";

	public void setDescriptionText(String text) {

		descriptionText = text;
	}

	public String getDescriptionText() {

		return descriptionText;
	}

	public void setTypeOfUser(Fragment fragment) {

		String type = "REPORT";

		/*
		 * if (fragment instanceof VolunteerFragment) {
		 * 
		 * type = "VOLUNTEER";
		 * 
		 * }
		 * 
		 * Editor editor = typeOfUser.edit();
		 * 
		 * editor.putString(TYPE_OF_USER, type);
		 * 
		 * editor.commit();
		 */

	}

	public String getUploadPhoneNumber() {

		return sharedPrefs.getString(new InternetClient().UPLOAD_NUMBER, "");
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getSupportActionBar().setTitle("Flare");

		sharedPrefs = this.getSharedPreferences("com.steins.flare",
				Context.MODE_PRIVATE);

		InternetClient ic = new InternetClient();

		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_layout);

		if (!sharedPrefs.contains(ic.COUNTRY)
				|| !sharedPrefs.contains(ic.UPLOAD_NUMBER)
				|| !sharedPrefs.contains(ic.NUMBER)) {

			setFragment(new SetupFragment());

		}

		// This obtains the usual job of the user. Therefore, it sets the user
		// to a reporter or volunteer based on
		// past usage of the application

		// if(!typeOfUser.contains(TYPE_OF_USER)){ TODO: UNCOMMENT

		else {

			StartFragment sFragment = new StartFragment();

			setFragment(sFragment);
		}

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

		transaction.setCustomAnimations(R.anim.enter, R.anim.exit,
				R.anim.pop_enter, R.anim.pop_exit);

		transaction.replace(R.id.content_frame, fragment);
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	// This removes previous fragment from the Activity

	public void setFragment(Fragment fragment, Fragment previous) {

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.remove(previous);

		transaction.commit();

		setFragment(fragment);
	}

	public void setImage(String path) {

		imagePath = path;

	}

	public void setCoordinates(LatLng l, String address) {

		mineLocation = l;

		this.address = address;

	}

	public String getImage() {

		return imagePath;

	}

	public LatLng getCoordinates() {

		return mineLocation;

	}

	public String getAddress() {

		return address;
	}

	public boolean areCoordinatesSaved() {

		return mineLocation != null;
	}

	public void setTypeOfFlare(String type) {

		typeOfFlare = type;

	}

	public void setUpInternetFlare() {

		if (getCoordinates() == null || getAddress() == null) {

			return;
		}

		String lat = Double.valueOf(getCoordinates().latitude).toString();

		String lng = Double.valueOf(getCoordinates().longitude).toString();

		InternetClient client = new InternetClient();

		client.postData(this, getImage(), getDescriptionText(), lat, lng,
				getAddress());

	}

	public void setUpSMSFlare(String message) {

		JSONObject json_obj = new JSONObject();

		TelephonyManager tMgr = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);

		String number = sharedPrefs.getString(NUMBER, "");

		String IMEI = tMgr.getDeviceId();

		Long tsLong = System.currentTimeMillis() / 1000;

		String ts = tsLong.toString();

		try {

			json_obj.put("IMEI", IMEI);
			json_obj.put("Time", ts);
			json_obj.put("Message", message);
			json_obj.put("Number", number);

		} catch (Exception e) {

		}

		String formattedMessage = json_obj.toString();

		sendSMS(getUploadPhoneNumber(), formattedMessage);

	}

	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		Button setUpButton = (Button) findViewById(R.id.setupButton);
		Button aboutButton = (Button) findViewById(R.id.aboutButton);

		/*
		 * aboutButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * setFragment(new AboutFragment());
		 * 
		 * } });
		 */

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.setupButton:
			setFragment(new SetupFragment());
			return true;
		case R.id.aboutButton:
			setFragment(new AboutFragment());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onBackPressed() {
		Fragment frag = getSupportFragmentManager().findFragmentById(
				R.id.content_frame);

		if (frag instanceof ImageViewFragment) {
			((ImageViewFragment) frag).onBackPressed();
		} else {
			super.onBackPressed();
		}
	}

	private String SENT = "SMS_SENT";
	private String DELIVERED = "SMS_DELIVERED";
	private int MAX_SMS_MESSAGE_LENGTH = 160;

	// ---sends an SMS message to another device---
	// http://stackoverflow.com/questions/14452808/sending-and-receiving-sms-and-mms-in-android-pre-kit-kat-android-4-4
	public void sendSMS(String phoneNumber, String message) {

		PendingIntent piSent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, new Intent(SENT), 0);
		PendingIntent piDelivered = PendingIntent.getBroadcast(
				getApplicationContext(), 0, new Intent(DELIVERED), 0);
		SmsManager smsManager = SmsManager.getDefault();

		int length = message.length();
		if (length > MAX_SMS_MESSAGE_LENGTH) {
			ArrayList<String> messagelist = smsManager.divideMessage(message);
			smsManager.sendMultipartTextMessage(phoneNumber, null, messagelist,
					null, null);
		} else
			smsManager.sendTextMessage(phoneNumber, null, message, piSent,
					piDelivered);
	}

}
