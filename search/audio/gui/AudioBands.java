package search.audio.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

import search.audio.AudioDocument;
import search.audio.AudioProcessor;
import search.audio.data.AudioReader;

@SuppressWarnings("serial")
public class AudioBands extends JFrame {

	private ArrayList<Double> bands;

	public static void main(String[] args) {
		if (args.length != 1)
			System.err.println("usage: java AudioBands <audiodir>");
		else {
			AudioReader reader = new AudioReader(args[0]);
			AudioProcessor processor = new AudioProcessor();
			while (reader.hasNext()) {
				AudioDocument doc = reader.next();
				if (doc != null) {
					AudioBands gui = new AudioBands(processor.getBands(doc
							.getFrames()), doc.getDocID());
					gui.setVisible(true);
				}
			}
		}
	}

	public AudioBands(ArrayList<Double> bands, int docID) {
		this.bands = bands;
		setTitle("Document " + docID);
		setSize(800, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	public void paint(Graphics g) {
		Dimension dim = getSize();
		g.clearRect(0, 0, dim.width, dim.height);
		for (int i = 0; i < bands.size(); ++i)
			g.drawLine(i * dim.width / bands.size(), dim.height, i * dim.width
					/ bands.size(), (int) (dim.height - bands.get(i) / 100.0));
	}

}
