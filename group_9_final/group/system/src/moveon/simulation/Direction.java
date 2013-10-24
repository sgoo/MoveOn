package moveon.simulation;

import java.util.ArrayList;

import moveon.cars.Car;
import moveon.cars.VTLCar;

/**
 * Represents one side of an intersection Could be thought of as a road
 * 
 * @author Jourdan, Scott, Mike, Roy
 * 
 */
public enum Direction {
	// Sides
	N, S, E, W;

	// Pairing of lights
	static {
		N.lights = S.lights = new Lights(Lights.Color.G);
		E.lights = W.lights = new Lights(Lights.Color.G);
	}

	public static final int PED_CORSSING_TIME = 15;

	// A set of traffic lights
	public Lights lights;

	// Some number of cars
	private ArrayList<Car> cars;

	private Direction() {
		cars = new ArrayList<Car>();
	}

	public boolean pedsWaiting = false;

	public int pedsCrossing = -1;

	/**
	 * Adds a Car to this Directions cars. Cars are either waiting or proceeding
	 * through
	 * 
	 * @param car
	 */
	public void addCar(Car car) {
		cars.add(car);
	}

	/**
	 * Cars should be remvoed once they have crossed the intersection
	 * 
	 * @param car
	 */
	public void removeCar(Car car) {
		cars.remove(car);
	}

	/**
	 * Check for VTL cars
	 * 
	 * @return whether or not this intersection has VTL Cars
	 */
	public boolean hasVTLCars() {
		for (Car c : cars) {
			if (c instanceof VTLCar) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check for Non-VTL cars
	 * 
	 * @return whether or not this intersection has Non-VTL Cars
	 */
	public boolean hasNonVTLCars() {
		for (Car c : cars) {
			if (!(c instanceof VTLCar)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given car can proceed forward.
	 * 
	 * @param car
	 * @return Whether or not the car can proceed
	 */
	public boolean canAdvance(Car car) {
		int index = cars.indexOf(car);
		index--;
		if (index < 0) {
			return true;
		}
		Car carInFront = cars.get(index);
		return carInFront.distanceFromIntersection + Car.CAR_LENGTH < car.distanceFromIntersection;
	}

	/**
	 * Get the closest VTL car to the crossing
	 * 
	 * @return A VTL Car or null if there are none
	 */
	public VTLCar getClosestVTLCar() {
		for (Car car : cars) {
			if (car.distanceFromIntersection >= -(Intersection.INTERSECTION_SPAN + 1)
					&& car instanceof VTLCar) {
				return (VTLCar) car;
			}
		}
		return null;
	}

	/**
	 * Get the closest VTL car to the crossing
	 * 
	 * @return A VTL Car or null if there are none
	 */
	public VTLCar getClosestVTLCar2() {
		for (Car car : cars) {
			if (car.distanceFromIntersection >= 0 && car instanceof VTLCar) {
				return (VTLCar) car;
			}
		}
		return null;
	}

	/**
	 * Static method to get the direction for a given string e.g. north, North,
	 * n, N would return Direction.N
	 * 
	 * @param string
	 * @return
	 */
	public static Direction getDirFromStr(String string) {
		if (string.length() == 0)
			return null;
		string = string.substring(0, 1);
		string.toUpperCase();
		if (string.equals("N"))
			return Direction.N;
		if (string.equals("E"))
			return Direction.E;
		if (string.equals("S"))
			return Direction.S;
		if (string.equals("W"))
			return Direction.W;
		return null;
	}

	/**
	 * Create a string representation of the cars on this direction Iterate
	 * through each car and add an arrow representing it to be its distance away
	 * from the 'intersection' The intersection is conceptually on the left side
	 * of the screen This is outdated by the graphical GUI
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		int carsPrinted = 0;
		Car car;
		int soFar = 0;

		for (; cars.size() > carsPrinted
				&& (car = cars.get(carsPrinted)).distanceFromIntersection < 0; carsPrinted++) {
			String carOut = car.getCarId() + "-"
					+ (-car.distanceFromIntersection);
			sb.append(carOut);
			soFar += Car.CAR_LENGTH;
			for (int i = carOut.length(); i < Car.CAR_LENGTH; i++) {
				sb.append('-');
			}
		}

		for (; soFar < 5; soFar++) {
			sb.append(' ');
		}
		soFar = 0;
		sb.append(super.toString());
		sb.append(lights);

		for (; cars.size() > carsPrinted; carsPrinted++) {
			car = cars.get(carsPrinted);
			for (; soFar < car.distanceFromIntersection; soFar++) {
				sb.append(' ');
			}
			soFar += Car.CAR_LENGTH;
			sb.append(car);
		}
		for (; soFar < Intersection.VTL_SPAN; soFar++) {
			sb.append(' ');
		}

		sb.append('|');
		return sb.toString();
	}

	/**
	 * Get an array of directions
	 * 
	 * @return
	 */
	public static Direction[] getDirections() {
		return new Direction[] { N, S, E, W };
	}

	/**
	 * Add a pedestrian We only represent pedestrians as being present or not
	 * present So we just add a current pedestrian to this direction
	 */
	public void addPed() {
		pedsWaiting = true;
	}

	/**
	 * Check for pedestrians Check whether there are pedestrians waiting to
	 * cross or currently crossing
	 * 
	 * @param tick
	 * @return
	 */
	public boolean hasPeds(int tick) {
		return pedsWaiting || isPedsCrossing(tick);
	}

	/**
	 * Check whether pedestrians are currently crossing
	 * 
	 * @param tick
	 * @return
	 */
	public boolean isPedsCrossing(int tick) {
		if (pedsCrossing + PED_CORSSING_TIME < tick) {
			pedsCrossing = -1;
		}
		return pedsCrossing != -1;
	}

	/**
	 * Tell waiting pedestrians to begin crossing
	 * @param tick
	 */
	public void startCrossing(int tick) {
		if (pedsWaiting) {
			pedsCrossing = tick;
			pedsWaiting = false;
		}
	}
}
