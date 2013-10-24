package moveon.controllers;

import moveon.simulation.Direction;
import moveon.simulation.Intersection.Mode;

/**
 * Controller used when non vtl and vtl cars are present
 * 
 * @author Jourdan Harvey, Scott Goodhew, Mike Little, Roy Lin
 */

public class MixedController extends Controller {

	public MixedController() {
	}

	
	/* (non-Javadoc)
	 * @see moveon.controllers.Controller#tick(int)
	 */
	@Override
	public Mode tick(int ticks) {
		
		//If we have pedestrians or cars wating to come along North-South set the lights desired state to Green
		if (Direction.N.hasNonVTLCars() || Direction.S.hasNonVTLCars() || Direction.N.pedsWaiting || Direction.S.pedsWaiting) {
			nsGreenEWRed();
		} else {
			//Else make East-West Green
			nsRedEWGreen();
		}
		
		//progress the lights to desired state
		progressLights(ticks);
		
		return null;
	}

	/* (non-Javadoc)
	 * @see moveon.controllers.Controller#init(int)
	 */
	@Override
	public void init(int currentTick) {
		
	}
}
