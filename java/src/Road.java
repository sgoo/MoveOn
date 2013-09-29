import java.util.LinkedList;
import java.util.Queue;


public class Road {
	private String name;
	private Road opposite;
	
	private Car[] carSpaces = new Car[5];
	private Queue<Car> carsApproaching = new LinkedList<Car>();
	
	public Road(String name) {
		this.name = name;
	}

	public Road getOpposite() {
		return opposite;
	}

	public void setOpposite(Road opposite) {
		this.opposite = opposite;
		this.opposite.opposite = this;
	}
	
	
	
	
	public String toString() {
		return "";
	}
}
