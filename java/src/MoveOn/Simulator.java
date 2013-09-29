package MoveOn;

import java.util.ArrayList;

public class Simulator {

	private ArrayList<Car> cars;

	public Simulator() {
		cars = new ArrayList<Car>();
	}

	public void initialize() {
		cars.add(new Car(15, Direction.N));
		cars.add(new Car(30, Direction.S));
		cars.add(new Car(60, Direction.N));
		cars.add(new Car(30, Direction.E));
		cars.add(new Car(40, Direction.W));
		cars.add(new Car(10, Direction.N));
	}

	public void simulate() {
		for (int i = 0;; i++) {
			for (Car car : cars) {
				car.tick(i);
				System.out.println(car.toString());
				
			}
			try {
				Thread.sleep(1000);
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
