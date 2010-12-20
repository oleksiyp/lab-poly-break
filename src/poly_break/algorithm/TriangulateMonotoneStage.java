package poly_break.algorithm;

import java.awt.Color;
import java.util.List;

import poly_break.algorithm.log.Log;
import poly_break.algorithm.structures.MonotonePolygon;
import poly_break.model.Triangle;

public class TriangulateMonotoneStage extends Stage {

	TriangulateMonotoneStage(TriangulationAlgorithm algorithm) {
		super(algorithm);
	}

	@Override
	public void execute() {
		Log log = getLog();
		log.start("Triangulate monontone polygons");
		log.clear();
		log.setPenColor(Color.RED);
		logPolygons();
		log.setPenColor(Color.BLACK);
		for (MonotonePolygon poly : algorithm.monotones) {
			List<Triangle> trigs = poly.triangulate();
			for (Triangle t : trigs)
				log.drawTraingle(t);
			algorithm.results.addAll(trigs);
		}
		log.end();
	}

}
