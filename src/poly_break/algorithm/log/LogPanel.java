package poly_break.algorithm.log;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.Scrollable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import poly_break.algorithm.structures.Edge;
import poly_break.algorithm.structures.MonotonePolygon;
import poly_break.algorithm.structures.Vertex;
import poly_break.model.Point;
import poly_break.model.Triangle;

public class LogPanel extends JSplitPane {
	private static final long serialVersionUID = 8242717598563540845L;

	public static final Color BACKGROUND_COLOR = Color.WHITE;

	private static final double SCALE_X = 1;

	private static final double SCALE_Y = 1;

	private BufferedImage defaultImage;

	private JTree tree;

	private PaintPanel paintPanel;

	private JScrollPane scrollPane;

	private DefaultMutableTreeNode currentStageNode;

	private LogCanvas logCanvas = new LogCanvas();

	private LogStage getSelectedStage() {
		TreePath path = tree.getSelectionPath();
		if (path == null)
			return null;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		return (LogStage) node.getUserObject();
	}

	private BufferedImage getSelectedImage() {
		LogStage stage = getSelectedStage();
		if (stage == null)
			return defaultImage;
		return stage.getImage();
	}

	private BufferedImage getImage() {
		// LogStage stage = getCurrentStage();
		// if (stage == null)
		return defaultImage;
		// return stage.getImage();
	}

	private void setImage(BufferedImage image) {
		// LogStage stage = getCurrentStage();
		// if (stage == null)
		defaultImage = image;
		// stage.setImage(image);
	}

	private LogStage getCurrentStage() {
		return (LogStage) currentStageNode.getUserObject();
	}

	LogPanel() {
		initComponents();
		setOrientation(HORIZONTAL_SPLIT);
	}

