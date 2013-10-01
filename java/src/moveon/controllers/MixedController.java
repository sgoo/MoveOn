package moveon.controllers;

import moveon.simulation.Direction;
import moveon.simulation.Intersection.Mode;

public class MixedController extends Controller {

	public MixedController() {
	}

	@Override
	public Mode tick(int ticks) {
		
		if (Direction.N.hasNonVTLCars() || Direction.S.hasNonVTLCars()) {
			nsGreenEWRed();
		} else {
			nsRedEWGreen();
		}

		progressLights(ticks);
		return null;
	}

	@Override
	public void init(int currentTick) {

	}

}
