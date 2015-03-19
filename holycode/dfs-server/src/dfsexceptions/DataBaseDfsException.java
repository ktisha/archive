/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dfsexceptions;


public class DataBaseDfsException extends DfsException {
	public DataBaseDfsException(Exception e) {
		super(e);
	}
	public DataBaseDfsException(String string) {
		super(string);
	}
}
