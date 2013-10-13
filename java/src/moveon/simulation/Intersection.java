package moveon.simulation;

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
public class Intersection implements Tickable {

	public final static int VTL_SPAN = 100;
	public final static int INTERSECTION_SPAN = 1;
	public final static int ORANGE_TIME = Car.CAR_LENGTH + INTERSECTION_SPAN;
	public final static int GREEN_TIME = 30;

	/**
	 * Representation of which mode or state this intersection is in.
	 */
	public enum Mode {
		NORMAL(new NormalController()), VTLPLUS(new VTLPlusController()), MIXED(new MixedController());
		private final Controller c;

		Mode(Controller c) {
			this.c = c;
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
	}

	@Override
	public boolean tick(int ticks) {
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
