package poly_break.algorithm.structures;

import java.util.Arrays;

public class ChainRange {
	private final int first, count;

	public ChainRange(final int start, final int count) {
		super();
		this.first = start;
		this.count = count;
	}

	public static ChainRange union(ChainRange... ranges) {
		int start = -1, count = 0;
		for (ChainRange r : ranges) {
			if (start == -1)
				start = r.getFirst();
			if (start + count == r.getFirst()) {
				count += r.getCount();
			} else {
				System.out.println("ranges error " + Arrays.toString(ranges));
			}
		}
		return new ChainRange(start, count);
	}

	public static ChainRange[] split(ChainRange range, int... weights) {
		int start = range.getFirst();
		ChainRange[] result = new ChainRange[weights.length];
		int i = 0;
		for (int w : weights) {
			result[i++] = new ChainRange(start, w);
			start += w;
		}
		if (start != range.getFirst() + range.getCount())
			System.out.println("error " + range + " " + Arrays.toString(weights));
		return result;
	}

	public int getFirst() {
		return first;
	}
	
	public int getLast() {
		return first + count - 1;
	}

	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		if (count == 1) 
			return "[" + first + "]";
		return "[" + first + "-" + (first + count - 1) + "]";
	}
}
