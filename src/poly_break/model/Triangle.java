package poly_break.model;


public class Triangle {
	private final Point first, second, third;

	public Triangle(final Point first, final Point second, final Point third) {
		super();
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public Point getFirst() {
		return first;
	}

	public Point getSecond() {
		return second;
	}

	public Point getThird() {
		return third;
	}
	@Override
	public String toString() {
		return "<" + first + "-" + second + "-" + third + ">";		
	}
}
