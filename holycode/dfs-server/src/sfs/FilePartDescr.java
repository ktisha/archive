package sfs;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author an
 */
public class FilePartDescr {
	public int id;
	public int num;
	public int size;
	public InputStream inStream;
	public OutputStream outStream;
}
