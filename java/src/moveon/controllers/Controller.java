package moveon.controllers;

import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.Intersection.Mode;
import moveon.simulation.Lights.Color;

public abstract class Controller {

	private Color[] desiredState; // NS, EW
	private int lastLightChange = 0;

	public abstract void init(int currentTick);

	public abstract Mode tick(int currentTick);

	public Controller() {
		desiredState = new Color[] { Color.G, Color.G };
	}

	/**
	 * Set the desired state
	 */
	protected void nsGreenEWRed() {
		desiredState[0] = Color.G;
		desiredState[1] = Color.R;
	}

	/**
	 * Set the desired state
	 */
	protected void nsRedEWGreen() {
		desiredState[0] = Color.R;
		desiredState[1] = Color.G;
	}

	/**
	 * Set all lights to green
	 */
	protected void allGreen() {
		desiredState[0] = Color.G;
		desiredState[1] = Color.G;
	}

	protected void progressLights(int tickNumber) {
		if (Direction.N.lights.currentColor != desiredState[0]) {
			switch (Direction.N.lights.currentColor) {
			case R:
				// changing to G-G is allowed in VTLPlus mode so we can shift
				// sometimes when the light isn't red
				// check if we should wait for the other light to shift into red
				// before changing
				if (desiredState[1] == Color.R
						&& Direction.E.lights.currentColor != Color.R) {
					break;
				}
			case G:
				Direction.N.lights.switchColor();
				lastLightChange = tickNumber;
				break;
			case O:
				// If the lights are orange continue to attempt to change them
				// to red
				if (tickNumber - lastLightChange > Intersection.ORANGE_TIME) {
					Direction.N.lights.switchColor();
				}
				break;
			}
		}

		if (Direction.E.lights.currentColor != desiredState[1]) {
			switch (Direction.E.lights.currentColor) {
			case R:
				// changing to G-G is allowed in VTLPlus mode so we can shift
				// sometimes when the light isn't red
				// check if we should wait for the other light to shift into red
				// before changing
				if (desiredState[0] == Color.R
						&& Direction.N.lights.currentColor != Color.R) {
					break;
				}
			case G:
				Direction.E.lights.switchColor();
				lastLightChange = tickNumber;
				break;
			case O:
				// If the lights are orange continue to attempt to change them
				// to red
				if (tickNumber - lastLightChange > Intersection.ORANGE_TIME) {
					Direction.E.lights.switchColor();
				}
				break;
			}
		}
	}
}
