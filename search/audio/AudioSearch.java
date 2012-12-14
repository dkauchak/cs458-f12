package search.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

import javax.sound.sampled.UnsupportedAudioFileException;

import search.VectorResult;
import search.audio.data.AudioReader;
import search.evaluation.Evaluator;

public class AudioSearch {

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		if (args.length < 1 || args.length > 3)
			System.out
					.println("usage: java AudioSearch <corpus-directory> [<query-file> [<relevants-file>]]");
		else {
			BufferedReader console = new BufferedReader(
					args.length == 1 ? new InputStreamReader(System.in)
							: new FileReader(args[1]));
			String line;

			// collect index parameters
			if (args.length == 1)
				System.out
						.println("enter minimum frequency to be indexed (blank line --> default value):");
			line = console.readLine().trim();
			int minFreq = line.isEmpty() ? AudioProcessor.MIN_FREQ : Integer
					.parseInt(line);
			if (args.length == 1)
				System.out
						.println("enter maximum frequency to be indexed (blank line --> default value):");
			line = console.readLine().trim();
			int maxFreq = line.isEmpty() ? AudioProcessor.MAX_FREQ : Integer
					.parseInt(line);
			if (args.length == 1)
				System.out
						.println("enter number of bands in frequency profile (blank line --> default value):");
			line = console.readLine().trim();
			int numBands = line.isEmpty() ? AudioProcessor.NUM_BANDS : Integer
					.parseInt(line);
			if (args.length == 1)
				System.out
						.println("enter number of oscilations in filter (blank line --> default value):");
			line = console.readLine().trim();
			int numOsc = line.isEmpty() ? AudioProcessor.NUM_OSC : Integer
					.parseInt(line);
			System.out.println("minFreq: " + minFreq + " maxFreq: " + maxFreq
					+ " numBands: " + numBands + " numOsc: " + numOsc);
			AudioProcessor processor = new AudioProcessor(minFreq, maxFreq,
					numBands, numOsc);

			// get a list of the files indexed
			File[] files = new File(args[0]).listFiles();
			Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return f1.getName().compareTo(f2.getName());
				}
			});

			// construct index
			long indexingTime = System.currentTimeMillis();
			System.out.println("Indexing audio files...");
			AudioReader reader = new AudioReader(args[0]);
			AudioIndex index = new AudioIndex(reader, processor,
					AudioIndex.LENGTH_MODIFIER.c);
			indexingTime = System.currentTimeMillis() - indexingTime;
			System.out.println("Indexing complete! (" + indexingTime + " ms)");

			// if a file containing the relevant lists for each query is
			// supplied, process it
			BufferedReader relevants = null;
			if (args.length == 3)
				relevants = new BufferedReader(new FileReader(args[2]));

			// parse and process queries to the database
			System.out.println("Processing queries...");
			File queryFile = null;
			try {
				int queries = 0;
				double rPrec = 0.0, map = 0.0;
				while ((line = console.readLine()) != null) {
					queryFile = new File(line.trim());
					if (!AudioProcessor.checkFileFormat(queryFile))
						throw new UnsupportedAudioFileException();
					else {
						if (relevants == null)
							System.out.println(" == results for "
									+ queryFile.getName() + " == ");
						else
							System.out.format("%4d - %s\n", ++queries,
									queryFile.getName());
						AudioQuery query = new AudioQuery(queryFile, processor);
						VectorResult result = index.rankedQuery(query);
						int[] results = result.getIDs();
						double[] scores = result.getScores();
						if (relevants == null)
							for (int i = 0; i < result.size(); ++i)
								System.out.println(results[i] + " " + scores[i]
										+ " " + files[results[i]].getName());
						else {
							String[] docNames = relevants.readLine().split(
									"\\s+");
							int[] relevantDocs = new int[docNames.length];
							for (int i = 0; i < docNames.length; ++i)
								relevantDocs[i] = Integer.parseInt(docNames[i]);
							rPrec += Evaluator
									.rPrecision(relevantDocs, results);
							map += Evaluator.map(relevantDocs, results);
						}
					}
				}
				if (relevants != null) {
					rPrec /= queries;
					map /= queries;
					System.out.println("rPrecision: " + rPrec);
					System.out.println("MAP:        " + map);
				}
			} catch (UnsupportedAudioFileException e) {
				System.err.println("error: could not read audio query file "
						+ queryFile.getName());
			}
		}

	}

}
