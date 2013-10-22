package moveon.simulation;

/**
 * Represents a set of traffic lights
 * 
 * @author Jourdan, Roy, Mike, Scott
 * 
 */
public class Lights {

	public Color currentColor;

	public Lights(Color start) {
		currentColor = start;
	}

	public enum Color {
		G(0), O(1), R(2);
		public int i;

		Color(int i) {
			this.i = i;
		}
	}

	// Cycle the colour
	public Color switchColor() {
		switch (currentColor) {
		case G:
			return currentColor = Color.O;
		case O:
			return currentColor = Color.R;
		default: // case R
			return currentColor = Color.G;
		}
	}

	@Override
	public String toString() {
		return currentColor.toString();
	}

	public boolean isGreen() {
		return currentColor == Color.G;
	}

}
