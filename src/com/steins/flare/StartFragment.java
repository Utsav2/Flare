package com.steins.flare;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class StartFragment extends SherlockFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		setRetainInstance(true);

		super.onCreate(savedInstanceState);
	}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	    	
    	View view  =  inflater.inflate(R.layout.report_or_volunteer, null);
    	
    	createButtonListeners(view);

    	return view;
    }
    
    public void createButtonListeners(View view){
    	    	
    	Button reportButton = (Button) view.findViewById(R.id.reportButton);
    	
    	Button volunteerButton = (Button) view.findViewById(R.id.volunteerButton);
    	
    	reportButton.setOnClickListener(new OnClickListener(){
    		
            public void onClick(View view) {
            	
            	ReportFragment rFragment = new ReportFragment();

            	setUserType(rFragment);

            }		
    	});
    	
    	
    	
    	volunteerButton.setOnClickListener(new OnClickListener(){
    		
            public void onClick(View view) {
            	
            	VolunteerFragment vFragment = new VolunteerFragment();

            	setUserType(vFragment);
            	
            }		
    	});
    	   	
    }
    
    public void setUserType(Fragment type){
    	
    	MainActivity mActivity = (MainActivity) getActivity();
    	
    	mActivity.setTypeOfUser(type);
    	    	
    	mActivity.setFragment(type, this);
    	
    }
    
    
}
