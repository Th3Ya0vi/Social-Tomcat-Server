package com.class3601.social.actions;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.class3601.social.common.MessageStore;
import com.class3601.social.models.User;
import com.class3601.social.persistence.HibernateUserManager;
import com.opensymphony.xwork2.ActionSupport;

public class LogoffAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private static String PARAMETER_1 = "token";
	private static String XML_1 = 
			"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n" +
					"<logoff>\n" +
					"<parameter>";
	private static String XML_2 = 
			"</parameter>\n" +
					"<parameter>";
	private static String XML_3 = 
			"</parameter>\n";
	
	private static String XML_4 = "</logoff>\n";

	private MessageStore messageStore;
	private HttpServletRequest request;
	HibernateUserManager uManager = new HibernateUserManager();
	long counter = 0;

	public String execute() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// preferred method is to implement ServletRequestAware interface
		// http://struts.apache.org/2.0.14/docs/how-can-we-access-the-httpservletrequest.html

		//http://localhost:8080/social/initial?parameter1=dog&parameter2=cat
		//http://localhost:8080/social/initial?parameter1=dog&parameter2=error
		String token = getServletRequest().getParameter(PARAMETER_1).replace(' ', '+');
		User user = uManager.getUserByToken(token);

		// Error checking parameters are set properly
		if (token == null) {
			addActionError("Danger! Danger Will Robinson! Token field not set correctly.");
			return "fail";
		}
		if (user == null) {
			addActionError("User with that token does not exist. Could not logoff.");
			return "fail";
		}
//		else if (user.getPassword().equals(krypto.encrypt(password))) {
//			user.setCounter(user.getCounter()+1);
//			String token = new String(user.getId() + " :: " + user.getPassword() + " :: " + user.getTimestamp());
//			String kryptoken = krypto.encrypt(token);
//			user.setToken(kryptoken);
//			uManager.update(user);
//		}		
		else {
			user.setToken(null);
			uManager.update(user);
			//addActionError("User " + user.getId() + " has been logged off successfully.");
		}
		
		messageStore = new MessageStore();

		messageStore.appendToMessage(XML_1);
		messageStore.appendToMessage("Username: " + user.getId() + " has been logged out successfully!");
		messageStore.appendToMessage(XML_2);
		//messageStore.appendToMessage("<br/>Decrypted Token: " + krypto.decrypt(user.getToken()));
		messageStore.appendToMessage(XML_3);
		messageStore.appendToMessage(XML_4);
		
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
