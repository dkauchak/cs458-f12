package crawler;

import java.net.*;
import java.util.*;

public class URLQueue implements threads.Queue {

	LinkedList<String> evenQueue;
	LinkedList<String> oddQueue;
	Set<String> gatheredLinks;
	Set<String> processedLinks;
	private HashMap<String, Long> domainTimeAccessedLookup;

	/**
	 * Maximum number of elements allowed in the gatheredLinks set
	 */
	int maxElements;

	/**
	 * Additional data to be passed to the thread: A filename prefix that
	 * specifies where the spidered files should be stored
	 */
	String filenamePrefix;

	public URLQueue() {
		evenQueue = new LinkedList<String>();
		oddQueue = new LinkedList<String>();
		gatheredLinks = new HashSet<String>();
		processedLinks = new HashSet<String>();
		maxElements = -1;
		domainTimeAccessedLookup = new HashMap<String, Long>();
		filenamePrefix = "";
	}

	public URLQueue(int _maxElements, String _filenamePrefix) {
		evenQueue = new LinkedList<String>();
		oddQueue = new LinkedList<String>();
		gatheredLinks = new HashSet<String>();
		processedLinks = new HashSet<String>();
		maxElements = _maxElements;
		domainTimeAccessedLookup = new HashMap<String, Long>();
		filenamePrefix = _filenamePrefix;
	}

	public void setFilenamePrefix(String _filenamePrefix) {
		filenamePrefix = _filenamePrefix;
	}

	public String getFilenamePrefix() {
		return filenamePrefix;
	}

	public void setMaxElements(int _maxElements) {
		maxElements = _maxElements;
	}

	public Set<String> getGatheredElements() {
		return gatheredLinks;
	}

	public Set<String> getProcessedElements() {
		return processedLinks;
	}

	public int getQueueSize(int level) {
		if (level % 2 == 0) {
			return evenQueue.size();
		} else {
			return oddQueue.size();
		}
	}

	public int getProcessedSize() {
		return processedLinks.size();
	}

	public int getGatheredSize() {
		return gatheredLinks.size();
	}

	public synchronized Object pop(int level) {
		String s;
		if (level % 2 == 0) {
			if (evenQueue.size() == 0) {
				return null;
			} else {
				s = (String) evenQueue.removeFirst();
			}
		} else {
			if (oddQueue.size() == 0) {
				return null;
			} else {
				s = (String) oddQueue.removeFirst();
			}
		}
		try {
			URL url = new URL(s);
			if (hasSufficientTimePassed(url.getHost())){
				domainTimeAccessedLookup.put(url.getHost(), System.currentTimeMillis());
				processedLinks.add(s);
				return url;
			}
			
			addToQueue(url.toString(), level);
			return pop(level);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private boolean hasSufficientTimePassed(String host) {
		if (domainTimeAccessedLookup.get(host) == null)
			return true;
		if (System.currentTimeMillis() - domainTimeAccessedLookup.get(host) >= 1000)
			return true;
		return false;
	}

	public synchronized boolean push(Object url, int level) {
		// don't allow more than maxElements links to be gathered
		if (maxElements != -1 && maxElements <= gatheredLinks.size())
			return false;
		String s = ((URL) url).toString();
		if (gatheredLinks.add(s)) {
			// has not been in set yet, so add to the appropriate queue
			addToQueue(s, level);
			return true;
		} else {
			// this link has already been gathered
			return false;
		}
	}

	private void addToQueue(String s, int level) {
		if (level % 2 == 0) {
			evenQueue.addLast(s);
		} else {
			oddQueue.addLast(s);
		}
	}

	public synchronized void clear() {
		evenQueue.clear();
		oddQueue.clear();
	}
}
