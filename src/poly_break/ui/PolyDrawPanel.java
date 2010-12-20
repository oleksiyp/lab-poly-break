package poly_break.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.ui.tools.Tool;
import poly_break.util.math.GeomUtils;
import poly_break.util.math.Segment;

public class PolyDrawPanel extends JPanel {

	private static final long serialVersionUID = 8593163387833633101L;

	private Polygon model = new Polygon();

	private Tool tool;

	public PolyDrawPanel() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				toolMousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				toolMouseReleased(e);
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				toolMouseDragged(e);
			}

			public void mouseMoved(MouseEvent e) {
				toolMouseMoved(e);
			}

		});
	}

	@Override
	public Color getBackground() {
		return Color.WHITE;
	}

	public void drawPoint(Graphics g, Point pt) {
		int x = (int) pt.getX();
		int y = (int) pt.getY();
		g.fillOval(x - 4, y - 4, 8, 8);
	}

	private void drawSegment(final Graphics g, Point pointA, Point pointB) {
		int x1 = (int) pointA.getX();
		int y1 = (int) pointA.getY();
		int x2 = (int) pointB.getX();
		int y2 = (int) pointB.getY();
		g.drawLine(x1, y1, x2, y2);
	}

	public Point intersects(Segment seg) {
		for (Polygon p : model.getAllPolygons()) {
			for (Segment s : p.getSegments()) {
				Point pt;
				if ((pt = GeomUtils.intersectOnlyPoint(seg, s)) != null)
					return pt;
			}
		}
		return null;
	}

	public void drawSegment(Graphics g, Segment segment) {
		drawSegment(g, segment.getPointA(), segment.getPointB());
	}

	public void drawGrid(Graphics g) {
		g.setColor(Tool.GRID_COLOR);
		for (double x = 0; x < getWidth(); x += Tool.GRID_SIZE) {
			for (double y = 0; y < getHeight(); y += Tool.GRID_SIZE) {
				g.drawLine((int) x, 0, (int) x, getHeight());
				g.drawLine(0, (int) y, getWidth(), (int) y);
			}
		}
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		if (model == null)
			return;
		toolPaintBefore(g);
		for (Polygon p : model.getAllPolygons()) {
			drawPoly(g, p);
		}
		toolPaintAfter(g);
	}

	public void drawPoly(final Graphics g, Polygon p) {
		int depth = p.getDepth();
		int intes = 0 + 128 * depth;
		if (intes > 240)
			intes = 240;
		g.setColor(new Color(intes, intes, intes));
		for (Segment seg : p.getSegments()) {
			drawSegment(g, seg);
			drawPoint(g, seg.getPointA());
		}
	}

	public Polygon getModel() {
		return model;
	}

	public void setModel(Polygon model) {
		this.model = model;
		if (tool != null)
			tool.recalc();
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	private void toolMouseMoved(MouseEvent e) {
		if (tool != null) {
			tool.onMouseMoved(e);
		}
	}

	private void toolMousePressed(MouseEvent e) {
		if (tool != null) {
			tool.onMousePressed(e);
		}
	}

	private void toolMouseReleased(MouseEvent e) {
		if (tool != null) {
			tool.onMouseReleased(e);
		}
	}

	private void toolMouseDragged(MouseEvent e) {
		if (tool != null) {
			tool.onMouseDragged(e);
		}
	}

	private void toolPaintBefore(final Graphics g) {
		if (tool != null) {
			tool.paintBefore(g);
		}
	}

	private void toolPaintAfter(final Graphics g) {
		if (tool != null) {
			tool.paintAfter(g);
		}
	}
}
