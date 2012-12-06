package search.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

import javax.sound.sampled.UnsupportedAudioFileException;

import search.VectorResult;
import search.audio.data.AudioReader;

public class AudioSearch {

	public static void main(String[] args) {
		if (args.length != 1)
			System.out.println("usage: java AudioSearch <corpus-directory>");
		else {
			System.out.println("Indexing audio files...");
			AudioReader reader = new AudioReader(args[0]);
			AudioIndex index = new AudioIndex(reader,
					AudioIndex.LENGTH_MODIFIER.c);
			File[] files = new File(args[0]).listFiles();
			Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return f1.getName().compareTo(f2.getName());
				}
			});
			System.out.println("Indexing complete!");
			BufferedReader console = new BufferedReader(new InputStreamReader(
					System.in));
			String line;
			try {
				while ((line = console.readLine()) != null) {
					File file = new File(line.trim());
					if (!AudioProcessor.checkFileFormat(file))
						System.out.println("file " + file.getName()
								+ " could not be opened");
					else {
						try {
							System.out.println(" == results for "
									+ file.getName() + " == ");
							AudioQuery query = new AudioQuery(file);
							VectorResult result = index.rankedQuery(query);
							int[] docIDs = result.getIDs();
							double[] scores = result.getScores();
							for (int i = 0; i < result.size(); ++i)
								System.out.println(docIDs[i] + " " + scores[i]
										+ " " + files[docIDs[i]].getName());
						} catch (UnsupportedAudioFileException e) {
							System.err
									.println("error: could not read audio file "
											+ file.getName());
						}
					}
				}
			} catch (IOException e) {
				System.err.println("error: could not read from console");
			}
		}

	}

}
