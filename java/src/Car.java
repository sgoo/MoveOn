public abstract class Car implements Tickable {
	int approachAt;

	public enum State {
		SAFE, APPROACHING, CROSSING, LEAVING
	}

	State state = State.SAFE;
	Intersection intersection;
	//	Road road;
	//	Road.Side side;
	SimController.Direction direction;

	//	public Car(int approachAt, Intersection intersection, Road r, Road.Side s) {
	//		this.intersection = intersection;
	//		this.approachAt = approachAt;
	//		this.road = r;
	//		this.side = s;
	//	}

	public Car(int timeToArrive, Intersection intersection, SimController.Direction d) {
		this.approachAt = timeToArrive;
		this.intersection = intersection;
		this.direction = d;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public abstract void activeTick(int ticks);

	@Override
	public void tick(int ticks) {
		switch (state) {
		case SAFE:
			if (approachAt != ticks) {
				break;
			}
			state = State.APPROACHING;
		case APPROACHING:
		case CROSSING:
		case LEAVING:
			activeTick(ticks);
		}
	}
}
