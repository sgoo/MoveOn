package moveon.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import moveon.simulation.Simulator;

public class GuiController extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String CAR_TYPE_LBL = "";

	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		GuiController guiController = new GuiController(simulator);
		simulator.initialize();
		simulator.simulate();
	}

	
	private Simulator simulator;
	private boolean isVtlCar = false;
	
	public GuiController(Simulator simulator) {
		super("VTL Interface");
		this.simulator = simulator;
		
		// add main canvas (nice gui)
		JPanel gui = new NiceGui(simulator);
		gui.setPreferredSize(new Dimension(576, 576));
		this.add(gui, BorderLayout.CENTER);
		
		
		// add key input
		JPanel keyInput = new KeyInput(this.simulator);
		this.add(keyInput, BorderLayout.PAGE_END);
		
		
		// sizes the window correctly
		pack();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(250, 80));
		setVisible(true);
		setLocation(50, 50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}



}
