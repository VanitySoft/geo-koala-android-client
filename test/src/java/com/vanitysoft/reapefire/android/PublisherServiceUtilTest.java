package com.vanitysoft.reapefire.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonProcessingException;
import org.junit.Test;
 

public class PublisherServiceUtilTest {

    
    @Test
    public void testThis() throws JsonProcessingException, IOException, Exception{
  
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
		+ "'/><br/&gt; [" + event.getUuid()
		+ "] Cell phone movements").getBytes());
	event.setEmail("unknown");
	event.setThumbnail(null);
Collection<Event> ets = new ArrayList<Event>();
ets.add(event);
	PublisherServiceUtil.publish(ets, "username", "password", "appKey");
	
    }
}
