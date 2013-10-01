package moveon.simulation;

import java.util.ArrayList;

import moveon.cars.Car;
import moveon.cars.VTLCar;
import moveon.gui.CarCreator;

public class Simulator {

	private ArrayList<Car> cars;
	private Intersection intersection;

	public Simulator() {
		cars = new ArrayList<Car>();
		intersection = new Intersection();
		CarCreator cc = new CarCreator(this);
	}

	public void initialize() {
		addCar(10, Direction.N);
		addCar(16, Direction.N);
		addCar(31, Direction.S);
		addCar(31, Direction.E);
		addCar(40, Direction.W);
		addCar(60, Direction.N);
		addCar(64, Direction.E);
	}

	public void addCar(int dist, Direction d) {
		Car c = new Car(dist, d);
		d.addCar(c);
		cars.add(c);
	}

	public void addVTLCar(int dist, Direction d) {
		Car c = new VTLCar(dist, d);
		d.addCar(c);
		cars.add(c);
	}

	public void simulate() {
		for (int i = 0;; i++) {

			intersection.tick(i);

			// tick all cars, and let us know what each is up to.

			for (int j = 0; j < cars.size(); j++) {
				if (!cars.get(j).tick(i)) {
					cars.remove(j);
					j--;
				}
			}
			System.out.println(Direction.N);
			System.out.println(Direction.S);
			System.out.println(Direction.E);
			System.out.println(Direction.W);
			System.out.println();
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		simulator.initialize();
		simulator.simulate();
	}

}
