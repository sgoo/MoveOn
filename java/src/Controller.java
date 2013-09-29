
public class Controller {
	
	private static final int tickDuration = 1000;
	
	/**
	 * Car Probabilities: 
	 * - index 0 = probability of 0 cars per tick,
	 * - index 1 = probability of 1 car per tick, etc ... 
	 * - Should add up to 1
	 */
	public static final double probabilityOfCarsPerTick[] = {0.4, 0.3, 0.2, 0.1}; 
	
	/**
	 * Pedestrians Probabilities
	 */
	public static final double probabilityOfPedestriansPerTick[] = {0.95, 0.05};
	
	/**
	 * Road Probabilities
	 */
	public static final double probabilityOfRoadPerTick[] = {0.5, 0.5};
	
	/**
	 * Road Probabilities
	 */
	public static final double probabilityCarTypePerTick[] = {1.0, 0};
	
	/**
	 * Road Probabilities
	 */
	public static final double probabilityOfSidePerTick[] = {0.5, 0.5};

	private Road roadNS;

	private Road roadEW;
	
	public Controller () throws InterruptedException {
		// north-south road
		roadNS = new Road("NS");
		
		// east-west road
		roadEW = new Road("EW");
		
		for(int tick = 0; ; tick++) {
			System.out.println("Tick " + tick + ":");
			this.tick();
			// tick ~~ 1s IRL
			Thread.sleep(tickDuration);
		}
	}
	
	/**
	 * Time is broken discretely
	 */
	public void tick() {
		produce();
//		pickLeaderIfNotExist();
//		changeLightsAsAppropriate();
//		verify();
		
	}
	
	/**
	 * Produce cars and pedestrians on the road
	 * 
	 * @param road
	 */
	public void produce() {
		// choose a whole lot of stuff
		// Road.
		Road selectedRoad = roadNS;
		
		if(Math.random()>probabilityOfRoadPerTick[0]){
			selectedRoad = roadEW;
		}
		
		// Cars
		double carProbability = Math.random();
		for(int i = 0;i<probabilityOfCarsPerTick.length;i++){
			carProbability-=probabilityOfCarsPerTick[i];
			if(carProbability<0){
				
				for (int j = 0; j < i; j++) {
					Road.Side choosenSide = Road.Side.ONE;
					if (Math.random() >= probabilityOfSidePerTick[0]) {
						choosenSide = Road.Side.TWO;
					}
					selectedRoad.addCar(new VTLCar(), choosenSide);
				}
				break;
			}
		}
		// TODO: Peds
		System.out.println(roadNS.toString());
		System.out.println(roadEW.toString());
	}
}
