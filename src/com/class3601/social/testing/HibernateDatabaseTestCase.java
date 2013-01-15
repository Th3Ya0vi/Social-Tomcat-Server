package com.class3601.social.testing;


import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.class3601.social.models.Friend;
import com.class3601.social.models.Game;
import com.class3601.social.models.GameOwner;
import com.class3601.social.models.Krypto;
import com.class3601.social.models.User;
import com.class3601.social.persistence.HibernateFriendManager;
import com.class3601.social.persistence.HibernateGameManager;
import com.class3601.social.persistence.HibernateGameOwnerManager;
import com.class3601.social.persistence.HibernateUserManager;

public class HibernateDatabaseTestCase  {
	
	HibernateUserManager uManager = new HibernateUserManager();
	HibernateFriendManager fManager = new HibernateFriendManager();
	HibernateGameManager gManager = new HibernateGameManager();
	HibernateGameOwnerManager oManager = new HibernateGameOwnerManager();
	
	@Test
	public void testSomething() {
		
		uManager.setupTable();
		fManager.setupTable();
		gManager.setupTable();
		oManager.setupTable();
		Krypto krypto = new Krypto();
		
		User andrew = new User();
		andrew.setId("andrew");
		andrew.setEmail("andrew_mackarous@carleton.ca");
		andrew.setPassword(krypto.encrypt("mackarous"));
		
		User kai = new User();
		kai.setId("kai");
		kai.setEmail("kai_cheng@carleton.ca");
		kai.setPassword(krypto.encrypt("chengfu"));
		
		User chris = new User();
		chris.setId("chris");
		chris.setEmail("chris_wright@carleton.ca");
		chris.setPassword(krypto.encrypt("schmidt"));
		
		uManager.add(andrew);
		uManager.add(kai);
		uManager.add(chris);
				
		Friend andrewkai = new Friend();
		andrewkai.setId(andrew.getId());
		andrewkai.setFriendid(kai.getId());
		andrewkai.setStatus("SENT");
		
		Friend kaiandrew = new Friend();
		kaiandrew.setId(kai.getId());
		kaiandrew.setFriendid(andrew.getId());
		kaiandrew.setStatus("RCVD");
		
		fManager.add(kaiandrew);
		fManager.add(andrewkai);
		
		Game halo = new Game();
		halo.setTitle("Halo 4");
		halo.setSystem("Xbox 360");

		Game cod = new Game();
		cod.setTitle("Call of Duty: Black Ops 2");
		cod.setSystem("Xbox 360");

		Game ac3 = new Game();
		ac3.setTitle("Assassin's Creed 3");
		ac3.setSystem("PS3");

		Game zu = new Game();
		zu.setTitle("Zombie U");
		zu.setSystem("Wii U");
		
		gManager.add(halo);
		gManager.add(cod);
		gManager.add(ac3);
		gManager.add(zu);
		
		GameOwner go1 = new GameOwner();
		go1.setUsername(andrew.getId());
		go1.setGametitle(halo.getTitle());
		GameOwner go2 = new GameOwner();
		go2.setUsername(chris.getId());
		go2.setGametitle(cod.getTitle());
		GameOwner go3 = new GameOwner();
		go3.setUsername(kai.getId());
		go3.setGametitle(zu.getTitle());
		GameOwner go4 = new GameOwner();
		go4.setUsername(andrew.getId());
		go4.setGametitle(ac3.getTitle());

		oManager.add(go1);
		oManager.add(go2);
		oManager.add(go3);
		oManager.add(go4);
		
		assertEquals("mackarous", krypto.decrypt(uManager.getUserById("andrew").getPassword()));		
		assertEquals("chengfu", krypto.decrypt(uManager.getUserById("kai").getPassword()));		
		assertEquals("schmidt", krypto.decrypt(uManager.getUserById("chris").getPassword()));		
	
	}

	@BeforeClass
	public static void setup () {

		
	}

	@AfterClass
	public static void takeDown() {

	}


}
