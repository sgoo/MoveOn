package moveon.simulation;

import java.util.ArrayList;

import moveon.cars.Car;
import moveon.cars.VTLCar;
import moveon.exceptions.CarFileFormatException;

/**
 * The simulator. Contains the main method to run the simulator
 * 
 * @author Mike, Roy, Scott, Jourdan
 * 
 */
public class Simulator {

	public static final String VTL = "VTL";
	public static final String NORMAL = "NORMAL";

	public ArrayList<Car> cars;
	public ArrayList<Car> leavingCars;
	public Intersection intersection;
	private boolean pause = false;
	private int tickTimeMillis;
	private boolean generateRandomCars;
	private ArrayList<SimulationListener> simListeners;
	private boolean generateRandomNormalCars;
	private boolean generateRandomVTLCars;
	private boolean generateRandomPeople;

	public int tick;

	// Specify a number of Ticks that the simulator will run for.
	// This allows JPF to finish
	public static final int SIM_LENGTH = 4000;

	/**
	 * Main method to run the simulation
	 * 
	 * @param args
	 * @throws CarFileFormatException
	 */
	public static void main(String[] args) throws CarFileFormatException,
			Exception {
		Simulator simulator = new Simulator();
		simulator.initialize();
		simulator.simulate();
	}

	public Simulator() {
		this(true);
	}

	public Simulator(boolean hasConsole) {
		cars = new ArrayList<Car>();
		leavingCars = new ArrayList<Car>();
		intersection = new Intersection();

		generateRandomCars = true;
		generateRandomVTLCars = true;
		generateRandomNormalCars = true;
		generateRandomPeople = true;

		simListeners = new ArrayList<SimulationListener>();
		if (hasConsole)
			simListeners.add(new SimulationConsoleOutputer());
	}

	public void addSimListener(SimulationListener l) {
		simListeners.add(l);
	}

	public void initialize() {

		addVTLCar(10, Direction.N);
		addVTLCar(16, Direction.N);
		addVTLCar(18, Direction.S);
		addVTLCar(18, Direction.E);
		addVTLCar(25, Direction.W);
		addVTLCar(25, Direction.N);
		addVTLCar(30, Direction.E);

	}

	/**
	 * Load cars from the given file
	 * 
	 * @param filename
	 * @throws CarFileFormatException
	 */
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

		for (tick = 0; tick < SIM_LENGTH || cars.size() != 0; tick++) {
			if (tick > SIM_LENGTH) {
				generateRandomCars = false;
			}
			try {
				setTickTimeMillis(100);
				Thread.sleep(getTickTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (pause) {
				continue;
			}

			intersection.tick(tick);
			StringBuilder sb = new StringBuilder();

			sb.append(intersection.mode + "  ");
			sb.append(Direction.N.pedsWaiting + " ");
			sb.append(Direction.S.pedsWaiting + " ");
			sb.append(Direction.E.pedsWaiting + " ");
			sb.append(Direction.W.pedsWaiting + "   ");

			sb.append(Direction.N.isPedsCrossing(tick) + " ");
			sb.append(Direction.S.isPedsCrossing(tick) + " ");
			sb.append(Direction.E.isPedsCrossing(tick) + " ");
			sb.append(Direction.W.isPedsCrossing(tick) + "\n");

			// tick all cars, and let us know what each is up to.
			for (int j = 0; j < cars.size(); j++) {
				if (!cars.get(j).tick(tick)) {
					leavingCars.add(cars.remove(j));
					j--;
				}
			}
			
			for (int j = 0; j < leavingCars.size(); j++) {
				if (!leavingCars.get(j).tick(tick)) {
					leavingCars.remove(j);
					j--;
				}
			}
			
			if (generateRandomCars) {
				// 75% of the time add a new car
				if (Math.random() < 0.15) {
					// randomly choose direction
					double randomChoice = Math.random();

					Direction randomDirection;

					if (randomChoice < 0.25) {
						randomDirection = Direction.N;
					} else if (randomChoice >= 0.25 && randomChoice < 0.5) {
						randomDirection = Direction.S;
					} else if (randomChoice >= 0.5 && randomChoice < 0.75) {
						randomDirection = Direction.E;
					} else {
						randomDirection = Direction.W;
					}
					// Randomly choose VTL or Non-VTL
					if (generateRandomNormalCars && generateRandomVTLCars) {
						if (Math.random() < 0.5) {
							addVTLCar(Intersection.VTL_SPAN, randomDirection);
						} else {
							addCar(Intersection.VTL_SPAN, randomDirection);
						}
					} else if (generateRandomNormalCars
							&& !generateRandomVTLCars) {
						addCar(Intersection.VTL_SPAN, randomDirection);
					} else if (!generateRandomNormalCars
							&& generateRandomVTLCars) {
						addVTLCar(Intersection.VTL_SPAN, randomDirection);
					}
				}
				if (generateRandomPeople && Math.random() > 0.97) {
					double d = Math.random();
					if (d < 0.25) {
						Direction.N.addPed();
					} else if (d < 0.5) {
						Direction.S.addPed();
					} else if (d < 0.75) {
						Direction.E.addPed();
					} else {
						Direction.W.addPed();
					}
				}
			}
			sb.append(Direction.N + "\n");
			sb.append(Direction.S + "\n");
			sb.append(Direction.E + "\n");
			sb.append(Direction.W + "\n");
			sb.append("\n");

			for (SimulationListener listener : simListeners) {
				listener.simulationUpdated(sb.toString());
			}

		}
	}

	public void playPause() {
		pause = !pause;
	}

	public boolean isPaused() {
		return pause;
	}

	public int getTickTimeMillis() {
		return tickTimeMillis;
	}

	public void setTickTimeMillis(int tickTimeMillis) {
		this.tickTimeMillis = tickTimeMillis;
	}

	public boolean isGenerateRandomCars() {
		return generateRandomCars;
	}

	public void setGenerateRandomCars(boolean generateRandomCars) {
		this.generateRandomCars = generateRandomCars;
	}

	public boolean isGenerateRandomPeople() {
		return generateRandomPeople;
	}

	public void setGenerateRandomPeople(boolean generateRandomPeople) {
		this.generateRandomPeople = generateRandomPeople;
	}

	public void toggleRandom() {
		generateRandomCars = !generateRandomCars;
	}

	public void toggleRandomVTLCars() {
		generateRandomVTLCars = !generateRandomVTLCars;
	}

	public void toggleRandomNormalCars() {
		generateRandomNormalCars = !generateRandomNormalCars;
	}
}
