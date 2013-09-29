import java.util.ArrayList;

public class SimController {

	ArrayList<Tickable> tickables = new ArrayList<Tickable>();

	public enum Direction {
		EW, NS;

		public static Direction get(char d) {
			return d == 'N' || d == 'S' ? NS : EW;
		}
	}

	ArrayList<Car> cars;

	Intersection intersection;

	public SimController() {
		intersection = new Intersection();

	}

	public void run() {

		for (int tick = 0;; tick++) {
			for (Tickable t : tickables) {
				t.tick(tick);
			}
		}
	}

	public void addCar(boolean isVTL, int timeToArrive, Direction d) {
		Car c;
		if (isVTL) {
			c = new VTLCar(timeToArrive, intersection, d);
		} else {
			c = new DumbCar(timeToArrive, intersection, d);
		}
		tickables.add(c);

	}

}
