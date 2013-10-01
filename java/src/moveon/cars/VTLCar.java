package moveon.cars;

import moveon.simulation.Direction;

public class VTLCar extends Car {

	private boolean leader;

	public VTLCar(int dist, Direction direction) {
		super(dist, direction);
	}

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
		}
		return car;
	}
}
