package poly_break.algorithm.log;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;

import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.MonotonePolygon;
import poly_break.algorithm.structures.Vertex;
import poly_break.model.Triangle;

public class NullLog implements Log {

	public void clear() {
	}

	public void drawArrow(Edge edge, boolean direction) {
	}

	public void drawLine(Edge edge) {
	}

	public void drawPoint(Vertex vertex) {
	}

	public void setPenColor(Color color) {
	}

	public void setPenSize(float size) {
	}

	public void setPointColor(Color fill, Color border) {
	}

	public void setPointSize(float size) {
	}

	public void end() {
	}

	public void start(String string) {
	}

	public void step(String string) {
	}

	public void hideVertexNumbers() {
	}

	public void showVertexNumbers() {
	}

	public PrintWriter getWriter() {
		return new PrintWriter(new StringWriter());
	}

	public void setEdgeConverter(Converter<Edge> converter) {
	}

	public void drawTraingle(Triangle triangle) {
	}

	public void drawMonotonePoly(MonotonePolygon poly) {
	}
}
