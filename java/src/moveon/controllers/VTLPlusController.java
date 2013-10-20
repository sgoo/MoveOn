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

	@Override
	public Mode tick(int ticks) {
		VTLCar closestN = Direction.N.getClosestVTLCar();
		VTLCar closestS = Direction.S.getClosestVTLCar();
		VTLCar closestE = Direction.E.getClosestVTLCar();
		VTLCar closestW = Direction.W.getClosestVTLCar();

		if ((closestN == null && closestS == null) || (closestE == null && closestW == null)) {
			allGreen();
		} else {

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

			if (Math.abs(closestN.getDistance() - closestE.getDistance()) > (Car.CAR_LENGTH + Intersection.INTERSECTION_SPAN)) {
				allGreen();
			} else {
				if (closestN.isLeader()) {
					if (lastLeaderChange + MAX_LEADER_TIME < ticks) {
						// need to switch leader
						nsGreenEWRed();
						lastLeaderChange = ticks;
						setLeader(closestE);
					}
				} else if (closestE.isLeader()) {
					if (lastLeaderChange + MAX_LEADER_TIME < ticks) {
						// need to switch leader
						nsRedEWGreen();
						lastLeaderChange = ticks;
						setLeader(closestN);
					}
				} else if ((bannedFromLeader == closestN && closestE.isLeader()) || (bannedFromLeader == closestE && closestN.isLeader())) {
					// do nothing
				} else if (closestN.getDistance() < closestE.getDistance()) {
					nsGreenEWRed();
					lastLeaderChange = ticks;
					setLeader(closestE);
				} else if (closestE.getDistance() < closestN.getDistance()) {
					setLeader(closestN);
					nsRedEWGreen();
					lastLeaderChange = ticks;
				} else if (closestN.getDistance() == closestE.getDistance()) {
					if (!closestN.isLeader() && !closestE.isLeader()) {
						if (ticks % 2 == 0) {
							lastLeaderChange = ticks;
							setLeader(closestN);
						} else {
							lastLeaderChange = ticks;
							setLeader(closestE);
						}
					}
					if (closestN.isLeader()) {
						nsRedEWGreen();
					} else if (closestE.isLeader()) {
						nsGreenEWRed();
					}
				} else {
				}
			}
		}

		progressLights(ticks);
		// Advance to desired state

		return null;
	}

	private VTLCar leader = null;

	public void setLeader(VTLCar c) {
		System.out.println("Setting leader");
		try {
			throw new Exception();
		} catch (Exception e) {
			System.out.println(e.getStackTrace()[1]);
		}
		if (leader != null) {
			leader.setLeader(false);
			bannedFromLeader = leader;
		}
		leader = c;
		leader.setLeader(true);
	}

	@Override
	public void init(int currentTick) {

	}

	@Override
	public void stop(int currentTick) {
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
