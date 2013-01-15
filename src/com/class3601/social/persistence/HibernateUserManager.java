package com.class3601.social.persistence;

import java.util.List;

//import org.apache.struts.action.ActionError;
//import org.apache.struts.action.ActionErrors;
//import org.apache.struts.action.ActionMessage;
//import org.apache.struts.action.ActionMessages;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;

import com.class3601.social.persistence.HibernateUtil;
import com.class3601.social.common.BookingLogger;
import com.class3601.social.common.Messages;
import com.class3601.social.models.User;

public class HibernateUserManager extends AbstractHibernateDatabaseManager {

	private static String USER_TABLE_NAME = "USER";
	private static String USER_CLASS_NAME = "User";

	private static String SELECT_ALL_USERS = "from " + USER_CLASS_NAME + " as user";
	private static String SELECT_USER_WITH_ID = "from " + USER_CLASS_NAME + " as user where user.id = ?";
	private static String SELECT_USER_WITH_TOKEN = "from " + USER_CLASS_NAME + " as user where user.token = ?";

	private static final String DROP_TABLE_SQL = "drop table if exists " + USER_TABLE_NAME + ";";

	private static String SELECT_NUMBER_USERS = "select count (*) from " + USER_CLASS_NAME;

	private static String METHOD_INCREMENT_USER_BY_ID_BY = "incrementUserByIdBy";

	private static final String CREATE_TABLE_SQL = "create table " + USER_TABLE_NAME + 
			"(USER_ID_PRIMARY_KEY char(36) primary key, " +
			"ID tinytext, " +
			"EMAIL tinytext, " +
			"PASSWORD tinytext, " +
			"COUNTER integer, " +
			"TIMESTAMP timestamp, " +
			"TOKEN tinytext);";

	private static final String METHOD_GET_N_USERS = "getNUsersStartingAtIndex";
	private static String METHOD_GET_OBJECT_WITH_NAME = "getObjectWithName";

	private static HibernateUserManager manager;

	public HibernateUserManager() {
		super();
	}

	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public static HibernateUserManager getDefault() {

		if (manager == null) {
			manager = new HibernateUserManager();
		}
		return manager;
	}

	public String getClassName() {
		return USER_CLASS_NAME;
	}

	@Override
	public boolean setupTable() {
		HibernateUtil.executeSQLQuery(DROP_TABLE_SQL);
		return HibernateUtil.executeSQLQuery(CREATE_TABLE_SQL);
	}

	/**
	 * Adds given object (user) to the database 
	 */
	public synchronized boolean add(Object object) {
		return super.add(object);
	}


	/**
	 * Updates given object (user).
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean update(User user) {
		boolean result = super.update(user);	
		return result;
	}


	/**
	 * Deletes given user from the database.
	 * Returns true if successful, otherwise returns false.
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean delete(User user){

		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(user);
			transaction.commit();
			return true;
		}
		catch (HibernateException exception) {
			rollback(transaction);
			BookingLogger.getDefault().severe(this, Messages.METHOD_DELETE_USER, Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		}	
		catch (RuntimeException exception) {
			rollback(transaction);
			BookingLogger.getDefault().severe(this, Messages.METHOD_DELETE_USER, Messages.GENERIC_FAILED, exception);
			return errorResult;
		}
		finally {
			closeSession();
		}
	}

	/**
	 * Increments counter found for given name by 1. 
	 * 
	 * @param name
	 */
	public synchronized void incrementUserById(String id) {

		incrementUserByIdBy(id, 1);
	}

	/**
	 * Increments counter found for given name by given count.
	 * 
	 * @param name
	 * @param count
	 */
	public synchronized void incrementUserByIdBy(String id, int count) {

		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_USER_WITH_ID);
			query.setParameter(0, id);
			User user = (User) query.uniqueResult();
			if (user != null) {
				user.setCounter(user.getCounter() + count);
				session.update(user);
				transaction.commit();
			} else {
				BookingLogger.getDefault().severe(this,
						METHOD_INCREMENT_USER_BY_ID_BY,
						Messages.OBJECT_NOT_FOUND_FAILED + ":" + id, null);
			}
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_INCREMENT_USER_BY_ID_BY,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_INCREMENT_USER_BY_ID_BY,
					Messages.HIBERNATE_FAILED, exception);
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_INCREMENT_USER_BY_ID_BY,
					Messages.GENERIC_FAILED, exception);
		} finally {
			closeSession();
		}
	}


	/**
	 * Returns user from the database with given id.
	 * Upon exception returns null.
	 * 
	 * @param id
	 * @return
	 */
	public synchronized User getUserById(String id) {

		Session session = null;
		User errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_USER_WITH_ID);
			query.setParameter(0, id);
			User aUser = (User) query.uniqueResult();
			return aUser;
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_GET_OBJECT_WITH_NAME,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_GET_OBJECT_WITH_NAME, Messages.HIBERNATE_FAILED,
					exception);
			return errorResult;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_GET_OBJECT_WITH_NAME, Messages.GENERIC_FAILED,
					exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}

	public synchronized User getUserByToken(String token) {

		Session session = null;
		User errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_USER_WITH_TOKEN);
			query.setParameter(0, token);
			User aUser = (User) query.uniqueResult();
			return aUser;
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_GET_OBJECT_WITH_NAME,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_GET_OBJECT_WITH_NAME, Messages.HIBERNATE_FAILED,
					exception);
			return errorResult;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_GET_OBJECT_WITH_NAME, Messages.GENERIC_FAILED,
					exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}


	/**
	 * Returns user with given emailAddress and password from the database. 
	 * If not found returns null.
	 * 
	 * @param emailAddress
	 * @param password
	 * @return
	 */
	public User getUser(String id, String password) {

		User user = getUserById(id);
		if ((user != null) && (user.getId().equals(id))) {
			return user;
		} else {
			return null;
		}
	}

	/**
	 * Returns users, 
	 * from database.
	 * If not found returns null.
	 * Upon error returns null.
	 * 
	 * @param phonenumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized  List<User> getNUsersStartingAtIndex(int index, int n) {
		List<User> errorResult = null;
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_USERS);
			query.setFirstResult(index);
			query.setMaxResults(n);
			List<User> users = query.list();

			return users;
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_USERS,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		}  catch (JDBCConnectionException exception) {
			HibernateUtil.clearSessionFactory();
			BookingLogger.getDefault().severe(this, METHOD_GET_N_USERS,
					Messages.HIBERNATE_CONNECTION_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_USERS,
					Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_USERS,
					Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}

	public String getTableName() {
		return USER_TABLE_NAME;
	}


	/**
	 * Returns number of users.
	 * 
	 * Upon error returns empty list.
	 * 
	 * @param a charge status
	 * @return
	 */
	public synchronized int getNumberOfUsers() {

		Session session = null;
		Long aLong;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session
					.createQuery(SELECT_NUMBER_USERS);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_USERS,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return 0;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_USERS,
					Messages.HIBERNATE_FAILED, exception);
			return 0;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_USERS,
					Messages.GENERIC_FAILED, exception);
			return 0;
		} finally {
			closeSession();
		}
	}
}