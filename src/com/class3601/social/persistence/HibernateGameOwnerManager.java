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
import com.class3601.social.models.GameOwner;

public class HibernateGameOwnerManager extends AbstractHibernateDatabaseManager {

	private static String GAME_OWNER_TABLE_NAME = "GAME_OWNER";
	private static String GAME_OWNER_CLASS_NAME = "GameOwner";

	private static String SELECT_ALL_GAME_OWNERS = "from " + GAME_OWNER_CLASS_NAME + " as gameowner";
	private static String SELECT_GAME_OWNER_WITH_ID = "from " + GAME_OWNER_CLASS_NAME + " as gameowner where gameowner.id = ?";
	private static String SELECT_GAME_OWNER_WITH_TOKEN = "from " + GAME_OWNER_CLASS_NAME + " as gameowner where gameowner.token = ?";

	private static final String DROP_TABLE_SQL = "drop table if exists " + GAME_OWNER_TABLE_NAME + ";";

	private static String SELECT_NUMBER_GAME_OWNERS = "select count (*) from " + GAME_OWNER_CLASS_NAME;

	private static String METHOD_INCREMENT_GAME_OWNER_BY_ID_BY = "incrementGameOwnerByIdBy";

	private static final String CREATE_TABLE_SQL = "create table " + GAME_OWNER_TABLE_NAME + 
			"(GAME_OWNER_ID_PRIMARY_KEY char(36) primary key, " +
			"USERNAME tinytext, " +
			"GAMETITLE tinytext);";

	private static final String METHOD_GET_N_GAME_OWNERS = "getNGameOwnersStartingAtIndex";
	private static String METHOD_GET_OBJECT_WITH_NAME = "getObjectWithName";

	private static HibernateGameOwnerManager manager;

	public HibernateGameOwnerManager() {
		super();
	}

	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public static HibernateGameOwnerManager getDefault() {

		if (manager == null) {
			manager = new HibernateGameOwnerManager();
		}
		return manager;
	}

	public String getClassName() {
		return GAME_OWNER_CLASS_NAME;
	}

	@Override
	public boolean setupTable() {
		HibernateUtil.executeSQLQuery(DROP_TABLE_SQL);
		return HibernateUtil.executeSQLQuery(CREATE_TABLE_SQL);
	}

	/**
	 * Adds given object (gameowner) to the database 
	 */
	public synchronized boolean add(Object object) {
		return super.add(object);
	}


	/**
	 * Updates given object (gameowner).
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean update(GameOwner gameowner) {
		boolean result = super.update(gameowner);	
		return result;
	}


	/**
	 * Deletes given gameowner from the database.
	 * Returns true if successful, otherwise returns false.
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean delete(GameOwner gameowner){

		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(gameowner);
			transaction.commit();
			return true;
		}
		catch (HibernateException exception) {
			rollback(transaction);
			BookingLogger.getDefault().severe(this, Messages.METHOD_DELETE_GAME_OWNER, Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		}	
		catch (RuntimeException exception) {
			rollback(transaction);
			BookingLogger.getDefault().severe(this, Messages.METHOD_DELETE_GAME_OWNER, Messages.GENERIC_FAILED, exception);
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
	public synchronized void incrementGameOwnerById(String id) {

		incrementGameOwnerByIdBy(id, 1);
	}

	/**
	 * Increments counter found for given name by given count.
	 * 
	 * @param name
	 * @param count
	 */
	public synchronized void incrementGameOwnerByIdBy(String id, int count) {

		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_GAME_OWNER_WITH_ID);
			query.setParameter(0, id);
			GameOwner gameowner = (GameOwner) query.uniqueResult();
			if (gameowner != null) {
				//gameowner.setCounter(gameowner.getCounter() + count);
				session.update(gameowner);
				transaction.commit();
			} else {
				BookingLogger.getDefault().severe(this,
						METHOD_INCREMENT_GAME_OWNER_BY_ID_BY,
						Messages.OBJECT_NOT_FOUND_FAILED + ":" + id, null);
			}
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_INCREMENT_GAME_OWNER_BY_ID_BY,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_INCREMENT_GAME_OWNER_BY_ID_BY,
					Messages.HIBERNATE_FAILED, exception);
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this,
					METHOD_INCREMENT_GAME_OWNER_BY_ID_BY,
					Messages.GENERIC_FAILED, exception);
		} finally {
			closeSession();
		}
	}


	/**
	 * Returns gameowner from the database with given id.
	 * Upon exception returns null.
	 * 
	 * @param id
	 * @return
	 */
	public synchronized GameOwner getGameOwnerById(String id) {

		Session session = null;
		GameOwner errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_GAME_OWNER_WITH_ID);
			query.setParameter(0, id);
			GameOwner aGameOwner = (GameOwner) query.uniqueResult();
			return aGameOwner;
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
	public synchronized  List<GameOwner> getGameOwnersByID(String id) {
		List<GameOwner> errorResult = null;
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_GAME_OWNER_WITH_ID);
			query.setParameter(0, id);
			List<GameOwner> gameowners = query.list();
			return gameowners;
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAME_OWNERS,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		}  catch (JDBCConnectionException exception) {
			HibernateUtil.clearSessionFactory();
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAME_OWNERS,
					Messages.HIBERNATE_CONNECTION_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAME_OWNERS,
					Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAME_OWNERS,
					Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}


	public synchronized GameOwner getGameOwnerByToken(String token) {

		Session session = null;
		GameOwner errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_GAME_OWNER_WITH_TOKEN);
			query.setParameter(0, token);
			GameOwner aGameOwner = (GameOwner) query.uniqueResult();
			return aGameOwner;
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
	 * Returns gameowner with given emailAddress and password from the database. 
	 * If not found returns null.
	 * 
	 * @param emailAddress
	 * @param password
	 * @return
	 */
	public GameOwner getGameOwner(String username, String gametitle) {

		GameOwner gameowner = getGameOwnerById(username);
		if ((gameowner != null) && (gameowner.getUsername().equals(username))) {
			return gameowner;
		} else {
			return null;
		}
	}

	/**
	 * Returns gameowners, 
	 * from database.
	 * If not found returns null.
	 * Upon error returns null.
	 * 
	 * @param phonenumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized  List<GameOwner> getNGameOwnersStartingAtIndex(int index, int n) {
		List<GameOwner> errorResult = null;
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_GAME_OWNERS);
			query.setFirstResult(index);
			query.setMaxResults(n);
			List<GameOwner> gameowners = query.list();

			return gameowners;
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAME_OWNERS,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		}  catch (JDBCConnectionException exception) {
			HibernateUtil.clearSessionFactory();
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAME_OWNERS,
					Messages.HIBERNATE_CONNECTION_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAME_OWNERS,
					Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAME_OWNERS,
					Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}

	public String getTableName() {
		return GAME_OWNER_TABLE_NAME;
	}


	/**
	 * Returns number of gameowners.
	 * 
	 * Upon error returns empty list.
	 * 
	 * @param a charge status
	 * @return
	 */
	public synchronized int getNumberOfGameOwners() {

		Session session = null;
		Long aLong;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session
					.createQuery(SELECT_NUMBER_GAME_OWNERS);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_GAME_OWNERS,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return 0;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_GAME_OWNERS,
					Messages.HIBERNATE_FAILED, exception);
			return 0;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_GAME_OWNERS,
					Messages.GENERIC_FAILED, exception);
			return 0;
		} finally {
			closeSession();
		}
	}
}