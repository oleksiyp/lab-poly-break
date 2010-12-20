package poly_break.algorithm.log;

import java.awt.Dimension;

import javax.swing.JFrame;

public class LogFrame extends JFrame {
	private static final long serialVersionUID = 2039708732429860547L;

	LogPanel panel = new LogPanel();

	LogFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		add(panel);
	}

	public Log getLog() {
		return panel.getLogCanvas();
	}

	public static LogFrame openFrame() {
		LogFrame frame = new LogFrame();
		frame.setSize(new Dimension(500, 500));
		frame.setVisible(true);
		return frame;

	}
}
