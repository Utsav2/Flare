package com.steins.flare;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.util.Log;

public class Volunteer extends SherlockFragmentActivity {

	private Volunteer thisActivity;

	SharedPreferences sharedPrefs;

	public BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			InternetClient ic = new InternetClient();

			try {

				String json_string = intent.getStringExtra("Message");

				JSONObject json = new JSONObject(json_string);

				json.put("Country", sharedPrefs.getString(ic.COUNTRY, ""));

				ic.SMSReport(json, thisActivity);
			}

			catch (Exception e) {

			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		thisActivity = this;

		sharedPrefs = this.getSharedPreferences("com.steins.flare",
				Context.MODE_PRIVATE);

		setContentView(R.layout.fragment_layout);

		// sharedPrefs.edit().clear().commit();

		InternetClient ic = new InternetClient();

		if (!sharedPrefs.contains(ic.COUNTRY)
				|| !sharedPrefs.contains(ic.UPLOAD_NUMBER)
				|| !sharedPrefs.contains(ic.NUMBER)) {

			setFragment(new SetupFragment());

		}

	}

	@Override
	public void onResume() {

		super.onResume();

		registerReceiver(receiver, new IntentFilter("MINE_RECEIVED"));

	}

	@Override
	public void onPause() {

		super.onPause();

	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		unregisterReceiver(receiver);

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

}
