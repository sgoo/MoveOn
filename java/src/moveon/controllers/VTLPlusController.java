package moveon.controllers;

import moveon.cars.Car;
import moveon.cars.VTLCar;
import moveon.simulation.Direction;
import moveon.simulation.Intersection.Mode;

/**
 * Controller used when only VTL cars are present
 * 
 * @author Jourdan Harvey, Scott Goodhew, Mike Little, Roy Lin
 * 
 */
public class VTLPlusController extends Controller {
	//TODO change lights aleast every 30 seconds

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

			if (Math.abs(closestN.getDistance() - closestE.getDistance()) > Car.CAR_LENGTH) {
				allGreen();
			} else if (closestN.getDistance() < closestE.getDistance()) {
				nsGreenEWRed();
				setLeader(closestE);
			} else if (closestN.getDistance() == closestE.getDistance()) {
				if (!closestN.isLeader() && !closestE.isLeader()) {
					if (ticks % 2 == 0) {
						setLeader(closestN);
					} else {
						setLeader(closestE);
					}
				}
				if (closestN.isLeader()) {
					nsRedEWGreen();
				} else if (closestE.isLeader()) {
					nsGreenEWRed();
				}
			} else {
				setLeader(closestN);
				nsRedEWGreen();
			}
		}

		progressLights(ticks);
		// Advance to desired state

		return null;
	}

	private VTLCar leader = null;

	public void setLeader(VTLCar c) {
		if (leader != null) {
			leader.setLeader(false);
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
}
