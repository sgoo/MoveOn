import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		Controller controller = new Controller();
		
		Scanner in = new Scanner(new File("input.txt"));
		while(in.hasNext()) {
			int tick = in.nextInt();
			boolean VTLEnabled = in.next().equals("vtl");
			String direction = in.next();
			controller.addCar(tick, VTLEnabled, direction);
		}
		
	}
}
