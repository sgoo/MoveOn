package moveon.controllers;

import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.Intersection.Mode;
import moveon.simulation.Lights.Color;

/**
 * 
 * @author michaellittle
 *
 * Controller abstract class, is the parent of the controllers for the different modes of our intersections.
 */
public abstract class Controller {

	private Color[] desiredState; // NS, EW
	private int lastLightChange = 0;

	/**
	 * Abstract method to initialize the controller with the current tick
	 * 
	 * @param currentTick
	 */
	public abstract void init(int currentTick);

	/**
	 * The method that progresses the controller.
	 * 
	 * @param currentTick
	 * @return The Mode that the intersection should change to.
	 */
	public abstract Mode tick(int currentTick);

	/**
	 * Default constructor
	 */
	public Controller() {
		desiredState = new Color[] { Color.G, Color.G };
	}

	/**
	 * Set the desired state so that North-South direction is Green and East-West direction is Red
	 */
	protected void nsGreenEWRed() {
		desiredState[0] = Color.G;
		desiredState[1] = Color.R;
	}

	/**
	 * Set the desired state so that North-South direction is Red and East-West direction is Green
	 */
	protected void nsRedEWGreen() {
		desiredState[0] = Color.R;
		desiredState[1] = Color.G;
	}

	/**
	 * Sets desired state to all lights green
	 */
	protected void allGreen() {
		desiredState[0] = Color.G;
		desiredState[1] = Color.G;
	}
	
	/**
	 * Progresses the light towards their desired state.
	 * 
	 * @param tickNumber the current tick
	 * @return A boolean value indicating that we have arrived at the desired state
	 */
	protected boolean progressLights(int tickNumber) {
		boolean output = false;
		if (Direction.N.lights.currentColor != desiredState[0]) {
			switch (Direction.N.lights.currentColor) {
			case R:
				// changing to G-G is allowed in VTLPlus mode so we can shift
				// sometimes when the light isn't red
				// check if we should wait for the other light to shift into red
				// before changing
				if (desiredState[1] == Color.R && Direction.E.lights.currentColor != Color.R) {
					break;
				}
			case G:
				//If the lights are green immediately switch colour and store the time of the last light change as now.
				Direction.N.lights.switchColor();
				lastLightChange = tickNumber;
				output = true;
				break;
			case O:
				// If the lights are orange continue to attempt to change them
				// to red
				if (tickNumber - lastLightChange > Intersection.ORANGE_TIME) {
					Direction.N.lights.switchColor();
					output = true;
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
				if (desiredState[0] == Color.R && Direction.N.lights.currentColor != Color.R) {
					break;
				}
			case G:
				//Immediately switch colour
				Direction.E.lights.switchColor();
				lastLightChange = tickNumber;
				output = true;
				break;
			case O:
				// If the lights are orange continue to attempt to change them
				// to red
				if (tickNumber - lastLightChange > Intersection.ORANGE_TIME) {
					Direction.E.lights.switchColor();
					output = true;
				}
				break;
			}
		}
		return output;
	}

	@Override
	public String toString() {
		return "";
	}
	
	/**
	 * Stops the controller
	 * @param currentTick
	 */
	public void stop(int currentTick) {
	}
}
