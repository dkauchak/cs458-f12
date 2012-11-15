package search.evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import search.Document;
import search.Query;
import search.TokenProcessor;
import search.Tokenizer;

/**
 * Class for iterating over a file containing test queries.
 * 
 * @author dkauchak
 */
public class BasicQueryReader implements QueryReader {
	private TokenProcessor tokenProcessor;
	private Tokenizer tokenizer;
	private BufferedReader in;
	private String nextLine;
	
	/**
	 * @param queryFile File containing queries, one per line
	 */
	public BasicQueryReader(String queryFile){
		try{
			in = new BufferedReader(new FileReader(queryFile));
			nextLine = in.readLine();
		}catch(IOException e){
			throw new RuntimeException("Problems opening file: " + queryFile + "\n" + e.toString());
		}
	}
	
	public void setTokenProcessor(TokenProcessor tokenProcessor) {
		this.tokenProcessor = tokenProcessor;
	}

	
	public void setTokenizer(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	/**
	 * Are there more queries to be read?
	 */
	public boolean hasNext() {
		return nextLine != null;
	}

	/**
	 * Get the next query.
	 */
	public Query next() {
		if( !hasNext() ){
			throw new NoSuchElementException();
		}
		
		if( tokenizer == null ){
			throw new RuntimeException("BasicQueryReader::next() - call without setting tokenizer");
		}
		
		ArrayList<String> tokens = tokenizer.tokenize(nextLine);
		
		if( tokenProcessor != null ){
			tokens = tokenProcessor.process(tokens);
		}
		
		Query returnMe = new Query(tokens);
		
		try{
			nextLine = in.readLine();			
		}catch(IOException e){
			throw new RuntimeException("Problems reading file\n" + e.toString());
		}
		
		return returnMe;
	}
	
	public void remove() {
		// optional, so we won't implement
	}
}
