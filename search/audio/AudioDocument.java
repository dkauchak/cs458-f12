package search.audio;

import java.util.ArrayList;

public class AudioDocument {

	private int docID;
	private ArrayList<Integer> frames;

	public AudioDocument(int docID, ArrayList<Integer> frames) {
		this.setDocID(docID);
		this.setFrames(frames);
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public ArrayList<Integer> getFrames() {
		return frames;
	}

	public void setFrames(ArrayList<Integer> frames) {
		this.frames = frames;
	}

}
