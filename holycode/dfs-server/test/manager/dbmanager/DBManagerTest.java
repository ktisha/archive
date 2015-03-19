package manager.dbmanager;

import dfsexceptions.DataBaseDfsException;
import org.junit.BeforeClass;
import org.junit.Test;
import sfs.FSException;
import static org.junit.Assert.*;
import sfs.SfsFileDescr;
import sfs.storage.StorageManager;
import sfs.storage.StorageManagerException;

/**
 *
 * @author an
 */
public class DBManagerTest {

	public DBManagerTest() {
	}

	@BeforeClass
	public static void beforeClass() throws DataBaseDfsException {
		DBManager.Init("dfs-test.db");
		StorageManager.Init(6000000);
	}

	/**
	 * Test of createFiledescr method, of class DBManager.
	 */
	@Test(expected = DataBaseDfsException.class)
	public void testCreateFiledescr() throws Exception {
		System.out.println("createFiledescr");
		SfsFileDescr filedescr = new SfsFileDescr();
		filedescr.user = "student";
		filedescr.filelistId = 6;
		filedescr.name = "testfile122";
		filedescr.size = 666;
		filedescr.copy = 3;
		filedescr.state = SfsFileDescr.State.UPLOADING;

		DBManager instance = DBManager.GetInstance();
		instance.createFiledescr(filedescr);

		instance.getFileDescr(filedescr.user, filedescr.id);
	}
}
