package poly_break.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import poly_break.algorithm.log.Log;
import poly_break.algorithm.log.LogFrame;
import poly_break.algorithm.log.NullLog;
import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.MonotonePolygon;
import poly_break.algorithm.structures.Vertex;
import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.model.Triangle;

public class TriangulationAlgorithm {
	final Polygon input;

	Log log = new NullLog();

	ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	ArrayList<Vertex> polygonStart = new ArrayList<Vertex>();

	List<MonotonePolygon> monotones = new LinkedList<MonotonePolygon>();

	List<Triangle> results = new LinkedList<Triangle>();
	
	int edgeNumber = 1; 

	public TriangulationAlgorithm(Polygon input) {
		this.input = input;
	}

	public List<Triangle> compute() {
		new PrepareStage(this).execute();
		new RegularizeStage(this).execute();
		new PushFlowStage(this).execute();
		new BindChainRangesStage(this).execute();
		new SplitOnMonotonesStage(this).execute();
		new TriangulateMonotoneStage(this).execute();
		return results;

	}

	Edge addAdjacent(Vertex a, Vertex b) {
		if (a.compareTo(b) > 0) {
			Vertex t = a;
			a = b;
			b = t;
		}
		Edge edge = new Edge(a, b, edgeNumber++);

		a.getOut().add(edge);
		b.getIn().add(edge);

		log.drawArrow(edge, true);
		return edge;
	}

	public static void main(String[] args) throws FileNotFoundException {
		// Polygon p = getTest4();
		Polygon p = Polygon.load(new File("tests/data9.txt"));
		if (args.length > 0)
			p = Polygon.load(new File(args[0]));
		TriangulationAlgorithm alg = new TriangulationAlgorithm(p);
		LogFrame fr = LogFrame.openFrame();
		alg.setLog(fr.getLog());
		alg.compute();

		// getTest1().save(new File("tests/data1.txt"));
		// getTest2().save(new File("tests/data2.txt"));
		// getTest3().save(new File("tests/data3.txt"));
		// getTest4().save(new File("tests/data4.txt"));
	}

	@SuppressWarnings("unused")
	public static Polygon getTest4() {
		Polygon p = new Polygon();
		p.addPoint(0, new Point(10, 10));
		p.addPoint(0, new Point(15, 15));
		p.addPoint(0, new Point(20, 10));
		p.addPoint(0, new Point(25, 15));
		p.addPoint(0, new Point(30, 10));
		p.addPoint(0, new Point(30, 40));
		p.addPoint(0, new Point(25, 30));
		p.addPoint(0, new Point(20, 40));
		p.addPoint(0, new Point(15, 30));
		p.addPoint(0, new Point(10, 40));
		return p;
	}

	@SuppressWarnings("unused")
	private static Polygon getTest3() {
		Polygon p = new Polygon();
		p.addPoint(0, new Point(10, 10));
		p.addPoint(0, new Point(15, 15));
		p.addPoint(0, new Point(20, 10));
		p.addPoint(0, new Point(25, 15));
		p.addPoint(0, new Point(30, 10));
		p.addPoint(0, new Point(15, 40));
		return p;
	}

	@SuppressWarnings("unused")
	private static Polygon getTest2() {
		Polygon p = new Polygon();
		p.addPoint(0, new Point(10, 10));
		p.addPoint(0, new Point(15, 15));
		p.addPoint(0, new Point(20, 10));
		p.addPoint(0, new Point(15, 30));
		return p;
	}

	@SuppressWarnings("unused")
	private static Polygon getTest1() {
		Polygon p = new Polygon();
		p.addPoint(0, new Point(10, 20));
		p.addPoint(0, new Point(20, 20));
		p.addPoint(0, new Point(20, 10));
		p.addPoint(0, new Point(10, 10));
		return p;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
}
