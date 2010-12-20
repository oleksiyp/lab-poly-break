package poly_break.algorithm;

import poly_break.algorithm.log.Log;
import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.MonotonePolygon;
import poly_break.algorithm.structures.Vertex;

public class SplitOnMonotonesStage extends Stage {

	SplitOnMonotonesStage(TriangulationAlgorithm algorithm) {
		super(algorithm);
	}

	@Override
	public void execute() {
		Log log = getLog();
		log.start("Spliting monotone polygons");
		log.clear();

		Vertex firstVertex = algorithm.vertices.get(0);

		int totalFlow = firstVertex.getOut().sumUpFlow();

		MonotonePolygon[] polysLeft = new MonotonePolygon[totalFlow];
		MonotonePolygon[] polysRight = new MonotonePolygon[totalFlow];

		for (Vertex v : algorithm.vertices) {
			MonotonePolygon poly;
			boolean first = true;
			for (Edge in : v.getIn()) {
				if (first) {
					first = false;
					continue;
				}
				int right = in.getChainRange().getFirst();
				int left = right - 1;

				if (polysLeft[left] != polysRight[right])
					System.out.println("splitOnMonotone error");

				poly = polysLeft[left];
				poly.end(v, in);
				System.out.println(poly);
				if (poly.isIncluded())
					algorithm.monotones.add(poly);

				polysLeft[left] = polysRight[right] = null;

			}
			first = true;
			for (Edge out : v.getOut()) {
				if (first) {
					first = false;
					continue;
				}
				int right = out.getChainRange().getFirst();
				int left = right - 1;

				if (polysLeft[left] != null || polysRight[right] != null)
					System.out.println("splitOnMonotone error");

				poly = new MonotonePolygon(v);
				polysLeft[left] = polysRight[right] = poly;
			}

			poly = polysRight[v.getChainRange().getFirst()];
			if (poly != null)
				poly.addRight(v);

			poly = polysLeft[v.getChainRange().getLast()];
			if (poly != null)
				poly.addLeft(v);
		}

		for (MonotonePolygon p : algorithm.monotones) {
			log.drawMonotonePoly(p);
		}
		log.end();
	}

}
