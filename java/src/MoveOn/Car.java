package MoveOn;

public class Car implements Tickable {

	/**
	 * Car length Unit: m
	 */
	public static final int CAR_LENGTH = 5;

	/**
	 * Car speed Unit: m/s
	 */
	public static final int SPEED = 1;

	protected int distanceFromIntersection;
	protected Direction direction;

	public Car(int dist, Direction direction) {
		this.distanceFromIntersection = dist;
		this.direction = direction;
	}

	@Override
	public void tick(int ticks) {
		// TODO: Check if there is a car in front
		if (distanceFromIntersection > 0) {
			distanceFromIntersection -= SPEED;
			if (distanceFromIntersection < 0){
				distanceFromIntersection = 0;
			}
		} else {
			// If the light is green go to Xing state
		}
	}
	@Override
	public String toString() {
		return String.format("%s %d", direction.toString(), distanceFromIntersection);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + distanceFromIntersection;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		if (direction != other.direction)
			return false;
		if (distanceFromIntersection != other.distanceFromIntersection)
			return false;
		return true;
	}
	
	
}
