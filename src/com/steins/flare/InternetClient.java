package com.steins.flare;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class InternetClient {

	private String uploadUrl = "http://requestb.in/1gbrdvu1";
	
	public static final int REQUEST_TIMEOUT_MS = 30000;

	public void setUploadUrl(String url) {

		this.uploadUrl = url;

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

	public void postData(Context mContext, String imagePath, String descriptionText, String lat,
			String lng) {
		
		RequestQueue req = Volley.newRequestQueue(mContext);  
		

		File dir = Environment.getExternalStorageDirectory();
		
		File image = null;
		
		if(imagePath != null){
			
			image = new File(imagePath);
		}

		Map<String, String> mHashMap = new HashMap<String, String>();
		
		mHashMap.put("Description", descriptionText);
		
		mHashMap.put("Latitude", lat);
		
		mHashMap.put("Longitude", lng);

		String url = getUploadUrl();

		MultipartRequest myRequest = new MultipartRequest(url,

		new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// error
				Log.e("Error.Response", "" + error.networkResponse);
			}
		}, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Log.d("Response", response);
			}
		},

		image,

		mHashMap

		);
		
		myRequest.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		
		req.add(myRequest);
		
	}
}
