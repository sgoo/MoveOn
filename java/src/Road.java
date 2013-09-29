import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a stretch of road. Cars can come from two sides. Implemented with
 * two queues. Each side also has a pedestrian crossing.
 * 
 * @author Roy
 * 
 */
public class Road {

	/**
	 * ONE == NORTH||EAST TWO == SOUTH||WEST //
	 */
	//	public enum Side {
	//		ONE, TWO
	//	}
	//
	//	private Queue<Car> carsWaiting[] = new Queue[2];
	//	private String name;
	//
	//	public Road(String name) {
	//		this.name = name;
	//	}
	//
	//	public void addPedestrian() {
	//		this.pedestrianWaiting.set(true);
	//	}
	//
	//	@Override
	//	public String toString() {
	//		StringBuilder sb = new StringBuilder("Road ");
	//		sb.append(name);
	//		sb.append("\n");
	//		for (Queue<Car> queue : carsWaiting) {
	//			sb.append("\t|");
	//			for (Car car : queue) {
	//				sb.append(car.toString());
	//				sb.append("|");
	//			}
	//			sb.append("\n");
	//		}
	//		return sb.toString();
	//	}
	//
	//	public void addCar(Car car, Side side) {
	//		switch (side) {
	//		case ONE:
	//			this.carsWaiting[0].add(car);
	//			break;
	//		case TWO:
	//			this.carsWaiting[1].add(car);
	//			break;
	//		}
	//	}
}
