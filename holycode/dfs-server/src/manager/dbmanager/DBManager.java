package manager.dbmanager;

import FileDiscriptor.FileDiscriptor;
import FileDiscriptor.FileList;
import FileDiscriptor.UserDiscr;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.*;

import dfsexceptions.*;
import java.util.ArrayList;
import java.util.List;
import sfs.FSException;
import sfs.RemoteFileDescr;
import sfs.SfsFileDescr;
import sfs.SfsFilePartDescr;
import sfs.storage.StorageManagerException;

public class DBManager {

	public class Storage {

		public final int id;
		public final String host;
		public final int port;

		public Storage(int id, String host, int port) {
			this.id = id;
			this.host = host;
			this.port = port;
		}
	}
	static private final int PART_SIZE = 10 * 1024 * 1024 * 1024; // 10Mb
	//static private final int PART_SIZE = 10;
	//static private final String DB_NAME = "dfs-test.db";
	static private final Logger log = Logger.getLogger("dblog");
	private final String connectionURL;
	static private final String GET_FILES =
			"select fd.id, fd.name, size, copy, fl.id, fl.name, fd.state from filediscr as fd " +
			"join filelist as fl " +
			"on fl.id=fd.filelist_id and fd.name LIKE ? " + // TODO and fd.state=0" +
			"join \"user\" as us " +
			"on us.id=fl.user_id and us.name = ?";
	static private final String GET_FILES_FILELIST =
			"select fd.id, fd.name, size, copy, fl.id, fl.name, fd.state from filediscr as fd " +
			"join filelist as fl " +
			"on fl.id=fd.filelist_id and fl.id=? and fd.name LIKE ?  " + //and fd.state=0" +
			"join \"user\" as us " +
			"on us.id=fl.user_id and us.name = ?";
	static private final String LOGIN = "UPDATE \"user\" SET session = ? " +
			"WHERE name = ? AND pass = ?";
	static private final String CHECK_LOGIN = "SELECT id, name FROM \"user\" " +
			"WHERE session = ?";
	static private final String GET_ALL_FILELIST = "select fl.id, fl.name " +
			"from filelist as fl " +
			"join \"user\" as u " +
			"on u.id=fl.user_id and u.name= ?";
	static private final String GET_FILE_PARTS = "SELECT fd.id, fd.size, " +
			"fp.id, fp.num, fp.size, st.id, fd.state, fd.copy FROM filediscr AS fd " +
			"JOIN filelist AS fl " +
			"ON fl.id=fd.filelist_id AND fd.id = ? " + // AND fd.state = " + SfsFileDescr.State.READY.ordinal() +
			"JOIN \"user\" AS us " +
			"ON us.id=fl.user_id AND us.name=?" +
			"JOIN filepart AS fp " +
			"ON fd.id=fp.filediscr_id " +
			"JOIN storage AS st " +
			"ON st.id=fp.storage_id " +
			"ORDER BY fp.num";
	static private final String ADD_FILELIST = "insert into " +
			"filelist(name, user_id) values(?, ?)";
	static private final String ADD_FILE = "insert into " +
			"filediscr(name, size, copy, state, filelist_id) " +
			"values(?, ?, ?, 2, ?)";
	static private final String ADD_USER = "INSERT into " +
			"\"user\"(name, pass) values(?, ?)";
	static private final String REMOVE_FILEPARTS_FOR_FILE =
			"DELETE from filepart WHERE id in (" +
			"SELECT fp.id from filepart as fp " +
			"JOIN filediscr as fd " +
			"on fd.id=? and fd.id=fp.filediscr_id " +
			"JOIN filelist as fl " +
			"on fl.id = fd.filelist_id " +
			"JOIN \"user\" as us " +
			"on fl.user_id = us.id and us.name=?)";
	static private final String REMOVE_FILEPARTS_FOR_FILELIST =
			"DELETE from filepart WHERE id in (" +
			"SELECT fp.id from filepart as fp " +
			"JOIN filediscr as fd " +
			"on fd.filelist_id=? and fd.id=fp.filediscr_id " +
			"JOIN filelist as fl on fl.id = fd.filelist_id and fl.user_id=?)";
	static private final String REMOVE_FILE_BY_ID =
			"DELETE from filediscr WHERE id in(" +
			"SELECT fd.id from filediscr as fd " +
			"JOIN filelist as fl " +
			"on fd.filelist_id=fl.id and fd.id=? " +
			"JOIN \"user\" as us " +
			"on us.id = fl.user_id and us.name = ?" +
			")";
	static private final String REMOVE_FILE_FOR_FILILIST =
			"DELETE from filediscr WHERE id in (" +
			"SELECT fd.id from filediscr as fd " +
			"JOIN filelist as fl " +
			"on fl.id = fd.filelist_id and fl.user_id=? and fl.id=?)";
	static private final String REMOVE_FILELIST =
			"DELETE from filelist WHERE id=? and user_id=?";
	static private final String GET_FILELIST_ID =
			"SELECT fl.id from filelist as fl " +
			"JOIN \"user\" as us " +
			"ON fl.user_id=us.id " +
			"AND us.name=?" +
			"AND fl.name=?";
	static private final String CREATE_FILEDESCR =
			"INSERT INTO filediscr(name, size, copy, state, filelist_id) " +
			"values(?, ?, ?, ?, ?)";
	static private final String GET_ALL_STORAGE =
			"SELECT id, hostname, port, state FROM storage";
	static private final String ADD_FILEPART =
			"insert into filepart(num, size, filediscr_id, storage_id) " +
			"values(?, ?, ?, ?)";
	static private final String MOVE_DEAD_FILE =
			"UPDATE filediscr " +
			"SET filelist_id = 0, name = ? " +
			"WHERE id IN " +
			"(SELECT fd.id FROM filediscr as fd " +
			"JOIN filelist as fl " +
			"on fl.id = fd.filelist_id AND fd.id = ? " +
			"JOIN \"user\" as us " +
			"on us.id = fl.user_id AND us.name = ?)";
	static private final String MOVE_FILE =
			"UPDATE filediscr " +
			"SET filelist_id = ? WHERE id = ? and " +
			"1 = (SELECT COUNT(*) from filediscr as fd " +
			"JOIN filelist as fl " +
			"on fl.id = fd.filelist_id " +
			"JOIN \"user\" as us " +
			"on us.id = fl.user_id AND us.name = ? where fd.id = ? ) and " +
			"1 = (SELECT COUNT(*) from filelist as fl " +
			"JOIN \"user\" as us " +
			"on us.id = fl.user_id AND us.name = ? " +
			"WHERE fl.id = ? " +
			")";
	static private final String ADD_STORAGE =
			"INSERT into storage(hostname, port, state) " +
			"values(?, ?, 0)";
	static private final util.KeyGenerator keygenerator = new util.KeyGenerator();
	static private DBManager dbmanager = null;

