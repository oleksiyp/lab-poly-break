package poly_break.util.math;

import poly_break.model.Point;

public class Segment {
	private final Point pointA, pointB;

	public Point getPointA() {
		return pointA;
	}

	public Point getPointB() {
		return pointB;
	}

	public Segment(final Point pointA, final Point pointB) {
		super();
		this.pointA = pointA;
		this.pointB = pointB;
	}

	public double distance2(Point point) {
		double x1 = pointA.getX();
		double x2 = pointB.getX();
		double y2 = pointB.getY();
		double y1 = pointA.getY();
		double a = y2 - y1;
		double b = x1 - x2;
		double c = -x1 * (y2 - y1) + y1 * (x2 - x1);

		double y = point.getY();
		double x = point.getX();
		double r = (a * x + b * y + c);
		r = r * r;
		r /= a * a + b * b;

		return r;
	}

	public void move(Point delta) {
		pointA.add(delta);
		pointB.add(delta);
	}

	public Line getLine() {
		return new Line(getPointA(), getPointB());
	}

	public Point getCenter() {
		return new Point((pointA.getX() + pointB.getX()) / 2.,
				(pointA.getY() + pointB.getY()) / 2.);
	}

	public boolean lies(Point pt) {
		if (!GeomUtils.inBoundOf(pt, this))
			return false;
		
		return getLine().lies(pt);
	}
}
