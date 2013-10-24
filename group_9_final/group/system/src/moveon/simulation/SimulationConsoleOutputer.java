package moveon.simulation;

/**
 * Output for the simulator
 * @author Jourdan Harvey
 *
 */
public class SimulationConsoleOutputer implements SimulationListener {

	/**
	 * Print the given string to the console
	 */
	@Override
	public void simulationUpdated(String simulationState) {
		System.out.print(simulationState);
	}
}
