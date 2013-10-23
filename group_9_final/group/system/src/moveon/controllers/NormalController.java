package moveon.controllers;

import gov.nasa.jpf.annotation.Invariant;
import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.Intersection.Mode;
import moveon.simulation.Lights.Color;

/**
 * Controller used when only non vtl cars are present
 * 
 * @author Jourdan Harvey, Scott Goodhew, Mike Little, Roy Lin
 */

//Expansion of implies to a form the aprop will understand:
// !N.Red -> E.Red
// !(!N.Red) || (!N.Red && E.Red)

// !E.Red -> N.Red
// (!E.Red && N.Red) || !(!E.Red)
@Invariant({ "(running == 0) || ((NColor == 2) || (NColor != 2 && EColor == 2))" })
public class NormalController extends Controller {

	int NColor, EColor;
	int running = 0;
	private int nextChangeCounter = 0;

	/* (non-Javadoc)
	 * @see moveon.controllers.Controller#init(int)
	 */
	@Override
	public void init(int currentTick) {
		
		if (Direction.N.lights.currentColor == Direction.W.lights.currentColor) {
			//If we are on an even tick and the lights are the same colour (i.e we came from VTL-Plus Mode)
			//select North-South to be green
			if (currentTick % 2 == 0) {
				nsGreenEWRed();
			} else {
				nsRedEWGreen();
			}
		//Confirm the state that we want to be in
		} else if (Direction.N.lights.isGreen()) {
			nsGreenEWRed();
		} else {
			nsRedEWGreen();
		}

		// for testing with aprop
		NColor = Direction.N.lights.currentColor.i;
		EColor = Direction.E.lights.currentColor.i;
		running = 1;
		
		//Figure out when we should next change the lights
		nextChangeCounter = currentTick + Intersection.GREEN_TIME + Intersection.ORANGE_TIME;
		
		//Tell pedestrians that they can cross
		if (Direction.N.lights.currentColor == Color.G) {
			Direction.N.startCrossing(currentTick);
			Direction.S.startCrossing(currentTick);
		} else if (Direction.E.lights.currentColor == Color.G) {
			Direction.E.startCrossing(currentTick);
			Direction.W.startCrossing(currentTick);
		}
	}

	/* (non-Javadoc)
	 * @see moveon.controllers.Controller#tick(int)
	 */
	@Override
	public Mode tick(int ticks) {
		//Figure out if a phase has completed
		if (nextChangeCounter <= ticks) {
			//swap lights
			if (Direction.N.lights.isGreen()) {
				nsRedEWGreen();
			} else {
				nsGreenEWRed();
			}
			//calculate next phase change tick
			nextChangeCounter = ticks + Intersection.GREEN_TIME + Intersection.ORANGE_TIME;
		}
		//Figure out of pedestrians can cross
		if (progressLights(ticks)) {
			if (Direction.N.lights.currentColor == Color.G) {
				Direction.N.startCrossing(ticks);
				Direction.S.startCrossing(ticks);
			} else if (Direction.E.lights.currentColor == Color.G) {
				Direction.E.startCrossing(ticks);
				Direction.W.startCrossing(ticks);
			}

		}

		// for testing
		NColor = Direction.N.lights.currentColor.i;
		EColor = Direction.E.lights.currentColor.i;
		return null;
	}

	@Override
	public String toString() {
		return "";
	}

}
