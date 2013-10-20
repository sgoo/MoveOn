package moveon.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import moveon.cars.Car;
import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.SimulationListener;
import moveon.simulation.Simulator;

/**
 * Design uses a thread running waiting on a blocking queue that produces
 * renders of the intersection into Bufferers. These are then rendered onto the
 * screen via a posted runnable
 * 
 * @author Scott
 * 
 */

public class NiceGui extends JPanel implements SimulationListener {
	public static final int GUI_M_LENGTH = 3;

	public static final int PIX_PER_TICK = 8;

	public static final int FRAME_SIZE = 72;
	
	public static final int CARS_IMAGE_COUNT = 13;

	BufferedImage backgroundImage;
	ArrayList<BufferedImage> carImages = new ArrayList<BufferedImage>();

	/**
	 * 
	 */
	private static final long serialVersionUID = -4825117902654245497L;
	private Simulator sim;

	public NiceGui(Simulator sim) {
		this.sim = sim;
		sim.addSimListener(this);
		setSize(700, 700);

		initImages();
	}

	private void initImages() {
		try {
			backgroundImage = ImageIO.read(new File("res/background.png"));
			
			for(int i = 0 ; i < CARS_IMAGE_COUNT; i++) {
				carImages.add(ImageIO.read(new File("res/Cars/" + (i+1) + ".png")));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void simulationUpdated(String simulationState) {
		new RenderThread().start();
	}

	public static void main(String[] args) {
		Simulator sim = new Simulator(true);
		final JFrame window = new JFrame("Test");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(700, 700);
		final NiceGui gui = new NiceGui(sim);
		window.add(gui);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				window.setVisible(true);
			}
		});

		sim.simulate();
	}

	BufferedImage currentImage = null;

	@Override
	public void paint(Graphics g) {
		if (currentImage != null) {
			g.drawImage(currentImage, 0, 0, this);
		} else {
			super.paint(g);
		}
	}

	public class RenderThread extends Thread {
		public void run() {

			BufferedImage img = new BufferedImage(NiceGui.FRAME_SIZE
					* NiceGui.PIX_PER_TICK, NiceGui.FRAME_SIZE
					* NiceGui.PIX_PER_TICK, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			g.drawImage(backgroundImage, 0, 0, null);

			for (Car c : sim.cars) {
				int carSize = Car.CAR_LENGTH * GUI_M_LENGTH;
				switch (c.direction) {
				case N:

					g.drawImage(
							getCarImage(c),
							Intersection.VTL_SPAN * PIX_PER_TICK,
							(Intersection.VTL_SPAN - 8 - c.distanceFromIntersection) * PIX_PER_TICK, null);
					break;
					
				case S:
					g.drawImage(
							getCarImage(c),
							Intersection.VTL_SPAN * PIX_PER_TICK,
							(Intersection.VTL_SPAN + 8 + c.distanceFromIntersection) * PIX_PER_TICK, null);
					break;
					
				case E:
					g.drawImage(
							getCarImage(c),
							(Intersection.VTL_SPAN + 8 + c.distanceFromIntersection) * PIX_PER_TICK,
							Intersection.VTL_SPAN * PIX_PER_TICK, null);
					break;
				case W:
					g.drawImage(
							getCarImage(c),
							(Intersection.VTL_SPAN - 8 - c.distanceFromIntersection) * PIX_PER_TICK,
							Intersection.VTL_SPAN * PIX_PER_TICK, null);
					break;
					
				}
			}
			currentImage = img;
			repaint();

		}
	}

	private Map<Car, BufferedImage> carMap = new HashMap<Car, BufferedImage>();

	private BufferedImage getCarImage(Car car) {
		if (!carMap.containsKey(car)) {
			BufferedImage image = copyImage(carImages
					.get((int) (Math.random() * carImages.size())));
			image = rotateImage(image, car.direction);
			carMap.put(car, image);
		}

		
		return carMap.get(car);
	}

	private BufferedImage copyImage(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	private BufferedImage rotateImage(BufferedImage image, Direction direction) {
		double radians = 0;
		switch (direction) {
		case E:
			radians += Math.PI / 2.0d;
		case N:
			radians += Math.PI / 2.0d;
		case W:
			radians += Math.PI / 2.0d;
		case S:
		}

		AffineTransform transform = new AffineTransform();
		transform.rotate(radians, image.getWidth(), image.getHeight());
		AffineTransformOp op = new AffineTransformOp(transform,
				AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}

}
