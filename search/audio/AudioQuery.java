package search.audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.UnsupportedAudioFileException;

import search.audio.data.AudioReader;

public class AudioQuery {

	private ArrayList<Double> bands;

	public AudioQuery(ArrayList<Integer> frames) {
		bands = AudioProcessor.getBands(frames);
	}

	public AudioQuery(File file) throws IOException, UnsupportedAudioFileException {
		this(AudioReader.readAudio(file));
	}

	/**
	 * @return the words in the query
	 */
	public ArrayList<Double> getBands() {
		return bands;
	}

}
