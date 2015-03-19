/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dfsexceptions;


public class LoginDfsException extends DfsException {
	public LoginDfsException(Exception e) {
		super(e);
	}
	public LoginDfsException(String string) {
		super(string);
	}
}
