package com.class3601.social.models;

import com.class3601.social.common.Messages;

public class GameOwner {

	private String gameownerIdPrimarKey;
	private String username;
	private String gametitle;

	public GameOwner() {
		setUsername(Messages.UNKNOWN);
		setGametitle(Messages.UNKNOWN);
	}
	
	public void updateFromGameOwner(GameOwner gameowner) {
		setGameOwnerIdPrimarKey(gameowner.getGameOwnerIdPrimarKey());
		setUsername(gameowner.getUsername());
		setGametitle(gameowner.getGametitle());
		
	}

	public GameOwner copy() {
		GameOwner gameowner = new GameOwner();
		gameowner.updateFromGameOwner(this);
		return gameowner;
	}
	
	public boolean equals(GameOwner gameowner) {
		return getGameOwnerIdPrimarKey().equals(gameowner.getGameOwnerIdPrimarKey());
	}

	public String getGameOwnerIdPrimarKey() {
		return gameownerIdPrimarKey;
	}

	public void setGameOwnerIdPrimarKey(String gameownerIdPrimarKey) {
		this.gameownerIdPrimarKey = gameownerIdPrimarKey;
	}

	public String getGametitle() {
		return gametitle;
	}

	public void setGametitle(String gametitle) {
		this.gametitle = gametitle;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}