public class Intersection implements Tickable {

	public enum Mode {
		Normal, MixedVTL, VTL
	};

	Mode currentMode;

	public Intersection() {
	}

	@Override
	public void tick(int ticks) {
		if (currentMode == Mode.Normal) {

		}
	}

	public Road approachDetector(Car car, Road r) {
		return null;
	}

	public Mode V2IApproach(VTLCar car, Road r) {
		r.VTLCars++;
		// r.cars.add(car);
		return currentMode;
	}

}
