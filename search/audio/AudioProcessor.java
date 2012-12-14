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

	private ArrayList<ArrayList<Double>> filters;

	public AudioProcessor(int minFreq, int maxFreq, int numBands, int numOsc) {
		// pre-compute the necessary convolution filters
		filters = new ArrayList<ArrayList<Double>>(numBands);
		for (int i = 0; i < numBands; ++i) {
			// calculate the frequency of the band to be examined
			int freq = minFreq
					+ (int) ((maxFreq - minFreq) * (Math.log(numBands
							/ (double) (numBands - i)) / Math.log(numBands)));
			// build a filter for that frequency
			ArrayList<Double> filter = getFilter(freq, numOsc);
			filters.add(filter);
		}
	}

	public static final int MIN_FREQ = 20;
	public static final int MAX_FREQ = 3000;
	public static final int NUM_BANDS = 50;
	public static final int NUM_OSC = 5;

	public AudioProcessor() {
		this(MIN_FREQ, MAX_FREQ, NUM_BANDS, NUM_OSC);
	}

	private ArrayList<Double> getFilter(int freq, int numOsc) {
		int filterSize = SAMPLE_RATE / freq * numOsc;
		ArrayList<Double> filter = new ArrayList<Double>(filterSize);
		for (int i = 0; i < filterSize; ++i)
			filter.add(i, Math.sin(2 * Math.PI * numOsc / filterSize * i));
		return filter;
	}

	public ArrayList<Double> getBands(ArrayList<Integer> frames) {
		ArrayList<Double> ret = new ArrayList<Double>(filters.size());
		// iterate through all bands
		for (int i = 0; i < filters.size(); ++i) {
			// get a filter for that band
			ArrayList<Double> filter = filters.get(i);
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
