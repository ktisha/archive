package sfs.storage;

import dfsexceptions.DataBaseDfsException;

import sfs.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.dbmanager.DBManager;

/**
 *
 * @author an
 */
public class StorageManager {

	private static final Logger log = Logger.getLogger("storage");
	private static StorageManager instance = null;
	private final ConcurrentHashMap<Integer, Storage> storages = new ConcurrentHashMap<Integer, Storage>();

	/**
	 * Create instance of StorageManager, saves to static var.
	 * @throws DataBaseDfsException if some problem with database.
	 */
	public static void Init(int spaceChechSleepTime) throws DataBaseDfsException {
		if (instance == null) {
			instance = new StorageManager(spaceChechSleepTime);
		}
	}

	/**
	 * Returns single instance.
	 * @return single instance
	 * @throws RuntimeException if StorageManager has not been init.
	 */
	public static StorageManager GetInstance() {
		if (instance == null) {
			throw new RuntimeException("Using not initialized StorageManager");
		}
		return instance;
	}

	/**
	 * Returns Storage by storage.id
	 * @param storage id
	 * @return storage with the same id
	 * @throws StorageManagerException - if there are not storage with the same id
	 */
	public Storage getStorageById(int id) throws StorageManagerException {
		Storage storage = storages.get(new Integer(id));
		if (storage == null) {
			throw new StorageManagerException("No storage with id = " + id);
		}
		return storage;
	}

	public void place(SfsFileDescr fileDescr, int partSize) throws StorageManagerException, DataBaseDfsException, FSException {
		int wholeParts = fileDescr.size / partSize;
		int lastpartSize = fileDescr.size % partSize;

		fileDescr.fileparts = new ArrayList<SfsFilePartDescr>();

		try {

			// place parts to storages
			for (int i = 0; i < wholeParts; ++i) {
				fileDescr.fileparts.add(placeWithCopy(partSize, fileDescr.copy));
			}

			// place last part
			if (lastpartSize != 0) {
				fileDescr.fileparts.add(placeWithCopy(lastpartSize, fileDescr.copy));
			}

			//fileDescr = DBManager.GetInstance().createFiledescr(fileDescr);


		} catch (NoSuchElementException ex) {
			throw new StorageManagerException("Not free space on storages");
		} finally {
			// unblock blocked space
			for (SfsFilePartDescr filePartDescr : fileDescr.fileparts) {
				for (RemoteFileDescr remoteFileDescr : filePartDescr.remotedescrs) {
					unblockSpace(remoteFileDescr.size, remoteFileDescr.storage);
				}
			}
		}
	}

	private SfsFilePartDescr placeWithCopy(int size, int copy) throws StorageManagerException {
		SfsFilePartDescr filePartDescr = new SfsFilePartDescr();
		filePartDescr.remotedescrs = new ArrayList<RemoteFileDescr>();
		filePartDescr.size = size;

		try {
			Iterator<Storage> it = storages.values().iterator();
			Storage currentStorage = null;

			for (int i = 0; i < copy; ++i) {
				currentStorage = it.next();
				// try to place this part to storage
				boolean placed = false;
				while (!placed) {
					// check storage state
					if (currentStorage.getState() == Storage.State.OFFLINE) {
						currentStorage = it.next();
					} else {
						// try block space on storage
						if (blockSpace(size, currentStorage)) {
							RemoteFileDescr remoteFileDescr = new RemoteFileDescr();
							remoteFileDescr.size = size;
							remoteFileDescr.storage = currentStorage;
							filePartDescr.remotedescrs.add(remoteFileDescr);
							placed = true;
						} else {
							// fial palce on current storage, get next storage
							currentStorage = it.next();
						}
					}
				}
				
			}
		} catch (NoSuchElementException ex) {
			throw new StorageManagerException("Not free space on storages");
		}

		return filePartDescr;
	}

	private boolean blockSpace(int space, Storage storage) {
		if (storage.getFreeSpace() - storage.blockedSpace < space) {
			return false;
		}
		// TODO storage.blockedSpace += space;
		return true;
	}

	private void unblockSpace(int space, Storage storage) {
		// TODO storage.blockedSpace -= space;
	}

	private StorageManager(int spaceChechSleepTime) throws DataBaseDfsException {

		// Load storages from database
		for (DBManager.Storage dbStorage : DBManager.GetInstance().getStorages()) {
			storages.put(new Integer(dbStorage.id), new Storage(new Storage(dbStorage)));
		}

		// start storage guard
		Guard storageGuard = new Guard(storages, spaceChechSleepTime);
		storageGuard.start();

	}
}
