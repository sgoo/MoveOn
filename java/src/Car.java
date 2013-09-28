public abstract class Car {
	
	public enum State {
		NONE,
		APPROACHING, // detected by the loop
		CROSSING, // crossing in the intersection
		LEAVING // finished crossing
	}

	State state = State.NONE; 
	
	public Car () {
		
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
