package com.steins.flare;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class InternetClient {

	private String uploadUrl = "http://calm-beach-2980.herokuapp.com/upload";

	public static final int REQUEST_TIMEOUT_MS = 60000;

	SharedPreferences sP;

	// A way to save the number in shared prefs

	public String NUMBER = "100";
	
	public String UPLOAD_NUMBER = "101";
			
	public String COUNTRY = "102";

	public void setUploadUrl(String url) {

		this.uploadUrl = url;

	}

	public void clearNumber() {

		Editor e = sP.edit();

		e.clear();

		e.commit();
	}

	public String getUploadUrl() {

		return uploadUrl;
	}

	public boolean haveNetworkConnection(Context mContext) {

		/*
		 * helps in determining if the user has Internet connection. If not,
		 * then, the app falls back to sms service
		 */

		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	public void SMSReport(JSONObject mine_data, Volunteer mActivity) {
		
		RequestQueue req = Volley.newRequestQueue(mActivity
				.getApplicationContext());

		Map<String, String> mHashMap = new HashMap<String, String>();

		try {
			mHashMap.put("Number", mine_data.getString("Number"));
			mHashMap.put("IMEI", mine_data.getString("IMEI"));
			mHashMap.put("Country", mine_data.getString("Country"));
			mHashMap.put("Time", mine_data.getString("Time"));
			mHashMap.put("Type", "SMS");
			mHashMap.put("Description", mine_data.getString("Message"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = getUploadUrl();

		MultipartRequest myRequest = new MultipartRequest(url,

		new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// error
				// the "" + is being added to avoid Logcat exception if the
				// error is null
				Log.e("Error.Response", "" + error.getMessage());

			}
		}, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Log.d("Response", response);

			}
		},

		null,

		mHashMap

		);

		// Setting request timeout to be 30 seconds before trying again. Change
		// REQUEST_TIMEOUT_MS to change

		myRequest.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		
		

		req.add(myRequest);

	}

	public void postData(final MainActivity mActivity, String imagePath,
			String descriptionText, String lat, String lng, String address) {

		// Creating hashmap of data to send. Implementation done in
		// MultipartRequest class

		sP = mActivity.getSharedPreferences("com.steins.flare",
				Context.MODE_PRIVATE);

		Map<String, String> mHashMap = new HashMap<String, String>();

		String number = sP.getString(NUMBER, "");

		mHashMap.put("Number", number);

		startSending(mActivity, imagePath, descriptionText, lat, lng, address,
				mHashMap);

	}

	public void startSending(final MainActivity mActivity, String imagePath,
			String descriptionText, String lat, String lng, String address,
			Map<String, String> mHashMap) {

		Log.e("ONE", "REACHED");

		RequestQueue req = Volley.newRequestQueue(mActivity
				.getApplicationContext());

		File image = null;

		if (imagePath != null) {

			image = new File(imagePath);
		}

		TelephonyManager tMgr = (TelephonyManager) mActivity
				.getApplicationContext().getSystemService(
						Context.TELEPHONY_SERVICE);

		String IMEI = tMgr.getDeviceId();

		Long tsLong = System.currentTimeMillis() / 1000;

		String ts = tsLong.toString();

		final CompletedFlareFragment cFragment = new CompletedFlareFragment();

		mHashMap.put("Type", "Internet");

		mHashMap.put("Time", ts);

		mHashMap.put("Description", descriptionText);

		lat = lat.substring(0, 10);

		lng = lng.substring(0, 10);

		mHashMap.put("Latitude", lat);

		mHashMap.put("Longitude", lng);

		mHashMap.put("IMEI", IMEI);

		mHashMap.put("Address", address);

		String url = getUploadUrl();

		MultipartRequest myRequest = new MultipartRequest(url,

		new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// error
				// the "" + is being added to avoid Logcat exception if the
				// error is null
				Log.e("Error.Response", "" + error.getMessage());

				cFragment.setSuccessState(false);

				mActivity.setFragment(cFragment);

			}
		}, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Log.d("Response", response);

				cFragment.setSuccessState(true);

				mActivity.setFragment(cFragment);

			}
		},

		image,

		mHashMap

		);

		// Setting request timeout to be 30 seconds before trying again. Change
		// REQUEST_TIMEOUT_MS to change

		myRequest.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		req.add(myRequest);

	}

	public void openDialogForNumber(final Activity mActivity) {

		final SharedPreferences pref = mActivity.getSharedPreferences(
				"com.steins.flare", Context.MODE_PRIVATE);

		LayoutInflater layoutInflater = LayoutInflater.from(mActivity);

		View promptView = layoutInflater.inflate(R.layout.prompttext, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mActivity);

		final EditText input = (EditText) promptView
				.findViewById(R.id.userInput);

		alertDialogBuilder.setView(promptView);

		alertDialogBuilder
				.setTitle("Your phone number")
				.setMessage("Please type in your mobile number")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								String val = input.getText().toString();

								if (val.equals(null) || val.length() == 0)
									return;

								Editor e = pref.edit();

								e.putString(NUMBER, val);

								e.commit();

							}
						}).setIcon(android.R.drawable.ic_dialog_alert);

		alertDialogBuilder.show();

	}

}
