package com.steins.flare;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

//This class was taken from http://stackoverflow.com/a/21635786/3399432
//and modified to suit this application's needs.
//It is mostly a reimplementation of Request<T> of Google Volley using the http.entity.mime.MultipartEntityBuilder
//Thus the application requires httpclient and httpcore from Apache.


public class MultipartRequest extends Request<String> {

	MultipartEntityBuilder entity = MultipartEntityBuilder.create();
	HttpEntity httpentity;
	private static final String FILE_PART_NAME = "image";

	private final Response.Listener<String> mListener;
	private final File mFilePart;
	private final Map<String, String> mStringPart;

	public MultipartRequest(String url, Response.ErrorListener errorListener,
			Response.Listener<String> listener, File file,
			Map<String, String> mStringPart) {
		super(Method.POST, url, errorListener);

		mListener = listener;
		mFilePart = file;

		this.mStringPart = mStringPart;
		entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		buildMultipartEntity();
	}

	public void addStringBody(String param, String value) {
		mStringPart.put(param, value);
	}

	private void buildMultipartEntity() {

		// Checks whether user decided to upload image or skip it.

		if (mFilePart != null) {
			entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));
		}
		for (Map.Entry<String, String> entry : mStringPart.entrySet()) {

			
			//Adding the location, description, etc to the body
			entity.addTextBody(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getBodyContentType() {
		return httpentity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			httpentity = entity.build();
			httpentity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		return Response.success("Uploaded", getCacheEntry());
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}
}