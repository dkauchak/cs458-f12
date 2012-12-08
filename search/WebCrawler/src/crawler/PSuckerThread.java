package crawler;

import threads.*;
import java.net.*;
import java.io.*;
import java.util.Vector;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PSuckerThread extends ControllableThread {
	public void process(Object o) {
		try {
			URL pageURL = (URL) o;
 
			saveImageAndVideoMedia(pageURL);

 			String mimetype = pageURL.openConnection().getContentType();
            if (!mimetype.startsWith("text")) return;

			Vector links = getLinks(pageURL);

			getPageText(pageURL);

			addLinksToQueue(pageURL, links);
			
		} catch (Exception e) {
			// e.printStackTrace();
			// process of this object has failed, but we just ignore it here
		}
	}

	private void saveImageAndVideoMedia(URL pageURL) {
		String filename = pageURL.getFile().toLowerCase();
		if (filename.endsWith(".jpg") ||
			filename.endsWith(".jpeg")||
			filename.endsWith(".mpeg")||
			filename.endsWith(".mpg") ||
			filename.endsWith(".avi") ||
			filename.endsWith(".wmv")) {
			filename = filename.replace('/', '-');
			filename = ((URLQueue) queue).getFilenamePrefix() +
				pageURL.getHost() + filename;
			System.out.println("Saving to file " + filename);
			try {
				SaveURL.writeURLtoFile(pageURL, filename);
			} catch (Exception e) {
				System.out.println("Saving to file " + filename + " from URL " + pageURL.toString() + " failed due to a " + e.toString());
			}
			return;
		}
	}

	private void addLinksToQueue(URL pageURL, Vector links) {		
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

	private Vector getLinks(URL pageURL) throws IOException {
		String rawPage = SaveURL.getURL(pageURL);
		
		String smallPage = rawPage.toLowerCase().replaceAll("\\s", " ");

		Vector links = SaveURL.extractLinks(rawPage, smallPage);
		return links;
	}
	
	private void getPageText(URL pageURL) throws IOException {

		Document doc = Jsoup.connect(pageURL.toString()).get();
		Connection hello = Jsoup.connect(pageURL.toString());
		
		
		
		String pageTitle = doc.title();
		String pageText = doc.text().toLowerCase();
		
        bufferedWriter.write(getFormattedDocument(pageURL, pageTitle, pageText));
	}

	private String getFormattedDocument(URL pageURL, String pageTitle,
			String pageText) {
		return "<URL>" 	+
		pageURL 	+
		"</URL>\r\n"	+
		"<TITLE>"	+
		pageTitle	+
		"</TITLE>\r\n"	+
		"<PAGE>\r\n" +
		pageText+
		"\r\n</PAGE>\r\n";
	}
}
