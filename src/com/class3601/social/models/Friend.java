package com.class3601.social.models;

import com.class3601.social.common.Messages;

public class Friend {

	private String friendIdPrimarKey;
	private String id;
	private String friendid;
	private String status;

	public Friend() {
		setId(Messages.UNKNOWN);
		setFriendid(Messages.UNKNOWN);
		setStatus(Messages.UNKNOWN);
	}
	
	public void updateFromFriend(Friend friend) {
		setFriendIdPrimarKey(friend.getFriendIdPrimarKey());
		setId(friend.getId());
		setStatus(friend.getStatus());
	}

	public Friend copy() {
		Friend friend = new Friend();
		friend.updateFromFriend(this);
		return friend;
	}
	
	public boolean equals(Friend friend) {
		return getFriendIdPrimarKey().equals(friend.getFriendIdPrimarKey());
	}

	public String getFriendIdPrimarKey() {
		return friendIdPrimarKey;
	}

	public void setFriendIdPrimarKey(String friendIdPrimarKey) {
		this.friendIdPrimarKey = friendIdPrimarKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}