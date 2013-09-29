package MoveOn;

import java.util.ArrayList;

public enum Direction {
	N, S, E, W;

	private ArrayList<Car> cars;

	private Direction() {
		cars = new ArrayList<Car>();
	}

	public void addCar(Car car) {
		cars.add(car);
	}

	public void removeCar(Car car) {
		cars.remove(car);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		int soFar = 0;
		for (Car car : cars) {
			for (; soFar < car.distanceFromIntersection; soFar++) {
				sb.append(' ');
			}
			soFar += Car.CAR_LENGTH;
			String id = car.getCarId() + "";
			sb.append(id);
			for (int i = id.length(); i < Car.CAR_LENGTH; i++) {
				sb.append('-');
			}
		}
		for (; soFar < Intersection.VTL_SPAN; soFar++) {
			sb.append(' ');
		}
		
		sb.append('|');
		return sb.toString();
	}
}
