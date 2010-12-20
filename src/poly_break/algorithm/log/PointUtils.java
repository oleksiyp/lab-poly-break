package poly_break.algorithm.log;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class PointUtils {

	static Point2D copy(Point2D dir) {
		return (Point2D) dir.clone();
	}

	static Point2D normalize(Point2D vector) {
		vector = copy(vector);
		double d = vector.distance(0, 0);
		vector.setLocation(vector.getX() / d, vector.getY() / d);
		return vector;
	}

	static Point2D multiply(Point2D vector, double d) {
		vector = copy(vector);
		vector.setLocation(vector.getX() * d, vector.getY() * d);
		return vector;
	}

	static Point2D subtract(Point2D a, Point2D b) {
		Point2D c = copy(a);
		c.setLocation(c.getX() - b.getX(), c.getY() - b.getY());
		return c;
	}

	static Point2D add(Point2D a, Point2D b) {
		Point2D c = copy(b);
		c.setLocation(c.getX() + a.getX(), c.getY() + a.getY());
		return c;
	}

	static Point2D rotate(Point2D endCopy, double theta) {
		AffineTransform r = AffineTransform.getRotateInstance(theta);
		Point2D endLeft = copy(endCopy);
		r.transform(endLeft, endLeft);
		return endLeft;
	}

}
