package moveon.controllers;

import gov.nasa.jpf.annotation.Invariant;
import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.Intersection.Mode;

/**
 * Controller used when only non vtl cars are present
 * 
 * @author Jourdan Harvey, Scott Goodhew, Mike Little, Roy Lin
 */

//!N.Red -> E.Red
// (!N.Red && E.Red) || !(!N.Red)

//!E.Red -> N.Red 
//(!E.Red && N.Red) || !(!E.Red)
@Invariant({ "(running == 0) || ((NColor == 2) || (NColor != 2 && EColor == 2))" })
public class NormalController extends Controller {

	int NColor, EColor;
	int running = 0;
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

		// for testing
		NColor = Direction.N.lights.currentColor.i;
		EColor = Direction.E.lights.currentColor.i;
		running = 1;

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

		// for testing
		NColor = Direction.N.lights.currentColor.i;
		EColor = Direction.E.lights.currentColor.i;
		return null;
	}

	@Override
	public String toString() {
		return " " + nextChangeCounter;
	}

}
