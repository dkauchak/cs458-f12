package search;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is a simple tokenizer that only splits tokens using whitespace 
 * 
 * @author dkauchak
 *
 */
public class SimpleTokenizer implements Tokenizer {

	/**
	 * Breaks the input text into words based only on whitespace. 
	 */
	public ArrayList<String> tokenize(String text) {
		// remove leading and trailing whitespace
		while( text.matches("^\\s+(.*?)$") ){
			text = text.substring(1);
		}
		
		while( text.matches("^(.*?)\\s+$") ){
			text = text.substring(0, text.length()-1);
		}
				
		return new ArrayList<String>(Arrays.asList(text.split("\\s+")));
	}

}
