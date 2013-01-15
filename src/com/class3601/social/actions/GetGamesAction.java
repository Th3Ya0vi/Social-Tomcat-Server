package com.class3601.social.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.class3601.social.common.MessageStore;
import com.class3601.social.models.Game;
import com.class3601.social.models.GameOwner;
import com.class3601.social.models.User;
import com.class3601.social.persistence.HibernateFriendManager;
import com.class3601.social.persistence.HibernateGameManager;
import com.class3601.social.persistence.HibernateGameOwnerManager;
import com.class3601.social.persistence.HibernateUserManager;
import com.opensymphony.xwork2.ActionSupport;

public class GetGamesAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private static String PARAMETER_1 = "id";
	private static String XML_1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n" + "<gamelist>\n";// + "<result>";
	private static String XML_2 = "<game>\n " + "<gametitle>";
	private static String XML_3 = "</gametitle>\n" + "<system>";
	private static String XML_4 = "</system>\n" + "</game>\n";
	private static String XML_5 = "</gamelist>\n";

	private MessageStore messageStore;
	private HttpServletRequest request;
	HibernateUserManager uManager = new HibernateUserManager();
	HibernateFriendManager fManager = new HibernateFriendManager();
	HibernateGameManager gManager = new HibernateGameManager();
	HibernateGameOwnerManager oManager = new HibernateGameOwnerManager();

	public String execute() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// preferred method is to implement ServletRequestAware interface
		// http://struts.apache.org/2.0.14/docs/how-can-we-access-the-httpservletrequest.html

		//http://localhost:8080/social/initial?parameter1=dog&parameter2=cat
		//http://localhost:8080/social/initial?parameter1=dog&parameter2=error
		String id = getServletRequest().getParameter(PARAMETER_1);
		User user = uManager.getUserById(id);
		messageStore = new MessageStore();

		// Error checking parameters are set properly
		if (id == null) {
			System.out.println(messageStore.getMessage());
			return "success";
		}
		if (user == null) {
			messageStore.appendToMessage(XML_1);
			messageStore.appendToMessage("fail");
			messageStore.appendToMessage(XML_2);
			messageStore.appendToMessage(XML_5);
			System.out.println("========================- Users -=========================");
			System.out.println(messageStore.getMessage());
			System.out.println("==========================================================");
			return "success";
		}	
		else {
			List<GameOwner> games = oManager.getNGameOwnersStartingAtIndex(0, oManager.getNumberOfGameOwners());
			List<String> gArray = new ArrayList<String>(64);

			for (int i = 0; i < games.size(); i++) {
				if (games.get(i).getUsername().equals(id.toLowerCase())) {
					gArray.add(games.get(i).getGametitle());
				}
			}
			
			List<Game> glist = gManager.getNGamesStartingAtIndex(0, gManager.getNumberOfGames());
			messageStore.appendToMessage(XML_1);
			for (int i = 0; i < glist.size(); i++) {
				if (!gArray.contains(glist.get(i).getTitle())) {
					messageStore.appendToMessage(XML_2);
					messageStore.appendToMessage(glist.get(i).getTitle());
					messageStore.appendToMessage(XML_3);
					messageStore.appendToMessage(glist.get(i).getSystem());
					messageStore.appendToMessage(XML_4);
				}
			}
			messageStore.appendToMessage(XML_5);
		}
		
		System.out.println("========================- Users -=========================");
		System.out.println(messageStore.getMessage());
		System.out.println("==========================================================");
		return "success";
	}

	public MessageStore getMessageStore() {
		return messageStore;
	}

	public void setMessageStore(MessageStore messageStore) {
		this.messageStore = messageStore;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	private HttpServletRequest getServletRequest() {
		return request;
	}

}
