package com.vanitysoft.reapefire.android;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.Point;

public class PublisherServiceUtil {

    protected static final String TAG = PublisherServiceUtil.class
	    .getSimpleName();

    
    public static  JSONObject convertToJson(Collection<Event> events) throws JSONException, ParseException{
  
   	FeatureCollection featureCollection = new FeatureCollection();

   	for (final Event event : events) {
   	    Feature feature = new Feature();

   	    Point point = new Point(event.getLatitude(), event.getLongitude());

   	    feature.setGeometry(point);

   	    JSONObject jsonObject = new JSONObject();

   	    final Map<String, Object> metadataItems = event.getMetaData();
   	    if (metadataItems != null) {
   		for (final Entry<String, Object> entry : metadataItems
   			.entrySet()) {
   		    jsonObject.put(entry.getKey(), entry.getValue());
   		}
   	    }
   	    // Add event to properties.
  
   	    jsonObject.put("description",createJSONArray(event.getDescription()));
   	    jsonObject.put("address", event.getAddress());
   	    jsonObject.put("attachment", event.getAttachment());
   	    jsonObject.put("azimuth", event.getAzimuth());
   	    jsonObject.put("dateTime", toDateTimeString(event.getDateTime()));// comeback
   	    jsonObject.put("email", event.getEmail());
   	    jsonObject.put("inclination", event.getInclination());
   	    jsonObject.put("appKey", event.getIndexKey());
   	    jsonObject.put("info", event.getInfo());
   	    jsonObject.put("phone", event.getPhone());
   	    jsonObject.put("url", event.getUrl());
   	    jsonObject.put("uuid", event.getUuid());

   	    feature.setProperties(jsonObject);

   	    featureCollection.addFeature(feature);

   	}

 
   	return featureCollection.toJSON();

       }
    
    
    public static JSONObject publish(Collection<Event> events, String username,
	    String password, String appKey) throws JsonProcessingException,
	    IOException, Exception {

	JSONObject statusJSONObject = new JSONObject();

	JSONObject topNode = convertToJson(events);

	final HttpBasicAuthentication authHeader = new HttpBasicAuthentication(
		username, password);

	final HttpHeaders requestHeaders = new HttpHeaders();

	requestHeaders.setAuthorization(authHeader);

	requestHeaders.add("X-Mashape-Key",
		"E0yCNooETJmsh1J1S4me9PvLaZgXp1Ryh4LjsnsSSjbIYqOxYl");
	requestHeaders.add("Content-Type", "application/json");

	requestHeaders.setAccept(Collections
		.singletonList(MediaType.APPLICATION_JSON));

	HttpParams params = new BasicHttpParams();
	HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	HttpProtocolParams.setContentCharset(params,
		HTTP.DEFAULT_CONTENT_CHARSET);
	HttpProtocolParams.setUseExpectContinue(params, true);

	SchemeRegistry schReg = new SchemeRegistry();
	schReg.register(new Scheme("https",
		SSLSocketFactory.getSocketFactory(), 443));
	ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
		params, schReg);

	HttpClient client = new DefaultHttpClient(conMgr, params);

	final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
	factory.setHttpClient(client);

	// Create a new RestTemplate instance
	final RestTemplate restTemplate = new RestTemplate(factory);

	restTemplate.getMessageConverters().add(
		new MappingJacksonHttpMessageConverter());

	final String url = "https://twoencore-reaperfire-aka-geokoala-v1.p.mashape.com/rest/v1/public/accounts/"
		+ appKey + "/events";
	 JsonNode featureCollectionJsonNode = null;
	try {
	    final ObjectMapper mapper = new ObjectMapper();

	    featureCollectionJsonNode = mapper.readTree(topNode
		    .toString());

	    final HttpEntity<JsonNode> entity = new HttpEntity<JsonNode>(
		    featureCollectionJsonNode, requestHeaders);

	    trustSelfSignedSSL();

	    final ResponseEntity<JsonNode> response = restTemplate.exchange(
		    url, HttpMethod.POST, entity, JsonNode.class);

	    statusJSONObject.put("response", response);

	    synchronized (events) {
		events.clear();
	    }
	} catch (final HttpClientErrorException ex) {
	    StringWriter errors = new StringWriter();
	    ex.printStackTrace(new PrintWriter(errors));
	    statusJSONObject.put("url", url);
	    statusJSONObject.put("error", errors.toString());
	}

	return statusJSONObject;

    }

    public static Collection<Integer> createArrayList(byte[] description) {
	ArrayList<Integer> byteArray = new ArrayList<Integer>();
	for (byte by : description) {
	    byteArray.add((int) by);
	}
	return byteArray;
    }
    
    public static JSONArray createJSONArray(byte[] description){
	    JSONArray jsonArray = new JSONArray();   
		for (byte by : description) {
		    jsonArray.put((int) by);
		}	    
	    return jsonArray;
    }

    public static String toDateTimeString(Date date) throws ParseException {
	final SimpleDateFormat simpleDateformat = new SimpleDateFormat(
		"MM/dd/yy HH:mm:ss");
	return simpleDateformat.format(date);
    }

    public static void trustSelfSignedSSL() {
	try {
	    SSLContext ctx = SSLContext.getInstance("TLS");
	    X509TrustManager tm = new X509TrustManager() {

		@SuppressWarnings("unused")
		public void checkClientTrusted(X509Certificate[] xcs,
			String string) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] xcs,
			String string) throws CertificateException {
		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		    return null;
		}

		@Override
		public void checkClientTrusted(
			java.security.cert.X509Certificate[] arg0, String arg1)
			throws java.security.cert.CertificateException {
		}

		@Override
		public void checkServerTrusted(
			java.security.cert.X509Certificate[] arg0, String arg1)
			throws java.security.cert.CertificateException {

		}
	    };
	    ctx.init(null, new TrustManager[] { tm }, null);
	    SSLContext.setDefault(ctx);
	} catch (Exception ex) {
	    throw new RuntimeException("Exception occurred ", ex);
	}
    }

}
