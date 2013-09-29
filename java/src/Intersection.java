import java.util.LinkedList;
import java.util.Queue;

public class Intersection implements Tickable {

	public enum Mode {
		Normal, MixedVTL, VTL
	};

	Mode currentMode;
	private Road EW;
	private Road NS;

	public Intersection() {
	}

	@Override
	public void tick(int ticks) {
		// TODO Auto-generated method stub

	}

	public void approachDetector(DumbCar car, SimController.Direction d) {
		Road r = d == SimController.Direction.NS ? NS : EW;
		r.dumbCars++;
		r.cars.add(car);
	}

	public Mode V2IApproach(VTLCar car, SimController.Direction d) {
		Road r = d == SimController.Direction.NS ? NS : EW;
		r.VTLCars++;
		r.cars.add(car);
		return currentMode;
	}

	public class Road {
		Queue<Car> cars = new LinkedList<Car>();
		public int VTLCars = 0;
		public int dumbCars = 0;
		boolean pedestrianWaiting = false;

		public Road() {

		}

	}

}
