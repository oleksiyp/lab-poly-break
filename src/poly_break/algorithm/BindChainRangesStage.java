package poly_break.algorithm;

import poly_break.algorithm.log.Converter;
import poly_break.algorithm.log.Log;
import poly_break.algorithm.structures.AdjacentList;
import poly_break.algorithm.structures.ChainRange;
import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.Vertex;

public class BindChainRangesStage extends Stage {

	BindChainRangesStage(TriangulationAlgorithm algorithm) {
		super(algorithm);
	}

	@Override
	public void execute() {
		Log log = getLog();
		log.start("Bind chain ranges");
		log.clear();
		log.setEdgeConverter(Converter.CHAIN_RANGE_CONVERTER);

		for (Vertex v : algorithm.vertices) {
			ChainRange vertexRange;
			if (v.getIn().isEmpty()) {
				vertexRange = new ChainRange(0, v.getOut().sumUpFlow());
			} else {
				vertexRange = ChainRange.union(collectRanges(v.getIn()));
			}
			v.setChainRange(vertexRange);
			if (v.getOut().isEmpty())
				continue;
			
			int[] weights = collectWeights(v.getOut());
			ChainRange[] ranges = ChainRange.split(vertexRange, weights);
			applyRanges(v.getOut(), ranges);
		}
		logGraph();

		log.end();
	}

	private void applyRanges(AdjacentList lst, ChainRange[] ranges) {
		int i = 0;
		for (Edge e : lst)
			e.setChainRange(ranges[i++]);
	}

	private ChainRange[] collectRanges(AdjacentList lst) {
		ChainRange[] result = new ChainRange[lst.size()];
		int i = 0;
		for (Edge e : lst)
			result[i++] = e.getChainRange();
		return result;
	}

	private int[] collectWeights(AdjacentList lst) {
		int[] result = new int[lst.size()];
		int i = 0;
		for (Edge e : lst)
			result[i++] = e.getFlow();
		return result;
	}

}
