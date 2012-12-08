package threads;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


import crawler.WSDLCrawlerThread;

public class ThreadController {

	int level;
	int maxLevel;
	int maxThreads;
	Queue tasks;
	MessageReceiver receiver;
	Class threadClass;
	int counter;
	int nThreads;
	private BufferedWriter bufferedWriter;

	public ThreadController(Class class1,
							int _maxThreads,
							int _maxLevel,
							Queue _tasks,
							int _level,
							MessageReceiver _receiver)
		throws InstantiationException, IllegalAccessException {
		threadClass = class1;
		maxThreads = _maxThreads;
		maxLevel = _maxLevel;
		tasks = _tasks;
		level = _level;
		receiver = _receiver;
		counter = 0;
		nThreads = 0;
		try {
			FileWriter fileWriter = new FileWriter("C:\\Users\\Daniel\\Documents\\fall2012\\cs458\\pages\\hello.txt");
			bufferedWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		startThreads();
	}

	public synchronized int getUniqueNumber() {
		return counter++;
	}

	public synchronized void setMaxThreads(int _maxThreads)
		throws InstantiationException, IllegalAccessException {
		maxThreads = _maxThreads;
		startThreads();
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public int getRunningThreads() {
		return nThreads;
	}

	public synchronized void finished(int threadId) {
		nThreads--;
		receiver.finished(threadId);
		if (nThreads == 0) {
			level++;
			if (level > maxLevel) {
				receiver.finishedAll();
				return;
			}
			if (tasks.getQueueSize(level) == 0) {
				receiver.finishedAll();
				return;
			}
			try {
				startThreads();
			} catch (InstantiationException e) {
				// Something has gone wrong on the way, because if it hadn't
				// worked at all we wouldn't be here. Anyway, we can do
				// nothing about it, so we just quit instead of moving to
				// a new level.
			} catch (IllegalAccessException e) {
				// Something has gone wrong on the way, because if it hadn't
				// worked at all we wouldn't be here. Anyway, we can do
				// nothing about it, so we just quit instead of moving to
				// a new level.
			}
		}
	}

	public synchronized void startThreads()
		throws InstantiationException, IllegalAccessException {
		// Start m threads
		// For more information on where m comes from see comment on
		// the constructor.
		int m = maxThreads - nThreads;
		int ts = tasks.getQueueSize(level);
		if (ts < m || maxThreads == -1) {
			m = ts;
		}
		// debug
		// System.err.println(m + " " + maxThreads + " " + nThreads + " " + ts);
		// Create some threads
		for (int n = 0; n < m; n++) {
			ControllableThread thread =
				(ControllableThread) threadClass.newInstance();
			thread.setThreadController(this);
			thread.setMessageReceiver(receiver);
			thread.setLevel(level);
			thread.setQueue(tasks);
			thread.setId(nThreads++);
			thread.setBufferedWriter(bufferedWriter);
			thread.start();
		}
	}
}
