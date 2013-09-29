public class VTLCar extends Car {

	private boolean isLeader = false;

	public VTLCar(int timeToArrive, Intersection intersection, Road r) {
		super(timeToArrive, intersection, r);
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
		return String.format("%s%s", this.getState().toString().subSequence(0, 1), leaderString);
	}

	@Override
	public void activeTick(int ticks) {
		if (ticks == approachAt) {
			Intersection.Mode m = intersection.V2IApproach(this, road);
		}


	}
}
