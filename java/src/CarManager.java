import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CarManager {

	/**
	 * Integer represents tick at which the car approaches
	 */
	private Map<Integer, LinkedList<Car>> carsToAddMap = new HashMap<Integer, LinkedList<Car>>();

	private Map<String, Road> roads = new HashMap<String, Road>();

	public CarManager(Map<String, Road> roads) {
		this.roads = roads;
	}

	

//		carCount++;
//		if (VTLenabled) {
//			return new CarVTL("VTLCar " + (carCount - 1));
//		} else {
//			return new CarNormal("DumbCar " + (carCount - 1));
//		}


	private static int carCount = 0;
	public void addCar(int tick, boolean VTLEnabled, String direction) {

		Car newCar;
		if (VTLEnabled) {
			newCar = new CarVTL(""+carCount, direction);
		} else {
			newCar = new CarNormal(""+carCount, direction);
		}
		carCount++;
		
		if (!carsToAddMap.containsKey(tick)) {
			carsToAddMap.put(tick, new LinkedList<Car>());
		}
		carsToAddMap.get(tick).add(newCar);
	}
}
