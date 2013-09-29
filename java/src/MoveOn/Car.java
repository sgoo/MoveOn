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

	public int distanceFromIntersection;
	public Direction direction;

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
}
