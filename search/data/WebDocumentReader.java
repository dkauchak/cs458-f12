package search.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import search.Document;
import search.ImprovedTokenizer;
import search.WebDocument;
import search.TokenProcessor;
import search.Tokenizer;
import search.data.BasicDocumentReader.TextIDPair;

/**
 * Web reader for reading documents with title and url information embedded.
 * 
 * Note that I basically copied and pasted code from BasicDocumentReader which is not great, but
 * works for now.
 * 
 * @author dkauchak
 */
public class WebDocumentReader implements DocumentReader{
	private Tokenizer tokenizer = null;
	private TokenProcessor tokenProcessor = null;
	private BufferedReader in;
	private TextIDPair nextDoc;
	protected int docID = -1;
	
	private String documentFile;
	
	/**
	 * The text file containing the TDT data with documents delimited
	 * by <DOC> ... </DOC>
	 * 
	 * @param documentFile
	 */
	public WebDocumentReader(String documentFile){
		this.documentFile = documentFile;
		
		try{
			in = new BufferedReader(new FileReader(documentFile));
			nextDoc = readNextDocText(in);			
		}catch(IOException e){
			throw new RuntimeException("Problems opening file: " + documentFile + "\n" + e.toString());
		}
	}
	
	/**
	 * Set the tokenizer for this reader
	 * 
	 * @param tokenizer
	 */
	public void setTokenizer(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}
	
	/**
	 * set the token processor for this reader
	 * 
	 * @param tokenProcessor
	 */
	public void setTokenProcessor(TokenProcessor tokenProcessor) {
		this.tokenProcessor = tokenProcessor;
	}

	/**
	 * Are there more documents to be read?
	 */
	public boolean hasNext() {
		return nextDoc != null;
	}

	/**
	 * Get the next document.
	 */
	public Document next(){
		if( !hasNext() ){
			throw new NoSuchElementException();
		}
		
		if( tokenizer == null ){
			throw new RuntimeException("BasicDocumentReader::next() - call without setting tokenizer");
		}
		
		ArrayList<String> tokens = tokenizer.tokenize(nextDoc.text);
		
		if( tokenProcessor != null ){
			tokens = tokenProcessor.process(tokens);
		}
		
		Document returnMe = new WebDocument(nextDoc.id, nextDoc.url, nextDoc.title, tokens);
		
		try{
			nextDoc = readNextDocText(in);			
		}catch(IOException e){
			throw new RuntimeException("Problems reading file\n" + e.toString());
		}
		
		return returnMe;
	}
	
	public void reset(){
		try{
			in.close();
			in = new BufferedReader(new FileReader(documentFile));
			nextDoc = readNextDocText(in);			
		}catch(IOException e){
			throw new RuntimeException("Problems opening file: " + documentFile + "\n" + e.toString());
		}
	}

	/**
	 * Read the next line as the next document
	 * 
	 * @param docIn The reader that the documents are from
	 * @return the next document (with ID)
	 * @throws IOException
	 */
	private TextIDPair readNextDocText(BufferedReader docIn) throws IOException{
		String line = docIn.readLine();
		
		// find the beginning of the document
		while( line != null &&
			   !line.equals("<DOC>") ){
			line = docIn.readLine();
		}
		
		if( line == null ){
			return null;
		}else{
			String title = "";
			String url = "";
			
			// the title should be the next line
			line = docIn.readLine();
			title = extractTag(line, "<TITLE>", "</TITLE>");
			
			// the url should be the next line
			line = docIn.readLine();
			url = extractTag(line, "<URL>", "</URL>");
						
			StringBuffer buffer = new StringBuffer();
		
			line = docIn.readLine();
			
			// grab all the text between <DOC> and </DOC>
			while( line != null &&
				   !line.equals("</DOC>") ){
				buffer.append(" " + line);
				line = docIn.readLine();
			}

			docID++;
			return new TextIDPair(buffer.toString(), docID, title, url);
		}
	}
	
	private String extractTag(String line, String preTag, String postTag) throws IOException{
		String result = "";
		
		// should probably do this with regex, but feeling lazy :)
		if( line.startsWith(preTag) && line.endsWith(postTag)){
			result = line.replace(preTag, "");
			result = result.replace(postTag, "");
			return result;
		}else{
			throw new IOException("Bad file format: No title");
		}
	}
	
	public void remove() {
		// method is optional
	}
	
	/**
	 * A shell class to store both the document's id and the 
	 * text from the document
	 * 
	 * @author dkauchak
	 */
	protected class TextIDPair{
		public String text;
		public int id;
		public String title;
		public String url;
		
		public TextIDPair(String text, int id, String title, String url){
			this.text = text;
			this.id = id;
			this.title = title;
			this.url = url;
		}
	}
	
	// just a quick test
	/*public static void main(String[] args){
		WebDocumentReader r = new WebDocumentReader("/Users/dkauchak/classes/cs458/final_project/code/cs458-f12/data/web_data.test");
		r.setTokenizer(new ImprovedTokenizer());
		r.setTokenProcessor(new TokenProcessor());
		
		while(r.hasNext()){
			WebDocument d = (WebDocument)r.next();
			
			System.out.println(d.getTitle());
			System.out.println(d.getUrl());
			
			for( String w: d.getText() ){
				System.out.print(w + " ");
			}
			
			System.out.println();
		}
	}*/
}