	static public void Init(String dbPath) {
		if (dbmanager == null) {
			dbmanager = new DBManager(dbPath);
		}

	}

	static public DBManager GetInstance() {
		if (dbmanager == null) {
			throw new RuntimeException("Using unit database manager");
		}
		return dbmanager;
	}

	/**
	 * Moves fileDescr to removed lits.
	 * @param sfsFileDescr fileDescr for moving
	 * @throws DataBaseDfsException if some error on database.
	 */
	public void moveDeadFiledescr(SfsFileDescr sfsFileDescr) throws DataBaseDfsException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			conn = DriverManager.getConnection(connectionURL);
			// remove fileparts for this file

			ps = conn.prepareStatement(MOVE_DEAD_FILE);
			ps.setInt(1, sfsFileDescr.id);
			ps.setInt(2, sfsFileDescr.id);
			ps.setString(3, sfsFileDescr.user);

			if (ps.executeUpdate() != 1) {
				throw new DataBaseDfsException("Error on removing file: can't move file to dead list");
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			throw new DataBaseDfsException(e);
		} finally {
			closeDBRes(result, ps, conn);
		}
	}

	public void moveFiledescr(SfsFileDescr sfsFileDescr, int destFilelistId) throws DataBaseDfsException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			conn = DriverManager.getConnection(connectionURL);

			ps = conn.prepareStatement(MOVE_FILE);

			ps.setInt(1, destFilelistId);
			ps.setInt(2, sfsFileDescr.id);
			ps.setString(3, sfsFileDescr.user);
			ps.setInt(4, sfsFileDescr.id);
			ps.setString(5, sfsFileDescr.user);
			ps.setInt(6, destFilelistId);


			if (ps.executeUpdate() != 1) {
				throw new DataBaseDfsException("Error on moving file: " +
						"file [id=" + sfsFileDescr.id + "] doesn't exist " +
						"or destination filelist [id=" + destFilelistId + "] does't exist");
			}

		} catch (SQLIntegrityConstraintViolationException ex) {
			log.log(Level.SEVERE, "Duplicate file in filelist", ex);
			throw new DataBaseDfsException("Duplicate file in filelist");
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "", ex);
			throw new DataBaseDfsException(ex);
		} finally {
			closeDBRes(result, ps, conn);
		}
	}

	/**
	 * Removes filediscriptor from database, also removes all fileparts 
	 * @param fileDiscriptor
	 * @param user
	 * @throws DataBaseDfsException if some error on database.
	 */
