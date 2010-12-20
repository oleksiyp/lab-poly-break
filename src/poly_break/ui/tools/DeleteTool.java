package poly_break.ui.tools;

import java.awt.event.MouseEvent;

import poly_break.model.Point;
import poly_break.model.Polygon;
import poly_break.ui.PolyDrawPanel;

public class DeleteTool extends Tool {

	public DeleteTool(PolyDrawPanel draw) {
		super(draw);
	}

	@Override
	public void onMousePressed(MouseEvent event) {
		find(new Point(event.getX(), event.getY()));
		Polygon polygon = null;
		if (foundPoint != null) {
			polygon = foundPoint.polygon;
			if (polygon.getContainer() == null
					&& polygon.getPoints().size() <= 3)
				return;
			polygon.getPoints().remove(foundPoint.position);
			recalc();
		} else if (foundSegment != null) {
			int pos = foundSegment.position;
			polygon = foundSegment.polygon;
			if (polygon.getContainer() == null
					&& polygon.getPoints().size() <= 4)
				return;

			polygon.getPoints().remove(pos);

			if (pos > polygon.getPoints().size())
				pos = 0;

			polygon.getPoints().remove(pos);
			recalc();
		}
		if (polygon != null) {
			if (polygon.getPoints().size() < 3) {
				Polygon container = polygon.getContainer();
				container.removeHole(polygon);
			}
			recalc();
		}

		foundPoint = null;
		foundSegment = null;

		getDraw().repaint();

		super.onMousePressed(event);
	}

}
