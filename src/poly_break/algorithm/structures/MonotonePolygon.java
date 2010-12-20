package poly_break.algorithm.structures;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import poly_break.model.Polygon;
import poly_break.model.Triangle;
import poly_break.util.math.GeomUtils;

public class MonotonePolygon {
	public static class MVertex {
		private final Vertex vertex;

		private MVertex prev, next;

		private final boolean left;

		private MVertex(Vertex vertex) {
			this(vertex, true);
		}

		public MVertex(Vertex vertex, boolean left) {
			this.vertex = vertex;
			this.left = left;
		}

		public Vertex getVertex() {
			return vertex;
		}

		public MVertex getNext() {
			return next;
		}

		public MVertex getPrev() {
			return prev;
		}

		@Override
		public String toString() {
			return vertex.toString();
		}

		public boolean isAdjacent(MVertex v) {
			return prev == v || next == v;
		}

		public boolean isLeft() {
			return left;
		}
	}

	private MVertex left, right;

	private List<MVertex> vertices = new LinkedList<MVertex>();

	private Polygon polygon;

	public MonotonePolygon(Vertex start) {
		vertices.add(left = right = new MVertex(start));
	}

	public void addLeft(Vertex vertex) {
		MVertex m = new MVertex(vertex);
		left.next = m;
		m.prev = left;
		vertices.add(left = m);
	}

	public void addRight(Vertex vertex) {
		MVertex m = new MVertex(vertex, false);
		right.prev = m;
		m.next = right;
		vertices.add(right = m);
	}

	public void end(Vertex vertex, Edge leftEdge) {
		MVertex end = new MVertex(vertex);
		if (left == right) {
			left.next = end;
			left.prev = end;
		}
		right.prev = end;
		end.next = right;
		left.next = end;
		end.prev = left;
		vertices.add(end);
		polygon = leftEdge.getLeftPolygon();
	}

	public boolean isIncluded() {
		Polygon poly = polygon;
		return (depth(poly) % 2 != 0);
	}

	private int depth(Polygon poly) {
		if (poly == null)
			return 0;
		return depth(poly.getContainer()) + 1;
	}

	@Override
	public String toString() {
		return toList().toString() + " lays in " + polygon;
	}

	public List<Vertex> toList() {
		List<Vertex> lst = new LinkedList<Vertex>();
		if (!vertices.isEmpty()) {
			MVertex v = vertices.get(0), c = v;
			do {
				lst.add(c.getVertex());
				c = c.getNext();
			} while (c != v);
		}
		return lst;
	}

	public List<Triangle> triangulate() {
		// Shamos algorithm
		List<Triangle> result = new LinkedList<Triangle>();
		if (toString().contains("1, 4, 5, 6, 8, 13, 3")) {
			System.out.println(toString());
		}

		Stack<MVertex> stack = new Stack<MVertex>();
		for (MVertex u : vertices) {
			if (stack.size() >= 2) {
				MVertex v1 = stack.firstElement();
				MVertex vI = stack.lastElement();

				if (u.isAdjacent(v1) && !u.isAdjacent(vI)) {
					for (int i = 1; i < stack.size(); i++) {
						result.add(triangle(u.getVertex(), stack.get(i - 1)
								.getVertex(), stack.get(i).getVertex()));
					}
					stack.clear();
					stack.push(vI);
					stack.push(u);
				} else if (!u.isAdjacent(v1) && u.isAdjacent(vI)) {
					while (stack.size() > 1) {
						vI = stack.lastElement();
						MVertex vI1 = stack.get(stack.size() - 2);

						if (moreThanPi(u, vI, vI1) ^ vI.isLeft())
							break;

						result.add(triangle(u.getVertex(), vI.getVertex(), vI1
								.getVertex()));
						stack.pop();
					}
					stack.push(u);
				} else {
					for (int i = 1; i < stack.size(); i++) {
						result.add(triangle(u.getVertex(), stack.get(i)
								.getVertex(), stack.get(i - 1).getVertex()));
					}
					stack.clear();
					stack.push(vI);
				}
			} else {
				stack.push(u);
			}
		}
		return result;
	}

	private Triangle triangle(Vertex vertex, Vertex vertex2, Vertex vertex3) {
		return new Triangle(vertex.getPoint(), vertex2.getPoint(), vertex3
				.getPoint());
	}

	private boolean moreThanPi(MVertex a, MVertex b, MVertex c) {
		return GeomUtils.moreThanPi(a.getVertex().getPoint(), b.getVertex()
				.getPoint(), c.getVertex().getPoint());
	}

}
