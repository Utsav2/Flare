/**
 * 
 */
package com.steins.flare;

import java.util.List;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.*;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Utsav Shah
 * 
 */
public class LocationFragment extends SherlockFragment implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private GoogleMap map;
	private SupportMapFragment fragment;
	LocationClient locationClient;
	MainActivity mActivity = (MainActivity) getActivity();
	LatLng lastMine;

	public void onCreate(Bundle savedInstanceState) {

		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setRetainInstance(true);

		mActivity = (MainActivity) getActivity();

		(mActivity).getSupportActionBar().setTitle("Select where the mine is");

		if (mActivity.areCoordinatesSaved()) {

			lastMine = mActivity.getCoordinates();

		}
		
		View view = inflater
				.inflate(R.layout.location_layout, container, false);
		map = ((SupportMapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.setMyLocationEnabled(true);

		locationClient = new LocationClient(mActivity.getApplicationContext(),
				this, this);

		locationClient.connect();

		createButtonListeners(view);

		return view;
	}

	@Override
	public void onDestroyView() {

		// http://stackoverflow.com/a/18905704/3399432

		super.onDestroyView();
		try {
			SupportMapFragment fragment = (SupportMapFragment) getActivity()
					.getSupportFragmentManager().findFragmentById(R.id.map);
			if (fragment != null)
				getFragmentManager().beginTransaction().remove(fragment)
						.commit();

		} catch (IllegalStateException e) {

		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {

		Location location = locationClient.getLastLocation();
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		LatLng coordinate = new LatLng(lat, lng);

		if (lastMine == null)
			lastMine = coordinate;

		// http://stackoverflow.com/a/14153018/3399432

		map.addMarker(new MarkerOptions().position(lastMine)
				.title("Mine location")
				.snippet("Please move the marker if needed.").draggable(true));
		
		moveToCoordinate(lastMine);

		map.setOnMarkerDragListener(new OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker marker) {
				(mActivity).getSupportActionBar().setTitle(
						"Selected mine position");
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				LatLng position = marker.getPosition();
				lastMine = position;
				String lat = Double.valueOf((position.latitude)).toString()
						.substring(0, 8);
				String lng = Double.valueOf((position.longitude)).toString()
						.substring(0, 8);
				(mActivity).getSupportActionBar().setTitle(lat + ", " + lng);
			}

			@Override
			public void onMarkerDrag(Marker marker) {
				(mActivity).getSupportActionBar().setTitle(
						"Changing mine position...");
			}
		});

	}

	public void moveToCoordinate(LatLng coordinate) {
		
		map.moveCamera( CameraUpdateFactory.newLatLngZoom(coordinate , 14.0f) );
	}

	public void createButtonListeners(View view) {

		final DescriptionFragment dFragment = new DescriptionFragment();

		Button sendButton = (Button) view.findViewById(R.id.nextLocationButton);

		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				
				LocationDataAsync asyncSetter = new LocationDataAsync(mActivity, lastMine);
			
				asyncSetter.doInBackground(null);

				mActivity.setFragment(dFragment);

			}
		});


	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}
	


}
