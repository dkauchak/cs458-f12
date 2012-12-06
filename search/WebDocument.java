package search;

import java.util.ArrayList;

public class WebDocument extends Document{
	private String title;
	private String url;
	
	public WebDocument(int docID, String url, String title, ArrayList<String> text){
		super(docID, text);
		
		this.url = url;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

}
