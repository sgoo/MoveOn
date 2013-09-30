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

	private static int CAR_COUNT = 0;

	private int carId;

	protected int distanceFromIntersection;
	protected Direction direction;

	public Car(int dist, Direction direction) {
		this.distanceFromIntersection = dist;
		this.direction = direction;
		carId = CAR_COUNT++;
	}

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

	@Override
	public boolean tick(int ticks) {
		// TODO: Check if there is a car in front
		if (distanceFromIntersection > 0 && direction.canAdvance(this)) {
			distanceFromIntersection -= SPEED;
			if (distanceFromIntersection < 0) {
				distanceFromIntersection = 0;
			}
		} else if (distanceFromIntersection == 0 && direction.lights.isGreen()) {
			distanceFromIntersection -= SPEED;
			// If the light is green go to Xing state
		} else if (distanceFromIntersection < 0) {
			if (distanceFromIntersection < -(CAR_LENGTH
					+ Intersection.INTERSECTION_SPAN - 2)) {
				direction.removeCar(this);
				return false;
			} else {
				distanceFromIntersection -= SPEED;
			}

		}

		return true;
	}

	@Override
	public String toString() {
		return String.format("%s %d", direction.toString(),
				distanceFromIntersection);
	}

}
