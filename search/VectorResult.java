package search;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A class to hold the results from a ranked query
 * 
 * @author dkauchak
 *
 */
public class VectorResult implements QueryResult{
	private ArrayList<Pair> results;
	private boolean sorted = false;
	
	/**
	 * Create a new set of results of a fixed size (though 
	 * this is technically adjustable)
	 * 
	 * @param size
	 */
	public VectorResult(int size){
		results = new ArrayList<Pair>(size);
	}
	
	/**
	 * Add a document and score to this set of results
	 * 
	 * @param docID
	 * @param score
	 */
	public void addDocument(int docID, double score){
		results.add(new Pair(docID, score));
	}
	
	/**
	 * The IDs that match the query
	 */
	public int[] getIDs() {
		// sort only once
		if( !sorted ){
			sort();
		}
		
		int[] ids = new int[results.size()];
		
		for(int i = 0; i < results.size(); i++ ){
			ids[i] = results.get(i).docID;
		}
		
		return ids;
	}
	
	/**
	 *  The scores for each document after the dot product with the query vector
	 */
	public double[] getScores() {
		if( !sorted ){
			sort();
		}
		
		double[] scores = new double[results.size()];
		
		for(int i = 0; i < results.size(); i++ ){
			scores[i] = results.get(i).score;
		}
		
		return scores;
	}
	
	/**
	 * Sort this set of results by score
	 */
	private void sort(){
		Collections.sort(results);
		sorted = true;
	}

	/**
	 * The number of query results
	 */
	public int size() {
		return results.size();
	}
	
	/**
	 * Entries to keep track of (docId and score)
	 * 
	 * @author dkauchak
	 */
	private class Pair implements Comparable<Pair>{
		public int docID;
		public double score;
		
		public Pair(int docID, double score){
			this.docID = docID;
			this.score = score;
		}
		
		public boolean equals(Object obj){
			boolean returnMe = true;
			
			if( !obj.getClass().equals(this.getClass()) ){
				returnMe = false;
			}else{
				if( docID != ((Pair)obj).docID ){
					returnMe = false;
				}else{
					if( score != ((Pair)obj).score ){
						returnMe = false;
					}
				}
			}
			
			return returnMe;
		}

		public int compareTo(Pair p2) {
			int returnMe = -Double.compare(score, p2.score);
			
			if( returnMe == 0 ){
				if( docID < p2.docID ){
					returnMe = -1;
				}else if( docID > p2.docID ){
					returnMe = 1;
				}
			}
			
			return returnMe;
		}		
	}
}
