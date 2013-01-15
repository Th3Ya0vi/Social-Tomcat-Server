package com.class3601.social.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.class3601.social.common.MessageStore;
import com.class3601.social.models.Friend;
import com.class3601.social.models.User;
import com.class3601.social.persistence.HibernateFriendManager;
import com.class3601.social.persistence.HibernateUserManager;
import com.opensymphony.xwork2.ActionSupport;

public class GetUsersAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private static String PARAMETER_1 = "id";
	private static String XML_1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n" + "<users>\n";// + "<result>";
	private static String XML_2 = "<user>\n " + "<username>";
	private static String XML_3 = "</username>\n" + "<email>";
	private static String XML_4 = "</email>\n" + "</user>\n";
	private static String XML_5 = "</users>\n";

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
			List<User> ulist = uManager.getNUsersStartingAtIndex(0, uManager.getNumberOfUsers());
			List<Friend> flist = fManager.getFriendsByID(id);
			
			List<String> uArray = new ArrayList<String>(64);
			List<String> fArray = new ArrayList<String>(64);

			messageStore.appendToMessage(XML_1);
			for (int i = 0; i < ulist.size(); i++) {
				uArray.add(ulist.get(i).getId());
			}
			for (int i = 0; i < flist.size(); i++) {
				fArray.add(flist.get(i).getFriendid());
			}
			if (!fArray.contains(id))
				fArray.add(id);
			
			for (int i=0; i < uArray.size(); i++) {
				uArray.set(i, uArray.get(i).toLowerCase());
			}
			for (int i=0; i < fArray.size(); i++) {
				fArray.set(i, fArray.get(i).toLowerCase());
			}
			uArray.removeAll(fArray);
			
			for (int i = 0; i < uArray.size(); i++) {
				User newUser = uManager.getUserById(uArray.get(i));
				
				messageStore.appendToMessage(XML_2);
				messageStore.appendToMessage(newUser.getId());
				messageStore.appendToMessage(XML_3);
				messageStore.appendToMessage(newUser.getEmail());
				messageStore.appendToMessage(XML_4);
			}
			System.out.println("ulist: " + uArray);
			System.out.println("flist: " + fArray);
			
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
