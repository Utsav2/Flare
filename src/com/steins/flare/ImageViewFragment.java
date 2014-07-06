package com.steins.flare;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

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
		    	
		makeImageSmall(bmp);

		image.setImageBitmap(bmp);

	}
	
	public void makeImageSmall(Bitmap bmp){
				
		
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		
		bmp.compress(Bitmap.CompressFormat.JPEG, 30, bytes);
		
		File f = new File(mImagePath);
		
		try {
			
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();

		} catch (IOException e) {
			
			Log.e("ERROR IN IMAGE", "");
			
			e.printStackTrace();
		}


		
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
