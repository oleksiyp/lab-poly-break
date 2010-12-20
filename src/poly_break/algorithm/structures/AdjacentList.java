/**
 * 
 */
package poly_break.algorithm.structures;

import java.util.TreeSet;

import poly_break.algorithm.utils.OnLineComparator;

public class AdjacentList extends TreeSet<Edge> {
	private static final long serialVersionUID = 2282923352017409153L;

	private Vertex vertex;

	public AdjacentList(OnLineComparator comparator) {
		super(comparator);
	}

	public Vertex getVertex() {
		return vertex;
	}

	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}
	
	public int sumUpFlow() {
		int r = 0;
		for (Edge e : this)
			r += e.getFlow();
		return r;
	}
}