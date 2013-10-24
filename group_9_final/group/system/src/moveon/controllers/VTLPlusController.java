package moveon.controllers;

import moveon.cars.Car;
import moveon.cars.VTLCar;
import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.Intersection.Mode;

/**
 * Controller used when only VTL cars are present
 * 
 * @author Jourdan Harvey, Scott Goodhew, Mike Little, Roy Lin
 * 
 */
public class VTLPlusController extends Controller {

	public static final int MAX_LEADER_TIME = 40;

	int lastLeaderChange = 0;
	VTLCar bannedFromLeader = null;

	public VTLPlusController() {
	}

	/* (non-Javadoc)
	 * @see moveon.controllers.Controller#tick(int)
	 */
	@Override
	public Mode tick(int ticks) {
		
		//Get the closest VTL car in each direction
		VTLCar closestN = Direction.N.getClosestVTLCar();
		VTLCar closestS = Direction.S.getClosestVTLCar();
		VTLCar closestE = Direction.E.getClosestVTLCar();
		VTLCar closestW = Direction.W.getClosestVTLCar();
		
		//If either of the directions is empty switch all lights to green
		if ((closestN == null && closestS == null) || (closestE == null && closestW == null)) {
			allGreen();
		} else {
			
			//Determine which direction has the closest car for each of North-South and East-West
			if (closestN == null) {
				closestN = closestS;
			} else if (closestS != null && closestS.getDistance() < closestN.getDistance()) {
				closestN = closestS;
			}

			if (closestE == null) {
				closestE = closestW;
			} else if (closestW != null && closestW.getDistance() < closestE.getDistance()) {
				closestE = closestW;
			}
			//Calculate whether the cars would collide
			if (Math.abs(closestN.getDistance() - closestE.getDistance()) > (Car.CAR_LENGTH + Intersection.INTERSECTION_SPAN)) {
				allGreen();
			} else {
				//If the closest North-South car is leader
				if (closestN.isLeader()) {
					//If we've been leader for too long tell the other direction to 
					if (lastLeaderChange + MAX_LEADER_TIME < ticks) {
						// need to switch leader
						nsGreenEWRed();
						
						//Reset the timer for leader changing
						lastLeaderChange = ticks;
						
						//Find out which direction of East, West is closest.
						VTLCar c1 = Direction.E.getClosestVTLCar2();
						VTLCar c2 = Direction.W.getClosestVTLCar2();
						if (c1 == null) {
							closestE = c2;
						} else if (c2 == null) {
							closestE = c1;
						} else if (c1.getDistance() < c2.getDistance()) {
							closestE = c1;
						} else {
							closestE = c2;
						}
						
						//Make NorthSouth car not leader
						if (closestE == null) { 
							closestN.setLeader(false);
						} else {
							//If there is a closest car in EastWest make it leader
							setLeader(closestE);
						}
					}
				} else if (closestE.isLeader()) {
					//Same logic as above except that the directions are swapped
					if (lastLeaderChange + MAX_LEADER_TIME < ticks) {
						// need to switch leader
						nsRedEWGreen();
						lastLeaderChange = ticks;

						VTLCar c1 = Direction.N.getClosestVTLCar2();
						VTLCar c2 = Direction.S.getClosestVTLCar2();
						if (c1 == null) {
							closestN = c2;
						} else if (c2 == null) {
							closestN = c1;
						} else if (c1.getDistance() < c2.getDistance()) {
							closestN = c1;
						} else {
							closestN = c2;
						}
						if (closestN == null) {
							closestE.setLeader(false);
						} else {
							setLeader(closestN);
						}
					}
				} else if ((bannedFromLeader == closestN && closestE.isLeader()) || (bannedFromLeader == closestE && closestN.isLeader())) {
					// do nothing
				} else if (closestN.getDistance() < closestE.getDistance()) {
					//If there isnt a leader make the closest NorthSouth car leader if its closest
					nsGreenEWRed();
					lastLeaderChange = ticks;
					setLeader(closestE);
				} else if (closestE.getDistance() < closestN.getDistance()) {
					//If there isnt a leader make the closest EastWest car leader if its closest
					setLeader(closestN);
					nsRedEWGreen();
					lastLeaderChange = ticks;
				} else if (closestN.getDistance() == closestE.getDistance()) {
					//If they're equidistant from the intersections, randomly select one of the to be leader.
					if (!closestN.isLeader() && !closestE.isLeader()) {
						if (ticks % 2 == 0) {
							lastLeaderChange = ticks;
							setLeader(closestN);
						} else {
							lastLeaderChange = ticks;
							setLeader(closestE);
						}
					}
					//Set the lights appropriately
					if (closestN.isLeader()) {
						nsRedEWGreen();
					} else if (closestE.isLeader()) {
						nsGreenEWRed();
					}
				} else {
				}
			}
		}

		// Advance to desired state
		progressLights(ticks);

		return null;
	}

	private VTLCar leader = null;

	
	
	/**
	 * Set the VTL car c to be leader
	 * 
	 * @param c the VTL car
	 */
	public void setLeader(VTLCar c) {
		System.out.println("Setting leader");
		//wat?
		try {
			throw new Exception();
		} catch (Exception e) {
			System.out.println(e.getStackTrace()[1]);
		}
		//Make sure that we make the current leader not the leader any more and prevent it from becomming leader again.
		if (leader != null) {
			leader.setLeader(false);
			bannedFromLeader = leader;
		}
		leader = c;
		leader.setLeader(true);
	}

	/* (non-Javadoc)
	 * @see moveon.controllers.Controller#init(int)
	 */
	@Override
	public void init(int currentTick) {
		
	}

	/* (non-Javadoc)
	 * @see moveon.controllers.Controller#stop(int)
	 */
	@Override
	public void stop(int currentTick) {
		//Unelect the leader
		if (leader != null) {
			leader.setLeader(false);
		}
		leader = null;
	}

	@Override
	public String toString() {
		return " " + lastLeaderChange;
	}
}
