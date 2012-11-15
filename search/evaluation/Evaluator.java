package search.evaluation;

/**
 * This class is used to evaluate the ranked results from a search engine
 * 
 * @author dkauchak
 *
 */
public class Evaluator {
	/** 
	 * Calculate recall at k.
	 * 
	 * @param relevantIDs the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs the list of IDs returned from the system in order
	 * @param k
	 * @return Returns the recall at k
	 */
	public static double recall(int[] relevantIDs, int[] returnedIDs, int k){
		return 0.0;
	}
	
	/** 
	 * Calculate precision at k.
	 * 
	 * @param relevantIDs the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs the list of IDs returned from the system in order
	 * @param k
	 * @return Returns the precision at k
	 */
	public static double precision(int[] relevantIDs, int[] returnedIDs, int k){
		return 0.0;
	}

	/** 
	 * Calculate R-Precision
	 * 
	 * @param relevantIDs the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs the list of IDs returned from the system in order
	 * @return Returns the R-Precision
	 */
	public static double rPrecision(int[] relevantIDs, int[] returnedIDs){
		return 0.0;
	}
	
	/** 
	 * Calculate MAP
	 * 
	 * @param relevantIDs the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs the list of IDs returned from the system in order
	 * @return Returns the MAP
	 */
	public static double map(int[] relevantIDs, int[] returnedIDs){
		return 0.0;
	}
}
