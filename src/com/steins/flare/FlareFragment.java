package com.steins.flare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

public class FlareFragment extends SherlockFragment {

	public void onCreate(Bundle savedInstanceState) {

		setRetainInstance(true);

		super.onCreate(savedInstanceState);
	}

	MainActivity mActivity = (MainActivity) getActivity();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.flare_layout, null);

		mActivity = (MainActivity) getActivity();

		(mActivity).getSupportActionBar().setTitle("Click Flare to send data");

		createFlareImageAndButton(view);

		return view;

	}

	public void createFlareImageAndButton(View view) {

		Button flareButton = (Button) view.findViewById(R.id.flareButton);

		flareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				ConfirmFlareDialogFragment confirmDialog = new ConfirmFlareDialogFragment();

				confirmDialog.show(mActivity.getSupportFragmentManager(),
						"HELLO");

			}
		});

	}



}
