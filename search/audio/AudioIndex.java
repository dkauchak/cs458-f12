package search.audio;

import java.util.ArrayList;

import search.Accumulator;
import search.PostingsList;
import search.VectorResult;
import search.audio.data.AudioReader;

public class AudioIndex {

	public static enum LENGTH_MODIFIER {
		n, c
	};

	private int maxDocID;
	private ArrayList<PostingsList> index;

	public AudioIndex(AudioReader reader, LENGTH_MODIFIER lengthModifier) {

		index = createIndex(reader);
		normalizeIndex(lengthModifier);
	}

	public VectorResult rankedQuery(AudioQuery query) {
		Accumulator acc = new Accumulator();

		for (int i=0; i < index.size(); ++i) {
			if (index.get(i) != null) {
				index.get(i).addToResult(acc, query.getBands().get(i));
			}
		}

		return acc.getResults();
	}

	private ArrayList<PostingsList> createIndex(AudioReader reader){
		ArrayList<PostingsList> index = new ArrayList<PostingsList>();
		
		while (reader.hasNext()) {
			AudioDocument doc = reader.next();
			int docID = doc.getDocID();
			ArrayList<Double> bands = AudioProcessor.getBands(doc.getFrames());

			for (int i=0; i< bands.size(); ++i) {
				if (index.size() <= i || index.get(i) == null)
					index.add(i, new PostingsList());
				index.get(i).addDoc(docID, bands.get(i));
			}

			if (docID % 100 == 0) {
				System.out.println(docID);
			}

			if (docID > maxDocID) {
				maxDocID = docID;
			}
		}
		return index;
	}

	private void normalizeIndex(LENGTH_MODIFIER lengthModifier) {
		// finally, do any length normalization
		if (lengthModifier.equals(LENGTH_MODIFIER.c)) {
			// first, get all of the sum of the squared of the weights for each
			// document
			double[] docLengths = new double[maxDocID + 1];

			for (PostingsList list : index) {
				if (list != null)
					list.accumulateDocumentLengths(docLengths);
			}

			// then, normalize by the square root of the lengths
			for (int i = 0; i < docLengths.length; i++) {
				docLengths[i] = 1 / Math.sqrt(docLengths[i]);
			}

			// finally, apply the weights to the postings lists
			for (PostingsList list : index) {
				if (list != null)
					list.genericWeight(docLengths);
			}
		}
	}
}