package com.class3601.social.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.class3601.social.common.MessageStore;
import com.class3601.social.models.Friend;
import com.class3601.social.persistence.HibernateFriendManager;
import com.class3601.social.persistence.HibernateUserManager;
import com.opensymphony.xwork2.ActionSupport;

public class GetFriendsAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private static String PARAMETER_1 = "id";
	//private static String PARAMETER_2 = "password";
	private static String XML_1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n" + "<friends>\n";// + "<result>";
	//private static String XML_2 = "</result>\n";
	private static String XML_2 = "<user>\n " + "<username>";
	private static String XML_3 = "</username>\n" + "<friendname>";
	private static String XML_4 = "</friendname>\n" + "<status>";
	private static String XML_5 = "</status>\n";
	private static String XML_6 = "</user>\n";
	private static String XML_7 = "</friends>\n";

	private MessageStore messageStore;
	private HttpServletRequest request;
	HibernateUserManager uManager = new HibernateUserManager();
	HibernateFriendManager fManager = new HibernateFriendManager();
	long counter = 0;


	public String execute() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// preferred method is to implement ServletRequestAware interface
		// http://struts.apache.org/2.0.14/docs/how-can-we-access-the-httpservletrequest.html

		//http://localhost:8080/social/initial?parameter1=dog&parameter2=cat
		//http://localhost:8080/social/initial?parameter1=dog&parameter2=error
		String id = getServletRequest().getParameter(PARAMETER_1);
		//String password = getServletRequest().getParameter(PARAMETER_2);
		//String email = "";

		messageStore = new MessageStore();
		messageStore.appendToMessage(XML_1);

		Friend friend = new Friend();
		List<Friend> allFriendsWithID = fManager.getFriendsByID(id);

		for (int i = 0; i < allFriendsWithID.size(); i++) {
			friend = allFriendsWithID.get(i);
			messageStore.appendToMessage(XML_2);
			messageStore.appendToMessage(friend.getId());
			messageStore.appendToMessage(XML_3);
			messageStore.appendToMessage(friend.getFriendid());
			messageStore.appendToMessage(XML_4);
			messageStore.appendToMessage(friend.getStatus());
			messageStore.appendToMessage(XML_5);
			messageStore.appendToMessage(XML_6);
		}

		messageStore.appendToMessage(XML_7);

		System.out.println("========================- GetFriends -=========================");
		System.out.println(messageStore.getMessage());
		System.out.println("===============================================================");
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
