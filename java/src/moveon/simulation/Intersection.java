package moveon.simulation;

import gov.nasa.jpf.annotation.Invariant;
import moveon.cars.Car;
import moveon.controllers.Controller;
import moveon.controllers.MixedController;
import moveon.controllers.NormalController;
import moveon.controllers.VTLPlusController;

/**
 * Represents the intersection in the model
 * 
 * @author Jourdan, Scott, Roy, Mike
 * 
 */
@Invariant({ "(currentMode == 0 && hasNonVTLSum == 2) || " + // Normal has non-VTL in both directions
		"(currentMode == 1 && hasNonVTLSum == 0) || " + // VTL as no non-VTL cars
		"(currentMode == 2 && hasNonVTLSum == 1)" // MIXED has one direction with non-VTL 
})
public class Intersection implements Tickable {

	public final static int VTL_SPAN = 32;
	public final static int INTERSECTION_SPAN = 8;
	public final static int ORANGE_TIME = Car.CAR_LENGTH + INTERSECTION_SPAN;
	public final static int GREEN_TIME = 30;

	// for JPF
	int currentMode;
	int hasNSVTLCars = 0;
	int hasEWVTLCars = 0;
	int hasNSNonVTLCars = 0;
	int hasEWNonVTLCars = 0;
	int hasNonVTLSum = 0;

	/**
	 * Representation of which mode or state this intersection is in.
	 */
	public enum Mode {
		NORMAL(new NormalController(), 0), VTLPLUS(new VTLPlusController(), 1), MIXED(new MixedController(), 2);
		private final Controller c;
		private int i;

		Mode(Controller c, int i) {
			this.c = c;
			this.i = i;
		}

		@Override
		public String toString() {
			return super.toString() + c.toString();
		}

	}

	public Mode mode;

	public Intersection() {
		setMode(Mode.VTLPLUS, 0);
	}

	/**
	 * Change the mode of this intersection
	 * 
	 * @param m
	 * @param tick
	 */
	public void setMode(Mode m, int tick) {
		if (this.mode != m) {
			this.mode = m;
			mode.c.init(tick);
		}
		currentMode = mode.i;
	}

	@Override
	public boolean tick(int ticks) {

		hasNSNonVTLCars = (Direction.N.hasNonVTLCars() || Direction.S.hasNonVTLCars()) ? 1 : 0;
		hasEWNonVTLCars = (Direction.E.hasNonVTLCars() || Direction.W.hasNonVTLCars()) ? 1 : 0;
		hasNSVTLCars = (Direction.N.hasVTLCars() || Direction.S.hasVTLCars()) ? 1 : 0;
		hasEWVTLCars = (Direction.E.hasVTLCars() || Direction.W.hasVTLCars()) ? 1 : 0;
		hasNonVTLSum = hasNSNonVTLCars + hasEWNonVTLCars;

		if ((Direction.N.hasNonVTLCars() || Direction.S.hasNonVTLCars()) && (Direction.W.hasNonVTLCars() || Direction.E.hasNonVTLCars())) {
			setMode(Mode.NORMAL, ticks);
		} else if (!(Direction.N.hasNonVTLCars() || Direction.S.hasNonVTLCars() || Direction.W.hasNonVTLCars() || Direction.E.hasNonVTLCars())) {
			setMode(Mode.VTLPLUS, ticks);
		} else {
			setMode(Mode.MIXED, ticks);
		}

		mode.c.tick(ticks);
		return true;
	}

}
