/**
 * 
 */
package poly_break.ui.tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;

import poly_break.algorithm.TriangulationAlgorithm;
import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.model.Triangle;
import poly_break.ui.PolyDrawPanel;
import poly_break.util.math.Segment;
import poly_break.util.math.Utils;

public class Tool {
	public static final double GRID_SIZE = 50.;

	public static final double GRID_SENS = 20.;

	public static final Color GRID_COLOR = new Color(250, 250, 250);

	public static final Color MESH_COLOR = new Color(230, 250, 230);

	private PolyDrawPanel draw = getDraw();

	private boolean paintGrid = false;

	protected PointPosition foundPoint;

	protected SegmentPosition foundSegment;

	protected Point foundInPoint = new Point(0, 0);

	private Collection<Triangle> mesh = null;

	public Tool(PolyDrawPanel draw) {
		this.draw = draw;
		recalc();
	}

	protected PolyDrawPanel getDraw() {
		return draw;
	}

	protected Polygon getModel() {
		return getDraw().getModel();
	}

	protected Collection<Polygon> getAllPolygons() {
		return getModel().getAllPolygons();
	}

	protected PointPosition findPoint(Point pt) {
		for (Polygon p : getAllPolygons()) {
			List<Point> points = p.getPoints();
			for (int i = 0; i < points.size(); i++) {
				Point point = points.get(i);
				if (point.distance2(pt) < 8. * 8.) {
					PointPosition ret = new PointPosition();
					ret.polygon = p;
					ret.point = point;
					ret.position = i;
					return ret;
				}
			}
		}

		return null;
	}

	protected SegmentPosition findSegment(Point pt) {
		for (Polygon p : getAllPolygons()) {
			List<Point> points = p.getPoints();
			int sz = points.size();
			for (int i = 0; i < sz; i++) {
				Point pointA = points.get(i);
				Point pointB = i == sz - 1 ? points.get(0) : points.get(i + 1);
				Segment seg = new Segment(pointA, pointB);
				if (seg.distance2(pt) < 4. * 4.) {
					SegmentPosition ret = new SegmentPosition();
					ret.polygon = p;
					ret.segment = seg;
					ret.position = i;
					return ret;
				}
			}
		}

		return null;
	}

	protected void find(Point point) {
		PointPosition pt = findPoint(point);
		if (!PointPosition.isEqual(pt, foundPoint)) {
			foundPoint = pt;
			getDraw().repaint();
		}
		if (foundPoint == null) {
			SegmentPosition seg = findSegment(point);
			if (!SegmentPosition.isEqual(seg, foundSegment)) {
				foundSegment = seg;
				foundInPoint.set(point.getX(), point.getY());
				getDraw().repaint();
			}
		} else {
			foundSegment = null;
		}
		if (foundPoint != null || foundSegment != null) {
			getDraw().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		} else {
			getDraw().setCursor(Cursor.getDefaultCursor());
		}
	}

	public void onMouseMoved(MouseEvent event) {
	}

	public void onMouseDragged(MouseEvent event) {

	}

	public void onMousePressed(MouseEvent event) {
	}

	public void onMouseReleased(MouseEvent event) {

	}

	public void paintBefore(Graphics g) {
		paintMesh(g);
		if (paintGrid)
			getDraw().drawGrid(g);
	}

	public void paintAfter(Graphics g) {
	}

	protected Point convert(MouseEvent event) {
		Point pt = new Point(event.getX(), event.getY());
		paintGrid = (event.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK;
		if (paintGrid) {
			double x = pt.getX();
			double r = Utils.mod(x, GRID_SIZE);
			if (r < GRID_SENS) {
				x -= r;
			}
			if (r >= GRID_SIZE - GRID_SENS) {
				x += GRID_SIZE - r;
			}

			double y = pt.getY();
			r = Utils.mod(y, GRID_SIZE);
			if (r < GRID_SENS) {
				y -= r;
			}
			if (r >= GRID_SIZE - GRID_SENS) {
				y += GRID_SIZE - r;
			}
			pt.set(x, y);
		}

		return pt;
	}

	private void paintMesh(Graphics g) {
		if (mesh != null) {
			for (Triangle t : mesh) {
				PolyDrawPanel d = getDraw();
				Segment seg1 = new Segment(t.getFirst(), t.getSecond());
				Segment seg2 = new Segment(t.getSecond(), t.getThird());
				Segment seg3 = new Segment(t.getThird(), t.getFirst());
				g.setColor(MESH_COLOR);
				d.drawSegment(g, seg1);
				d.drawSegment(g, seg2);
				d.drawSegment(g, seg3);
			}
		}
	}

	public void recalc() {
		final Polygon clone = getModel().clone();
		Thread thread = new Thread() {
			@Override
			public void run() {
				TriangulationAlgorithm alg = null;
				alg = new TriangulationAlgorithm(clone);
				List<Triangle> result = alg.compute();
				mesh = result;
				issueRepaint();
			}
		};
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	protected void issueRepaint() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getDraw().repaint();
			}
		});
	}
}