package com.steins.flare;

import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
	private final String DEBUG_TAG = getClass().getSimpleName().toString();
	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private Context mContext;
	private Intent mIntent;

	// Retrieve SMS
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		mIntent = intent;

		String action = intent.getAction();

		if (action.equals(ACTION_SMS_RECEIVED)) {

			String address, str = "";
			int contactId = -1;

			SmsMessage[] msgs = getMessagesFromIntent(mIntent);
			if (msgs != null) {
				for (int i = 0; i < msgs.length; i++) {
					address = msgs[i].getOriginatingAddress();
					str += msgs[i].getMessageBody().toString();
					str += "\n";
				}
			}

			showNotification(context, str);

			// ---send a broadcast intent to update the SMS received in the
			// activity---
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction("SMS_RECEIVED_ACTION");
			broadcastIntent.putExtra("sms", str);
			context.sendBroadcast(broadcastIntent);
		}

	}

	public static SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}

	/**
	 * The notification is the icon and associated expanded entry in the status
	 * bar.
	 */
	protected void showNotification(Context context, String message) {

		try {

			JSONObject try_if_message = new JSONObject(message);

			if (try_if_message.has("IMEI") && try_if_message.has("Message")
					&& try_if_message.has("Time")) {

				Intent i = new Intent("MINE_RECEIVED");

				i.putExtra("Message", message);

				context.sendOrderedBroadcast(i, null, new BroadcastReceiver() {

					@Override
					public void onReceive(Context context, Intent intent) {

						Log.e("RECEIVED", "RECEIVED");

					}

				}, null, 0, message, null);

			}

		}
		// Not a Mine message
		catch (Exception e) {

			// Nothing.

		}
	}
}