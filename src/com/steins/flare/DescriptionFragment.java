package com.steins.flare;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DescriptionFragment extends SherlockFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		setRetainInstance(true);

		super.onCreate(savedInstanceState);
	}

	
	MainActivity mActivity = (MainActivity) getActivity();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		View view = inflater.inflate(R.layout.description_layout, null);
		
		mActivity = (MainActivity) getActivity();

		(mActivity).getSupportActionBar().setTitle("Describe it a little");
		
		createButtonListeners(view);
		
		return view;
			
	}
	
	public void createButtonListeners(View view){
				
		Button flareButton = (Button) view.findViewById(R.id.descriptionNextButton);
		
		flareButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				MainActivity mActivity = ((MainActivity)getActivity());
				
				EditText mEdit = (EditText)mActivity.findViewById(R.id.descriptionText);
				
				mActivity.setDescriptionText(mEdit.getText().toString());
				
				mActivity.setFragment(new FlareFragment());
				
			}
		});

	}

}
