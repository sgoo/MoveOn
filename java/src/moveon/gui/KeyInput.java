package moveon.gui;

import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import moveon.simulation.Direction;
import moveon.simulation.Simulator;

public class KeyInput extends JFrame implements ActionListener,
		KeyEventDispatcher {

	private static final long serialVersionUID = 1L;
	private Simulator s;

	public KeyInput(Simulator s) {
		super("Car Creator");
		this.s = s;

		init();
	}

	GridLayout layout;
	JButton NButton;
	JButton SButton;
	JButton EButton;
	JButton WButton;
	JButton pauseButton;

	private void init() {
		layout = new GridLayout(3, 3);

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

		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);

		setLayout(layout);
		add(new JLabel());
		add(NButton);
		add(new JLabel());
		add(WButton);
		add(pauseButton);
		add(EButton);
		add(new JLabel());
		add(SButton);
		add(new JLabel());

		setSize(300, 250);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == NButton) {
			s.addCar(100, Direction.N);
		} else if (e.getSource() == SButton) {
			s.addCar(100, Direction.S);
		} else if (e.getSource() == EButton) {
			s.addCar(100, Direction.E);
		} else if (e.getSource() == WButton) {
			s.addCar(100, Direction.W);
		} else if (e.getSource() == pauseButton) {
			changePause();
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
		case ' ':
			changePause();
			return true;
		}
		if (d == null) {
			return false;
		}
		if (Character.isUpperCase(e.getKeyChar())) {
			s.addVTLCar(100, d);
		} else {
			s.addCar(100, d);
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

}