//	private void removeFiledescr(SfsFileDescr sfsFileDescr) throws DataBaseDfsException {
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet result = null;
//
//		try {
//			conn = DriverManager.getConnection(connectionURL);
//			// remove fileparts for this file
//
//			ps = conn.prepareStatement(REMOVE_FILEPARTS_FOR_FILE);
//			ps.setInt(1, sfsFileDescr.id);
//			ps.setString(2, sfsFileDescr.user);
//
//			ps.executeUpdate();
//			ps.close();
//
//			// remove file
//			ps = conn.prepareStatement(REMOVE_FILE_BY_ID);
//			ps.setInt(1, sfsFileDescr.id);
//			ps.setString(2, sfsFileDescr.user);
//
//			ps.executeUpdate();
//
//			// TODO remove file from storage
//		} catch (SQLException e) {
//			log.log(Level.SEVERE, "", e);
//			throw new DataBaseDfsException(e);
//		} finally {
//			closeDBRes(result, ps, conn);
//		}
//	}
	/**
	 * Removes empty filelist from database.
	 * @param userId
	 * @param filelistId id or filelist for removing
	 * @throws DataBaseDfsException if some error on database.
	 */
	public void removeFilelist(int userId, int filelistId) throws DataBaseDfsException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			conn = DriverManager.getConnection(connectionURL);

			ps = conn.prepareStatement(REMOVE_FILELIST);
			ps.setInt(1, filelistId);
			ps.setInt(2, userId);

			if (ps.executeUpdate() != 1) {
				throw new DataBaseDfsException("Removed " + ps.getUpdateCount() + " rows");
			}
		} catch (SQLIntegrityConstraintViolationException ex) {
			throw new DataBaseDfsException("Filelist is not empty");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			throw new DataBaseDfsException(e);
		} finally {
			closeDBRes(result, ps, conn);
		}
	}

