package search.audio.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

import search.audio.AudioDocument;
import search.audio.AudioProcessor;
import search.audio.data.AudioReader;

@SuppressWarnings("serial")
public class AudioWave extends JFrame {

	private ArrayList<Integer> frames;

	public static void main(String[] args) {
		if (args.length != 1)
			System.err.println("usage: java AudioWave <audiodir>");
		else {
			AudioReader reader = new AudioReader(args[0]);
			while (reader.hasNext()) {
				AudioDocument doc = reader.next();
				if (doc != null) {
					AudioWave gui = new AudioWave(doc);
					gui.setVisible(true);
				}
			}
		}
	}

	public AudioWave(AudioDocument audio) {
		this.frames = audio.getFrames();
		setTitle("Document " + audio.getDocID());
		setSize(800, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	public void paint(Graphics g) {
		Dimension dim = getSize();
		g.clearRect(0, 0, dim.width, dim.height);
		for (int i = 0; i < frames.size() - 1; ++i)
			g.drawLine(
					i * dim.width / frames.size(),
					dim.height
							/ 2
							+ (int) (frames.get(i) * dim.height / Math.pow(2,
									AudioProcessor.BIT_DEPTH)),
					(i + 1) * dim.width / frames.size(),
					dim.height
							/ 2
							+ (int) (frames.get(i + 1) * dim.height / Math.pow(
									2, AudioProcessor.BIT_DEPTH)));
	}
}
