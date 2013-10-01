package moveon.controllers;

import moveon.cars.Car;
import moveon.cars.VTLCar;
import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.Intersection.Mode;
import moveon.simulation.Lights;
import moveon.simulation.Lights.Color;

public class VTLPlusController extends Controller {

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
				closestE.setLeader(true);
				closestN.setLeader(false);
			} else if (closestN.getDistance() == closestE.getDistance()) {
				System.out.println("A");
				if (!closestN.isLeader() && !closestE.isLeader()) {
					if (ticks % 2 == 0) {
						closestN.setLeader(true);
					} else {
						closestE.setLeader(true);
					}
				}
				if (closestN.isLeader()) {
					nsRedEWGreen();
				} else if (closestE.isLeader()) {
					nsGreenEWRed();
				}
			} else {
				closestE.setLeader(false);
				closestN.setLeader(true);
				nsRedEWGreen();
			}
		}

		progressLights(ticks);
		// Advance to desired state

		return null;
	}

	@Override
	public void init(int currentTick) {

	}
}
