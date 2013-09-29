import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws Exception {

		SimController controller = new SimController();

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		// (direction) <space> (is VTL) <space> (time to arrive)
		String line;
		while ((line = in.readLine()) != null) {
			char dir = line.charAt(0);
			SimController.Direction direction = SimController.Direction.get(dir);
			boolean isVTL = line.charAt(2) == 't';
			int timeToArrive = Integer.parseInt(line.substring(4));

			//			Road.Side side = direction.toString().charAt(0) == dir ? Road.Side.ONE : Road.Side.TWO;

			controller.addCar(isVTL, timeToArrive, direction);

		}
		controller.run();

	}
}
