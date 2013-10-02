package moveon.cars;

import moveon.simulation.Direction;

/**
 * VTLCar
 * @author Jourdan Harvey, Scott Goodhew, Mike Little, Roy Lin
 *
 */
public class VTLCar extends Car {

	private boolean leader;
	
	public VTLCar(int dist, Direction direction) {
		super(dist, direction);
	}
	
	/**
	 * VTL Cars are aware of their distance from the intersection
	 * @return
	 */
	public int getDistance() {
		return distanceFromIntersection;
	}

	public boolean isLeader() {
		return leader;
	}

	public void setLeader(boolean leader) {
		this.leader = leader;
	}

	@Override
	public String toString() {
		String car = super.toString();
		if (leader) {
			return car.substring(0, 4) + "L";
		} else {
			return car.substring(0, 4) + "V";
		}
	}
}
