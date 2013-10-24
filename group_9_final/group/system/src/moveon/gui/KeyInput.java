package moveon.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.SimulationListener;
import moveon.simulation.Simulator;

/**
 * Enable the GUI to receive keyboard input which corresponds to the buttons in
 * the GUI
 * 
 * @author Scott Goodhew, Jourdan Harvey
 * 
 */
public class KeyInput extends JPanel implements ActionListener,
		KeyEventDispatcher, SimulationListener {

	private static final long serialVersionUID = 1L;
	private String generateTypeLabel = "NORMAL";

	private Simulator simulator;

	// Buttons, North, South, East, West respestively
	JButton NButton;
	JButton SButton;
	JButton EButton;
	JButton WButton;

	JButton carTypeButton;
	JButton pauseButton;
	JLabel currentMode;
	JLabel currentTick;

	private JCheckBox toggleRandom;
	private JCheckBox toggleVTLCars;
	private JCheckBox toggleNormalCars;
	private JCheckBox togglePedestrians;

	public KeyInput(Simulator simulator) {
		this.simulator = simulator;
		simulator.addSimListener(this);

		// set layout
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Left buttons, for introducing cars from the respective directions
		JPanel LeftPanel = new JPanel(new GridLayout(2, 3));

		NButton = new JButton("North");
		NButton.addActionListener(this);
		LeftPanel.add(new JPanel());
		LeftPanel.add(NButton);

		WButton = new JButton("West");
		WButton.addActionListener(this);
		LeftPanel.add(new JPanel());
		LeftPanel.add(WButton);

		SButton = new JButton("South");
		SButton.addActionListener(this);
		LeftPanel.add(SButton);

		EButton = new JButton("East");
		EButton.addActionListener(this);
		LeftPanel.add(EButton);

		// padding
		LeftPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		this.add(LeftPanel, BorderLayout.LINE_START);

		// Middle Buttons
		JPanel midPanel = new JPanel(new GridLayout(2, 1));

		// For pausing the GUI
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		midPanel.add(pauseButton);

		// For changing the type of car generated
		carTypeButton = new JButton(generateTypeLabel);
		carTypeButton.addActionListener(this);
		midPanel.add(carTypeButton);

		midPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(midPanel, BorderLayout.CENTER);

		// Right Checkboxes, for controlling the options of generating cars
		JPanel rightPanel = new JPanel(new GridLayout(3, 1));

		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		toggleRandom = new JCheckBox("Generate cars randomly", true);
		toggleVTLCars = new JCheckBox("Generate VTL cars", true);
		toggleNormalCars = new JCheckBox("Generate normal cars", true);
		togglePedestrians = new JCheckBox("Generate pedestrians", true);

		toggleRandom.addActionListener(this);
		toggleVTLCars.addActionListener(this);
		toggleNormalCars.addActionListener(this);
		togglePedestrians.addActionListener(this);
		rightPanel.add(toggleRandom);
		rightPanel.add(toggleVTLCars);
		rightPanel.add(toggleNormalCars);
		rightPanel.add(togglePedestrians);

		this.add(rightPanel, BorderLayout.LINE_END);

		JPanel bottomPanel = new JPanel();
		JLabel modeLabel = new JLabel("Current Mode: ");
		currentMode = new JLabel("VTL+,");

		bottomPanel.add(modeLabel);
		bottomPanel.add(currentMode);

		JLabel tickLabel = new JLabel("Current Second: ");
		currentTick = new JLabel("0");

		bottomPanel.add(tickLabel);
		bottomPanel.add(currentTick);

		this.add(bottomPanel, BorderLayout.PAGE_END);
		// handles key presses
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == NButton) {
			// add a car from the north
			addCar(Intersection.VTL_SPAN, Direction.N);
		} else if (e.getSource() == SButton) {
			// add a car from the south
			addCar(Intersection.VTL_SPAN, Direction.S);
		} else if (e.getSource() == EButton) {
			// add a car from the east
			addCar(Intersection.VTL_SPAN, Direction.E);
		} else if (e.getSource() == WButton) {
			// add a car from the west
			addCar(Intersection.VTL_SPAN, Direction.W);
		} else if (e.getSource() == pauseButton) {
			// pause the simulation and change the button text to unpause
			changePause();
		} else if (e.getSource() == carTypeButton) {
			// change the car type to be generated
			changeCarType();
		} else if (e.getSource() == toggleRandom) {
			// turn random generation on/off
			simulator.toggleRandom();
		} else if (e.getSource() == toggleNormalCars) {
			// turn on random generation of normal cars
			simulator.toggleRandomNormalCars();
		} else if (e.getSource() == toggleVTLCars) {
			// turn on random generation of VTL Cars
			simulator.toggleRandomVTLCars();
		} else if (e.getSource() == togglePedestrians) {
			// turn on random generation of pedestrians
			simulator.setGenerateRandomPeople(togglePedestrians.isSelected());
		}
	}

	/**
	 * Dispatch the relevant key events, ignore others
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() != KeyEvent.KEY_PRESSED) {
			return false;
		}
		Direction d = null;
		switch (Character.toUpperCase(e.getKeyChar())) {
		case 'W':
			d = Direction.N;
			break;
		case 'S':
			d = Direction.S;
			break;
		case 'D':
			d = Direction.E;
			break;
		case 'A':
			d = Direction.W;
			break;
		case 'Q':
			changeCarType();
			return true;
		case ' ':
			changePause();
			return true;
		}
		if (d == null) {
			return false;
		}

		carTypeButton.setText(Simulator.VTL);
		addCar(Intersection.VTL_SPAN, d);

		return false;
	}

	/**
	 * Change pause to the opposite of what is currently set and alter button
	 * text accordingly
	 */
	private void changePause() {
		// pause/unpause the simulator
		simulator.playPause();
		// check if paused or unpaused and set relevant text
		if (simulator.isPaused()) {
			pauseButton.setText("Unpause");
		} else {
			pauseButton.setText("Pause");
		}
	}

	/**
	 * Cycle through the relevant generation types to generate entities of
	 * either: Normal,VTL,Pedestrians
	 */
	private void changeCarType() {
		if (generateTypeLabel.equals("NORMAL")) {
			carTypeButton.setText(Simulator.VTL);
			generateTypeLabel = "VTL";
		} else if (generateTypeLabel.equals("VTL")) {
			carTypeButton.setText("PEDESTRIANS");
			generateTypeLabel = "PEDESTRIANS";
		} else {
			carTypeButton.setText(Simulator.NORMAL);
			generateTypeLabel = "NORMAL";
		}
	}

	/**
	 * Add a car to the simulation
	 * 
	 * @param The
	 *            distance away from the intersection that the car will be
	 * @param The
	 *            direction the car will be coming from (N,S,E,W)
	 */
	private void addCar(int dist, Direction d) {
		if (generateTypeLabel.equals("VTL")) {
			simulator.addVTLCar(dist, d);
		} else if (generateTypeLabel.equals("NORMAL")) {
			simulator.addCar(dist, d);
		} else {
			d.addPed();
		}
	}

	/**
	 * Update text at the bottom of the GUI which gives infromation of the
	 * current state of the system
	 */
	@Override
	public void simulationUpdated(String simulationState) {
		currentMode.setText(simulator.intersection.mode.toString() + ",");
		currentTick.setText(simulator.tick / 8 + "");
	}

}
