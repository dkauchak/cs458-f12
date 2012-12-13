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

	public AudioIndex(AudioReader reader, AudioProcessor processor,
			LENGTH_MODIFIER lengthModifier) {

		index = createIndex(reader, processor);
		normalizeIndex(lengthModifier);
	}

	public VectorResult rankedQuery(AudioQuery query) {
		Accumulator acc = new Accumulator();

		for (int i = 0; i < index.size(); ++i) {
			if (index.get(i) != null) {
				index.get(i).addToResult(acc, query.getBands().get(i));
			}
		}

		return acc.getResults();
	}

	private ArrayList<PostingsList> createIndex(AudioReader reader,
			AudioProcessor processor) {
		ArrayList<PostingsList> index = new ArrayList<PostingsList>();

		System.out.println("|---------- Constructing Index ----------|");
		System.out.print("|");

		int numFiles = reader.numFiles();
		double acc = 0;
		while (reader.hasNext()) {
			AudioDocument doc = reader.next();
			int docID = doc.getDocID();
			ArrayList<Double> bands = processor.getBands(doc.getFrames());

			for (int i = 0; i < bands.size(); ++i) {
				if (index.size() <= i || index.get(i) == null)
					index.add(i, new PostingsList());
				index.get(i).addDoc(docID, bands.get(i));
			}

			++acc;
			while (acc > numFiles / 40.0) {
				System.out.print("#");
				acc -= numFiles / 40.0;
			}

			if (docID > maxDocID) {
				maxDocID = docID;
			}
		}
		System.out.println("|");
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