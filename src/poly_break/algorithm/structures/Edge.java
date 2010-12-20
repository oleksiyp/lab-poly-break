package poly_break.algorithm.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.util.math.Line;
import poly_break.util.math.Segment;
import poly_break.util.math.Utils;

public class Edge implements Comparable<Edge> {
	public enum Disposition {
		BELOW(-1), ON(0), ABOVE(1), CROSS(-2), LEFT(-1), RIGHT(1);

		private final int cmpNumber;

		Disposition(int cmpNumber) {
			this.cmpNumber = cmpNumber;
		}

		public int toInteger() {
			return cmpNumber;
		}
	}

	private final Vertex start, end;

	private Vertex leftBoundVertex;

	private Vertex rightBoundVertex;

	private int flow = 1;

	private ChainRange chainRange;

	private final int num;

	private Polygon leftPolygon;

	private Polygon rightPolygon;

	public Edge(Vertex start, Vertex end, int num) {
		super();
		this.start = start;
		this.end = end;
		this.num = num;
	}

	public Vertex getStart() {
		return start;
	}

	public Vertex getEnd() {
		return end;
	}

	public int compareTo(Edge edge) {
		int cmp;
		if ((cmp = start.compareTo(edge.start)) != 0)
			return cmp;
		return end.compareTo(edge.end);
	}

	public Vertex getLeftBoundVertex() {
		return leftBoundVertex;
	}

	public Vertex getRightBoundVertex() {
		return rightBoundVertex;
	}

	public void setBoundVertex(Vertex boundVertex) {
		Disposition d = yDisposition(boundVertex);
		if (d == Disposition.LEFT) {
			this.leftBoundVertex = boundVertex;
		} else if (d == Disposition.RIGHT) {
			this.rightBoundVertex = boundVertex;
		} else {
			System.out.println("set bound vertex error");
		}
	}

	public void clearBoundVertex(Vertex vertex) {
		if (leftBoundVertex == vertex)
			leftBoundVertex = null;
		if (rightBoundVertex == vertex)
			rightBoundVertex = null;
	}

	public Collection<Vertex> getBoundVertex(Vertex to) {
		Disposition d = yDisposition(to);
		List<Vertex> ret = new ArrayList<Vertex>(2);
		if (d == Disposition.LEFT) {
			if (this.leftBoundVertex != null)
				ret.add(this.leftBoundVertex);
		} else if (d == Disposition.RIGHT) {
			if (this.rightBoundVertex != null)
				ret.add(this.rightBoundVertex);
		} else {
			if (this.leftBoundVertex != null)
				ret.add(this.leftBoundVertex);
			if (this.rightBoundVertex != null)
				ret.add(this.rightBoundVertex);
		}
		return ret;
	}

	public Segment getSegment() {
		return new Segment(getStart().getPoint(), getEnd().getPoint());
	}

	@Override
	public String toString() {
		if (true)
			return "" + num;
		StringBuffer buf = new StringBuffer();
		if (leftBoundVertex != null)
			buf.append("b");
		buf.append(getStart());
		buf.append("->");
		buf.append(getEnd());
		return buf.toString();
	}

	public Disposition yDisposition(double y) {
		Point s = start.getPoint(), e = end.getPoint();

		if (Utils.cmp(s.getY(), y) == 0 && Utils.cmp(e.getY(), y) == 0)
			return Disposition.ON;

		if (Utils.cmp(s.getY(), y) >= 0 && Utils.cmp(e.getY(), y) >= 0)
			return Disposition.ABOVE;

		if (Utils.cmp(s.getY(), y) <= 0 && Utils.cmp(e.getY(), y) <= 0)
			return Disposition.BELOW;

		return Disposition.CROSS;
	}

	public Disposition yDisposition(Vertex v) {
		Line line = getSegment().getLine();
		Point point = v.getPoint();
		Point startPoint = getStart().getPoint();
		Point endPoint = getEnd().getPoint();
		double x = line.projectX(point.getY());
		if (Double.isNaN(x)) {
			if (startPoint.getX() <= point.getX()
					&& endPoint.getX() <= point.getX()) {
				return Disposition.RIGHT;
			}
			if (startPoint.getX() >= point.getX()
					&& endPoint.getX() >= point.getX()) {
				return Disposition.LEFT;
			}
		}
		if (point.getX() > x)
			return Disposition.RIGHT;
		if (point.getX() < x)
			return Disposition.LEFT;

		return Disposition.ON;
	}

	public int getFlow() {
		return flow;
	}

	public void setFlow(int flow) {
		this.flow = flow;
	}

	public ChainRange getChainRange() {
		return chainRange;
	}

	public void setChainRange(ChainRange chainRange) {
		this.chainRange = chainRange;
	}

	public void bindPolygon(Polygon left, Polygon right) {
		this.leftPolygon = left;
		this.rightPolygon = right;
	}

	public Set<Polygon> getPolygons() {
		HashSet<Polygon> set = new HashSet<Polygon>();
		set.add(leftPolygon);
		set.add(rightPolygon);
		return set;
	}

	public Polygon getLeftPolygon() {
		return leftPolygon;
	}

	public Polygon getRightPolygon() {
		return rightPolygon;
	}

}
