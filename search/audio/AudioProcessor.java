package search.audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioProcessor {

	public final static int BIT_DEPTH = 16;
	public final static int SAMPLE_RATE = 44100;

	public static boolean checkFileFormat(File file) {
		if (!file.exists() && !file.isFile())
			return false;
		try {
			AudioFormat format = AudioSystem.getAudioFileFormat(file)
					.getFormat();
			if ((format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED || format
					.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED)
					&& format.getSampleSizeInBits() == BIT_DEPTH
					&& format.getSampleRate() == SAMPLE_RATE)
				return true;
		} catch (UnsupportedAudioFileException e) {
			System.err.println("error: unsupported audio format in file "
					+ file.getName());
		} catch (IOException e) {
			System.err.println("error: could not read file " + file.getName());
		}
		return false;
	}

	public final static int MAX_FREQ = 10000;
	public final static int MIN_FREQ = 20;
	public final static int NUM_BANDS = 500;

	private static ArrayList<Double> getFilter(int freq) {
		int filterSize = SAMPLE_RATE / freq;
		ArrayList<Double> filter = new ArrayList<Double>(filterSize);
		for (int i = 0; i < filterSize; ++i)
			filter.add(i, Math.sin(Math.PI / filterSize * i));
		return filter;
	}

	public static ArrayList<Double> getBands(ArrayList<Integer> frames) {
		ArrayList<Double> ret = new ArrayList<Double>(NUM_BANDS);
		// iterate through all bands
		for (int i = 0; i < NUM_BANDS; ++i) {
			// calculate the frequency of the band to be examined
			int freq = MIN_FREQ
					+ (int) ((MAX_FREQ - MIN_FREQ) * (Math.log(NUM_BANDS
							/ (double) (NUM_BANDS - i)) / Math.log(NUM_BANDS)));
			// build a filter for that frequency
			ArrayList<Double> filter = getFilter(freq);
			// convolve that filter with the input audio data
			double result = 0;
			for (int k = 0; k < frames.size() - filter.size(); ++k) {
				double sum = 0;
				for (int j = 0; j < filter.size(); ++j)
					sum += frames.get(k + j) * filter.get(j);
				result += Math.abs(sum);
			}
			// store the average response in the appropriate band of the return
			// vector
			ret.add(i, result / frames.size() - filter.size());
		}
		return ret;
	}

}
