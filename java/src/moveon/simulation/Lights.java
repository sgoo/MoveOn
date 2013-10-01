package moveon.simulation;

public class Lights {

	public Color currentColor;

	public Lights(Color start) {
		currentColor = start;

	}

	public enum Color {
		G, O, R
	}

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
