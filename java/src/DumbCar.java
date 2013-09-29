public class DumbCar extends Car {
	int approachAt;

	public DumbCar(int timeToArrive, Intersection intersection, Road r) {
		super(timeToArrive, intersection, r);
	}

	@Override
	public void activeTick(int ticks) {
		if (ticks == approachAt) {
		} else {

		}

	}

}
