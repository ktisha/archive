/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dfsexceptions;

/**
 *
 * @author an
 */
public class DfsException extends Exception {
	public DfsException(Exception e) {
		super(e);
	}
	public DfsException(String string) {
		super(string);
	}
}