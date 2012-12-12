package crawler;
import threads.*;
import java.net.*;

public class Crawler implements MessageReceiver {
	public Crawler(Queue q, int maxLevel, int maxThreads)
		throws InstantiationException, IllegalAccessException {
		ThreadController tc = new ThreadController(MediaExtractorThread.class,
												   maxThreads,
												   maxLevel,
												   q,
												   0,
												   this);
	}

	public void finishedAll() {
		// ignore
	}

	public void receiveMessage(Object o, int threadId) {
		System.out.println("[" + threadId + "] " + o.toString());
	}

	public void finished(int threadId) {
		System.out.println("[" + threadId + "] finished");
	}

	public static void main(String[] args) {
		try {			
			URLQueue q = new URLQueue();
			q.setFilenamePrefix("C:\\Users\\Daniel\\Documents\\fall2012\\cs458\\pages\\");
			q.setMaxElements(-1);
			q.push(new URL("http://www.reddit.com"), 0);
			
			InetAddress address = InetAddress.getByName(new URL("http://www.reddit.com").getHost());
						
			new Crawler(q, -1, 100);
			return;

		} catch (Exception e) {
			System.err.println("An error occured: ");
			e.printStackTrace();
			 System.err.println(e.toString());
		}
		System.err.println("Usage: java PSucker <url> <filenamePrefix> [<maxLevel> [<maxDoc> [<maxThreads>]]]");
		System.err.println("Crawls the web for jpeg pictures and mpeg, avi or wmv movies.");
		System.err.println("-1 for either maxLevel or maxDoc means 'unlimited'.");
	}
}
