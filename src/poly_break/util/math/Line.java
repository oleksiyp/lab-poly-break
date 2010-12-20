package poly_break.util.math;

import poly_break.model.Point;

public class Line {
	// ax + by + c = 0
	private final double a, b, c;

	public Line(Point from, Point to) {
		double x1 = from.getX(), y1 = from.getY();
		double x2 = to.getX(), y2 = to.getY();

		a = (y2 - y1);
		b = -(x2 - x1);
		c = -x1 * (y2 - y1) + y1 * (x2 - x1);

	}

	public Line(double a, double b, double c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public double getC() {
		return c;
	}

	public double projectX(double y) {
		return -(c + b * y) / a;
	}

	public boolean lies(Point pt) {
		return Utils.cmp(a * pt.getX() + b * pt.getY() + c, 0) == 0;
	}

}
