public class VTLCar extends Car {

	private boolean isLeader = false;

	public VTLCar() {
		isLeader = Controller.getInstance().requestLeadership(this);
	}

	public boolean isLeader() {
		return isLeader;
	}

	@Override
	public String toString() {
		String leaderString = " ";
		if (isLeader) {
			leaderString = "L";
		}
		return String.format("%s%s",
				this.getState().toString().subSequence(0, 1), leaderString);
	}
}
