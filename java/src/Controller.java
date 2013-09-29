import java.util.HashMap;
import java.util.Map;


public class Controller {
	
	public enum Mode {
		NORMAL, MIXED, VTL
	}
	
	public Mode mode;
	
	public static final int SIMULATION_TIME = 200;
	
	private Map<String, Road> roads = new HashMap<String, Road>();
	private CarManager carManager = new CarManager(roads);

	
	public Controller() {
		
		roads.put("N", new Road("N"));
		roads.put("S", new Road("S"));
		roads.put("E", new Road("E"));
		roads.put("W", new Road("W"));

		roads.get("N").setOpposite(roads.get("S"));
		roads.get("E").setOpposite(roads.get("W"));
	}
	
	public void addCar(int tick, boolean VTLEnabled, String direction) {
		this.carManager.addCar(tick, VTLEnabled, direction);
	}
	
	public void runSimulation() {
		for(int tick = 0; tick < SIMULATION_TIME; tick++) {
			changeLights();
			moveCars();
			addApproachingCars();
		}
	}

	private void moveCars() {
		// TODO Auto-generated method stub
		
	}

	private void changeLights() {
		// TODO Auto-generated method stub
		
	}

	private void addApproachingCars() {
		
	}
	
	
}
