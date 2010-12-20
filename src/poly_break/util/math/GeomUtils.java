package poly_break.util.math;

import java.awt.geom.Line2D;

import poly_break.model.Point;

public class GeomUtils {

	public static boolean intersects(Point a1Pt, Point a2Pt, Point b1Pt,
			Point b2Pt) {
		if (GeomUtils.equalPoints(a1Pt, b1Pt)
				|| GeomUtils.equalPoints(a1Pt, b2Pt))
			return false;
		if (GeomUtils.equalPoints(a2Pt, b1Pt)
				|| GeomUtils.equalPoints(a2Pt, b2Pt))
			return false;

		return Line2D
				.linesIntersect(a1Pt.getX(), a1Pt.getY(), a2Pt.getX(), a2Pt
						.getY(), b1Pt.getX(), b1Pt.getY(), b2Pt.getX(), b2Pt
						.getY());
	}

	public static boolean equalPoints(Point p1, Point p2) {
		return Utils.cmp(p1.getX(), p2.getX()) == 0
				&& Utils.cmp(p1.getY(), p2.getY()) == 0;
	}

	/**
	 * Is angle from a to c using c > 180
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static boolean moreThanPi(Point a, Point b, Point c) {
		a = subtract(a, b);
		c = subtract(c, b);
		return a.getX() * c.getY() - a.getY() * c.getX() < 0;
	}

	private static Point subtract(Point a, Point b) {
		return new Point(a.getX() - b.getX(), a.getY() - b.getY());
	}

	public static Object intersect(Line a, Line b) {
		double a1 = a.getA(), b1 = a.getB(), c1 = a.getC();
		double a2 = b.getA(), b2 = b.getB(), c2 = b.getC();
		double d = a1 * b2 - a2 * b1;
		double dX = b1 * c2 - b2 * c1;
		double dY = a2 * c1 - a1 * c2;
		if (Utils.cmp(d, 0.) == 0) { // parallel
			if (Utils.cmp(c1, c2) == 0)
				return a;
			return null;
		} else {
			return new Point(dX / d, dY / d);
		}

	}

	public static Object intersect(Segment a, Segment b) {
		Object r = intersect(a.getLine(), b.getLine());
		if (r == null)
			return null;

		if (r instanceof Point) {
			if (inBoundOf((Point) r, a) && inBoundOf((Point) r, b))
				return r;
			return null;
		} else {
			// FIX ME : handle line
			return r;
		}
	}

	public static Point intersectOnlyPoint(Segment a, Segment b) {
		Object r = intersect(a, b);
		if (r instanceof Point)
			return (Point) r;
		return null;
	}

	public static boolean inBoundOf(Point point, Segment segment) {
		Point pt1 = segment.getPointA();
		Point pt2 = segment.getPointB();

		double minX = Math.min(pt1.getX(), pt2.getX());
		double minY = Math.min(pt1.getY(), pt2.getY());
		double maxX = Math.max(pt1.getX(), pt2.getX());
		double maxY = Math.max(pt1.getY(), pt2.getY());

		return Utils.cmp(point.getX(), minX) >= 0
				&& Utils.cmp(point.getX(), maxX) <= 0
				&& Utils.cmp(point.getY(), minY) >= 0
				&& Utils.cmp(point.getY(), maxY) <= 0;
	}

	public static double orientedSquare(Point a, Point base, Point b) {
		a = subtract(a, base);
		b = subtract(b, base);
		return (a.getX() * b.getY() - a.getY() * b.getX()) / 2.;
	}
}
