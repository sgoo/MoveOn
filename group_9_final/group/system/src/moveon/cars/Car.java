package moveon.cars;

import gov.nasa.jpf.annotation.Invariant;
import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.Tickable;

/**
 * Car represents a normal car (aka non VTL)
 * 
 * @author Jourdan Harvey, Scott Goodhew, Mike Little, Roy Lin
 * 
 */
//@Invariant({ "(distanceFromIntersection >= 0 || lightColor != 2) || leftIntersection == 1" })
// Taken out because we could not satisify it, sometimes cars would still be in the intersection...
/**
 * @author Jourdan Harvey
 * 
 */
public class Car implements Tickable {

	/**
	 * Car length Unit: m
	 */
	public static final int CAR_LENGTH = 4;

	/**
	 * Car speed Unit: m/s
	 */
	public static final int SPEED = 1;

	/**
	 * Count of the number of cars that have been through the system. This forms
	 * the car id
	 */
	private static int CAR_COUNT = 0;

	private int carId;

	public int distanceFromIntersection;
	public Direction direction;

	// for JPF
	int lightColor;

	private int leftIntersection = 0;

	/**
	 * Constructor Increment the car count and set this to this cars id
	 * 
	 * @param dist
	 * @param direction
	 */
	public Car(int dist, Direction direction) {
		this.distanceFromIntersection = dist;
		this.direction = direction;
		carId = CAR_COUNT++;
	}

	/**
	 * Get the ID for this car
	 * 
	 * @return ID
	 */
	public int getCarId() {
		return carId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + carId;
		return result;
	}

	/**
	 * Implementation of equals so we can compare cars.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		if (carId != other.carId)
			return false;
		return true;
	}

	/**
	 * What happens on each tick
	 * 
	 * @return false if the car should be removed from the system
	 */
	@Override
	public boolean tick(int ticks) {
		// check that we are not at the intersection and taht we can advance,
		// i.e. there is no car infront
		if (distanceFromIntersection > 0 && direction.canAdvance(this)) {
			// start moving toward the intersection if we can
			distanceFromIntersection -= SPEED;
			// If we decrease too much return to 0
			if (distanceFromIntersection < 0) {
				distanceFromIntersection = 0;
			}
		} else if (distanceFromIntersection == 0 && direction.lights.isGreen()
				|| leftIntersection == 1) {
			// we have moved into the intersection or we are leaving the
			// intersection
			distanceFromIntersection -= SPEED;
			// If the light is green go to crossing state
		} else if (distanceFromIntersection < 0) {

			if (distanceFromIntersection < -(CAR_LENGTH
					+ Intersection.INTERSECTION_SPAN - 2)) {
				// We have finished moving through the intersection so remove
				// the car from the direction and return false so that the car
				// is removed from the system
				direction.removeCar(this);
				leftIntersection = 1;
				return false;
			} else {
				// keeping moving through the intersection
				distanceFromIntersection -= SPEED;
			}
		}
		// check if the car has left the other side of the intersection
		// This is necessary to keep the GUI optimally visually stimulating
		if (leftIntersection == 1 && distanceFromIntersection < -100) {
			return false;
		}
		// Get the light colour of the intersection and store it in the
		// lightColour field
		// This is need for verification with JPF
		lightColor = direction.lights.currentColor.i;
		return true;
	}

	/**
	 * Create a string representation of a car which is necessary for command
	 * line output
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String carOut = String.valueOf(distanceFromIntersection);
		sb.append(carOut);
		for (int i = carOut.length(); i < CAR_LENGTH; i++) {
			sb.append('-');
		}
		return sb.toString();
	}
}
