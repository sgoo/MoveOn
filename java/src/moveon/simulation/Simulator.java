package moveon.simulation;

import java.util.ArrayList;

import moveon.cars.Car;
import moveon.cars.VTLCar;
import moveon.gui.KeyInput;

public class Simulator {

	private ArrayList<Car> cars;
	private Intersection intersection;

	private boolean pause = false;
	private int tickTimeMillis;

	public Simulator() {
		cars = new ArrayList<Car>();
		intersection = new Intersection();
		new KeyInput(this);
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
			try {
				setTickTimeMillis(250);
				Thread.sleep(getTickTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (pause) {
				continue;
			}

			intersection.tick(i);
			
			System.out.println(intersection.mode);
			
			// tick all cars, and let us know what each is up to.
			for (int j = 0; j < cars.size(); j++) {
				if (!cars.get(j).tick(i)) {
					cars.remove(j);
					j--;
				}
			}
			// 75% of the time add a new car
			if(Math.random() < 0.75){
				addCar(100, Direction.N);
			}
			
			System.out.println(Direction.N);
			System.out.println(Direction.S);
			System.out.println(Direction.E);
			System.out.println(Direction.W);
			System.out.println();

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

	public void playPause() {
		pause = !pause;
	}

	public int getTickTimeMillis() {
		return tickTimeMillis;
	}

	public void setTickTimeMillis(int tickTimeMillis) {
		this.tickTimeMillis = tickTimeMillis;
	}

}
