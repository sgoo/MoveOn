package MoveOn;

import java.util.ArrayList;

public class Simulator {
	
	private ArrayList<Car> cars;

	public Simulator() {
		cars = new ArrayList<Car>();
	}

	public void initialize() {
		addCar(10, Direction.N);
		addCar(15, Direction.N);
		addCar(30, Direction.S);
		addCar(30, Direction.E);
		addCar(40, Direction.W);
		addCar(60, Direction.N);
	}
	
	public void addCar(int dist, Direction d){
		Car c = new Car(dist,d);
		d.addCar(c);
		cars.add(c);
	}

	public void simulate() {
		for (int i = 0;; i++) {
			// tick all cars, and let us know what each is up to.
			for (Car car : cars) {
				car.tick(i);
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
