package org.springframework.android.basicauth;

import java.util.HashSet;
import java.util.Set;

public class Tag {
	String id;
	String name;
	Set<Account> accounts = new HashSet<Account>();

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
