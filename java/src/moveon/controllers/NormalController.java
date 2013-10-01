package moveon.controllers;

import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.Intersection.Mode;
import moveon.simulation.Lights;

public class NormalController extends Controller {
	private int nextChangeCounter = 0;

	@Override
	public void init(int currentTick) {

		if (Direction.N.lights.currentColor == Direction.W.lights.currentColor) {
			if (currentTick % 2 == 0) {
				nsGreenEWRed();
			} else {
				nsRedEWGreen();
			}
		} else if (Direction.N.lights.isGreen()) {
			nsGreenEWRed();
		} else {
			nsRedEWGreen();
		}

		nextChangeCounter = currentTick + Intersection.GREEN_TIME + Intersection.ORANGE_TIME;
	}

	@Override
	public Mode tick(int ticks) {
		if (nextChangeCounter == ticks) {
			if (Direction.N.lights.isGreen()) {
				nsRedEWGreen();
			} else {
				nsGreenEWRed();
			}
			nextChangeCounter = ticks + Intersection.GREEN_TIME + Intersection.ORANGE_TIME;
		}
		progressLights(ticks);
		return null;
	}

}
