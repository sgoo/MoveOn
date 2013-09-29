public enum Road {

	//	public static enum Direction {
	//		N(new Road()), S(new Road()), E(new Road()), W(new Road());
	//
	//		public Road road;
	//
	//		Direction(Road r) {
	//			this.road = r;
	//		}
	//
	//		
	//	}
	N, S, E, W;
	public static Road get(char d) {
		switch (d) {
		case 'N':
			return N;
		case 'S':
			return S;
		case 'E':
			return E;
		default:
			return W;
		}
	}

	private Road() {
	}

	public Light light;

	Car[] cars = new Car[SimController.TICK_SPAN_LENGTH];
	public int VTLCars = 0;
	public int detectedCars = 0;
	boolean pedestrianWaiting = false;

}