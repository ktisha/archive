/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dfsexceptions;


public class TaskException extends DfsException {
	public TaskException(Exception e) {
		super(e);
	}
	public TaskException(String string) {
		super(string);
	}
}