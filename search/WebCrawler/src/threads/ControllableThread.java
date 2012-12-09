package threads;

import java.io.BufferedWriter;
import java.io.IOException;


abstract public class ControllableThread extends Thread {
	protected int level;
	protected int id;
	protected Queue queue;
	protected ThreadController threadController;
	protected MessageReceiver messageReceiver;
	protected BufferedWriter bufferedWriter;
	public void setId(int _id) {
		id = _id;
	}
	public void setLevel(int _level) {
		level = _level;
	}
	public void setQueue(Queue _queue) {
		queue = _queue;
	}
	public void setThreadController(ThreadController _tc) {
		threadController = _tc;
	}
	public void setMessageReceiver(MessageReceiver _mr) {
		messageReceiver = _mr;
	}
	public ControllableThread() {
	}
	public void run() {
		for (Object newTask = queue.pop(level);
			 newTask != null;
			 newTask = queue.pop(level)) {
			messageReceiver.receiveMessage(newTask, id);
			process(newTask);
			if (threadController.getMaxThreads() > threadController.getRunningThreads()) {
				try {
					threadController.startThreads();
				} catch (Exception e) {
					System.err.println("[" + id + "] " + e.toString());
				}
			}
		}
		threadController.finished(id);
		try {
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The thread invokes the process method for each object in the queue
	 */
	public abstract void process(Object o);
	
	public void setBufferedWriter(BufferedWriter bufferedWriter) {
		this.bufferedWriter = bufferedWriter;
	}
}
