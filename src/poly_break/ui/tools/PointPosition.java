/**
 * 
 */
package poly_break.ui.tools;

import poly_break.model.Point;
import poly_break.model.Polygon;

public class PointPosition {
	public Polygon polygon;

	public Point point;

	public int position;

	public static boolean isEqual(PointPosition a, PointPosition b) {
		if (a == b)
			return true;
		if (a == null)
			return false;
		if (b == null)
			return false;
		return (a.polygon == b.polygon && a.position == b.position);
	}
}