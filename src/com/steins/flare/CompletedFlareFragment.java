package com.steins.flare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class CompletedFlareFragment extends SherlockFragment {

	private boolean mSuccesfulFlare;

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

		(mActivity).getSupportActionBar().setTitle("Flare sent");

		createStatus(view);

		return view;

	}

	// Sets whether the upload was successful or not.

	// IMPORTANT: Needs to be set before creating the view of the fragment, as
	// it determines whether the flare was a success
	// or failure

	public void setSuccessState(boolean state) {

		mSuccesfulFlare = state;
	}

	public boolean getSuccessState() {

		return mSuccesfulFlare;
	}

	public void createStatus(View view) {

		ImageView image = (ImageView) view.findViewById(R.id.statusImage);

		int resID;

		// the upload succeeded

		if (mSuccesfulFlare) {

			resID = getResources().getIdentifier("green_check_mark",
					"drawable", getActivity().getPackageName());

			completedFlare(view);

		}

		else {

			resID = getResources().getIdentifier("red_cross", "drawable",
					getActivity().getPackageName());

		}

		image.setImageResource(resID);

	}

	public void completedFlare(View view) {

		Button completedButton = (Button) view
				.findViewById(R.id.completedFlareButton);

		completedButton.setVisibility(view.VISIBLE);

		completedButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(getActivity().getApplicationContext(), "HI",
						Toast.LENGTH_SHORT).show();

			}
		});
	}
}
