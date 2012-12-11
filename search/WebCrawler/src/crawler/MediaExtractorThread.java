package crawler;

import threads.*;
import java.net.*;
import java.io.*;
import java.util.Vector;

public class MediaExtractorThread extends ControllableThread {
	public void process(Object o) {
		try {
			URL pageURL = (URL) o;
 
			saveMedia(pageURL);

 			String mimetype = pageURL.openConnection().getContentType();
            if (!mimetype.startsWith("text")) return;

			Vector<String> links = getLinks(pageURL);

			DocumentWriter documentWriter = new DocumentWriter(bufferedWriter);
			documentWriter.getPageText(pageURL);

			addLinksToQueue(pageURL, links);
			
		} catch (Exception e) {
			// e.printStackTrace();
			// process of this object has failed, but we just ignore it here
		}
	}

	private void saveMedia(URL pageURL) {
		String filename = pageURL.getFile().toLowerCase();

		if (isImageFile(filename) || isMovieFile(filename) || isAudioFile(filename)) {
			filename = prepareFileName(pageURL, filename);			
			try {
				SaveURL.writeURLtoFile(pageURL, filename);
			} catch (Exception e) {
				System.out.println("Saving to file " + filename + " from URL " + pageURL.toString() + " failed due to a " + e.toString());
			}
			return;
		}
	}
	
	private boolean isAudioFile(String filename) {
		return filename.endsWith(".mp3")||
				filename.endsWith(".wma") ||
				filename.endsWith(".wav");
	}

	private String prepareFileName(URL pageURL, String filename) {
		filename = filename.replace('/', '-');
		filename = ((URLQueue) queue).getFilenamePrefix() +
			pageURL.getHost() + filename;
		System.out.println("Saving to file " + filename);
		return filename;
	}

	private boolean isMovieFile(String filename) {
		return filename.endsWith(".mpeg")||
				filename.endsWith(".mpg") ||
				filename.endsWith(".avi") ||
				filename.endsWith(".wmv");
	}

	private boolean isImageFile(String filename) {
		return filename.endsWith(".jpg") ||
			filename.endsWith(".jpeg") ||
			filename.endsWith(".png") ||
			filename.endsWith(".tiff") ||
			filename.endsWith(".gif");
	}

	private void addLinksToQueue(URL pageURL, Vector<String> links) {		
		for (int n = 0; n < links.size(); n++) {
			try {
				URL link = new URL(pageURL,
								   (String) links.elementAt(n));
				if (threadController.getMaxLevel() == -1)
					queue.push(link, level);
				else
					queue.push(link, level + 1);
			} catch (MalformedURLException e) {
			}
		}
	}

	private Vector<String> getLinks(URL pageURL) throws IOException {
		String rawPage = SaveURL.getURL(pageURL);
		
		String smallPage = rawPage.toLowerCase().replaceAll("\\s", " ");

		Vector<String> links = SaveURL.extractLinks(rawPage, smallPage);
		return links;
	}
	

}
