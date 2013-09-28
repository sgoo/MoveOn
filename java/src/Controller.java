public class Controller {
	
	/**
	 * Car Probabilities: 
	 * - index 0 = probability of 0 cars per tick,
	 * - index 1 = probability of 1 car per tick, etc ... 
	 * - Should add up to 1
	 */
	public static final float probabilityOfCarsPerTick[] = {0.4f, 0.3f, 0.2f, 0.1f}; 
	
	/**
	 * Pedestrians Probabilities
	 */
	public static final float probabilityOfPedestriansPerTick[] = {0.95f, 0.05f};
	
	public Controller () throws InterruptedException {
		
		// north-south road
		Road roadNS = new Road();
		
		// east-west road
		Road roadEW = new Road();
		
		
		for(int tick = 0; ; tick++) {
			System.out.println("Tick " + tick + ":");
			this.tick();
			Thread.sleep(200);
		}
	}
	
	/**
	 * Time is broken discretely
	 */
	public void tick() {
		
	}
	
	/**
	 * Produce cars and padestrians on the road
	 * 
	 * @param road
	 */
	public void produce(Road road) {
		
	}
}
