package com.steins.flare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class SMSReportFlare extends SherlockFragment {

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

		(mActivity).getSupportActionBar().setTitle(
				"Describe it. No upper limit");

		createButtonListeners(view);

		return view;

	}

	public void createButtonListeners(View view) {

		Button flareButton = (Button) view
				.findViewById(R.id.descriptionNextButton);
		
		TextView tv = (TextView)view.findViewById(R.id.messageText1);
		
		tv.setText("Atleast 15 characters");
		
		flareButton.setText("Send");
		
		flareButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				MainActivity mActivity = ((MainActivity) getActivity());

				EditText mEdit = (EditText) mActivity
						.findViewById(R.id.descriptionText);

				String text = mEdit.getText().toString();

				if(text.length() < 15)
					return;
				
				mActivity.setUpSMSFlare(text);
				
				CompletedFlareFragment cFragment = new CompletedFlareFragment();
				
				cFragment.setSuccessState(true);
				
				mActivity.setFragment(cFragment);
				
				
				
			}
		});

	}
}
