package com.steins.flare;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SMSFlareFragment extends SherlockFragment {

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
		
		createSMSView(view);
		
		return view;

	}
	
	public void createSMSView(View view){
		
		
		
	}
	
}
