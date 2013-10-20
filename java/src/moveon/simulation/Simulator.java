package moveon.simulation;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
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

	// private static final Charset ENCODING = StandardCharsets.UTF_8;
	public static final String VTL = "VTL";
	public static final String NORMAL = "NORMAL";
	// private static final String TESTFOLDER = "TestFiles" + File.separator;

	public ArrayList<Car> cars;
	public Intersection intersection;
	private boolean pause = false;
	private int tickTimeMillis;
	private boolean generateRandomCars;
	private ArrayList<SimulationListener> simListeners;
	// private KeyInput gui;
	private boolean generateRandomNormalCars;
	private boolean generateRandomVTLCars;

	public static final int SIM_LENGTH = 4000;

	/**
	 * Main method to run the simulation
	 * 
	 * @param args
	 * @throws CarFileFormatException
	 */
	public static void main(String[] args) throws CarFileFormatException, Exception {
		Simulator simulator = new Simulator();
		// simulator.initialize(TESTFOLDER + "Test1");
		simulator.initialize();
		simulator.simulate();
	}

	public Simulator() {
		this(true);
	}

	public Simulator(boolean hasConsole) {
		cars = new ArrayList<Car>();
		intersection = new Intersection();
		// gui = new KeyInput(this);

		generateRandomCars = true;
		generateRandomVTLCars = true;
		generateRandomNormalCars = true;

		simListeners = new ArrayList<SimulationListener>();
		// simListeners.add(gui);
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
	/*public void initialize(String filename) throws IOException, CarFileFormatException {
		readCarsFromFile(filename);
	}*/

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

		for (int i = 0; i < SIM_LENGTH || cars.size() != 0; i++) {
			if (i > SIM_LENGTH) {
				generateRandomCars = false;
			}
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
			StringBuilder sb = new StringBuilder();

			sb.append(intersection.mode + "\n");

			// tick all cars, and let us know what each is up to.
			for (int j = 0; j < cars.size(); j++) {
				if (!cars.get(j).tick(i)) {
					cars.remove(j);
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
						/* (randomChoice >= 0.75 && randomChoice < 1.0) */
						randomDirection = Direction.W;
					}
					// Randomly choose VTL or Non-VTL
					if (generateRandomNormalCars && generateRandomVTLCars) {
						if (Math.random() < 0.5) {
							addVTLCar(Intersection.VTL_SPAN, randomDirection);
						} else {
							addCar(Intersection.VTL_SPAN, randomDirection);
						}
					} else if (generateRandomNormalCars && !generateRandomVTLCars) {
						addCar(Intersection.VTL_SPAN, randomDirection);
					} else if (!generateRandomNormalCars && generateRandomVTLCars) {
						addVTLCar(Intersection.VTL_SPAN, randomDirection);
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

	/**
	 * Read a file and add the cars represented in it Format is: TIME(int),DIRECTION(N,S,E,W),CAR_TYPE(VTL,NORMAL)
	 * 
	 * @param fileName
	 * @return
	 * @throws CarFileFormatException
	 */
	/*private void readCarsFromFile(String fileName) throws IOException, CarFileFormatException {
		Path path = Paths.get(fileName);
		BufferedReader reader = Files.newBufferedReader(path, ENCODING);
		// line format TIME DIRECTION CAR_TYPE
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(",");
			if (parts.length != 3) {
				throw new CarFileFormatException();
			}
			int dist = Integer.parseInt(parts[0]);
			Direction direction = Direction.getDirFromStr(parts[1]);
			if (direction == null) {
				throw new CarFileFormatException();
			}
			if (parts[2].equals(VTL)) {
				addVTLCar(dist, direction);
			} else if (parts[2].equals(NORMAL)) {
				addCar(dist, direction);
			}
		}
	}*/

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
