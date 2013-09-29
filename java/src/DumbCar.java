public class DumbCar extends Car {
	int approachAt;

	public DumbCar(int timeToArrive, Intersection intersection, SimController.Direction d) {
		super(timeToArrive, intersection, d);
	}

	@Override
	public void activeTick(int ticks) {
		if (ticks == approachAt) {
			intersection.approachDetector(this, direction);
		}else{
			
		}
		
		

	}

}
