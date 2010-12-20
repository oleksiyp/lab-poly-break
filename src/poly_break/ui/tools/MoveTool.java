/**
 * 
 */
package poly_break.ui.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import poly_break.model.Point;
import poly_break.ui.PolyDrawPanel;

public class MoveTool extends Tool {
	public MoveTool(PolyDrawPanel draw) {
		super(draw);
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		find(convert(e));
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		if (foundPoint != null) {
			foundPoint.point.set(convert(e));
			getDraw().repaint();
		} else if (foundSegment != null) {
			Point mouse = convert(e);
			mouse.subtract(foundInPoint);
			foundSegment.segment.move(mouse);
			foundInPoint = convert(e);
			getDraw().repaint();
		}
		super.onMouseDragged(e);
	}
	
	@Override
	public void onMouseReleased(MouseEvent event) {
		super.onMouseReleased(event);
		recalc();
	}

	@Override
	public void paintAfter(Graphics g) {
		if (foundPoint != null) {
			g.setColor(Color.BLACK);
			getDraw().drawPoint(g, foundPoint.point);
		}
		if (foundSegment != null) {
			g.setColor(Color.BLACK);
			getDraw().drawSegment(g, foundSegment.segment);
		}
		super.paintAfter(g);
	}
}