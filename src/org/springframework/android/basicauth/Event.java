package org.springframework.android.basicauth;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
 
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
 

public class Event {
	Long id;
	String indexKey="";//Which index this event belongs to.
	String description = "";//THIS SHOULD BE HTML! for KARML 
	String attachment = "";
	String address = "";
	String email = "";;
	String url = "http://www.vanity-soft.com";
	String thumbnail = "";
	String info   = "";
	String phone = "";
	String uuid= "";
 
	double azimuth;
	double inclination;
	double latitude;
	double longitude;
	Date dateTime;
	double geoDistance;
	double distance;
	double llm;
	Map<String,String> metaData =  new HashMap<String,String>();

	 
	
	public Event(String uuid){
		this.uuid = uuid;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
 
	public Event(){
		
	}
	public String toJSONString() throws IOException, ParseException{
		JsonFactory jsonFactory = new JsonFactory();
		StringWriter writer = new StringWriter();
		JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(writer);
		jsonGenerator.useDefaultPrettyPrinter();
	
		jsonGenerator.writeStartObject();
		jsonGenerator.writeArrayFieldStart("eventList");
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("uuid", uuid);
		jsonGenerator.writeStringField("indexKey", indexKey);
		SimpleDateFormat simpleDateformat = new SimpleDateFormat( "MM/dd/yy HH:mm:ss" );
		jsonGenerator.writeStringField("dateTime",simpleDateformat.format(dateTime) );
		jsonGenerator.writeNumberField("azimuth", azimuth);		
		jsonGenerator.writeNumberField("inclination", inclination);//same as inclination. see wikipedia.
		jsonGenerator.writeStringField("address", address);
		jsonGenerator.writeStringField("attachment", attachment);
		jsonGenerator.writeStringField("info",  info );
		jsonGenerator.writeStringField("thumbnail",  thumbnail );
		jsonGenerator.writeStringField("phone",  phone );
		jsonGenerator.writeStringField("url", url);
		jsonGenerator.writeStringField("type",  "Point" );
		jsonGenerator.writeArrayFieldStart("coordinates");
		jsonGenerator.writeNumber(latitude);
		jsonGenerator.writeNumber(longitude);
		jsonGenerator.writeEndArray();
 
		jsonGenerator.writeEndObject();
		jsonGenerator.flush();
		jsonGenerator.close();
		
		return writer.getBuffer().toString(); 
	}
	public Map<String, String> getMetaData() {
		return metaData;
	}
	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}
	 
	public String getIndexKey() {
		return indexKey;
	}
	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
 
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public double getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}
	public double getInclination() {
		return inclination;
	}
	public void setInclination(double inclination) {
		this.inclination = inclination;
	}	
	public double getGeoDistance() {
		return geoDistance;
	}
	public void setGeoDistance(double geoDistance) {
		this.geoDistance = geoDistance;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getLlm() {
		return llm;
	}
	public void setLlm(double llm) {
		this.llm = llm;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
 
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public void copy(Event other) {
	 this.address = other.address;
	 this.uuid = other.uuid;
	}
	
 
}
