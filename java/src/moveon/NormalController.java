package moveon;

public class NormalController implements Controller {
	private int nextChangeCounter = 0;
	private Direction[] green;
	private Direction[] red;

	@Override
	public void init(int currentTick) {

		if (Direction.N.lights.currentColor == Direction.W.lights.currentColor) {
			if (currentTick % 2 == 0) {
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

		nextChangeCounter = currentTick + 30;
	}

	@Override
	public boolean tick(int ticks) {
		if (nextChangeCounter == ticks) {
			if (green[0].lights.isGreen()) {
				green[0].lights.switchColor();

				nextChangeCounter = ticks + Intersection.ORANGE_TIME;
			} else {
				green[1].lights.switchColor();
				red[1].lights.switchColor();

				Direction[] tmp = green;
				green = red;
				red = tmp;
				nextChangeCounter = ticks + Intersection.GREEN_TIME;
			}
		}
		return true;
	}

}
