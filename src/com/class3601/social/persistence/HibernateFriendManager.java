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
import com.class3601.social.models.Friend;

public class HibernateFriendManager extends AbstractHibernateDatabaseManager {

	private static String FRIEND_TABLE_NAME = "FRIEND";
	private static String FRIEND_CLASS_NAME = "Friend";

	private static String SELECT_ALL_FRIENDS = "from " + FRIEND_CLASS_NAME + " as friend";
	private static String SELECT_FRIEND_WITH_ID = "from " + FRIEND_CLASS_NAME + " as friend where friend.id = ?";
	private static String SELECT_FRIEND_WITH_TOKEN = "from " + FRIEND_CLASS_NAME + " as friend where friend.token = ?";

	private static final String DROP_TABLE_SQL = "drop table if exists " + FRIEND_TABLE_NAME + ";";

	private static String SELECT_NUMBER_FRIENDS = "select count (*) from " + FRIEND_CLASS_NAME;

	private static String METHOD_INCREMENT_FRIEND_BY_ID_BY = "incrementFriendByIdBy";

	private static final String CREATE_TABLE_SQL = "create table " + FRIEND_TABLE_NAME + 
			"(FRIEND_ID_PRIMARY_KEY char(36) primary key, " +
			"ID tinytext, " +
			"FRIENDID tinytext," +
			"STATUS tinytext);";

	private static final String METHOD_GET_N_FRIENDS = "getNFriendsStartingAtIndex";
	private static String METHOD_GET_OBJECT_WITH_NAME = "getObjectWithName";

	private static HibernateFriendManager manager;

	public HibernateFriendManager() {
		super();
	}

	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public static HibernateFriendManager getDefault() {

		if (manager == null) {
			manager = new HibernateFriendManager();
		}
		return manager;
	}

	public String getClassName() {
		return FRIEND_CLASS_NAME;
	}

	@Override
	public boolean setupTable() {
		HibernateUtil.executeSQLQuery(DROP_TABLE_SQL);
		return HibernateUtil.executeSQLQuery(CREATE_TABLE_SQL);
	}

	/**
	 * Adds given object (friend) to the database 
	 */
	public synchronized boolean add(Object object) {
		return super.add(object);
	}


	/**
	 * Updates given object (friend).
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean update(Friend friend) {
		boolean result = super.update(friend);	
		return result;
	}


	/**
	 * Deletes given friend from the database.
	 * Returns true if successful, otherwise returns false.
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean delete(Friend friend){

		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(friend);
			transaction.commit();
			return true;
		}
		catch (HibernateException exception) {
			rollback(transaction);
			BookingLogger.getDefault().severe(this, Messages.METHOD_DELETE_FRIEND, Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		}	
		catch (RuntimeException exception) {
			rollback(transaction);
			BookingLogger.getDefault().severe(this, Messages.METHOD_DELETE_FRIEND, Messages.GENERIC_FAILED, exception);
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
	public synchronized void incrementFriendById(String id) {

		incrementFriendByIdBy(id, 1);
	}

	/**
	 * Increments counter found for given name by given count.
	 * 
	 * @param name
	 * @param count
	 */
	public synchronized void incrementFriendByIdBy(String id, int count) {

		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_FRIEND_WITH_ID);
			query.setParameter(0, id);
			Friend friend = (Friend) query.uniqueResult();
			if (friend != null) {
				//friend.setCounter(friend.getCounter() + count);
				session.update(friend);
				transaction.commit();
			} else {
				BookingLogger.getDefault().severe(this,
						METHOD_INCREMENT_FRIEND_BY_ID_BY,
						Messages.OBJECT_NOT_FOUND_FAILED + ":" + id, null);
			}
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_INCREMENT_FRIEND_BY_ID_BY,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_INCREMENT_FRIEND_BY_ID_BY,
					Messages.HIBERNATE_FAILED, exception);
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_INCREMENT_FRIEND_BY_ID_BY,
					Messages.GENERIC_FAILED, exception);
		} finally {
			closeSession();
		}
	}


	/**
	 * Returns friend from the database with given id.
	 * Upon exception returns null.
	 * 
	 * @param id
	 * @return
	 */
	public synchronized Friend getFriendById(String id) {

		Session session = null;
		Friend errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_FRIEND_WITH_ID);
			query.setParameter(0, id);
			Friend aFriend = (Friend) query.uniqueResult();
			return aFriend;
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
	
	@SuppressWarnings("unchecked")
	public synchronized  List<Friend> getFriendsByID(String id) {
		List<Friend> errorResult = null;
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_FRIEND_WITH_ID);
			query.setParameter(0, id);
			List<Friend> friends = query.list();
			return friends;
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_FRIENDS,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		}  catch (JDBCConnectionException exception) {
			HibernateUtil.clearSessionFactory();
			BookingLogger.getDefault().severe(this, METHOD_GET_N_FRIENDS,
					Messages.HIBERNATE_CONNECTION_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_FRIENDS,
					Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_FRIENDS,
					Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}


	public synchronized Friend getFriendByToken(String token) {

		Session session = null;
		Friend errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_FRIEND_WITH_TOKEN);
			query.setParameter(0, token);
			Friend aFriend = (Friend) query.uniqueResult();
			return aFriend;
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
	 * Returns friend with given emailAddress and password from the database. 
	 * If not found returns null.
	 * 
	 * @param emailAddress
	 * @param password
	 * @return
	 */
	public Friend getFriend(String id, String friendid) {

		Friend friend = getFriendById(id);
		if ((friend != null) && (friend.getId().equals(id)) && (friend.getFriendid().equals(friendid))) {
			return friend;
		} else {
			return null;
		}
	}

	/**
	 * Returns friends, 
	 * from database.
	 * If not found returns null.
	 * Upon error returns null.
	 * 
	 * @param phonenumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized  List<Friend> getNFriendsStartingAtIndex(int index, int n) {
		List<Friend> errorResult = null;
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_FRIENDS);
			query.setFirstResult(index);
			query.setMaxResults(n);
			List<Friend> friends = query.list();

			return friends;
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_FRIENDS,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		}  catch (JDBCConnectionException exception) {
			HibernateUtil.clearSessionFactory();
			BookingLogger.getDefault().severe(this, METHOD_GET_N_FRIENDS,
					Messages.HIBERNATE_CONNECTION_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_FRIENDS,
					Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_FRIENDS,
					Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}

	public String getTableName() {
		return FRIEND_TABLE_NAME;
	}


	/**
	 * Returns number of friends.
	 * 
	 * Upon error returns empty list.
	 * 
	 * @param a charge status
	 * @return
	 */
	public synchronized int getNumberOfFriends() {

		Session session = null;
		Long aLong;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session
					.createQuery(SELECT_NUMBER_FRIENDS);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_FRIENDS,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return 0;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_FRIENDS,
					Messages.HIBERNATE_FAILED, exception);
			return 0;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_FRIENDS,
					Messages.GENERIC_FAILED, exception);
			return 0;
		} finally {
			closeSession();
		}
	}
}