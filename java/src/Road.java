import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a stretch of road. Cars can come from two sides.
 * Implemented with two queues.
 * Each side also has a pedestrian crossing.
 * 
 * @author Roy
 *
 */
public class Road {
	
	public enum Side { ONE, TWO	}
	
	private Queue<Car> carsWaiting[] = new Queue[2];
	private AtomicBoolean pedestrianWaiting[] = new AtomicBoolean[2];
	
	public Road() {
		this.carsWaiting[0] = new ConcurrentLinkedQueue<Car>();
		this.carsWaiting[1] = new ConcurrentLinkedQueue<Car>();
	
		this.pedestrianWaiting[0] = new AtomicBoolean(false);
		this.pedestrianWaiting[1] = new AtomicBoolean(false);
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
	
	public void addPadestrian(Side side) {
		switch (side) {
			case ONE:
				this.pedestrianWaiting[0].set(true);
				break;
			case TWO:
				this.pedestrianWaiting[1].set(true);
				break;
		}
	}
}
