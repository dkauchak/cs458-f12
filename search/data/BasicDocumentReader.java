package search.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import search.Document;
import search.TokenProcessor;
import search.Tokenizer;

/**
 * Basic reader that reads through documents and assumes each document is 
 * on a newline.  To extend this class, extend this reader and
 * rewrite the readNextDocTex function.
 * 
 * @author dkauchak
 */
public class BasicDocumentReader implements DocumentReader{
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
	public BasicDocumentReader(String documentFile){
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
		
		Document returnMe = new Document(nextDoc.id, tokens);
		
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
	protected TextIDPair readNextDocText(BufferedReader docIn) throws IOException{
		String line = docIn.readLine();
		
		if( line != null ){
			docID++;
			return new TextIDPair(line, docID);
		}else{
			return null;
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
		
		public TextIDPair(String text, int id){
			this.text = text;
			this.id = id;
		}
	}
}
