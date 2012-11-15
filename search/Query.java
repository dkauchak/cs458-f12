package search;

import java.util.ArrayList;

/**
 * A class representing a text query (i.e. non-boolean) to our IR system.
 * 
 * @author dkauchak
 */
public class Query{
	private ArrayList<String> text;
	
	/**
	 * The words in the query.
	 * 
	 * @param text
	 */
	public Query(ArrayList<String> text){
		this.text = text;
	}
	
	/**
	 * @return the words in the query
	 */
	public ArrayList<String> getText(){
		return text;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		for( String s: text ){
			buffer.append(s);
			buffer.append(" ");
		}
		
		return buffer.substring(0, buffer.length()-1);		
	}
}