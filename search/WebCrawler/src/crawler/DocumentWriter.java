package crawler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DocumentWriter {
	private BufferedWriter bufferedWriter;

	public DocumentWriter(BufferedWriter bufferedWriter) {
		this.bufferedWriter = bufferedWriter;
	}

	public void getPageText(URL pageURL) throws IOException {

		Document doc = Jsoup.connect(pageURL.toString()).get();
		Connection hello = Jsoup.connect(pageURL.toString());
		
		String pageTitle = doc.title();
		String pageText = doc.text().toLowerCase();
		
        bufferedWriter.write(getFormattedDocument(pageURL, pageTitle, pageText));
	}

	public String getFormattedDocument(URL pageURL, String pageTitle,
			String pageText) {
		return 
				"<URL>" + pageURL + "</URL>\r\n" 
				+ "<TITLE>" + pageTitle + "</TITLE>\r\n" 
				+ "<PAGE>\r\n" + pageText + "\r\n</PAGE>\r\n";
	}
}
