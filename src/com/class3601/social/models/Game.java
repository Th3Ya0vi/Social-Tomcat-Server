package com.class3601.social.models;

import com.class3601.social.common.Messages;

public class Game {

	private String gameIdPrimarKey;
	private String title;
	private String system;

	public Game() {
		setTitle(Messages.UNKNOWN);
		setSystem(Messages.UNKNOWN);
	}
	
	public void updateFromGame(Game game) {
		setGameIdPrimarKey(game.getGameIdPrimarKey());
		setTitle(game.title);
		setSystem(game.system);
	}

	public Game copy() {
		Game game = new Game();
		game.updateFromGame(this);
		return game;
	}
	
	public boolean equals(Game game) {
		return getGameIdPrimarKey().equals(game.getGameIdPrimarKey());
	}

	public String getGameIdPrimarKey() {
		return gameIdPrimarKey;
	}

	public void setGameIdPrimarKey(String gameIdPrimarKey) {
		this.gameIdPrimarKey = gameIdPrimarKey;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}