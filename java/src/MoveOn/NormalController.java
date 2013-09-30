package MoveOn;

public class NormalController implements Controller {
	private int nextChangeCounter = 0;
	private Direction[] green;
	private Direction[] red;

	@Override
	public void init(int currentTick, Direction[] green, Direction[] red) {
		this.green = green;
		this.red = red;
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