	private void initComponents() {
		currentStageNode = new DefaultMutableTreeNode(new LogStage(
				"Triangulation"));
		tree = new JTree(currentStageNode);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				repaint();
			}
		});
		paintPanel = new PaintPanel();
		scrollPane = new JScrollPane(paintPanel);
		setDividerLocation(0.45);
		setLeftComponent(tree);
		setRightComponent(scrollPane);
	}

	private class PaintPanel extends JPanel implements Scrollable {
		private static final long serialVersionUID = 575132755626720310L;

		public PaintPanel() {
			setBackground(BACKGROUND_COLOR);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			BufferedImage img = getSelectedImage();
			if (img != null) {
				g.drawImage(img, 30, 30, null);
			}
		}

		public int getImageWidth() {
			BufferedImage img = getSelectedImage();
			if (img == null)
				return 60;
			return img.getWidth() + 60;
		}

		public int getImageHeight() {
			BufferedImage img = getSelectedImage();
			if (img == null)
				return 60;
			return img.getHeight() + 60;
		}

		public Dimension getPreferredScrollableViewportSize() {
			return new Dimension(getImageWidth(), getImageHeight());
		}

		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 100;
		}

		public boolean getScrollableTracksViewportHeight() {
			return true;
		}

		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 10;
		}
	}

	public static BufferedImage createCanvasImage(int width, int height) {
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics gr = img.getGraphics();
		gr.setColor(BACKGROUND_COLOR);
		gr.fillRect(0, 0, width, height);
		img.getGraphics().translate(30, 30);
		return img;
	}

	public static BufferedImage copyCanvasImage(BufferedImage from, int width,
			int height) {
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics gr = img.getGraphics();
		gr.setColor(BACKGROUND_COLOR);
		gr.fillRect(0, 0, width, height);
		gr.drawImage(from, 0, 0, null);
		gr.translate(30, 30);
		return img;
	}

	private class LogCanvas implements Log {

		private double penSize;

		private Color penColor;

		private float pointSize;

		private Color pointFill;

		private Color pointBorder;

		private boolean showVertexNumbers;

		private Converter<Edge> edgeConverter = Converter.DEFAULT_EDGE_CONVERTER;

		private void init() {
			if (getImage() == null) {
				setImage(createCanvasImage(800, 800));
				paintPanel.setSize(160, 160);
				penSize = 1.;
				penColor = Color.BLACK;
				pointSize = 4;
				pointFill = Color.WHITE;
				pointBorder = Color.BLACK;
				showVertexNumbers = false;
				edgeConverter = Converter.DEFAULT_EDGE_CONVERTER;
			}
		}

		private Graphics2D getGraphicsObject() {
			init();

			Graphics2D g2d = (Graphics2D) getImage().getGraphics();
			g2d.addRenderingHints(new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON));
			g2d.addRenderingHints(new RenderingHints(
					RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON));
			g2d.addRenderingHints(new RenderingHints(
					RenderingHints.KEY_STROKE_CONTROL,
					RenderingHints.VALUE_STROKE_NORMALIZE));
			return g2d;
		}

		private BufferedImage copy(BufferedImage image) {
			int width = image.getWidth();
			int height = image.getHeight();
			BufferedImage img = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics gr = img.getGraphics();
			gr.setColor(BACKGROUND_COLOR);
			gr.fillRect(0, 0, width, height);
			gr.drawImage(image, 0, 0, null);
			gr.translate(30, 30);
			return img;
		}

		private void ensureHasSize(Rectangle2D bounds) {
			if (getImage() == null)
				return;

			int x = (int) bounds.getMaxX() + 60;
			int y = (int) bounds.getMaxY() + 60;

			boolean resize = false;
			int w = getImage().getWidth(), h = getImage().getHeight();
			if (x > w) {
				w = x;
				resize = true;
			}
			if (y > h) {
				h = y;
				resize = true;
			}

			if (resize) {
				setImage(copyCanvasImage(getImage(), w, h));
				paintPanel.setSize(w + 60, h + 60);
			}
		}

		public void setPenColor(Color color) {
			init();
			penColor = color;
		}

		public void setPenSize(float size) {
			init();
			getGraphicsObject();
		}

		public void setPointSize(float size) {
			if (showVertexNumbers)
				return;
			init();
			pointSize = size;
		}

		public void setPointColor(Color fill, Color border) {
			init();
			pointFill = fill;
			pointBorder = border;
		}

		public void start(String string) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(
					new LogStage(string));
			currentStageNode.add(node);
			currentStageNode = node;
		}

		public void end() {
			getCurrentStage().setImage(copy(getImage()));
			TreeNode parent = currentStageNode.getParent();
			if (parent == null)
				return;
			currentStageNode = (DefaultMutableTreeNode) parent;
			tree.setSelectionPath(new TreePath(currentStageNode.getPath()));
		}

		public void hideVertexNumbers() {
			showVertexNumbers = false;
		}

		public void showVertexNumbers() {
			init();
			showVertexNumbers = true;
			pointSize = 20;
		}

		public PrintWriter getWriter() {
			PrintWriter printWriter = new PrintWriter(System.out);
			return printWriter;
		}

		public void setEdgeConverter(Converter<Edge> converter) {
			init();
			edgeConverter = converter;
		}

		public void drawTraingle(Triangle triangle) {
			Point2D a = tranform(pointAsPoint(triangle.getFirst()));
			Point2D b = tranform(pointAsPoint(triangle.getSecond()));
			Point2D c = tranform(pointAsPoint(triangle.getThird()));

			Point2D addA = delta(a, b, c, 5);
			Point2D addB = delta(b, a, c, 5);
			Point2D addC = delta(c, a, b, 5);

			a = PointUtils.add(a, addA);
			b = PointUtils.add(b, addB);
			c = PointUtils.add(c, addC);

			Line2D lineAB = new Line2D.Double(a, b);
			Line2D lineBC = new Line2D.Double(b, c);
			Line2D lineAC = new Line2D.Double(a, c);

			ensureHasSize(lineAB.getBounds2D());
			ensureHasSize(lineBC.getBounds2D());
			ensureHasSize(lineAC.getBounds2D());

			Graphics2D g = getGraphicsObject();

			g.setStroke(new BasicStroke((float) penSize));
			g.setColor(penColor);
			g.draw(lineAB);
			g.draw(lineBC);
			g.draw(lineAC);
			repaint();
		}

		public void drawMonotonePoly(MonotonePolygon poly) {
			List<Vertex> verts = poly.toList();
			List<Point2D> points = new LinkedList<Point2D>();
			for (int i = 0; i < verts.size(); i++) {
				Vertex p = verts.get((i - 1 + verts.size()) % verts.size());
				Vertex v = verts.get(i);
				Vertex n = verts.get((i + 1) % verts.size());

				Point2D a = tranform(vertexAsPoint(p));
				Point2D b = tranform(vertexAsPoint(v));
				Point2D c = tranform(vertexAsPoint(n));

				Point2D addB = delta(b, a, c, 5);
				b = PointUtils.add(b, addB);
				points.add(b);
			}

			for (int i = 0; i < points.size(); i++) {
				Point2D a = points.get(i);
				Point2D b = points.get((i + 1) % points.size());
				Line2D l = new Line2D.Double(a, b);
				ensureHasSize(l.getBounds2D());
			}
			Graphics2D g = getGraphicsObject();

			g.setStroke(new BasicStroke((float) penSize));
			g.setColor(penColor);
			for (int i = 0; i < points.size(); i++) {
				Point2D a = points.get(i);
				Point2D b = points.get((i + 1) % points.size());
				Line2D l = new Line2D.Double(a, b);
				g.draw(l);
			}
			repaint();
		}

		private Point2D delta(Point2D a, Point2D b, Point2D c, double d) {
			Point2D r = PointUtils.add(PointUtils.subtract(b, a), PointUtils
					.subtract(c, a));
			r = PointUtils.normalize(r);
			return PointUtils.multiply(r, d);
		}

		public void clear() {
			setImage(null);
			getGraphicsObject();
		}

		public void drawArrow(Edge edge, boolean direction) {
			init();
			Point2D start = new Point2D.Double(edge.getStart().getPoint()
					.getX()
					* SCALE_X, edge.getStart().getPoint().getY() * SCALE_Y);
			Point2D end = new Point2D.Double(edge.getEnd().getPoint().getX()
					* SCALE_X, edge.getEnd().getPoint().getY() * SCALE_Y);

			if (!direction) {
				Point2D pt = start;
				start = end;
				end = pt;
			}

			Point2D dir = PointUtils.normalize(PointUtils.subtract(end, start));

			subtractPointSize(start, end);

			Point2D arr = PointUtils.copy(dir);
			arr = PointUtils.multiply(arr, 2. * penSize + 5);

			double theta = Math.PI - Math.PI / 10;

			Point2D endLeft = PointUtils.rotate(arr, theta);
			Point2D endRight = PointUtils.rotate(arr, -theta);

			endLeft = PointUtils.add(end, endLeft);
			endRight = PointUtils.add(end, endRight);

			Line2D base = new Line2D.Double(start, end);
			Line2D arrowLeft = new Line2D.Double(end, endLeft);
			Line2D arrowRight = new Line2D.Double(end, endRight);

			ensureHasSize(base.getBounds2D());
			ensureHasSize(arrowLeft.getBounds2D());
			ensureHasSize(arrowRight.getBounds2D());

			Graphics2D g = getGraphicsObject();

			g.setStroke(new BasicStroke((float) penSize));
			g.setColor(penColor);

			g.draw(base);
			g.draw(arrowLeft);
			g.draw(arrowRight);

			String str = edgeConverter.toString(edge);
			int w = g.getFontMetrics().stringWidth(str);
			int h = g.getFontMetrics().getHeight();
			double textX = (start.getX() + end.getX()) / 2 - w / 2;
			double textY = (start.getY() + end.getY()) / 2 + h / 2;
			g.drawString(str, (float) textX, (float) textY);

			repaint();
		}

		private void subtractPointSize(Point2D start, Point2D end) {
			Point2D dir = PointUtils.normalize(PointUtils.subtract(end, start));
			Point2D v2 = PointUtils.multiply(dir, pointSize / 2.);
			end.setLocation(PointUtils.subtract(end, v2));
			start.setLocation(PointUtils.add(start, v2));
		}

		public void drawLine(Edge edge) {
			init();
			Point2D start = new Point2D.Double(edge.getStart().getPoint()
					.getX()
					* SCALE_X, edge.getStart().getPoint().getY() * SCALE_Y);
			Point2D end = new Point2D.Double(edge.getEnd().getPoint().getX()
					* SCALE_X, edge.getEnd().getPoint().getY() * SCALE_Y);

			subtractPointSize(start, end);

			Line2D line = new Line2D.Double(start, end);

			ensureHasSize(line.getBounds2D());

			Graphics2D g = getGraphicsObject();

			g.setStroke(new BasicStroke((float) penSize));
			g.setColor(penColor);
			g.draw(line);
			repaint();
		}

		public void drawPoint(Vertex vertex) {
			init();
			double x = vertex.getPoint().getX() * SCALE_X;
			double y = vertex.getPoint().getY() * SCALE_Y;

			Ellipse2D el = new Ellipse2D.Double(x - pointSize / 2., y
					- pointSize / 2., pointSize, pointSize);

			ensureHasSize(el.getBounds2D());

			Graphics2D g = getGraphicsObject();

			g.setStroke(new BasicStroke((float) penSize));
			g.setColor(penColor);
			g.setColor(pointFill);
			g.fill(el);
			g.setColor(pointBorder);
			g.draw(el);

			if (showVertexNumbers)
				g.drawString("" + vertex.getVertexIndex(), (float) x - 5,
						(float) y + 5);
			repaint();

		}
	}

	private Point2D tranform(Point2D pt) {
		return new Point2D.Double(pt.getX() * SCALE_X, pt.getY() * SCALE_Y);
	}

	private Point2D vertexAsPoint(Vertex v) {
		Point pt = v.getPoint();
		return new Point2D.Double(pt.getX(), pt.getY());
	}

	private Point2D pointAsPoint(Point pt) {
		return new Point2D.Double(pt.getX(), pt.getY());
	}

	public LogCanvas getLogCanvas() {
		return logCanvas;
	}
}
