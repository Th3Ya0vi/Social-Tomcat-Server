package com.class3601.social.models;

import java.sql.Timestamp;
import java.util.Calendar;

import com.class3601.social.common.Messages;

public class User {

	private String userIdPrimarKey;
	private String id;
	private String email;
	private String password;
	private long counter;
	private Timestamp timestamp;
	private String token;

	public User() {
		setId(Messages.UNKNOWN);
		setEmail(Messages.UNKNOWN);
		setPassword(Messages.UNKNOWN);
		setCounter(0);
		setTimestampNow();
		setToken(Messages.UNKNOWN);
	}
	
	public void updateFromUser(User user) {
		setUserIdPrimarKey(user.getUserIdPrimarKey());
		setId(user.getId());
		setPassword(user.getPassword());
		setCounter(user.getCounter());
	}

	public User copy() {
		User user = new User();
		user.updateFromUser(this);
		return user;
	}
	
	public boolean equals(User user) {
		return getUserIdPrimarKey().equals(user.getUserIdPrimarKey());
	}
	
	public void setTimestampNow() {
		setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}

	public String getUserIdPrimarKey() {
		return userIdPrimarKey;
	}

	public void setUserIdPrimarKey(String userIdPrimarKey) {
		this.userIdPrimarKey = userIdPrimarKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public long getCounter() {
		return counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}