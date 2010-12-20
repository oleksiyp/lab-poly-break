package poly_break.algorithm.utils;

import java.util.Comparator;

import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.Vertex;
import poly_break.algorithm.structures.Edge.Disposition;
import poly_break.util.math.Line;
import poly_break.util.math.Utils;

public class OnLineComparator implements Comparator<Edge> {
	private double y;

	public OnLineComparator(double y) {
		super();
		this.y = y;
	}

	public OnLineComparator() {
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setVertex(Vertex v) {
		setY(v.getPoint().getY());
	}

	public int compare(Edge o1, Edge o2) {
		if (o1 == o2)
			return 0;
		Line l1 = o1.getSegment().getLine();
		Line l2 = o2.getSegment().getLine();
		double x1 = l1.projectX(y);
		double x2 = l2.projectX(y);
		if (Double.isNaN(x1))
			x1 =o1.getSegment().getCenter().getX();
		if (Double.isNaN(x2))
			x2 =o2.getSegment().getCenter().getX();
		int cmp;
		if ((cmp = Utils.cmp(x1, x2)) != 0) {
			return cmp;
		}

		Disposition d1 = o1.yDisposition(y);
		Disposition d2 = o1.yDisposition(y);

		if (d1 == Disposition.CROSS)
			System.out.println("wrong disposition " + o1 + " " + y);
		if (d2 == Disposition.CROSS)
			System.out.println("wrong disposition" + o2 + " " + y);

		if (d1.toInteger() > 0 && d2.toInteger() <= 0)
			return 1;
		if (d2.toInteger() > 0 && d1.toInteger() <= 0)
			return -1;
		if (d1.toInteger() >= 0 && d2.toInteger() < 0)
			return 1;
		if (d2.toInteger() >= 0 && d1.toInteger() < 0)
			return -1;

		boolean invert = (d1 == Disposition.ABOVE && d2 == Disposition.ABOVE);

		double c = l1.getB() * l2.getA() - l2.getB() * l1.getA();
		cmp = Utils.cmp(c, 0);
		if (invert)
			cmp = -cmp;
		return cmp;
	}

	public static OnLineComparator forVertex(Vertex v) {
		return new OnLineComparator(v.getPoint().getY());
	}
}