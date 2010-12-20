/**
 * 
 */
package poly_break.ui.tools;

import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.util.math.Segment;

public class SegmentPosition {
	public Polygon polygon;

	public Segment segment;

	public int position;

	public static boolean isEqual(SegmentPosition a, SegmentPosition b) {
		if (a == b)
			return true;
		if (a == null)
			return false;
		if (b == null)
			return false;
		return (a.polygon == b.polygon && a.position == b.position);
	}

	public Point center() {
		return new Point((segment.getPointA().getX() + segment.getPointB()
				.getX()) / 2., (segment.getPointA().getY() + segment
				.getPointB().getY()) / 2.);
	}

}