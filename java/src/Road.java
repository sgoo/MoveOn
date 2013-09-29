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
	 * ONE == NORTH||EAST TWO == SOUTH||WEST
	 */
	public enum Side {
		ONE, TWO
	}

	private Queue<Car> carsWaiting[] = new Queue[2];
	private AtomicBoolean pedestrianWaiting = new AtomicBoolean(false);
	private String name;

	public Road(String name) {
		this.carsWaiting[0] = new ConcurrentLinkedQueue<Car>();
		this.carsWaiting[1] = new ConcurrentLinkedQueue<Car>();
		this.name = name;
	}

	public void addCar(Car car, Side side) {
		switch (side) {
		case ONE:
			this.carsWaiting[0].add(car);
			break;
		case TWO:
			this.carsWaiting[1].add(car);
			break;
		}
	}

	public void addPedestrian() {
		this.pedestrianWaiting.set(true);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Road ");
		sb.append(name);
		sb.append("\n");
		for (Queue<Car> queue : carsWaiting) {
			sb.append("\t|");
			for (Car car : queue) {
				sb.append(car.toString());
				sb.append("|");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
