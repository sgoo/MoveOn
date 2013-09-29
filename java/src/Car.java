public abstract class Car {

	public enum State {
		SAFE, APPROACHING, CROSSING, LEAVING
	}

	private State state = State.SAFE;
	
	public Car() {

	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
