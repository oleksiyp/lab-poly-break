package poly_break.algorithm;

import java.io.PrintWriter;

import poly_break.algorithm.log.Log;
import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.Vertex;

public abstract class Stage {
	protected final TriangulationAlgorithm algorithm;

	protected void logGraph() {
		for (Vertex v : algorithm.vertices) {
			for (Edge e : v.getOut()) {
				if (true)
					getLog().drawArrow(e, true);
			}
		}
		for (Vertex v : this.algorithm.vertices) {
			getLog().drawPoint(v);
		}
	}

	protected void logPolygons() {
		for (Vertex v : algorithm.polygonStart) {
			Vertex p = v, prev = v.getPrevVertex();
			do {
				getLog().drawLine(new Edge(prev, p, 0));
				prev = p;
				p = p.getNextVertex();
			} while (p != v);
		}
	}

	protected Log getLog() {
		return this.algorithm.log;
	}

	protected PrintWriter getWriter() {
		return getLog().getWriter();
	}

	Stage(final TriangulationAlgorithm algorithm) {
		super();
		this.algorithm = algorithm;
	}

	public abstract void execute();

}