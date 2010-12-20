package poly_break.ui.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.ui.PolyDrawPanel;
import poly_break.util.math.GeomUtils;
import poly_break.util.math.Segment;

public class BreakSegmentTool extends Tool {
	private Point currentPoint;

	public BreakSegmentTool(PolyDrawPanel draw) {
		super(draw);
	}

	@Override
	public void onMousePressed(MouseEvent event) {
		currentPoint = null;
		Point point = convert(event);
		foundSegment = findSegment(point);
	}

	@Override
	public void onMouseReleased(MouseEvent event) {
		if (foundSegment != null && currentPoint != null) {
			foundSegment.polygon.addPoint(foundSegment.position + 1,
					currentPoint);
			foundSegment = null;
			getDraw().repaint();
			recalc();
		}
		super.onMouseReleased(event);
	}

	@Override
	public void onMouseDragged(MouseEvent event) {
		Point point = convert(event);
		if (checkIntersects(point)) {
			currentPoint = point;
		}
		getDraw().repaint();
	}

	private boolean checkIntersects(Point point) {
		if (foundSegment == null)
			return false;
		Segment segA = new Segment(foundSegment.segment.getPointA(), point);
		Segment segB = new Segment(foundSegment.segment.getPointB(), point);
		for (Polygon p : getAllPolygons()) {
			for (Segment s : p.getSegments()) {
				Point pt;
				if ((pt = GeomUtils.intersectOnlyPoint(segA, s)) != null) {
					if (!foundSegment.segment.lies(pt))
						return false;
				}
				if ((pt = GeomUtils.intersectOnlyPoint(segB, s)) != null) {
					if (!foundSegment.segment.lies(pt))
						return false;
				}
			}
		}
		return true;
	}

	@Override
	public void paintAfter(Graphics g) {
		if (foundSegment != null && currentPoint != null) {
			g.setColor(Color.BLACK);
			Segment segA = new Segment(foundSegment.segment.getPointA(),
					currentPoint);
			Segment segB = new Segment(foundSegment.segment.getPointB(),
					currentPoint);
			getDraw().drawSegment(g, segA);
			getDraw().drawSegment(g, segB);
		}

		super.paintAfter(g);
	}

}
