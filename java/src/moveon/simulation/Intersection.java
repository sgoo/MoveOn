package moveon.simulation;

import moveon.cars.Car;
import moveon.controllers.Controller;
import moveon.controllers.MixedController;
import moveon.controllers.NormalController;
import moveon.controllers.VTLPlusController;

public class Intersection implements Tickable {
	public final static int VTL_SPAN = 100;
	public final static int INTERSECTION_SPAN = 1;
	public final static int ORANGE_TIME = Car.CAR_LENGTH + INTERSECTION_SPAN;
	public final static int GREEN_TIME = 30;

	public enum Mode {
		NORMAL(new NormalController()), VTLPLUS(new VTLPlusController()), MIXED(
				new MixedController());
		private final Controller c;

		Mode(Controller c) {
			this.c = c;
		}

	}

	public Mode mode;

	public Intersection() {
		setMode(Mode.VTLPLUS, 0);
	}

	public void setMode(Mode m, int tick) {

		this.mode = m;
		mode.c.init(tick);
	}

	@Override
	public boolean tick(int ticks) {
		if ((Direction.N.hasNonVTLCars() || Direction.S.hasNonVTLCars())
				&& (Direction.W.hasNonVTLCars() || Direction.E.hasNonVTLCars())) {
			setMode(Mode.NORMAL, ticks);
		} else if (!(Direction.N.hasNonVTLCars() || Direction.S.hasNonVTLCars()
				|| Direction.W.hasNonVTLCars() || Direction.E.hasNonVTLCars())) {
			setMode(Mode.VTLPLUS, ticks);
		} else {
			setMode(Mode.MIXED, ticks);
		}

		mode.c.tick(ticks);
		return true;
	}

}
