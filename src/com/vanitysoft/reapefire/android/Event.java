package com.vanitysoft.reapefire.android;
 

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

 
public class Event {

    
    String id;
    String uuid = "";
    String indexKey = "";
    byte[] description = "www.vanity-soft.com".getBytes();
    String attachment = "";
    String address = "";
    String email = "";;
    String url="";
    String thumbnail = "";
    String info = "";
    String phone = "";
    double azimuth =0.0;
    double inclination =0.0;
    double[] position = new double[2];

    private Date dateTime = new Date();
    double geoDistance;
    double distance;
    double llm;
    Map<String, Object> metaData = new HashMap<String, Object>();

    public Event(){
	
    }
    
    public Event(String uuid, double lat, double longitude, double azimuth, double inclination,Date dateTime) {
	this.uuid = uuid;
	this.position[0] = lat;
	this.position[1] = longitude;
	this.dateTime = dateTime;
	
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public double[] getPosition() {
	return position;
    }

    public void setPosition(double[] position) {
	this.position = position;
    }

    public Map<String, Object> getMetaData() {
	return metaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
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
	if (  url  == null ){
	    this.url= "https://maps.googleapis.com/maps/api/streetview?size=400x400&location="+position[0]+","+position[1]+"&fov=90&heading="+azimuth+"&pitch="+inclination;
	}else{
	    this.url= url;
	}
	
    }

    public String getThumbnail() {
	return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
	if (  thumbnail  == null ){
	    this.thumbnail= "https://maps.googleapis.com/maps/api/streetview?size=400x400&location="+position[0]+","+position[1]+"&fov=90&heading="+azimuth+"&pitch="+inclination;
	}else{
	    this.thumbnail = thumbnail;
	}
    }

    public byte[] getDescription() {
 	return description;
     }

     public void setDescription(byte[] description) {
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
	return this.position[0];
    }

    public void setLatitude(double latitude) {
	this.position[0] = latitude;
    }

    public double getLongitude() {
	return position[1];
    }

    public void setLongitude(double longitude) {
	this.position[1] = longitude;
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
