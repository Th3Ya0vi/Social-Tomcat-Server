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
import com.class3601.social.models.Game;

public class HibernateGameManager extends AbstractHibernateDatabaseManager {

	private static String GAME_TABLE_NAME = "GAME";
	private static String GAME_CLASS_NAME = "Game";

	private static String SELECT_ALL_GAMES = "from " + GAME_CLASS_NAME + " as game";
	private static String SELECT_GAME_WITH_ID = "from " + GAME_CLASS_NAME + " as game where game.id = ?";
	private static String SELECT_GAME_WITH_TOKEN = "from " + GAME_CLASS_NAME + " as game where game.token = ?";

	private static final String DROP_TABLE_SQL = "drop table if exists " + GAME_TABLE_NAME + ";";

	private static String SELECT_NUMBER_GAMES = "select count (*) from " + GAME_CLASS_NAME;
	
	private static final String CREATE_TABLE_SQL = "create table " + GAME_TABLE_NAME + 
			"(GAME_ID_PRIMARY_KEY char(36) primary key, " +
			"TITLE tinytext, " +
			"SYSTEM tinytext);";

	private static final String METHOD_GET_N_GAMES = "getNGamesStartingAtIndex";
	private static String METHOD_GET_OBJECT_WITH_NAME = "getObjectWithName";

	private static HibernateGameManager manager;

	public HibernateGameManager() {
		super();
	}

	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public static HibernateGameManager getDefault() {

		if (manager == null) {
			manager = new HibernateGameManager();
		}
		return manager;
	}

	public String getClassName() {
		return GAME_CLASS_NAME;
	}

	@Override
	public boolean setupTable() {
		HibernateUtil.executeSQLQuery(DROP_TABLE_SQL);
		return HibernateUtil.executeSQLQuery(CREATE_TABLE_SQL);
	}

	/**
	 * Adds given object (game) to the database 
	 */
	public synchronized boolean add(Object object) {
		return super.add(object);
	}


	/**
	 * Updates given object (game).
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean update(Game game) {
		boolean result = super.update(game);	
		return result;
	}


	/**
	 * Deletes given game from the database.
	 * Returns true if successful, otherwise returns false.
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean delete(Game game){

		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(game);
			transaction.commit();
			return true;
		}
		catch (HibernateException exception) {
			rollback(transaction);
			BookingLogger.getDefault().severe(this, Messages.METHOD_DELETE_GAME, Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		}	
		catch (RuntimeException exception) {
			rollback(transaction);
			BookingLogger.getDefault().severe(this, Messages.METHOD_DELETE_GAME, Messages.GENERIC_FAILED, exception);
			return errorResult;
		}
		finally {
			closeSession();
		}
	}

	/**
	 * Returns game from the database with given id.
	 * Upon exception returns null.
	 * 
	 * @param id
	 * @return
	 */
	public synchronized Game getGameByTitle(String title) {

		Session session = null;
		Game errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_GAME_WITH_ID);
			query.setParameter(0, title);
			Game aGame = (Game) query.uniqueResult();
			return aGame;
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

	public synchronized Game getGameByToken(String token) {

		Session session = null;
		Game errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_GAME_WITH_TOKEN);
			query.setParameter(0, token);
			Game aGame = (Game) query.uniqueResult();
			return aGame;
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
	 * Returns game with given emailAddress and password from the database. 
	 * If not found returns null.
	 * 
	 * @param emailAddress
	 * @param password
	 * @return
	 */
	public Game getGame(String title) {

		Game game = getGameByTitle(title);
		if ((game != null) && (game.getTitle().equals(title))) {
			return game;
		} else {
			return null;
		}
	}

	/**
	 * Returns games, 
	 * from database.
	 * If not found returns null.
	 * Upon error returns null.
	 * 
	 * @param phonenumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized  List<Game> getNGamesStartingAtIndex(int index, int n) {
		List<Game> errorResult = null;
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_GAMES);
			query.setFirstResult(index);
			query.setMaxResults(n);
			List<Game> games = query.list();

			return games;
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAMES,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		}  catch (JDBCConnectionException exception) {
			HibernateUtil.clearSessionFactory();
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAMES,
					Messages.HIBERNATE_CONNECTION_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAMES,
					Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this, METHOD_GET_N_GAMES,
					Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}

	public String getTableName() {
		return GAME_TABLE_NAME;
	}


	/**
	 * Returns number of games.
	 * 
	 * Upon error returns empty list.
	 * 
	 * @param a charge status
	 * @return
	 */
	public synchronized int getNumberOfGames() {

		Session session = null;
		Long aLong;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session
					.createQuery(SELECT_NUMBER_GAMES);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_GAMES,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return 0;
		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_GAMES,
					Messages.HIBERNATE_FAILED, exception);
			return 0;
		} catch (RuntimeException exception) {
			BookingLogger.getDefault().severe(this,
					Messages.METHOD_GET_NUMBER_OF_GAMES,
					Messages.GENERIC_FAILED, exception);
			return 0;
		} finally {
			closeSession();
		}
	}
}