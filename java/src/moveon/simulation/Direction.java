package moveon.simulation;

import java.util.ArrayList;

import moveon.cars.Car;
import moveon.cars.VTLCar;

public enum Direction {

	N, S, E, W;

	static {
		N.lights = S.lights = new Lights(Lights.Color.G);
		E.lights = W.lights = new Lights(Lights.Color.G);
	}

	public Lights lights;

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

		// TODO print cars that are crossing
		int carsPrinted = 0;
		Car car;
		int soFar = 0;

		for (; cars.size() > carsPrinted && (car = cars.get(carsPrinted)).distanceFromIntersection < 0; carsPrinted++) {
			String carOut = car.getCarId() + "-" + (-car.distanceFromIntersection);
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

	public boolean canAdvance(Car car) {
		int index = cars.indexOf(car);
		index--;
		if (index < 0) {
			return true;
		}
		Car carInFront = cars.get(index);
		return carInFront.distanceFromIntersection + Car.CAR_LENGTH < car.distanceFromIntersection;
	}

	public VTLCar getClosestVTLCar() {
		for (Car car : cars) {
			if (car instanceof VTLCar) {
				return (VTLCar) car;
			}
		}
		return null;
	}
}
