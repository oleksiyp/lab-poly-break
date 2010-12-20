package poly_break.algorithm;

import poly_break.algorithm.log.Converter;
import poly_break.algorithm.log.Log;
import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.Vertex;
import poly_break.util.math.Utils;

public class PushFlowStage extends Stage {

	PushFlowStage(TriangulationAlgorithm algorithm) {
		super(algorithm);
	}

	@Override
	public void execute() {
		Log log = getLog();
		log.start("Pushing flow stage");
		log.clear();

		for (Vertex v : algorithm.vertices) {
			int inFlow = v.getIn().sumUpFlow();
			int outFlow = v.getOut().sumUpFlow();

			if (inFlow > outFlow && !v.getOut().isEmpty()) {
				Edge firstOut = v.getOut().first();
				firstOut.setFlow(inFlow - outFlow + firstOut.getFlow());
			}
		}

		for (Vertex v : Utils.reverse(algorithm.vertices)) {
			int inFlow = v.getIn().sumUpFlow();
			int outFlow = v.getOut().sumUpFlow();

			if (inFlow < outFlow && !v.getIn().isEmpty()) {
				Edge firstIn = v.getIn().first();
				firstIn.setFlow(outFlow - inFlow + firstIn.getFlow());
			}
		}
		log.showVertexNumbers();
		log.setEdgeConverter(Converter.FLOW_CONVERTER);
		logGraph();
		log.end();
	}
}
