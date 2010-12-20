package poly_break.model;

import java.io.File;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import poly_break.util.math.Segment;

public class Polygon {
	private List<Point> points = new ArrayList<Point>();

	private Polygon container = null;

	private Collection<Polygon> holes = new ArrayList<Polygon>();

	private static int number = 1;

	private int num = number++;

	public void addPoint(int pos, Point point) {
		points.add(pos, point);
	}

	public List<Point> getPoints() {
		return points;
	}

	@Override
	public String toString() {
		return Integer.toString(num);
	}

	public interface PointIterator {
		void visit(Point point);
	}

	public interface SegmentIterator {
		void visit(Point pointA, Point pointB);
	}

	public Collection<Polygon> getHoles() {
		return Collections.unmodifiableCollection(holes);
	}

	public void addHole(Polygon poly) {
		if (poly.container != null)
			poly.container.removeHole(poly);
		holes.add(poly);
		poly.container = this;
	}

	public void removeHole(Polygon poly) {
		holes.remove(poly);
		poly.container = null;
	}

	public List<Polygon> getAllPolygons() {
		List<Polygon> polys = new ArrayList<Polygon>();
		polys.add(this);
		for (Polygon h : holes) {
			polys.addAll(h.getAllPolygons());
		}
		return polys;
	}

	public static Polygon load(Scanner s) {
		Polygon p = new Polygon();
		int nPoints = s.nextInt();
		for (int i = 0; i < nPoints; i++) {
			double x = s.nextDouble();
			double y = s.nextDouble();
			p.addPoint(i, new Point(x, y));
		}
		int nHoles = s.nextInt();
		for (int i = 0; i < nHoles; i++) {
			p.addHole(load(s));
		}
		return p;
	}

	public static Polygon load(Reader r) {
		Scanner s = new Scanner(r);
		s.useLocale(Locale.US);
		return load(s);
	}

	public static Polygon load(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		s.useLocale(Locale.US);
		Polygon load = load(s);
		s.close();
		return load;
	}

	public void save(PrintWriter wrt) {
		wrt.println(points.size());
		for (Point p : points)
			wrt.println(p.getX() + " " + p.getY());
		wrt.println(holes.size());
		for (Polygon h : holes) {
			h.save(wrt);
		}
		wrt.flush();
	}

	public void save(Writer w) {
		save(new PrintWriter(w));
	}

	public void save(File f) throws FileNotFoundException {
		PrintWriter wrt = new PrintWriter(f);
		save(wrt);
		wrt.close();
	}

	public Iterable<Segment> getSegments() {
		return new AbstractList<Segment>() {
			@Override
			public Segment get(int i) {
				int j = i + 1;
				if (j >= size())
					j = 0;
				return new Segment(points.get(i), points.get(j));
			}

			@Override
			public int size() {
				return points.size();
			}
		};
	}

	public Polygon getContainer() {
		return container;
	}

	@Override
	public Polygon clone() {
		Polygon ret = new Polygon();

		for (int i = 0; i < points.size(); i++)
			ret.addPoint(i, points.get(i).clone());

		for (Polygon hole : holes)
			ret.addHole(hole.clone());

		return ret;
	}

	public int getDepth() {
		if (container == null)
			return 0;
		return container.getDepth() + 1;
	}

	public Polygon getEnclosing(Point point) {
		if (isInside(point)) {
			for (Polygon hole : holes) {
				if (hole.isInside(point)) {
					return hole.getEnclosing(point);
				}
			}
			return this;
		} else {
			return null;
		}
	}

	private boolean isInside(Point point) {
		double y = point.getY();
		int cnt = 0;
		for (Segment s : getSegments()) {
			double miny = Math.min(s.getPointA().getY(), s.getPointB().getY());
			double maxy = Math.max(s.getPointA().getY(), s.getPointB().getY());
			if (miny < y && y < maxy) {
				double x = s.getLine().projectX(y);
				if (x < point.getX())
					cnt++;
			}
		}
		return cnt % 2 == 1;
	}
}
