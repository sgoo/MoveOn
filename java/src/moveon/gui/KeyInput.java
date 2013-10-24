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
 * The interface (JPanel) for the key input.
 * 
 * @author sgoo052
 */
public class KeyInput extends JPanel implements ActionListener,
		KeyEventDispatcher, SimulationListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Button type for generating. Can be "Normal", "VTL" or "PEDESTRIANS"
	 */
	private String generateTypeLabel = "NORMAL";

	/**
	 * Simulator
	 */
	private Simulator simulator;

	/**
	 * Four buttons for north, south, east and west.
	 */
	JButton NButton, SButton, EButton, WButton;

	/**
	 * Other buttons
	 */
	JButton carTypeButton, pauseButton;

	/**
	 * Labels
	 */
	JLabel currentMode, currentTick;

	/**
	 * Checkboxes
	 */
	private JCheckBox toggleRandom, toggleVTLCars, toggleNormalCars,
			togglePedestrians;

	/**
	 * Constructor
	 * 
	 * @param simulator
	 */
	public KeyInput(Simulator simulator) {
		
		this.simulator = simulator;
		simulator.addSimListener(this);

		// ==================================================
		// Set layout
		// ==================================================
		
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// ==================================================
		// Left (direction generation) buttons
		// ==================================================
		
		JPanel LeftPanel = new JPanel(new GridLayout(2, 3));

		// North Button
		NButton = new JButton("North");
		NButton.addActionListener(this);
		LeftPanel.add(new JPanel());
		LeftPanel.add(NButton);

		// West Button
		WButton = new JButton("West");
		WButton.addActionListener(this);
		LeftPanel.add(new JPanel());
		LeftPanel.add(WButton);

		// South Button
		SButton = new JButton("South");
		SButton.addActionListener(this);
		LeftPanel.add(SButton);

		// East Button
		EButton = new JButton("East");
		EButton.addActionListener(this);
		LeftPanel.add(EButton);

		LeftPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		this.add(LeftPanel, BorderLayout.LINE_START);

		// ==================================================
		// Middle Buttons
		// ==================================================
		
		JPanel midPanel = new JPanel(new GridLayout(2, 1));

		// Pause Button
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		midPanel.add(pauseButton);

		// Car type (to generate) Button
		carTypeButton = new JButton(generateTypeLabel);
		carTypeButton.addActionListener(this);
		midPanel.add(carTypeButton);

		midPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(midPanel, BorderLayout.CENTER);

		// ==================================================
		// Right Checkboxes (x4)
		// ==================================================
		
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

		// ==================================================
		// Buttom Information Labels
		// ==================================================
		
		// Current Mode {VTL, Mixed, Normal}
		JPanel bottomPanel = new JPanel();
		JLabel modeLabel = new JLabel("Current Mode: ");
		currentMode = new JLabel("VTL+,");

		bottomPanel.add(modeLabel);
		bottomPanel.add(currentMode);

		// Current time since start of simulation
		JLabel tickLabel = new JLabel("Current Second: ");
		currentTick = new JLabel("0");

		bottomPanel.add(tickLabel);
		bottomPanel.add(currentTick);

		// ==================================================
		// Keyboard presses to map to buttons
		// ==================================================
		
		this.add(bottomPanel, BorderLayout.PAGE_END);
		// handles key presses
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
	}

	/**
	 * Handler to handle (on-screen) button presses
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == NButton) {
			addCar(Intersection.VTL_SPAN, Direction.N);
		} else if (e.getSource() == SButton) {
			addCar(Intersection.VTL_SPAN, Direction.S);
		} else if (e.getSource() == EButton) {
			addCar(Intersection.VTL_SPAN, Direction.E);
		} else if (e.getSource() == WButton) {
			addCar(Intersection.VTL_SPAN, Direction.W);
		} else if (e.getSource() == pauseButton) {
			changePause();
		} else if (e.getSource() == carTypeButton) {
			changeCarType();
		} else if (e.getSource() == toggleRandom) {
			simulator.toggleRandom();
		} else if (e.getSource() == toggleNormalCars) {
			simulator.toggleRandomNormalCars();
		} else if (e.getSource() == toggleVTLCars) {
			simulator.toggleRandomVTLCars();
		} else if (e.getSource() == togglePedestrians) {
			simulator.setGenerateRandomPeople(togglePedestrians.isSelected());
		}
	}

	/**
	 * Handler to handle keyboard presses
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
	 * To pause and unpause the simluation
	 */
	private void changePause() {
		simulator.playPause();
		if (simulator.isPaused()) {
			pauseButton.setText("Unpause");
		} else {
			pauseButton.setText("Pause");
		}
	}

	/**
	 * To change the car type (or pedestrians) to generate
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
	 * Add a car to the simulation.
	 * 
	 * @param dist distance from intersection
	 * @param d direction for which the car should come from
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
	 * Updated on each tick.
	 */
	@Override
	public void simulationUpdated(String simulationState) {
		currentMode.setText(simulator.intersection.mode.toString() + ",");
		currentTick.setText(simulator.tick / 8 + "");
	}

}
