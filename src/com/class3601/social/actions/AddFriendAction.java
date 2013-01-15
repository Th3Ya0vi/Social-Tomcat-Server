package com.class3601.social.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.class3601.social.common.MessageStore;
import com.class3601.social.models.Friend;
import com.class3601.social.persistence.HibernateFriendManager;
import com.opensymphony.xwork2.ActionSupport;

public class AddFriendAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private static String PARAMETER_1 = "id";
	private static String PARAMETER_2 = "friend";
	private static String XML_1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n" + "<friends>\n";// + "<result>";
	//private static String XML_2 = "</result>\n";
	private static String XML_2 = "<username>";
	private static String XML_3 = "</username>\n" + "<friendname>";
	private static String XML_4 = "</friendname>\n" + "<status>";
	private static String XML_5 = "</status>\n";
	private static String XML_6 = "</friends>\n";
	private static String FAILURE = "<result>fail</result>\n";

	private MessageStore messageStore;
	private HttpServletRequest request;
	HibernateFriendManager fManager = new HibernateFriendManager();

	public String execute() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// preferred method is to implement ServletRequestAware interface
		// http://struts.apache.org/2.0.14/docs/how-can-we-access-the-httpservletrequest.html

		//http://localhost:8080/social/initial?parameter1=dog&parameter2=cat
		//http://localhost:8080/social/initial?parameter1=dog&parameter2=error
		String id = getServletRequest().getParameter(PARAMETER_1);
		String friendid = getServletRequest().getParameter(PARAMETER_2);

		messageStore = new MessageStore();
		messageStore.appendToMessage(XML_1);

		Friend friend = new Friend();
		List<Friend> allFriendsWithID = fManager.getFriendsByID(id);
		boolean found = false;

		for (int i = 0; i < allFriendsWithID.size(); i++) {
			friend = allFriendsWithID.get(i);
			if (friend.getFriendid().equals(friendid)) {
				found = true;
				if (friend.getStatus().equals("ACPT") || friend.getStatus().equals("SENT"))
					break;
				friend.setStatus("ACPT");
				fManager.update(friend);
				
				System.out.println("HERE 1");

				if (found) {
					Friend f = new Friend();
					List<Friend> friendsWithFriendID = fManager.getFriendsByID(friendid);
					for (int j = 0; j < friendsWithFriendID.size(); i++) {
						f = friendsWithFriendID.get(j);
						if (f.getFriendid().equals(id)) {
							f.setStatus("ACPT");
							fManager.update(f);
							
							System.out.println("HERE 2");

							break;

						}
					}
				}

				messageStore.appendToMessage(XML_2);
				messageStore.appendToMessage(id);
				messageStore.appendToMessage(XML_3);
				messageStore.appendToMessage(friendid);
				messageStore.appendToMessage(XML_4);
				messageStore.appendToMessage("ACPT");
				messageStore.appendToMessage(XML_5);
				messageStore.appendToMessage(XML_6);
				System.out.println("========================- AddFriend -=========================");
				System.out.println(messageStore.getMessage());
				System.out.println("==============================================================");
				return "success";
			}
		}
		if (!found) {
			friend.setId(id);
			friend.setFriendid(friendid);
			friend.setStatus("SENT");

			Friend reverse = new Friend();
			reverse.setId(friendid);
			reverse.setFriendid(id);
			reverse.setStatus("RCVD");

			fManager.add(friend);
			fManager.add(reverse);

			messageStore.appendToMessage(XML_2);
			messageStore.appendToMessage(id);
			messageStore.appendToMessage(XML_3);
			messageStore.appendToMessage(friendid);
			messageStore.appendToMessage(XML_4);
			messageStore.appendToMessage("sent");
			messageStore.appendToMessage(XML_5);
			messageStore.appendToMessage(XML_6);

			System.out.println("========================- AddFriend -=========================");
			System.out.println(messageStore.getMessage());
			System.out.println("==============================================================");
			return "success";
		}
		
		messageStore.appendToMessage(FAILURE);
		messageStore.appendToMessage(XML_6);
		System.out.println("========================- AddFriend -=========================");
		System.out.println(messageStore.getMessage());
		System.out.println("==============================================================");
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
