public abstract class Car implements Tickable {
	int approachAt;

	public enum State {
		SAFE, APPROACHING, WAITING, CROSSING, LEAVING
	}

	State state = State.SAFE;
	Intersection intersection;
	int queuePosition = SimController.TICK_SPAN_LENGTH;
	int crossingStart;

	Road road;
	Light light;

	public Car(int timeToArrive, Intersection intersection, Road r) {
		this.approachAt = timeToArrive;
		this.intersection = intersection;
		this.road = r;
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
			if (approachAt == ticks) {
				if (road.cars[queuePosition - 1] == null) {
					intersection.approachDetector(this, road);
					state = State.APPROACHING;
					activeTick(ticks);
				} else {
					approachAt++;
					break;
				}
			}
			break;
			
		case APPROACHING:
			if (intersection.currentMode == Intersection.Mode.Normal) {
				if (queuePosition == 0) {
					if (road.light.currentColor == Light.Color.Green) {
						state = State.CROSSING;
						crossingStart = ticks;
						road.cars[queuePosition] = null;
					}
				} else {
					if (road.cars[queuePosition - 1] == null) {
						road.cars[queuePosition - 1] = this;
						road.cars[queuePosition] = null;
						queuePosition--;

					}
				}
				break;
			}
			activeTick(ticks);
			break;
		case CROSSING:
			if (crossingStart + 3 == ticks) {
				state = State.LEAVING;
			}
			break;
		case LEAVING:
			activeTick(ticks);
		}
	}
}
