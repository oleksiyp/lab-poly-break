/**
 * 
 */
package poly_break.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import poly_break.algorithm.log.Log;
import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.Vertex;
import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.util.math.GeomUtils;

class PrepareStage extends Stage {
	PrepareStage(TriangulationAlgorithm algorithm) {
		super(algorithm);
	}

	public void execute() {
		Log log = getLog();
		log.start("Prepare stage");
		int count = 0;
		List<Polygon> all = this.algorithm.input.getAllPolygons();
		for (Polygon p : all) {
			count += p.getPoints().size();
		}
		
		boolean []clockWise = new boolean[all.size()];
		for (int i = 0; i < all.size(); i++){
			List<Point> points = all.get(i).getPoints();
			Point start = points.get(0);
			double S = 0;
			for (int j = 1; j < points.size() - 1; j++) {
				Point a = points.get(j);
				Point b = points.get(j + 1);
				S += GeomUtils.orientedSquare(a, start, b);
			}
			clockWise[i] = S < 0;
		}
		

		// polygonStart = new ArrayList<Vertex>();
		int j = 0;
		for (Polygon p : all) {
			ArrayList<Vertex> lst = new ArrayList<Vertex>();
			for (int i = 0; i < p.getPoints().size(); i++) {
				lst.add(new Vertex(p, i, j));
			}
			for (int i = 0; i < lst.size(); i++) {
				Vertex vertex = lst.get(i);
				Vertex next = lst.get((i + 1) % lst.size());
				Vertex prev = lst.get((i + lst.size() - 1) % lst.size());

				vertex.setNextVertex(next);
				vertex.setPrevVertex(prev);
			}
			if (lst.size() > 0) {
				this.algorithm.polygonStart.add(lst.get(0));
			} else {
				this.algorithm.polygonStart.add(null);
			}
			this.algorithm.vertices.addAll(lst);
			j++;
		}
		Collections.sort(this.algorithm.vertices);
		for (int i = 0; i < this.algorithm.vertices.size(); i++) {
			Vertex vertex = this.algorithm.vertices.get(i);
			vertex.setVertexIndex(i);
		}

		for (int i = 0; i < this.algorithm.polygonStart.size(); i++) {
			Vertex v = this.algorithm.polygonStart.get(i);
			if (v == null)
				continue;
			Vertex min = null, max = null;
			Vertex p = v;
			do {
				Edge edge = this.algorithm.addAdjacent(p, p.getNextVertex());
				Polygon right = all.get(i);
				Polygon left = right.getContainer();
				
				if (edge.getStart() != p ^ (!clockWise[i])){
					Polygon t = right;
					right = left;
					left = t;
				}
				edge.bindPolygon(left, right);

				if (min == null || min.compareTo(p) > 0)
					min = p;
				if (max == null || max.compareTo(p) < 0)
					max = p;

				p = p.getNextVertex();
			} while (p != v);
			this.algorithm.polygonStart.set(i, min);
		}
		logGraph();

		log.end();
	}
}