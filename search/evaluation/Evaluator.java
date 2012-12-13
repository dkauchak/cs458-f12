package search.evaluation;

import java.util.TreeSet;

/**
 * This class is used to evaluate the ranked results from a search engine
 * 
 * @author dkauchak
 * 
 */
public class Evaluator {

	private static double relevant(int[] relevantIDs, int[] returnedIDs, int k) {
		TreeSet<Integer> relevant = new TreeSet<Integer>();
		for (int id : relevantIDs)
			relevant.add(id);

		int acc = 0;
		for (int i = 0; i < k && i < returnedIDs.length; ++i)
			if (relevant.contains(returnedIDs[i]))
				++acc;
		return (double) acc;
	}

	/**
	 * Calculate recall at k.
	 * 
	 * @param relevantIDs
	 *            the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs
	 *            the list of IDs returned from the system in order
	 * @param k
	 * @return Returns the recall at k
	 */
	public static double recall(int[] relevantIDs, int[] returnedIDs, int k) {
		return relevant(relevantIDs, returnedIDs, k) / (double) relevantIDs.length;
	}

	/**
	 * Calculate precision at k.
	 * 
	 * @param relevantIDs
	 *            the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs
	 *            the list of IDs returned from the system in order
	 * @param k
	 * @return Returns the precision at k
	 */
	public static double precision(int[] relevantIDs, int[] returnedIDs, int k) {
		return relevant(relevantIDs, returnedIDs, k) / (double) k;
	}

	/**
	 * Calculate R-Precision
	 * 
	 * @param relevantIDs
	 *            the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs
	 *            the list of IDs returned from the system in order
	 * @return Returns the R-Precision
	 */
	public static double rPrecision(int[] relevantIDs, int[] returnedIDs) {
		return precision(relevantIDs, returnedIDs, relevantIDs.length);
	}

	/**
	 * Calculate MAP
	 * 
	 * @param relevantIDs
	 *            the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs
	 *            the list of IDs returned from the system in order
	 * @return Returns the MAP
	 */
	public static double map(int[] relevantIDs, int[] returnedIDs) {
		TreeSet<Integer> relevant = new TreeSet<Integer>();
		for (int id : relevantIDs)
			relevant.add(id);

		double sum = 0;
		for (int i = 0; i < returnedIDs.length; ++i)
			if (relevant.contains(returnedIDs[i]))
				sum += precision(relevantIDs, returnedIDs, i + 1);

		return sum / (double) relevantIDs.length;
	}
}
