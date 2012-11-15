package search;

import java.util.Hashtable;

/**
 * A class for accumulating the various scores as we traverse the postings lists.  There's a better
 * way to do this, though it will suffice for now.
 * 
 * @author dkauchak
 *
 */
public class Accumulator{
	private Hashtable<Integer, ChangeableDouble> scores = new Hashtable<Integer, ChangeableDouble>();

	/**
	 * scores[docID] += wt,q * wt,d
	 * 
	 * @param docID
	 * @param weight
	 */
	public void addScore(int docID, double weight){
		if( !scores.containsKey(docID) ){
			scores.put(docID, new ChangeableDouble(weight));
		}else{
			scores.get(docID).addToDouble(weight);
		}
	}
	
	/**
	 * Generate and get the sorted vector results.
	 * 
	 * @return
	 */
	public VectorResult getResults(){
		VectorResult results = new VectorResult(scores.size());
		
		for(Integer id: scores.keySet() ){
			results.addDocument(id, scores.get(id).getValue());
		}
		
		return results;
	}
	
	/**
	 * Get the number of entries.
	 * 
	 * @return
	 */
	public int size(){
		return scores.size();
	}
	
	private class ChangeableDouble{
		private double value;
		
		public ChangeableDouble(double value){
			this.value = value;
		}
		
		public void addToDouble(double add){
			value += add;
		}
		
		public void setDouble(double newValue){
			value = newValue;
		}
		
		public double getValue(){
			return value;
		}		
	}
}
