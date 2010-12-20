package poly_break.algorithm.log;

import java.awt.image.BufferedImage;

public class LogStage {
	private String name;

	private BufferedImage image = LogPanel.createCanvasImage(800, 800);

	public LogStage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
