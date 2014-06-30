package com.steins.flare;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameraFragment extends SherlockFragment {

	Uri imageUri;

	MainActivity mActivity = (MainActivity) getActivity();

	LocationFragment lFragment = new LocationFragment();

	protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	String mCurrentPhotoPath;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				"Take a picture");

		View view = inflater.inflate(R.layout.camera_layout, null);

		createButtonListeners(view);

		return view;

	}

	public void createButtonListeners(View view) {

		final MainActivity mActivity = (MainActivity) getActivity();

		final LocationFragment lFragment = new LocationFragment();

		ImageButton cameraButton = (ImageButton) view
				.findViewById(R.id.cameraButton);

		Button skipButton = (Button) view.findViewById(R.id.skipButton);

		cameraButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				takePicture(mActivity);

			}
		});

		skipButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				mActivity.setFragment(lFragment);

			}
		});

	}

	public boolean takePicture(MainActivity mActivity) {

		Context mContext = mActivity.getApplicationContext();

		PackageManager packageManager = mContext.getPackageManager();

		JSONObject json = new JSONObject();

		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {

			return false;
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		imageUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "fname_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == getActivity().RESULT_OK) {

			complete();

		} else if (resultCode == getActivity().RESULT_CANCELED) {
			Toast.makeText(getActivity(), "Picture was not taken",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

	}

	public void complete() {

		// ((MainActivity)getActivity()).setImage(imageUri.getPath());

		ImageViewFragment iFragment = new ImageViewFragment();

		iFragment.setImagePath(imageUri.getPath());

		((MainActivity) getActivity()).setFragment(iFragment);

	}

}
