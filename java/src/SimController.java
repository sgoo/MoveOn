import java.util.ArrayList;

public class SimController {

	public static final int TICK_SPAN_LENGTH = 5;

	ArrayList<Car> cars;
	ArrayList<Tickable> tickables = new ArrayList<Tickable>();

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

	public void addCar(boolean isVTL, int timeToArrive, Road r) {
		Car c;
		if (isVTL) {
			c = new VTLCar(timeToArrive, intersection, r);
		} else {
			c = new DumbCar(timeToArrive, intersection, r);
		}
		tickables.add(c);

	}

}
