package search;

import java.util.ArrayList;

public class WebResultData{
	private static final int SNIPPET_SIZE = 50;
	
	private String title;
	private String url;
	private String[] snippet;
	
	public WebResultData(String title, String url, ArrayList<String> text){
		this.title = title;
		this.url = url;
		
		int size = SNIPPET_SIZE;
		
		if( text.size() < SNIPPET_SIZE ){
			size = text.size();
		}
		
		snippet = new String[size];
		
		for( int i = 0; i < size; i++ ){
			snippet[i] = text.get(i);
		}
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String[] getSnippet() {
		return snippet;
	}
}
