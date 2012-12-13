package search.audio.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import search.audio.AudioDocument;
import search.audio.AudioProcessor;

public class AudioReader {

	private File[] files;
	private int filePtr;

	public AudioReader(String audioDir) {
		File tempDir = new File(audioDir);
		files = tempDir.listFiles();
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		filePtr = 0;
	}

	public static ArrayList<Integer> readAudio(File audioFile)
			throws IOException, UnsupportedAudioFileException {
		if (!AudioProcessor.checkFileFormat(audioFile))
			throw new UnsupportedAudioFileException();
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
		AudioFormat format = audioIn.getFormat();
		boolean stereo = format.getChannels() != 1;
		byte[] rawFrame = new byte[stereo ? 4 : 2];
		ArrayList<Integer> frames = new ArrayList<Integer>();

		while (-1 != audioIn.read(rawFrame, 0, rawFrame.length)) {
			int frame = rawFrame[1];
			frame <<= 8;
			frame |= rawFrame[0];

			if (stereo) {
				int frame2 = rawFrame[3];
				frame2 <<= 8;
				frame2 |= rawFrame[2];
				frame = (frame + frame2) / 2;
			}
			frames.add(frame);
		}
		return frames;

	}

	public AudioDocument next() {
		if (!hasNext())
			throw new NoSuchElementException();
		try {
			AudioIDPair audioPair = new AudioIDPair(readAudio(files[filePtr]),
					filePtr++);
			return new AudioDocument(audioPair.id, audioPair.frames);
		} catch (UnsupportedAudioFileException e) {
			System.err.println("error: unsupported audio file format in file "
					+ files[filePtr].getName());
		} catch (IOException e) {
			System.err.println("error: could not read file "
					+ files[filePtr].getName());
		}
		return null;
	}

	public boolean hasNext() {
		return filePtr < files.length;
	}

	public void reset() {
		filePtr = 0;
	}

	public int numFiles() {
		return files.length;
	}

	protected class AudioIDPair {
		public ArrayList<Integer> frames;
		public int id;

		public AudioIDPair(ArrayList<Integer> frames, int id) {
			this.id = id;
			this.frames = frames;
		}
	}

}
