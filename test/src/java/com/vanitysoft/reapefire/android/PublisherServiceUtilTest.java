package com.vanitysoft.reapefire.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.codehaus.jackson.JsonProcessingException;

public class PublisherServiceUtilTest {

    public void testThis() throws JsonProcessingException, IOException,
	    Exception {

	final Event event = new Event();

	String googleStreetView = "https://maps.googleapis.com/maps/api/streetview?size=400x400&location="
		+ event.getLatitude()
		+ ","
		+ event.getLongitude()
		+ "&fov=90&heading="
		+ event.getAzimuth()
		+ "&pitch="
		+ event.getInclination() + "";

	event.setUrl(googleStreetView);

	event.setDescription(new String("<img src='" + googleStreetView
		+ "'/><br/&gt; [" + event.getUuid() + "] Cell phone movements")
		.getBytes());
	
	event.setEmail("jeryl.cook@vanity-soft.com");
	
	event.setThumbnail(null);

	Collection<Event> ets = new ArrayList<Event>();
	
	ets.add(event);
	 PublisherServiceUtil.convertToJson(ets);
	
	

    }
}
