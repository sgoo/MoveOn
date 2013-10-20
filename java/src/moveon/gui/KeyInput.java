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
import javax.swing.JPanel;

import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.SimulationListener;
import moveon.simulation.Simulator;

public class KeyInput extends JPanel implements ActionListener, KeyEventDispatcher, SimulationListener {

	private static final long serialVersionUID = 1L;
	private static final String CAR_TYPE_LBL = "";

	private Simulator simulator;

	private boolean isVtlCar = false;
	JButton NButton;
	JButton SButton;
	JButton EButton;
	JButton WButton;
	JButton carTypeButton;
	JButton pauseButton;

	private JCheckBox toggleRandom;
	private JCheckBox toggleVTLCars;
	private JCheckBox toggleNormalCars;
	private JCheckBox togglePedestrians;
	
	
	
	
	public KeyInput(Simulator simulator) {
		this.simulator = simulator;
		
		// set layout
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		
		// Left buttons
		JPanel LeftPanel = new JPanel(new GridLayout(2,3)); 
		
		
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
		
		LeftPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		this.add(LeftPanel,BorderLayout.LINE_START);
		
		// Middle Buttons
		JPanel midPanel = new JPanel(new GridLayout(1,2));
		
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		midPanel.add(pauseButton);
		
		carTypeButton = new JButton(CAR_TYPE_LBL + Simulator.NORMAL);
		carTypeButton.addActionListener(this);
		midPanel.add(carTypeButton);
		
		midPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		this.add(midPanel, BorderLayout.CENTER);
		
		// Right Checkboxes
		JPanel rightPanel = new JPanel(new GridLayout(3,1));
		
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
		
		// handles key presses
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
	}


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
		}else if(e.getSource() == togglePedestrians){
			simulator.setGenerateRandomPeople(togglePedestrians.isSelected());
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
		simulator.playPause();
		if (simulator.isPaused()) {
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
			simulator.addVTLCar(dist, d);
		} else {
			simulator.addCar(dist, d);
		}
	}

	@Override
	public void simulationUpdated(String simulationState) {

	}

}
