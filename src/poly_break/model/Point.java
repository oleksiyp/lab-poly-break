/**
 * 
 */
package poly_break.model;

public class Point {
	private double x, y;

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public double distance(Point pt) {
		return Math.sqrt(distance2(pt));
	}

	public double distance2(Point pt) {
		return (pt.x - x) * (pt.x - x) + (pt.y - y) * (pt.y - y);
	}

	public void subtract(Point pt) {
		x -= pt.x;
		y -= pt.y;
	}

	public void add(Point pt) {
		x += pt.x;
		y += pt.y;
	}

	public void set(Point pt) {
		set(pt.getX(), pt.getY());
	}

	@Override
	public Point clone() {
		return new Point(x, y);
	}
}