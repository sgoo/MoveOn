package moveon.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import moveon.simulation.Simulator;

/**
 * The class to run to display the Visualisation of the Simulation.
 * 
 * @author rlin050
 */
public class GuiController extends JFrame {

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Screen size of visualisation in pixels
	 */
	private static final int SIMULATION_SCREEN_SIZE = 576;
	
	/**
	 * The simulator for the visualisation
	 */
	private Simulator simulator;

	/**
	 * The main class to be executed
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		new GuiController(simulator);
		simulator.initialize();
		simulator.simulate();
	}

	/**
	 * Constructor
	 * 
	 * @param simulator
	 */
	public GuiController(Simulator simulator) {
		
		// Title for the JFrame
		super("VTL Interface");
		
		this.simulator = simulator;

		// Main visualisation interface
		JPanel gui = new NiceGui(simulator);
		gui.setPreferredSize(new Dimension(SIMULATION_SCREEN_SIZE, SIMULATION_SCREEN_SIZE));
		this.add(gui, BorderLayout.CENTER);

		// Key input interface
		JPanel keyInput = new KeyInput(this.simulator);
		this.add(keyInput, BorderLayout.PAGE_END);

		// Adjusting the window
		pack();
		setLayout(new BorderLayout());
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
