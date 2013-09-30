package MoveOn;

public interface Controller extends Tickable {
	public void init(int currentTick, Direction[] green, Direction[] red);
}
