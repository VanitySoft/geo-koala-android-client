/*
 * Copyright 2010-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vanitysoft.reapefire.android;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.codehaus.jackson.JsonProcessingException;
import org.json.JSONObject;
import org.springframework.android.basicauth.R;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;

/**
 * @author Jeryl Cook
 */
public class MainActivity extends AbstractAsyncActivity {

    protected static final String TAG = MainActivity.class.getSimpleName();

    // ***************************************
    // Activity methods
    // ***************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.main_activity_layout);

	EditText editText = (EditText) findViewById(R.id.username);
	editText.setText("geokoala", BufferType.EDITABLE);

	EditText editTextPassword = (EditText) findViewById(R.id.password);
	editTextPassword.setText("fbde14be-bfc5-496f-a56b-6df55644ff27", BufferType.EDITABLE);

	EditText editTextAppKey = (EditText) findViewById(R.id.accountKey);
	editTextAppKey.setText("393233d9-7997-4649-8291-73102d4b7358",
		BufferType.EDITABLE);

	// Initiate the request to the protected service
	final Button submitButton = (Button) findViewById(R.id.submit);
	
        	submitButton.setOnClickListener(new View.OnClickListener() {
        	    @Override
        	    public void onClick(View v) {
        		new FetchSecuredResourceTask().execute();
        	    }
        	});
    
    }

    // ***************************************
    // Private methods
    // ***************************************
    private void displayResponse(Message response) {
	Toast.makeText(this, response.getText(), Toast.LENGTH_LONG).show();
    }

    // ***************************************
    // Private classes
    // ***************************************
    private class FetchSecuredResourceTask<HttpAuthentication> extends
	    AsyncTask<Object, Object, Message> {

	private String username = "test@gmail.com";

	private String password = "password";

	private String accountKey = "e391hf01U9838hdo1hUs19IJFIQFa";

	@Override
	protected void onPreExecute() {
	    showLoadingProgressDialog();
 
	    EditText editText = (EditText) findViewById(R.id.username);
	    this.username = editText.getText().toString();

	    editText = (EditText) findViewById(R.id.password);
	    this.password = editText.getText().toString();

	    editText = (EditText) findViewById(R.id.accountKey);
	    this.accountKey = editText.getText().toString();
 
	}

	@Override
	protected Message doInBackground(Object... params) {
	    // will keep running every 1 minute push to Reaper.
	 Message message = new Message();
	 for(int index=0;index< 1;index++){
	       
	   
	    final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    
	    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	    
	    final String uuid = getPhoneNumber();

	    final Collection<Event> events = new ArrayList<Event>();

	    // while (true) {
	    Location lastFix = null;
	    final boolean isGPSOn = locationManager
		    .isProviderEnabled(LocationManager.GPS_PROVIDER);
	    double azimuth = 0.0;
	    double inclination = 0.0;

	    if (isGPSOn) {
		lastFix = locationManager
			.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	    }
	    if (lastFix == null) {
		lastFix = locationManager
			.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    }

	    if (lastFix != null) {
		if (isGPSOn) {
		    azimuth = lastFix.getBearing();
		    inclination = lastFix.getAltitude();
		}
		final Event event = new Event();

		event.setDateTime(new Date());
		event.setIndexKey(accountKey);
		event.setAzimuth(azimuth);
		event.setInclination(inclination);
		event.setLatitude(lastFix.getLatitude());
		event.setLongitude(lastFix.getLongitude());
		event.setUuid(uuid);

		String googleStreetView = "https://maps.googleapis.com/maps/api/streetview?size=400x400&location="
			+ event.getLatitude()
			+ ","
			+ event.getLongitude()
			+ "&fov=90&heading="
			+ event.getAzimuth()
			+ "&pitch="
			+ event.getInclination() + "";

		event.setUrl(googleStreetView);

		try {
		    event.setDescription(new String("<img src='" + googleStreetView
		    	+ "'/><br/&gt; [" + event.getUuid()
		    	+ "] Cell phone movements [" + PublisherServiceUtil.toDateTimeString(event.getDateTime()) ).getBytes());
		} catch (ParseException e1) {
 
		    e1.printStackTrace();
		}
		
		event.setEmail("jeryl.cook@vanity-soft.com");
		
		event.setThumbnail(null);

		synchronized (events) {
		    events.add(event);
		}

		try {

		    Log.e(TAG, "Publishing[ lat " + event.getLatitude()
			    + ", long " + event.getLongitude() + "]");
		  
		    JSONObject jsonResponse  =  PublisherServiceUtil.publish(events,username,password,accountKey);
		   
		   
		    message.setText(jsonResponse.toString(4));
		   
		  
		} catch (final JsonProcessingException e) {
		    message.setText(e.getMessage());
		} catch (final Exception e) {
		    message.setText(e.getMessage());
		}

	    } else {
		message.setText("GPS enabled? We Could not get a location. try again...");

	    }

	    Log.e(TAG, "doInBackground....!!!");
	    /*
	 	try {
		    Thread.sleep(5000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		*/
	 	  //  dismissProgressDialog();
		  //  displayResponse(message);
	}

	    return message;
	}

	
	@Override
	protected void onPostExecute(Message result) {
	    dismissProgressDialog();
	    displayResponse(result);
	}
	
	private String getPhoneNumber(){
	    TelephonyManager mTelephonyMgr;
	    mTelephonyMgr = (TelephonyManager)
	        getSystemService(Context.TELEPHONY_SERVICE); 
	    return mTelephonyMgr.getLine1Number();
	}

	private String get10DigitPhoneNumber(){
	    String s = getPhoneNumber();
	    return s != null && s.length() > 2 ? s.substring(2) : null;
	}
	


    }

}
