package com.steins.flare;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class ImageViewFragment extends SherlockFragment {

	String mImagePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		setRetainInstance(true);

		super.onCreate(savedInstanceState);
	}

	MainActivity mActivity = (MainActivity) getActivity();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.completed_flare, null);

		mActivity = (MainActivity) getActivity();

		(mActivity).getSupportActionBar().setTitle("Image");

		createImageView(view);

		createButton(view);

		return view;

	}

	public void createImageView(View view) {

		ImageView image = (ImageView) view.findViewById(R.id.statusImage);

		Bitmap bmp = BitmapFactory.decodeFile(mImagePath);

		image.setImageBitmap(bmp);

	}

	public void createButton(View view) {
		
		final MainActivity mActivity = (MainActivity)getActivity();
		
		final LocationFragment lFragment = new LocationFragment();
		
		Button completedButton = (Button) view
				.findViewById(R.id.completedFlareButton);

		completedButton.setVisibility(view.VISIBLE);

		completedButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mActivity.setImage(mImagePath);
				
				mActivity.setFragment(lFragment);
				
			}
		});

	}

	public void setImagePath(String path) {

		mImagePath = path;
	}
	
	
	public void onBackPressed(){
		
		new AlertDialog.Builder(getActivity())
	    .setTitle("Delete image")
	    .setMessage("Are you sure you want to take a new image?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            
	        	File f = new File(mImagePath);
	        	
	        	f.delete();
	        	
	        	getActivity().getSupportFragmentManager().popBackStackImmediate();
	        	
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
		
	}
}
