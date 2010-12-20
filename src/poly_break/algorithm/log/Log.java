package poly_break.algorithm.log;


import java.awt.Color;
import java.io.PrintWriter;

import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.MonotonePolygon;
import poly_break.algorithm.structures.Vertex;
import poly_break.model.Triangle;

public interface Log {
	void clear();

	void drawArrow(Edge edge, boolean direction);

	void drawLine(Edge edge);

	void setPenSize(float size);

	void setPenColor(Color color);
	
	void drawPoint(Vertex vertex);
	
	void drawTraingle(Triangle triangle);

	void drawMonotonePoly(MonotonePolygon poly);

	void setPointSize(float size);

	void setPointColor(Color fill, Color border);

	void start(String string);

	void end();
	
	void showVertexNumbers();
	
	void hideVertexNumbers();

	PrintWriter getWriter();

	void setEdgeConverter(Converter<Edge> converter);
}