//	private int createFile(int userId, int filelistId, String filename, int size) throws DataBaseDfsException {
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet result = null;
//		int fileId = 0;
//		int partId = 0;
//
//		try {
//			conn = DriverManager.getConnection(connectionURL);
//
//			ps = conn.prepareStatement(ADD_FILE, Statement.RETURN_GENERATED_KEYS);
//			ps.setString(1, filename);
//			ps.setInt(2, size);
//			ps.setInt(3, 1);
//			ps.setInt(4, filelistId);
//
//			try {
//				ps.execute();
//				ResultSet rs = ps.getGeneratedKeys();
//				rs.next();
//				fileId = rs.getInt(1);
//				log.info("newFile ID = " + fileId);
//				log.info("size = " + size);
//
//				ps = conn.prepareStatement(ADD_FILEPART, Statement.RETURN_GENERATED_KEYS);
//				ps.setInt(2, fileId);
//				ps.setInt(1, size);
//
//				ps.execute();
//				rs = ps.getGeneratedKeys();
//				rs.next();
//				partId = rs.getInt(1);
//
//				// TODO catch more exception
//			} catch (SQLException ex) {
//				log.log(Level.INFO, "", ex);
//				// TODO make good error response
//				throw new DataBaseDfsException("Duplicate fillist name or something else... i don't know..");
//			}
//
//		} catch (SQLException e) {
//			log.log(Level.SEVERE, "", e);
//			throw new DataBaseDfsException(e);
//		} finally {
//			closeDBRes(result, ps, conn);
//		}
//
//		return partId;
//	}
	/**
	 * Creates filelist in database
	 * @param user id
	 * @param filelist name
	 * @return id of created filelist
	 * @throws DataBaseDfsException - if some problems with database
	 */
	public int createFileList(int userId, String filelistname) throws DataBaseDfsException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		int filelistId = 0;

		try {
			conn = DriverManager.getConnection(connectionURL);

			ps = conn.prepareStatement(ADD_FILELIST, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, filelistname);
			ps.setInt(2, userId);

			try {
				ps.execute();
				ResultSet rs = ps.getGeneratedKeys();

				rs.next();
				filelistId = rs.getInt(1);

				// TODO catch more exception
			} catch (SQLIntegrityConstraintViolationException ex) {
				log.log(Level.INFO, "", ex);
				throw new DataBaseDfsException("Duplicate filelist name or something else... i don't know..");
			} catch (SQLException ex) {
				throw new DataBaseDfsException(ex);
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			throw new DataBaseDfsException(e);
		} finally {
			closeDBRes(result, ps, conn);
		}

		return filelistId;
	}

	/**
	 * Creates filedescr in database, places its parts on the storages.
	 * @param filedescr
	 * @throws DataBaseDfsException if some problem with database
	 * @throws sfs.FSException if some problem with StorageManager
	 */
	public void createFiledescr(SfsFileDescr filedescr) throws DataBaseDfsException, sfs.FSException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// Create connection, prepareStatement
			conn = DriverManager.getConnection(connectionURL);
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(CREATE_FILEDESCR, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, filedescr.name);
			ps.setInt(2, filedescr.size);
			ps.setInt(3, filedescr.copy);
			ps.setInt(4, SfsFileDescr.State.UPLOADING.ordinal());
			ps.setInt(5, filedescr.filelistId);

			// exec query
			ps.execute();
			rs = ps.getGeneratedKeys();
			rs.next();
			filedescr.id = rs.getInt(1);

			try {
				// place fileparts on storages
				sfs.storage.StorageManager.GetInstance().place(filedescr, PART_SIZE);
			} catch (StorageManagerException ex) {
				throw new DataBaseDfsException(ex);
			}

			// create fileparts in database
			createFileparts(conn, filedescr);

			conn.commit();
		} catch (SQLIntegrityConstraintViolationException ex) {
			String msg = null;
			if ("23503".compareTo(ex.getSQLState()) == 0) {
				msg = "Not filelist with id = " + filedescr.filelistId;
			} else if ("23505".compareTo(ex.getSQLState()) == 0) {
				msg = "Duplicate file in filelist";
			} else {
				throw new DataBaseDfsException(ex);
			}
			throw new sfs.FSException(msg);
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "", ex);
			throw new DataBaseDfsException(ex);
		} finally {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				log.log(Level.SEVERE, "The data source connection cannot be closed.", ex);
				throw new DataBaseDfsException(ex);
			} finally {
				// close darabase resource
				closeDBRes(rs, ps, conn);
			}
		}
	}

	/**
	 * Creates SfsFileDescr, gets information from database.
	 * @param user login
	 * @param file id
	 * @return SfsFileDescr
	 * @throws DataBaseDfsException - if some problems with database
	 */
	public SfsFileDescr getFileDescr(String login, int id) throws DataBaseDfsException, FSException {
		SfsFileDescr sfsfiledescr = new SfsFileDescr();
		sfsfiledescr.id = id;
		sfsfiledescr.fileparts = new ArrayList<SfsFilePartDescr>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			conn = DriverManager.getConnection(connectionURL);

			ps = conn.prepareStatement(GET_FILE_PARTS);
			ps.setInt(1, id);
			ps.setString(2, login);

			// get all fileparts
			result = ps.executeQuery();

			// TODO if result is emptu throw error

			int num = -1;
			while (result.next()) {
				// TODO uncomment next block
//				if (result.getInt(7) != SfsFileDescr.State.READY.ordinal()) {
//					throw new sfs.FSException("File is not available (" +
//											  SfsFileDescr.State.values()[result.getInt(7)] +
//											  ")");
//				}

				sfsfiledescr.size = result.getInt(2);
				sfsfiledescr.state = SfsFileDescr.State.values()[result.getInt(7)];
				sfsfiledescr.copy = result.getInt(8);

				int filepartNum = result.getInt(4);

				RemoteFileDescr remotefile = new RemoteFileDescr();
				remotefile.id = result.getInt(3);
				remotefile.size = result.getInt(5);
				remotefile.storage = sfs.storage.StorageManager.GetInstance().getStorageById(result.getInt(6));

				if (filepartNum == num + 1) {
					// next file part descr
					SfsFilePartDescr sfsfilepart = new SfsFilePartDescr();
					sfsfilepart.size = result.getInt(5);
					sfsfilepart.remotedescrs = new ArrayList<RemoteFileDescr>();
					// add it filepart to filedescr
					sfsfiledescr.fileparts.add(sfsfilepart);
					num++;
				}
//				} else if (filepartNum > num + 1) { // if not found one or more file part
//					throw new DataBaseDfsException("Illegal file state, not found " +
//							"file part # " + num);
//				}

				// add remote file to file part
				sfsfiledescr.fileparts.get(result.getInt(4)).remotedescrs.add(remotefile);
			}

			if (num == -1) {
				throw new FSException("File not found id=" + sfsfiledescr.id);
			}

		} catch (StorageManagerException ex) {
			log.log(Level.SEVERE, "", ex);
			throw new DataBaseDfsException(ex);
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "", ex);
			throw new DataBaseDfsException(ex);
		} finally {
			closeDBRes(result, ps, conn);
		}

		return sfsfiledescr;
	}

	/**
	 * Gets all filelist for user and returns it.
	 * @param user
	 * @return list of filelists
	 * @throws DataBaseDfsException if some problem with database
	 */
	public List<FileList> getAllFilelist(String user) throws DataBaseDfsException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		List<FileList> fileLists = new ArrayList<FileList>();

		//query = "?" + query + "?";

		try {
			conn = DriverManager.getConnection(connectionURL);
			ps = conn.prepareStatement(GET_ALL_FILELIST);
			ps.setString(1, user);
			log.info(ps.toString());
			result = ps.executeQuery();

			while (result.next()) {
				FileList filelist = new FileList();
				filelist.id = result.getInt(1);
				filelist.name = result.getString(2);
				fileLists.add(filelist);
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			throw new DataBaseDfsException(e);
		} finally {
			closeDBRes(result, ps, conn);
		}

		return fileLists;

	}

	/**
	 * Gets files from database
	 * @param user login
	 * @param query for search files
	 * @return list of FileDiscriptor
	 * @throws DataBaseDfsException if some problems in database,
	 * or login is not correct.
	 */
	public List<FileDiscriptor> getFiles(String login, String query, int filelistId) throws DataBaseDfsException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		List<FileDiscriptor> fileDiscriptionList = new ArrayList<FileDiscriptor>();

		//query = "?" + query + "?";

		try {
			conn = DriverManager.getConnection(connectionURL);

			if (filelistId == 0) {
				ps = conn.prepareStatement(GET_FILES);
				ps.setString(2, login);
				ps.setString(1, query);
			} else {
				ps = conn.prepareStatement(GET_FILES_FILELIST);
				ps.setString(3, login);
				ps.setString(2, query);
				ps.setInt(1, filelistId);
			}


			result = ps.executeQuery();

			while (result.next()) {
				FileDiscriptor fileDiscription = new FileDiscriptor();
				// read information
				fileDiscription.id = result.getInt(1);
				fileDiscription.name = result.getString(2);
				fileDiscription.size = result.getInt(3);
				fileDiscription.copy = result.getInt(4);
				fileDiscription.state = SfsFileDescr.State.values()[result.getInt(7)];

				fileDiscription.fileList = result.getString(6);
				// add filediscr to list
				fileDiscriptionList.add(fileDiscription);
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			throw new DataBaseDfsException(e);
		} finally {
			closeDBRes(result, ps, conn);
		}

		return fileDiscriptionList;
	}

	/**
	 * Checks session key, returns user login for it session key
	 * @param session key
	 * @return user login
	 * @throws DataBaseDfsException if some probles with database
	 * @throws LoginDfsException if such session key aren't in database
	 */
	public UserDiscr getLogin(String sessionKey) throws DataBaseDfsException, LoginDfsException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		UserDiscr userDiscr = new UserDiscr();

		try {
			// Create connection, prepareStatement
			conn = DriverManager.getConnection(connectionURL);
			ps = conn.prepareStatement(CHECK_LOGIN);
			ps.setString(1, sessionKey);

			// exec query
			result = ps.executeQuery();
			// get result
			if (result.next()) {
				userDiscr.id = result.getInt("id");
				userDiscr.name = result.getString("name");
			} else {
				throw new LoginDfsException("Bad session key");
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, "", ex);
			throw new DataBaseDfsException(ex);
		} finally {
			// close darabase resource
			closeDBRes(result, ps, conn);
		}

		return userDiscr;
	}

	/**
	 * Checks user login and pass, generates and returns session key.
	 * @param user login
	 * @param user pass
	 * @return session key
	 * @throws DataBaseDfsException if some probles with database
	 * @throws LoginDfsException if such login and pass aren't in database
	 */
	public String login(String user, String pass) throws DataBaseDfsException, LoginDfsException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		// generate random session key
		String sessionKey = keygenerator.generate();

		try {
			// Open connection, prepare sql statement
			conn = DriverManager.getConnection(connectionURL);
			ps = conn.prepareStatement(LOGIN);
			ps.setString(1, sessionKey);
			ps.setString(2, user);
			ps.setString(3, pass);

			// if fail on login
			if (ps.executeUpdate() == 0) {
				throw new LoginDfsException("Bad login or pass");
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			throw new DataBaseDfsException(e);
		} finally {
			closeDBRes(result, ps, conn);
		}

		return sessionKey;
	}

	public boolean addUser(String user, String pass) throws DataBaseDfsException {
		boolean result = true;
		String ADD_USER_STATEMENT = "insert into \"user\"(name, pass) values( ? , ? )";


		Connection conn = null;

		try {
			conn = DriverManager.getConnection(connectionURL);

			PreparedStatement sqlquery = conn.prepareStatement(ADD_USER_STATEMENT);
			sqlquery.setString(1, user);
			sqlquery.setString(2, pass);

			try {
				sqlquery.execute();
			} catch (SQLIntegrityConstraintViolationException ex) {
				result = false;
			}


		} catch (SQLException e) {
			log.log(Level.SEVERE, "DBManager can't connect to database " + connectionURL, e);
			throw new DataBaseDfsException(e);
		}

		return false;
	}

	/**
	 * Returns storages from databse.
	 * @return list of storages
	 * @throws DataBaseDfsException if some problem with databse
	 */
	public List<Storage> getStorages() throws DataBaseDfsException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		List<Storage> storages = new ArrayList<Storage>();

		try {
			// Create connection, prepareStatement
			conn = DriverManager.getConnection(connectionURL);
			ps = conn.prepareStatement(GET_ALL_STORAGE);

			// exec query
			result = ps.executeQuery();

			while (result.next()) {
				Storage storage = new Storage(result.getInt(1), result.getString(2), result.getInt(3));
				storages.add(storage);
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, "", ex);
			throw new DataBaseDfsException(ex);
		} finally {
			// close darabase resource
			closeDBRes(result, ps, conn);
		}

		return storages;
	}

	public void addStorage(String host, int port) throws DataBaseDfsException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			// Create connection, prepareStatement
			conn = DriverManager.getConnection(connectionURL);
			ps = conn.prepareStatement(ADD_STORAGE);
			ps.setString(1, host);
			ps.setInt(2, port);

			// exec query
			ps.execute();
		} catch (SQLIntegrityConstraintViolationException ex) {
			// storage exists in database already
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "", ex);
			throw new DataBaseDfsException(ex);
		} finally {
			// close darabase resource
			closeDBRes(null, ps, conn);
		}
	}




	public void updateFileState() throws DataBaseDfsException {


	}


	/**
	 * Trys close all resource
	 * @param ResultSet rs
	 * @param Statement ps
	 * @param Connection conn
	 */
	private void closeDBRes(ResultSet rs, Statement ps, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "The result set cannot b closed.", e);
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "The statement cannot be closed.", e);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "The data source connection cannot be closed.", e);
			}
		}
	}

	/**
	 * Creates in database fileparts for fileDescr
	 * @param conn connection for work with
	 * @param fileDescr, its filepasrt will be created in database
	 * @throws DataBaseDfsException if some problem with database
	 */
	private void createFileparts(Connection conn, SfsFileDescr fileDescr) throws DataBaseDfsException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			int i = 0;
			for (SfsFilePartDescr filePartDescr : fileDescr.fileparts) {
				for (RemoteFileDescr remoteFileDescr : filePartDescr.remotedescrs) {

					// prepare statement
					ps = conn.prepareStatement(ADD_FILEPART, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, i);
					ps.setInt(2, remoteFileDescr.size);
					ps.setInt(3, fileDescr.id);
					ps.setInt(4, remoteFileDescr.storage.id);

					ps.execute();
					rs = ps.getGeneratedKeys();
					rs.next();
					remoteFileDescr.id = rs.getInt(1);
				}
				i++;
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, "", ex);
			throw new DataBaseDfsException(ex);
		} finally {
			// close darabase resource
			closeDBRes(rs, ps, null);
		}
	}

//	private int getFilelistId(String login, String filelistName) throws DataBaseDfsException {
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet result = null;
//
//		int listId = 0;
//
//		try {
//			// Create connection, prepareStatement
//			conn = DriverManager.getConnection(connectionURL);
//			ps = conn.prepareStatement(GET_FILELIST_ID);
//			ps.setString(1, login);
//			ps.setString(2, filelistName);
//
//			// exec query
//			result = ps.executeQuery();
//			// get result
//			if (result.next()) {
//				listId = result.getInt(1);
//			}
//
//		} catch (SQLException ex) {
//			log.log(Level.SEVERE, "", ex);
//			throw new DataBaseDfsException(ex);
//		} finally {
//			// close darabase resource
//			closeDBRes(result, ps, conn);
//		}
//
//		return listId;
//	}
	private DBManager(String dbPath) {
		connectionURL = "jdbc:derby:" + dbPath;
	}
}
