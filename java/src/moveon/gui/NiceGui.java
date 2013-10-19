package moveon.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import moveon.cars.Car;
import moveon.simulation.Intersection;
import moveon.simulation.SimulationListener;
import moveon.simulation.Simulator;

/**
 * Design uses a thread running waiting on a blocking queue that produces renders of the intersection into Bufferers. These are then rendered onto the screen
 * via a posted runnable
 * 
 * @author Scott
 * 
 */

public class NiceGui extends JFrame implements SimulationListener {
	public static final int GUI_M_LENGTH = 3;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4825117902654245497L;
	private Simulator sim;

	public NiceGui(Simulator sim) {
		this.sim = sim;
		sim.addSimListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(700, 700);
	}

	@Override
	public void simulationUpdated(String simulationState) {
		System.out.println("got update!");
		new RenderThread().start();
	}

	public static void main(String[] args) {
		Simulator sim = new Simulator(true);

		final NiceGui gui = new NiceGui(sim);

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				gui.setVisible(true);
			}
		});

		sim.simulate();
	}

	BufferedImage currentImage = null;

	@Override
	public void paint(Graphics g) {
		System.out.println("Drawing");
		if (currentImage != null) {
			g.drawImage(currentImage, 0, 0, this);
			System.out.println("Drawing2");
		} else {
			super.paint(g);
		}
	}

	public class RenderThread extends Thread {
		public void run() {
			int size = (2 * (Intersection.VTL_SPAN + Car.CAR_LENGTH) + Car.CAR_LENGTH) * GUI_M_LENGTH;
			BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, size, size);
			g.setColor(Color.RED);
			for (Car c : sim.cars) {
				int carSize = Car.CAR_LENGTH * GUI_M_LENGTH;
				switch (c.direction) {
				case N:
					// System.out.format("%d %d %d %d\n", Intersection.VTL_SPAN * GUI_M_LENGTH, (Intersection.VTL_SPAN - c.distanceFromIntersection)
					// * GUI_M_LENGTH, carSize, carSize);
					g.fillRect(Intersection.VTL_SPAN * GUI_M_LENGTH, (Intersection.VTL_SPAN - c.distanceFromIntersection) * GUI_M_LENGTH, carSize, carSize);
					break;
				case S:
					g.fillRect(Intersection.VTL_SPAN * GUI_M_LENGTH,
							(Intersection.VTL_SPAN + (Intersection.INTERSECTION_SPAN * Car.CAR_LENGTH) + c.distanceFromIntersection) * GUI_M_LENGTH, carSize,
							carSize);
					break;
				case E:
					g.fillRect(Intersection.VTL_SPAN * GUI_M_LENGTH, (Intersection.VTL_SPAN - c.distanceFromIntersection) * GUI_M_LENGTH, carSize, carSize);
					break;
				case W:
					break;
				}
			}
			System.out.println("Invalidate!");
			currentImage = img;
			repaint();

		}
	}
}
