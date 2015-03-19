/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dfsagent.datastructures;

import java.io.Serializable;

/**
 *
 * @author student
 */
public class FileDescription implements Serializable {
    public final int fileName;
    public final int fileSize;
    public final String path;
    private State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
    public enum State { UPLOADING, COMPLETE}
    public FileDescription(int fileName, int fileSize, String path) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.path = path;
        state = State.UPLOADING;
    }

}
