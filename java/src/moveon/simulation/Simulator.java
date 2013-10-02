package moveon.simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import moveon.cars.Car;
import moveon.cars.VTLCar;
import moveon.exceptions.CarFileFormatException;
import moveon.gui.KeyInput;

public class Simulator {

	private static final Charset ENCODING = StandardCharsets.UTF_8;

	public static final String VTL = "VTL";

	public static final String NORMAL = "NORMAL";

	private static final String TESTFOLDER = "TestFiles" + File.separator;

	private ArrayList<Car> cars;
	private Intersection intersection;

	private boolean pause = false;
	private int tickTimeMillis;
	private boolean generateRandomCars;

	private ArrayList<SimulationListener> simListeners;

	private KeyInput gui;

	private boolean generateRandomNormalCars;

	private boolean generateRandomVTLCars;

	public Simulator() {
		cars = new ArrayList<Car>();
		intersection = new Intersection();
		gui = new KeyInput(this);

		generateRandomCars = true;
		generateRandomVTLCars = true;
		generateRandomNormalCars = true;

		simListeners = new ArrayList<SimulationListener>();
		simListeners.add(gui);
		simListeners.add(new SimulationConsoleOutputer());
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

	public void initialize(String filename) throws IOException,
			CarFileFormatException {
		readCarsFromFile(filename);
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
			StringBuilder sb = new StringBuilder();

			sb.append(intersection.mode + "\n");

			// tick all cars, and let us know what each is up to.

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

					if (randomChoice < 0.25)
						randomDirection = Direction.N;
					else if (randomChoice >= 0.25 && randomChoice < 0.5)
						randomDirection = Direction.S;
					else if (randomChoice >= 0.5 && randomChoice < 0.75)
						randomDirection = Direction.E;
					else
						/* (randomChoice >= 0.75 && randomChoice < 1.0) */randomDirection = Direction.W;

					// Randomly choose VTL or Non-VTL
					if (generateRandomNormalCars && generateRandomVTLCars) {
						if (Math.random() < 0.5)
							addVTLCar(100, randomDirection);
						else
							addCar(100, randomDirection);
					} else if (generateRandomNormalCars
							&& !generateRandomVTLCars) {
						addCar(100, randomDirection);
					} else if (!generateRandomNormalCars
							&& generateRandomVTLCars) {

						addVTLCar(100, randomDirection);
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

	/**
	 * @param args
	 * @throws CarFileFormatException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException,
			CarFileFormatException {
		Simulator simulator = new Simulator();
		simulator.initialize(TESTFOLDER + "Test1");
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

	public boolean isGenerateRandomCars() {
		return generateRandomCars;
	}

	public void setGenerateRandomCars(boolean generateRandomCars) {
		this.generateRandomCars = generateRandomCars;
	}

	public boolean isPaused() {
		return pause;
	}

	/**
	 * Read a file and add the cars represented in it
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @throws CarFileFormatException
	 */
	private void readCarsFromFile(String fileName) throws IOException,
			CarFileFormatException {
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
