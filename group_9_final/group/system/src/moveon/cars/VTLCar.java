package moveon.cars;

import gov.nasa.jpf.annotation.Invariant;
import moveon.simulation.Direction;

/**
 * VTLCar
 * 
 * @author Jourdan Harvey, Scott Goodhew, Mike Little, Roy Lin
 * 
 */
@Invariant({ "beenLeader <= 30" })
public class VTLCar extends Car {

	private boolean leader;

	// used by JPF
	private int beenLeader = 0;

	public VTLCar(int dist, Direction direction) {
		super(dist, direction);
	}

	/**
	 * VTL Cars are aware of their distance from the intersection
	 * 
	 * @return
	 */
	public int getDistance() {
		return distanceFromIntersection;
	}

	/**
	 * Find out whether this car can be the leader
	 * 
	 * @return
	 */
	public boolean isLeader() {
		return leader;
	}

	/**
	 * Set this car to be the leader
	 * 
	 * @param leader
	 */
	public void setLeader(boolean leader) {
		this.leader = leader;
		beenLeader = 0;
	}

	/**
	 * What to do each tick In this case we just increase the ammount of time
	 * that this car has been leader, if it is leader.
	 */
	@Override
	public boolean tick(int ticks) {
		if (leader)
			beenLeader++;
		return super.tick(ticks);
	}

	/**
	 * Get a string representation to be used in command line output
	 */
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
