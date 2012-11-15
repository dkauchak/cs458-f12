package search.evaluation;

import java.util.Iterator;

import search.Query;
import search.TokenProcessor;
import search.Tokenizer;

public interface QueryReader extends Iterator<Query>{
	/**
	 * set the tokenizer for this reader
	 * 
	 * @param tokenizer
	 */
	public void setTokenizer(Tokenizer tokenizer);
	/**
	 * set the token processor for this reader
	 * 
	 * @param tokenProcessor
	 */
	public void setTokenProcessor(TokenProcessor tokenProcessor);
}
