package moveon;

public class Intersection implements Tickable {
	public final static int VTL_SPAN = 100;
	public final static int INTERSECTION_SPAN = 1;
	public final static int ORANGE_TIME = 2;
	public final static int GREEN_TIME = 30;

	Controller normalController = new NormalController();

	public enum Mode {
		NORMAL(new NormalController()), VTLPLUS(new VTLPlusController()), MIXED(new MixedController());
		private final Controller c;

		Mode(Controller c) {
			this.c = c;

		}

	}

	private Mode mode;

	public Intersection() {
		// TODO: switch to VTLPLUS
		setMode(Mode.VTLPLUS, 0);
	}

	public void setMode(Mode m, int tick) {

		this.mode = m;
		mode.c.init(tick);
	}

	@Override
	public boolean tick(int ticks) {
		mode.c.tick(ticks);
		return true;
	}

}
