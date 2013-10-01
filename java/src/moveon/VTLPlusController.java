package moveon;

import moveon.Lights.Color;

public class VTLPlusController implements Controller {

	private Color[] desiredState; // NS, EW
	private int lastLightChange = 0;

	public VTLPlusController() {
		desiredState = new Color[] { Color.G, Color.G };
	}

	@Override
	public boolean tick(int ticks) {
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

	private void progressLights(int tickNumber) {
		if (Direction.N.lights.currentColor != desiredState[0]) {
			switch (Direction.N.lights.currentColor) {
			case R:
			case G:
				Direction.N.lights.switchColor();
				lastLightChange = tickNumber;
				break;
			case O:
				if (tickNumber - lastLightChange > Intersection.ORANGE_TIME) {
					Direction.N.lights.switchColor();
				}
				break;
			}
		}

		if (Direction.E.lights.currentColor != desiredState[1]) {
			switch (Direction.E.lights.currentColor) {
			case R:
			case G:
				Direction.E.lights.switchColor();
				lastLightChange = tickNumber;
				break;
			case O:
				if (tickNumber - lastLightChange > Intersection.ORANGE_TIME) {
					Direction.E.lights.switchColor();
				}
				break;
			}
		}

	}
}
