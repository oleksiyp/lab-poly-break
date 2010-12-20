package poly_break.algorithm.log;

import poly_break.algorithm.structures.Edge;

public interface Converter<E> {
	public static Converter<Edge> FLOW_CONVERTER = new Converter<Edge>() {
		public String toString(Edge obj) {
			return Integer.toString(obj.getFlow());
		}
	};
	public static Converter<Edge> DEFAULT_EDGE_CONVERTER = new Converter<Edge>() {
		public String toString(Edge obj) {
			return obj.toString();
		}
	};
	public static final Converter<Edge> CHAIN_RANGE_CONVERTER = new Converter<Edge>() {
		public String toString(Edge obj) {
			return obj.getChainRange().toString();
		}
	};

	String toString(E obj);
}
