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

package org.springframework.android.basicauth;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Jeryl Cook
 */
public class MainActivity extends AbstractAsyncActivity {
    private Location lastLocation;

    protected static final String TAG = MainActivity.class.getSimpleName();

    // ***************************************
    // Activity methods
    // ***************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.main_activity_layout);

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
	    AsyncTask<Void, Void, Message> {

	private final String username = "test@gmail.com";

	private final String password = "password";

	private final String accountKey = "e391hf01U9838hdo1hUs19IJFIQFa";

	@Override
	protected void onPreExecute() {
	    showLoadingProgressDialog();

	    // build the message object
	    // EditText editText = (EditText) findViewById(R.id.username);
	    // this.username = editText.getText().toString();

	    // editText = (EditText) findViewById(R.id.password);
	    // this.password = editText.getText().toString();

	    // editText = (EditText) findViewById(R.id.accountKey);
	    // this.accountKey = editText.getText().toString();

	}

	@Override
	protected Message doInBackground(Void... params) {
	    // will keep running every 1 minute push to Reaper.
	    final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	    final String uuid = telephonyManager.getDeviceId();

	    final Collection<Event> events = new ArrayList<Event>();

	    while (true) {
		Location lastFix = null;
		final boolean isGPSOn = locationManager
			.isProviderEnabled(LocationManager.GPS_PROVIDER);
		double azimuth = 0.0;
		double inclination = 0.0;

		if (isGPSOn) {
		    lastFix = locationManager
			    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
		    azimuth = locationManager.getLastKnownLocation(
			    LocationManager.GPS_PROVIDER).getBearing();
		    inclination = locationManager.getLastKnownLocation(
			    LocationManager.GPS_PROVIDER).getAltitude();
		} else {
		    lastFix = locationManager
			    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		    azimuth = locationManager.getLastKnownLocation(
			    LocationManager.NETWORK_PROVIDER).getBearing();
		    inclination = locationManager.getLastKnownLocation(
			    LocationManager.NETWORK_PROVIDER).getAltitude();
		}
		if (lastLocation == null) {
		    final Event event = new Event();
		    event.setDateTime(new Date());
		    event.setIndexKey(accountKey);
		    event.setAzimuth(azimuth);
		    event.setInclination(inclination);
		    event.setLatitude(lastFix.getLatitude());
		    event.setLongitude(lastFix.getLongitude());
		    event.setUuid(uuid);
		    event.setUrl("http://www.vanity-soft.com");
		    event.setDescription("Jery's Cell phone movements");
		    event.setEmail("jeryl.cook@vanity-soft.com");
		    event.setThumbnail("http://www.google.com/landing/chrome/ugc/chrome-icon.jpg");
		    synchronized (events) {
			events.add(event);
		    }

		    try {
			publish(events);
		    } catch (final JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    lastLocation = lastFix;
		    continue;
		}

		if (lastLocation != null) {
		    final double distance = lastFix.distanceTo(lastLocation);
		    if (distance >= 20) {// record if distance is greater than
					 // 10
			final Event event = new Event();
			event.setDateTime(new Date());
			event.setIndexKey(accountKey);
			event.setAzimuth(azimuth);
			event.setInclination(inclination);
			event.setLatitude(lastFix.getLatitude());
			event.setLongitude(lastFix.getLongitude());
			event.setUuid(uuid);
			synchronized (events) {
			    events.add(event);
			}
		    }// Don't add a new event if less than 10meters.
		}
		lastLocation = lastFix;

		if (events.size() >= 1) {// send everu 100
		    try {
			publish(events);
		    } catch (final JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
		try {
		    Thread.sleep(5000);
		} catch (final InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }

	}

	private void publish(Collection<Event> events)
		throws JsonProcessingException, IOException {

	    final JsonFactory jsonFactory = new JsonFactory();
	    final StringWriter writer = new StringWriter();
	    final JsonGenerator jsonGenerator = jsonFactory
		    .createJsonGenerator(writer);
	    jsonGenerator.useDefaultPrettyPrinter();

	    jsonGenerator.writeStartObject();
	    jsonGenerator.writeStringField("type", "FeatureCollection");
	    jsonGenerator.writeFieldName("features");
	    jsonGenerator.writeStartArray();

	    for (final Event event : events) {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("type", "Feature");
		jsonGenerator.writeObjectFieldStart("geometry");
		jsonGenerator.writeStringField("type", "Point");
		jsonGenerator.writeArrayFieldStart("coordinates");
		jsonGenerator.writeNumber(event.getLatitude());
		jsonGenerator.writeNumber(event.getLongitude());
		jsonGenerator.writeEndArray();

		jsonGenerator.writeObjectFieldStart("properties");
		jsonGenerator.writeStringField("indexKey", event.getIndexKey());

		jsonGenerator.writeStringField("uuid", event.getUuid());
		jsonGenerator.writeStringField("dateTime",
			new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(event
				.getDateTime()));
		jsonGenerator.writeNumberField("azimuth", event.getAzimuth());
		jsonGenerator.writeNumberField("inclination",
			event.getInclination());
		jsonGenerator.writeStringField("address", event.getAddress());
		jsonGenerator.writeStringField("attachment",
			event.getAttachment());
		jsonGenerator.writeStringField("info", event.getInfo());
		jsonGenerator.writeStringField("thumbnail",
			event.getThumbnail());
		jsonGenerator.writeStringField("phone", event.getPhone());
		jsonGenerator.writeStringField("url", event.getUrl());
		jsonGenerator.writeStringField("email", event.getEmail());

		final Map<String, String> metadataItems = event.getMetaData();
		if (metadataItems != null) {
		    for (final Entry<String, String> entry : metadataItems
			    .entrySet()) {
			jsonGenerator.writeStringField(entry.getKey(),
				entry.getValue());
		    }
		}

		jsonGenerator.writeEndObject();
		jsonGenerator.writeEndObject();
		jsonGenerator.writeEndObject();
	    }
	    jsonGenerator.writeEndArray();
	    jsonGenerator.writeObjectFieldStart("properties");
	    jsonGenerator.writeStringField("total",
		    Integer.toString(events.size()));
	    jsonGenerator.writeEndObject();
	    jsonGenerator.flush();
	    jsonGenerator.close();

	    final ObjectMapper mapper = new ObjectMapper();
	    final JsonNode eventsNode = mapper.readTree(writer.getBuffer()
		    .toString());

	    final HttpBasicAuthentication authHeader = new HttpBasicAuthentication(
		    username, password);
	    
	    final HttpHeaders requestHeaders = new HttpHeaders();
	    requestHeaders.setAuthorization(authHeader);
	    requestHeaders.setAccept(Collections
		    .singletonList(MediaType.APPLICATION_JSON));

	    // Create a new RestTemplate instance
	    final RestTemplate restTemplate = new RestTemplate();

	    restTemplate.getMessageConverters().add(
		    new MappingJacksonHttpMessageConverter());

	    try {
		// Make the network request
		final String url = "http://api.reaperfire.com/rest"																																																								
			+ "/v1/events.json?type=geoJson1_0&indexKey="
			+ accountKey;
		final HttpEntity<JsonNode> entity = new HttpEntity<JsonNode>(
			eventsNode, requestHeaders);
		Log.d(TAG, url);
		final ResponseEntity<JsonNode> response = restTemplate
			.exchange(url, HttpMethod.POST, entity, JsonNode.class);
		Log.e(TAG, response.toString());
		synchronized (events) {
		    events.clear();
		}
	    } catch (final HttpClientErrorException e) {
		Log.e(TAG, e.getLocalizedMessage(), e);

	    }
	}

	@Override
	protected void onPostExecute(Message result) {
	    dismissProgressDialog();
	    displayResponse(result);
	}

    }

}
