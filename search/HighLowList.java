package search;

public class HighLowList {
	
	private PostingsList raw = new PostingsList();
	private PostingsList high = new PostingsList();
	private PostingsList low = new PostingsList();
	
	public void addToResult( Accumulator acc, double termWeight ) {
		high.addToResult(acc, termWeight);
	}
	
	public void addDoc(int docID) {
		raw.addDoc(docID);
	}
	
	public void incrementTFOfLastEntry(int docID) {
		raw.incrementTFOfLastEntry(docID);
	}
	
	public void logNormalizeTermFrequencies() {
		raw.logNormalizeTermFrequencies();
	}
	
	public void booleanNormalizeTermFrequencies() {
		raw.booleanNormalizeTermFrequencies();
	}
	public void idfWeight(int numDocs){
		raw.idfWeight(numDocs);
	}
	
	public void accumulateDocumentLengths(double[] docLengths) {
		raw.accumulateDocumentLengths(docLengths);
	}
	
	public void genericWeight(double[] weights) {
		raw.genericWeight(weights);
	}
	
	public static HighLowList not(HighLowList hlList, int maxDocID) {
		HighLowList result = new HighLowList();
		
		result.setRaw(PostingsList.not(hlList.getRaw(), maxDocID));
		result.setHigh(PostingsList.not(hlList.getHigh(), maxDocID));
		result.setLow(PostingsList.not(hlList.getLow(), maxDocID));
		
		return result;
		
	}
	
	public void split(int k) {
		high = raw.getTopK(k);
		low = raw.removeList(high);
	}
	
	public PostingsList getRaw() {
		return raw;
	}
	
	public PostingsList getHigh() {
		return high;
	}
	
	public PostingsList getLow() {
		return low;
	}
	
	public void setRaw(PostingsList input) {
		raw = input;
	}
	
	public void setHigh(PostingsList input) {
		high = input;
	}
	
	public void setLow(PostingsList input) {
		low = input;
	}
}
