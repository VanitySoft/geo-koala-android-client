package com.vanitysoft.reapefire.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.Point;

public class PublisherServiceUtil {

    protected static final String TAG = PublisherServiceUtil.class.getSimpleName();
    
    public static JSONObject publish(Collection<Event> events,String username,String password,String appKey)
		throws JsonProcessingException, IOException, Exception{
	   
	    JSONObject statusJSONObject = new JSONObject();
	     
	    FeatureCollection featureCollection = new FeatureCollection();
	 	 
	     
	    for (final Event event :events) {	   
		    Feature feature = new Feature();
		   
		    Point point =  new Point( event.getLatitude(),event.getLongitude());
		
		    feature.setGeometry(point);
		    
		    JSONObject jsonObject = new JSONObject();
		    
		    final Map<String, Object> metadataItems = event.getMetaData();
		    if (metadataItems != null) {
			for (final Entry<String, Object> entry : metadataItems
				.entrySet()) {	     
			    jsonObject.put(entry.getKey(),
				     entry.getValue());
			}
		    }
		    //Add event to properties.
		    jsonObject.put("email", event.getEmail());
		     
		    feature.setProperties(jsonObject);
			  
		    featureCollection.addFeature(feature);
	 
		}
	    
	    	JSONObject topNode = featureCollection.toJSON();		
		 
		Log.e(TAG,topNode.toString());
		
		 
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
		final String url = "http://api.reaperfire.com/reaperfire/rest"
			+ "/v1/public/accounts/" + appKey + "/events";
		
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode featureCollectionJsonNode = mapper.readTree(topNode.toString());
		
		final HttpEntity< JsonNode > entity = new HttpEntity<JsonNode>(
			featureCollectionJsonNode , requestHeaders);
		
		Log.e(TAG, "post url [" + url +"]");
		
	 	
		final ResponseEntity<JsonNode> response = restTemplate
			.exchange(url, HttpMethod.POST, entity,JsonNode.class);
	 
		statusJSONObject.put("response", response);
	
		synchronized (events) {
		    events.clear();
		}
	    } catch (final HttpClientErrorException e) {
		Log.e(TAG, e.getLocalizedMessage(), e);
		statusJSONObject.put("error", e.getLocalizedMessage());
	    }
	    
	    return statusJSONObject;
	 
	}
    
    private static Collection<Integer> createArrayList(byte[] description) {
		ArrayList<Integer> byteArray = new ArrayList<Integer>();
		for(byte by:description){
		    byteArray.add((int)by);
		}
		return byteArray;
	    }

}
