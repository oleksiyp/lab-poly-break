/**
 * 
 */
package poly_break.algorithm.structures;

import java.util.List;

import poly_break.algorithm.utils.OnLineComparator;
import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.util.math.Utils;

public class Vertex implements Comparable<Vertex> {
	private final Polygon poly;

	private final int pointIndex;

	private final int polyIndex;

	private final Point point;

	private Vertex nextVertex;

	private Vertex prevVertex;

	private int vertexIndex = -1;

	private Edge boundLeft;

	private Edge boundRight;
	
	private ChainRange chainRange;

	private final AdjacentList in;

	private final AdjacentList out;

	public Vertex(final Polygon poly, final int index, final int polyIndex) {
		super();
		this.poly = poly;
		this.pointIndex = index;
		this.polyIndex = polyIndex;
		this.point = poly.getPoints().get(index);
		in = new AdjacentList(new OnLineComparator(point.getY()));
		out = new AdjacentList(new OnLineComparator(point.getY()));
	}

	public Point getPoint() {
		return point;
	}

	public Point getNext() {
		final List<Point> pts = poly.getPoints();
		return pts.get((pointIndex + 1) % pts.size());
	}

	public Point getPrevious() {
		final List<Point> pts = poly.getPoints();
		return pts.get((pointIndex + pts.size() - 1) % pts.size());
	}

	public int compareTo(final Vertex o) {
		int c = 0;
		if ((c = Utils.cmp(getPoint().getY(), o.getPoint().getY())) != 0)
			return c;
		if ((c = Utils.cmp(getPoint().getX(), o.getPoint().getX())) != 0)
			return c;
		return 0;
	}

	public void setNextVertex(final Vertex vertex) {
		this.nextVertex = vertex;

	}

	public void setPrevVertex(final Vertex vertex) {
		this.prevVertex = vertex;
	}

	public Vertex getNextVertex() {
		return nextVertex;
	}

	public Vertex getPrevVertex() {
		return prevVertex;
	}

	public int getPointIndex() {
		return pointIndex;
	}

	public Polygon getPoly() {
		return poly;
	}

	public int getPolyIndex() {
		return polyIndex;
	}

	public void setVertexIndex(final int index) {
		this.vertexIndex = index;
	}

	public int getVertexIndex() {
		return vertexIndex;
	}

	@Override
	public String toString() {
		if (true)
			return "" + vertexIndex;
		return point.toString();
	}

	public void unbound() {
		if (boundLeft != null)
			boundLeft.clearBoundVertex(this);
		if (boundRight != null)
			boundRight.clearBoundVertex(this);
	}

	public void bound(final Edge left, final Edge right) {
		unbound();
		boundLeft = left;
		boundRight = right;
		if (left != null)
			left.setBoundVertex(this);
		if (right != null)
			right.setBoundVertex(this);
	}

	public AdjacentList getIn() {
		return in;
	}

	public AdjacentList getOut() {
		return out;
	}

	public ChainRange getChainRange() {
		return chainRange;
	}

	public void setChainRange(ChainRange chainRange) {
		this.chainRange = chainRange;
	}

}