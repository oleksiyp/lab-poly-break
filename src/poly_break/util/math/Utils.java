package poly_break.util.math;

import java.util.AbstractList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
	private static final double EPSILON = Double.MIN_VALUE * 1024.d * 1024.d;

	public static int cmp(double a, double b) {
		if (Math.abs(a - b) < EPSILON)
			return 0;
		if (a < b)
			return -1;
		return 1;
	}

	public static <E> List<E> reverse(final List<E> list) {
		return new AbstractList<E>() {

			@Override
			public E get(int index) {
				return list.get(list.size() - index - 1);
			}

			@Override
			public int size() {
				return list.size();
			}
		};
	}

	public static double mod(double x, double y) {
		return x - Math.floor(x / y) * y;
	}

	public static <E> Set<E> intersect(Set<E> a, Set<E> b) {
		Set<E> result = new HashSet<E>();
		result.addAll(a);
		result.retainAll(b);
		return result;
	}
}
