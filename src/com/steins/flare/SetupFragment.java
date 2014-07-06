package com.steins.flare;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;

public class SetupFragment extends SherlockFragment {

	SharedPreferences sharedPrefs;

	Map<String, String> mHashMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		setRetainInstance(true);

		super.onCreate(savedInstanceState);

		sharedPrefs = getActivity().getSharedPreferences("com.steins.flare",
				Context.MODE_PRIVATE);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.setup_layout, null);

		createListeners(view);

		return view;

	}

	public void createListeners(View view) {

		final EditText editNumber = (EditText) view
				.findViewById(R.id.editNumber);

		final EditText editUploadNumber = (EditText) view
				.findViewById(R.id.editUploadNumber);

		final Spinner editCountry = (Spinner) view
				.findViewById(R.id.editCountry);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.CountryArray,
				android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Button saveButton = (Button) view.findViewById(R.id.saveSetupButton);

		Log.e("REACHED HERE", "REACHED");

		final InternetClient ic = new InternetClient();

		editCountry.setAdapter(adapter);

		editNumber.setText(sharedPrefs.getString(ic.NUMBER, ""));

		editUploadNumber.setText(sharedPrefs.getString(ic.UPLOAD_NUMBER, ""));

		String country = sharedPrefs.getString(ic.COUNTRY, "");

		if (country.length() != 0) {

			for (int i = 0; i < adapter.getCount(); i++) {

				String name = (String) adapter.getItem(i);

				if (name.equals(country)) {

					editCountry.setSelection(i);

					break;
				}

			}
		}

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String number = editNumber.getText().toString();

				String uploadNumber = editUploadNumber.getText().toString();

				if (number.length() < 5 || uploadNumber.length() < 5)
					return;

				String country = editCountry.getSelectedItem().toString();

				Editor edit = sharedPrefs.edit();

				edit.putString(ic.NUMBER, number);

				edit.putString(ic.UPLOAD_NUMBER, uploadNumber);

				edit.putString(ic.COUNTRY, country);

				edit.commit();

				Log.e("REACHED HERE", "REACHED HERE");

				back();
			}
		});

	}

	public void back() {

		getActivity().getSupportFragmentManager().popBackStack();

		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();

		transaction.setCustomAnimations(R.anim.enter, R.anim.exit,
				R.anim.pop_enter, R.anim.pop_exit);

		transaction.replace(R.id.content_frame, new StartFragment());
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();

	}
}
