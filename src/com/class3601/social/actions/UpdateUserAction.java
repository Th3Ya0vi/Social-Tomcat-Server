package com.class3601.social.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.class3601.social.common.MessageStore;
import com.class3601.social.models.Friend;
import com.class3601.social.models.User;
import com.class3601.social.persistence.HibernateFriendManager;
import com.class3601.social.persistence.HibernateUserManager;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateUserAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private static String PARAMETER_1 = "id";
	private static String PARAMETER_2 = "newid";
	private static String PARAMETER_3 = "email";
	private static String XML_1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n" + "<user>\n" + "<result>";
	private static String XML_2 = "</result>\n";
	private static String XML_3 = "<username>";
	private static String XML_4 = "</username>\n" + "<email>";
	private static String XML_5 = "</email>\n" + "<password>";
	private static String XML_6 = "</password>\n" + "<token>";
	private static String XML_7 = "</token>\n" + "<counter>";
	private static String XML_8 = "</counter>\n";
	private static String XML_9 = "</user>\n";

	private MessageStore messageStore;
	private HttpServletRequest request;
	HibernateUserManager uManager = new HibernateUserManager();
	HibernateFriendManager fManager = new HibernateFriendManager();

	public String execute() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// preferred method is to implement ServletRequestAware interface
		// http://struts.apache.org/2.0.14/docs/how-can-we-access-the-httpservletrequest.html

		//http://localhost:8080/social/initial?parameter1=dog&parameter2=cat
		//http://localhost:8080/social/initial?parameter1=dog&parameter2=error
		String id = getServletRequest().getParameter(PARAMETER_1);
		String newid = getServletRequest().getParameter(PARAMETER_2);
		String email = getServletRequest().getParameter(PARAMETER_3);
	
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
			messageStore.appendToMessage(XML_9);
			
			System.out.println("========================- Update -=========================");
			System.out.println(messageStore.getMessage());
			System.out.println("===========================================================");

			return "success";
		}
		
		Friend friend = new Friend();
		List<Friend> allFriends = fManager.getNFriendsStartingAtIndex(0, fManager.getNumberOfFriends());

		for (int i = 0; i < allFriends.size(); i++) {
			friend = allFriends.get(i);
			if (friend.getId().equals(id)) {
				friend.setId(newid);
				fManager.update(friend);
			}
			if (friend.getFriendid().equals(id)) {
				friend.setFriendid(newid);
				fManager.update(friend);
			}
		}
		
		user.setId(newid);
		user.setEmail(email);
		uManager.update(user);

		messageStore.appendToMessage(XML_1);
		messageStore.appendToMessage("success");
		messageStore.appendToMessage(XML_2);
		messageStore.appendToMessage(XML_3);
		messageStore.appendToMessage(user.getId());
		messageStore.appendToMessage(XML_4);
		messageStore.appendToMessage(user.getEmail());
		messageStore.appendToMessage(XML_5);
		messageStore.appendToMessage("SECRET");
		messageStore.appendToMessage(XML_6);
		messageStore.appendToMessage(user.getToken());
		messageStore.appendToMessage(XML_7);
		messageStore.appendToMessage("" + user.getCounter());
		messageStore.appendToMessage(XML_8);
		messageStore.appendToMessage(XML_9);

		System.out.println("=========================- Update -=========================");
		System.out.println(messageStore.getMessage());
		System.out.println("============================================================");
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
