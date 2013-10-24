package moveon.simulation;

/**
 * Interface to be used by different forms of simulation output
 * @author Scott
 *
 */
public interface SimulationListener {
	public void simulationUpdated(String simulationState);
}
