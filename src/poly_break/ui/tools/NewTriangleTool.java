package poly_break.ui.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.ui.PolyDrawPanel;
import poly_break.util.math.Segment;

public class NewTriangleTool extends Tool {
	private List<Point> points = new ArrayList<Point>();

	public NewTriangleTool(PolyDrawPanel draw) {
		super(draw);
	}

	@Override
	public void paintAfter(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < points.size() - 1; i++) {
			Segment seg = new Segment(points.get(i), points.get(i + 1));
			getDraw().drawSegment(g, seg);
		}
		for (Point pt : points)
			getDraw().drawPoint(g, pt);
	}

	@Override
	public void onMousePressed(MouseEvent event) {
		points.add(convert(event));
		if (points.size() == 3) {
			Polygon container = getInsidePolygon(points.get(0));
			if (container != null) {
				Polygon poly = new Polygon();
				poly.addPoint(0, points.get(0));
				poly.addPoint(1, points.get(1));
				poly.addPoint(2, points.get(2));
				container.addHole(poly);
				recalc();
			}
			points.clear();
		}
		getDraw().repaint();
		super.onMousePressed(event);
	}

	private Polygon getInsidePolygon(Point point) {		
		return getModel().getEnclosing(point);
	}
}
