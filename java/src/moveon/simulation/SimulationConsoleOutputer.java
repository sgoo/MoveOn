package moveon.simulation;

public class SimulationConsoleOutputer implements SimulationListener {

	@Override
	public void simulationUpdated(String simulationState) {
		System.out.print(simulationState);
	}

}
