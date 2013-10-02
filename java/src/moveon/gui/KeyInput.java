package moveon.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import moveon.simulation.Direction;
import moveon.simulation.SimulationListener;
import moveon.simulation.Simulator;

public class KeyInput extends JFrame implements ActionListener,
		KeyEventDispatcher, SimulationListener {

	private static final long serialVersionUID = 1L;
	private static final String CAR_TYPE_LBL = "";

	private Simulator s;

	private boolean isVtlCar = false;

	public KeyInput(Simulator s) {
		super("Car Creator");
		this.s = s;
		init();
	}

	LayoutManager layout;
	JButton NButton;
	JButton SButton;
	JButton EButton;
	JButton WButton;
	JButton carTypeButton;
	JButton pauseButton;
	JTextArea display;
	private Timer timer;
	private JCheckBox toggleRandom;
	private JCheckBox toggleVTLCars;
	private JCheckBox toggleNormalCars;

	private void init() {
		layout = new BorderLayout();

		JPanel controls = new JPanel(new GridLayout(4, 3));

		display = new JTextArea(5, 50);
		display.setFont(new Font("Monaco", Font.PLAIN, 12));

		NButton = new JButton("North");
		NButton.addActionListener(this);
		SButton = new JButton("South");
		SButton.addActionListener(this);
		EButton = new JButton("East");
		EButton.addActionListener(this);
		WButton = new JButton("West");
		WButton.addActionListener(this);
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		carTypeButton = new JButton(CAR_TYPE_LBL + Simulator.NORMAL);
		carTypeButton.addActionListener(this);
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);

		setLayout(layout);

		// Controls Top row
		controls.add(new JLabel());
		controls.add(NButton, BorderLayout.PAGE_START);
		controls.add(new JLabel());

		// Controls second row
		controls.add(WButton, BorderLayout.LINE_START);
		controls.add(new JLabel());
		controls.add(EButton, BorderLayout.LINE_END);

		// Controls third Row
		controls.add(new JLabel());
		controls.add(SButton, BorderLayout.PAGE_END);
		controls.add(new JLabel());

		// Controls bottom row
		controls.add(carTypeButton);
		controls.add(pauseButton, BorderLayout.CENTER);

		// Checkboxes
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
		toggleRandom = new JCheckBox("Generate cars randomly", true);
		toggleVTLCars = new JCheckBox("Generate VTL cars", true);
		toggleNormalCars = new JCheckBox("Generate normal cars", true);

		toggleRandom.addActionListener(this);
		toggleVTLCars.addActionListener(this);
		toggleNormalCars.addActionListener(this);
		checkboxPanel.add(toggleRandom);
		checkboxPanel.add(toggleVTLCars);
		checkboxPanel.add(toggleNormalCars);

		controls.add(checkboxPanel);

		add(controls, BorderLayout.LINE_START);
		add(display, BorderLayout.CENTER);

		setSize(1000, 250);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == NButton) {
			addCar(100, Direction.N);
		} else if (e.getSource() == SButton) {
			addCar(100, Direction.S);
		} else if (e.getSource() == EButton) {
			addCar(100, Direction.E);
		} else if (e.getSource() == WButton) {
			addCar(100, Direction.W);
		} else if (e.getSource() == pauseButton) {
			changePause();
		} else if (e.getSource() == carTypeButton) {
			changeCarType();
		} else if (e.getSource() == toggleRandom) {
			s.toggleRandom();
		} else if (e.getSource() == toggleNormalCars) {
			s.toggleRandomNormalCars();
		} else if (e.getSource() == toggleVTLCars) {
			s.toggleRandomVTLCars();
		}
	}

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
		if (Character.isUpperCase(e.getKeyChar())) {
			isVtlCar = true;
			carTypeButton.setText(Simulator.VTL);
			addCar(100, d);
		} else {
			isVtlCar = false;
			carTypeButton.setText(Simulator.NORMAL);
			addCar(100, d);
		}
		return false;
	}

	private void changePause() {
		s.playPause();
		if (s.isPaused()) {
			pauseButton.setText("Unpause");
		} else {
			pauseButton.setText("Pause");
		}
	}

	private void changeCarType() {
		isVtlCar = !isVtlCar;
		if (isVtlCar) {
			carTypeButton.setText(Simulator.VTL);
		} else {
			carTypeButton.setText(Simulator.NORMAL);
		}
	}

	private void addCar(int dist, Direction d) {
		if (isVtlCar) {
			s.addVTLCar(dist, d);
		} else {
			s.addCar(dist, d);
		}
	}

	@Override
	public void simulationUpdated(String simulationState) {
		display.setText(simulationState);

	}

}
