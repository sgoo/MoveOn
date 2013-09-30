package MoveOn;

public class Intersection implements Tickable {
	public final static int VTL_SPAN = 100;
	public final static int INTERSECTION_SPAN = 1;
	public final static int ORANGE_TIME = 2;
	public final static int GREEN_TIME = 30;

	Controller normalController = new NormalController();

	public enum Mode {
		NORMAL(new NormalController()), VTLPLUS(new VTLPlusController()), MIXED(
				new MixedController());
		private final Controller c;

		Mode(Controller c) {
			this.c = c;

		}

	}

	private Mode mode;

	public Intersection() {
		// TODO: switch to VTLPLUS
		setMode(Mode.NORMAL, 0);
	}

	public void setMode(Mode m, int tick) {

		Direction[] green;
		Direction[] red;

		if (Direction.N.lights.currentColor == Direction.W.lights.currentColor) {
			if (tick % 2 == 0) {
				Direction.N.lights.currentColor = Lights.Color.G;
				green = new Direction[] { Direction.N, Direction.S };
				Direction.E.lights.currentColor = Lights.Color.R;
				red = new Direction[] { Direction.E, Direction.W };
			} else {
				Direction.N.lights.currentColor = Lights.Color.R;
				red = new Direction[] { Direction.N, Direction.S };
				Direction.E.lights.currentColor = Lights.Color.G;
				green = new Direction[] { Direction.E, Direction.W };
			}
		} else if (Direction.N.lights.isGreen()) {
			green = new Direction[] { Direction.N, Direction.S };
			red = new Direction[] { Direction.E, Direction.W };
		} else {
			red = new Direction[] { Direction.N, Direction.S };
			green = new Direction[] { Direction.E, Direction.W };
		}

		this.mode = m;
		mode.c.init(tick, green, red);
	}

	@Override
	public boolean tick(int ticks) {
		mode.c.tick(ticks);
		return true;
	}

}
