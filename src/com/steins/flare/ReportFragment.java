package com.steins.flare;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ReportFragment extends SherlockFragment {
	
    private ActionBar actionBar;
    private ViewPager viewPager;
	
	InternetClient internetClient;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		setRetainInstance(true);

		super.onCreate(savedInstanceState);
	}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	
    	
		((MainActivity)getActivity()).getSupportActionBar().setTitle("Flare");
    	
    	View view  =  inflater.inflate(R.layout.report, null);
        	
    	internetClient = new InternetClient(); // This is an internal helper class with all internet related stuff
    	
    	boolean hasInternet = internetClient.haveNetworkConnection(getActivity().getApplicationContext());
    	
    	if(hasInternet){ //If the user has access to internet, it will post data through it.
    		
    		createInternetReport(view);
    		
    	}
    	else{	//Fall back to SMS
    		
    		createSMSReport(view);
 
    	}
    	
    	
    	return view;
    }
    
    public void createInternetReport(View view){
    	
    	//This starts the process of the report using Internet
    	
    	CameraFragment cFragment = new CameraFragment();
    	    	
    	MainActivity mActivity = (MainActivity)getActivity();
    	
    	mActivity.setTypeOfFlare("INTERNET");
    	
    	mActivity.setFragment(cFragment);
    	
    	
    	
    }
    
    public void createSMSReport(View view){
    	
    	MainActivity mActivity = (MainActivity)getActivity();
		
		mActivity.setTypeOfFlare("SMS");
		
    	
    }
    
	

}

