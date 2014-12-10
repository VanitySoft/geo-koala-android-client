package com.vanitysoft.reapefire.android;

import java.util.HashSet;
import java.util.Set;

public class Account {
 
	User user =   new User();
	Set<Tag> tags = new HashSet<Tag>();
	String id="";//? email address of the account holder or webaddress?
	String indexKey; 
	String callback;
	String accessKey;
	String accessSecret;
	String name="";
	byte[] description;	
	String logo="";
	String icon="http://earth.google.com/images/kml-icons/track-directional/track-0.png";
	String styleUrl="";
	byte[] snippet;
	 
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getAccessSecret() {
		return accessSecret;
	}
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
	
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
 
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getIndexKey() {
		return indexKey;
	}
	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}
 
 	
	public String getStyleUrl() {
		return styleUrl;
	}
	public void setStyleUrl(String styleUrl) {
		this.styleUrl = styleUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String providerName) {
		this.name = providerName;
	}
 
	public byte[] getDescription() {
		return description;
	}
	public void setDescription(byte[] description) {
		this.description = description;
	}
	public byte[] getSnippet() {
		return snippet;
	}
	public void setSnippet(byte[] snippet) {
		this.snippet = snippet;
	}
	public String getId() {
		return id;
	}
	public void setId(String providerId) {
		this.id = providerId;
	}
 
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}


    
}
