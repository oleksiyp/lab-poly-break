/**
 * 
 */
package poly_break.algorithm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import poly_break.algorithm.log.Log;
import poly_break.algorithm.structures.AdjacentList;
import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.Vertex;
import poly_break.algorithm.utils.OnLineComparator;
import poly_break.model.Polygon;
import poly_break.util.math.GeomUtils;
import poly_break.util.math.Utils;

class RegularizeStage extends Stage {
	RegularizeStage(TriangulationAlgorithm algorithm) {
		super(algorithm);
	}

	private OnLineComparator cmp = new OnLineComparator();

	private TreeSet<Edge> edges = new TreeSet<Edge>(cmp);

	public void execute() {
		Log log = getLog();
		log.start("Regularize");
		log.clear();
		log.showVertexNumbers();
		logGraph();
		log.setPenColor(Color.GREEN);
		log.setPenSize(7.f);
		cmp.setVertex(this.algorithm.vertices.get(0));
		for (Vertex vertex : this.algorithm.vertices) {
			AdjacentList prev = vertex.getIn();
			AdjacentList next = vertex.getOut();

			boolean hasPrev = !prev.isEmpty();
			boolean hasNext = !next.isEmpty();
			if (hasPrev) {
				addBounded(prev, vertex);
			}
			if (vertex.toString().contains("15")) {
				System.out.println();
			}
			if (hasPrev && hasNext) {
				edges.removeAll(prev);
				cmp.setVertex(vertex);
				edges.addAll(next);
			} else if (hasPrev) {
				Edge left = getLeft(edges, prev);
				Edge right = getRight(edges, prev);
				vertex.bound(left, right);
				edges.removeAll(prev);
				cmp.setVertex(vertex);
			} else if (hasNext) {
				cmp.setVertex(vertex);
				edges.addAll(next);
			}
			if (hasNext) {
				checkPostBounded(edges, vertex, next);
			}
		}
		edges.clear();
		cmp.setVertex(this.algorithm.vertices.get(this.algorithm.vertices
				.size() - 1));
		for (Vertex vertex : Utils.reverse(this.algorithm.vertices)) {
			AdjacentList prev = vertex.getIn();
			AdjacentList next = vertex.getOut();

			boolean hasPrev = !prev.isEmpty();
			boolean hasNext = !next.isEmpty();
			if (hasNext) {
				addBounded(next, vertex);
			}
			if (hasPrev && hasNext) {
				edges.removeAll(next);
				cmp.setVertex(vertex);
				edges.addAll(prev);
			} else if (hasPrev) {
				cmp.setVertex(vertex);
				edges.addAll(prev);
			} else if (hasNext) {
				Edge left = getLeft(edges, next);
				Edge right = getRight(edges, next);
				vertex.bound(left, right);
				edges.removeAll(next);
				cmp.setVertex(vertex);
			}
			if (hasPrev) {
				checkPostBounded(edges, vertex, prev);
			}
			// checkEdges(edges, cmp);
		}
		log.end();
	}

	@SuppressWarnings("unused")
	private void checkEdges(TreeSet<Edge> edges, OnLineComparator cmp) {
		List<Edge> lst = new ArrayList<Edge>(edges);
		for (int i = 0; i < lst.size(); i++) {
			Edge a = lst.get(i);
			for (int j = i + 1; j < lst.size(); j++) {
				Edge b = lst.get(j);
				if (cmp.compare(a, b) >= 0)
					System.out.println("ERROR " + a + " " + b + " "
							+ cmp.compare(a, b) + " " + cmp.getY());
			}
		}
	}

	private void checkPostBounded(TreeSet<Edge> edges, Vertex vertex,
			AdjacentList next) {
		Edge left = getLeft(edges, next);
		Edge right = getRight(edges, next);
		ArrayList<Edge> lst = new ArrayList<Edge>(2);
		if (left != null)
			lst.add(left);
		if (right != null)
			lst.add(right);
		addBounded(lst, vertex);
	}

	private Edge getRight(NavigableSet<Edge> edges, AdjacentList prev) {
		Edge last = prev.last();
		SortedSet<Edge> tailSet = edges.tailSet(last, false);
		if (tailSet.isEmpty())
			return null;
		return tailSet.first();
	}

	private Edge getLeft(NavigableSet<Edge> edges, AdjacentList prev) {
		Edge first = prev.first();
		SortedSet<Edge> headSet = edges.headSet(first);
		if (headSet.isEmpty())
			return null;
		return headSet.last();
	}

	private void addBounded(Collection<Edge> lst, Vertex to) {
		Set<Vertex> verts = new TreeSet<Vertex>();
		for (Edge e : lst) {
			for (Vertex v : e.getBoundVertex(to)) {
				if (!GeomUtils.intersects(to.getPoint(), v.getPoint(), e
						.getStart().getPoint(), e.getEnd().getPoint())) {
					verts.add(v);
					v.unbound();
				}
			}
		}
		for (Vertex from : verts) {
			Edge edge = this.algorithm.addAdjacent(from, to);
			bindPolys(edge);
			// Polygon detectPoly = detectPoly(edge);
			// edge.bindPolygon(new ArrayList<Polygon>());
		}
	}

	private void bindPolys(Edge edge) {
		Vertex v = edge.getStart();
		AdjacentList setWithEdge = new AdjacentList(cmp);
		setWithEdge.add(edge);

		Edge e;
		Polygon left = null, right = null;

		e = getLeft(v.getOut(), setWithEdge);
		if (e != null) {
			left = e.getRightPolygon();
		} else if (!v.getIn().isEmpty()) {
			e = v.getIn().first();
			left = e.getLeftPolygon();
		} else if (!v.getOut().isEmpty()) {
			e = v.getOut().last();
			left = e.getRightPolygon();
		}

		e = getRight(v.getOut(), setWithEdge);
		if (e != null) {
			right = e.getLeftPolygon();
		} else if (!v.getIn().isEmpty()) {
			e = v.getIn().last();
			right = e.getRightPolygon();
		} else if (!v.getOut().isEmpty()) {
			e = v.getOut().first();
			right = e.getLeftPolygon();
		}

		edge.bindPolygon(left, right);
	}
}