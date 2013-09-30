package MoveOn;

import MoveOn.Lights.Color;

public class VTLPlusController implements Controller {

	private Color[] desiredState; // NS, EW

	public VTLPlusController() {
		desiredState = new Color[] { Color.G, Color.G };
	}

	@Override
	public boolean tick(int ticks) {
		VTLCar closestN = Direction.N.getClosestVTLCar();
		VTLCar closestS = Direction.S.getClosestVTLCar();
		VTLCar closestE = Direction.E.getClosestVTLCar();
		VTLCar closestW = Direction.W.getClosestVTLCar();

		if (closestN == null && closestS == null) {
			allGreen();
			return true;
		}
		if (closestE == null && closestW == null) {
			allGreen();
			return true;
		}

		if (closestN == null) {
			closestN = closestS;
		} else if (closestS != null
				&& closestS.getDistance() < closestN.getDistance()) {
			closestN = closestS;

		}

		if (closestE == null) {
			closestE = closestW;
		} else if (closestW != null
				&& closestW.getDistance() < closestE.getDistance()) {
			closestE = closestW;
		}

		if (Math.abs(closestN.getDistance() - closestE.getDistance()) > Car.CAR_LENGTH) {
			allGreen();
			return true;
		}

		if (closestN.getDistance() < closestE.getDistance()) {
			nsGreenEWRed();
			closestE.setLeader(true);
			closestN.setLeader(false);
			return true;
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
				return true;
			} else if (closestE.isLeader()) {
				nsGreenEWRed();
				return true;
			}
		} else {
			closestE.setLeader(false);
			closestN.setLeader(true);
			nsRedEWGreen();
			return true;
		}

		// Advance to desired state

		return true;
	}

	@Override
	public void init(int currentTick) {

	}

	private void nsGreenEWRed() {
		desiredState[0] = Color.G;
		desiredState[1] = Color.R;
	}

	private void nsRedEWGreen() {
		desiredState[0] = Color.R;
		desiredState[1] = Color.G;
	}

	private void allGreen() {
		desiredState[0] = Color.G;
		desiredState[1] = Color.G;
	}

	private void moveLights() {
		if (Direction.N.lights.currentColor != desiredState[0]) {
			switch(desiredState[0]){
			case O:
				
			}
		}
	}
}
