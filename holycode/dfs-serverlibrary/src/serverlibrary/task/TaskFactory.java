package serverlibrary.task;

import serverlibrary.exception.TaskDfsException;


abstract public class TaskFactory {

	// Build task from XML
	abstract public Task CreateTask(String xml) throws TaskDfsException;

}
