package moveon.gui;

import java.awt.Color;
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
import moveon.cars.VTLCar;
import moveon.simulation.Direction;
import moveon.simulation.Intersection;
import moveon.simulation.SimulationListener;
import moveon.simulation.Simulator;

/**
 * Design uses a thread running waiting on a blocking queue that produces
 * renders of the intersection into Bufferers. These are then rendered onto the
 * screen via a posted runnable
 * 
 * Hard coded numbers used in this class refer to the number of pixels along X & Y
 * 0,0 is the top left corner of the panel.
 * 
 *   0        X
 *  0+---------
 *   |
 *   |
 *   |
 *   |
 *  Y|
 * 
 * @author Scott Goodhew, Roy Lin, Jourdan Harvey, Mike Little
 * 
 */
public class NiceGui extends JPanel implements SimulationListener {

	//Constants defining th GUI
	public static final int GUI_M_LENGTH = 3;

	// Pixels per tick meter
	public static final int PIX_PER_TICK = 8;

	public static final int FRAME_SIZE = 72;

	// The total number of car images and vtl images available respectively
	public static final int CARS_IMAGE_COUNT = 11;
	public static final int VTL_CARS_IMAGE_COUNT = 11;

	// Background image
	BufferedImage backgroundImage;
	// Image to represent a pedestrian
	BufferedImage pedImage;
	ArrayList<BufferedImage> carImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> vtlCarImages = new ArrayList<BufferedImage>();
	Map<String, BufferedImage> lightImages = new HashMap<String, BufferedImage>();

	/**
	 * SrialVersionUID
	 */
	private static final long serialVersionUID = -4825117902654245497L;
	// Instance of the simulator
	private Simulator sim;

	// Gui Constructor
	public NiceGui(Simulator sim) {
		this.sim = sim;
		// add this GUI as a listener to the simulator
		sim.addSimListener(this);
		setSize(700, 700);
		// Initialize reused images
		initImages();
	}

	private void initImages() {
		try {
			// background image
			backgroundImage = ImageIO.read(new File("res/background.png"));
			pedImage = ImageIO.read(new File("res/ped.png"));

			// cars
			for (int i = 0; i < CARS_IMAGE_COUNT; i++) {
				carImages.add(ImageIO.read(new File("res/Cars/" + (i + 1) + ".png")));
			}

			// vtl cars
			for (int i = 0; i < VTL_CARS_IMAGE_COUNT; i++) {
				vtlCarImages.add(ImageIO.read(new File("res/VTLCars/" + (i + 1) + ".png")));
			}

			// lights
			// Names of each light
			String[] lightNames = new String[] { "EW_G", "EW_O", "EW_R", "NS_G", "NS_O", "NS_R", "PED_EW_G", "PED_EW_R", "PED_EW_NULL", "PED_NS_G", "PED_NS_R",
					"PED_NS_NULL" };
			// add the lights to a Map for retrieval
			for (String lightName : lightNames) {
				lightImages.put(lightName, ImageIO.read(new File("res/Lights/" + lightName + ".png")));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a new thread to process each update
	 */
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

			// ==================================================
			// BACKGROUND IMAGE
			// ==================================================
			BufferedImage img = new BufferedImage(NiceGui.FRAME_SIZE * NiceGui.PIX_PER_TICK, NiceGui.FRAME_SIZE * NiceGui.PIX_PER_TICK,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			g.drawImage(backgroundImage, 0, 0, null);

			// ==================================================
			// TRAFFIC LIGHTS
			// ==================================================
			BufferedImage imageNS = null;
			switch (Direction.N.lights.currentColor) {
			case R:
				imageNS = lightImages.get("NS_R");
				break;
			case O:
				imageNS = lightImages.get("NS_O");
				break;
			case G:
				imageNS = lightImages.get("NS_G");
				break;
			}
			g.drawImage(imageNS, 288 - imageNS.getWidth() / 2, 288 - imageNS.getHeight() / 2, null);

			BufferedImage imageEW = null;
			switch (Direction.E.lights.currentColor) {
			case R:
				imageEW = lightImages.get("EW_R");
				break;
			case O:
				imageEW = lightImages.get("EW_O");
				break;
			case G:
				imageEW = lightImages.get("EW_G");
				break;
			}
			g.drawImage(imageEW, 288 - imageEW.getWidth() / 2, 288 - imageEW.getHeight() / 2, null);

			// ==================================================
			// PEDESTRIANS LIGHTS
			// ==================================================
			BufferedImage imagePedEW = null;
			System.out.println(sim.tick);
			if (Direction.E.hasPeds(sim.tick) || Direction.W.hasPeds(sim.tick)) {
				if (Direction.E.isPedsCrossing(sim.tick) || Direction.W.isPedsCrossing(sim.tick)) {
					imagePedEW = lightImages.get("PED_EW_G");
				} else {
					imagePedEW = lightImages.get("PED_EW_R");
				}
			} else {
				imagePedEW = lightImages.get("PED_EW_NULL");
			}
			g.drawImage(imagePedEW, 288 - imagePedEW.getWidth() / 2, 288 - imagePedEW.getHeight() / 2, null);

			BufferedImage imagePedNS = null;
			System.out.println(sim.tick);
			if (Direction.N.hasPeds(sim.tick) || Direction.S.hasPeds(sim.tick)) {
				if (Direction.N.isPedsCrossing(sim.tick) || Direction.S.isPedsCrossing(sim.tick)) {
					imagePedNS = lightImages.get("PED_NS_G");
				} else {
					imagePedNS = lightImages.get("PED_NS_R");
				}
			} else {
				imagePedNS = lightImages.get("PED_NS_NULL");
			}
			g.drawImage(imagePedNS, 288 - imagePedNS.getWidth() / 2, 288 - imagePedNS.getHeight() / 2, null);

			// ==================================================
			// PEOPLE
			// ==================================================

			for (Direction d : Direction.getDirections()) {

				int x, y;
				if (d == Direction.S || d == Direction.W) {
					x = 245;
				} else {
					x = 325;
				}
				if (d == Direction.W || d == Direction.N) {
					y = 245;
				} else {
					y = 325;
				}
				if (d.pedsWaiting) {
					g.drawImage(pedImage, x, y, null);
				}
				if (d.isPedsCrossing(sim.tick)) {

					if (d == Direction.S) {
						y -= (325 - 245) * (sim.tick - d.pedsCrossing) / Direction.PED_CORSSING_TIME;
					} else if (d == Direction.N) {
						y += (325 - 245) * (sim.tick - d.pedsCrossing) / Direction.PED_CORSSING_TIME;
					} else if (d == Direction.E) {
						x -= (325 - 245) * (sim.tick - d.pedsCrossing) / Direction.PED_CORSSING_TIME;
					} else if (d == Direction.W) {
						x += (325 - 245) * (sim.tick - d.pedsCrossing) / Direction.PED_CORSSING_TIME;
					}
					g.drawImage(pedImage, x, y, null);
				}
			}

			// ==================================================
			// CARS AND VTL CARS
			// ==================================================
			for (Car c : sim.cars) {
				drawCar(g, c);
			}

			for (Car c : sim.leavingCars) {
				drawCar(g, c);
			}

			currentImage = img;
			repaint();

		}

		private void drawCar(Graphics2D g, Car c) {
			int x = 0, y = 0;

			int adjustedDistanceFromIntersection = c.distanceFromIntersection;
			if (adjustedDistanceFromIntersection >= 0)
				adjustedDistanceFromIntersection += 1;

			switch (c.direction) {
			case N:
				x = Intersection.VTL_SPAN * PIX_PER_TICK;
				y = (Intersection.VTL_SPAN - 8 - adjustedDistanceFromIntersection) * PIX_PER_TICK;
				break;

			case S:
				x = Intersection.VTL_SPAN * PIX_PER_TICK;
				y = (Intersection.VTL_SPAN + 8 + adjustedDistanceFromIntersection) * PIX_PER_TICK;
				break;

			case E:
				x = (Intersection.VTL_SPAN + 8 + adjustedDistanceFromIntersection) * PIX_PER_TICK;
				y = Intersection.VTL_SPAN * PIX_PER_TICK;
				break;

			case W:
				x = (Intersection.VTL_SPAN - 8 - adjustedDistanceFromIntersection) * PIX_PER_TICK;
				y = Intersection.VTL_SPAN * PIX_PER_TICK;
				break;

			}

			g.drawImage(getCarImage(c), x, y, null);
		}
	}

	private Map<Car, BufferedImage> carMap = new HashMap<Car, BufferedImage>();

	private BufferedImage getCarImage(Car car) {
		if (!carMap.containsKey(car)) {

			BufferedImage image = null;
			if (car instanceof VTLCar) {
				image = copyImage(vtlCarImages.get((int) (Math.random() * vtlCarImages.size())));

			} else {
				image = copyImage(carImages.get((int) (Math.random() * carImages.size())));
			}

			image = rotateImage(image, car.direction, image.getWidth(), image.getHeight());

			carMap.put(car, image);
		}

		if (car instanceof VTLCar) {
			Color color = null;
			switch (car.direction.lights.currentColor) {
			case G:
				color = Color.green;
				break;
			case O:
				color = Color.orange;
				break;
			case R:
				color = Color.red;
				break;
			}
			Graphics2D g = carMap.get(car).createGraphics();
			g.setColor(color);

			drawCircleForDirection(g, car);
		}

		return carMap.get(car);
	}

	private void drawCircleForDirection(Graphics2D g, Car car) {
		int x = 0;
		int y = 0;
		switch (car.direction) {
		case N:
			x = 43;
			y = 43;
			break;
		case S:
			x = 11;
			y = 11;
			break;
		case E:
			x = 11;
			y = 43;
			break;
		case W:
			x = 43;
			y = 11;
			break;
		}
		g.fillOval(x, y, 10, 10);

		g.setColor(Color.blue);
		if (((VTLCar) car).isLeader()) {
			g.fillOval(x + 2, y + 2, 6, 6);
		}

	}

	/**
	 * Duplicate a given image and return the copy
	 * @param bi
	 * @return
	 */
	private BufferedImage copyImage(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Rotate a given image, and return the rotated version of the image
	 * @param image
	 * @param direction
	 * @param anchorX
	 * @param anchorY
	 * @return
	 */
	private BufferedImage rotateImage(BufferedImage image, Direction direction, int anchorX, int anchorY) {

		AffineTransform transform = getAffineTransform(direction, anchorX, anchorY);

		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

		return op.filter(image, null);
	}

	/**
	 * get the affineTransform represenation of a transformation
	 * This allows us to move and rotate and hence reuse certain images,
	 * 
	 * E.g. traffic light images
	 * @param direction
	 * @param anchorX
	 * @param anchorY
	 * @return
	 */
	private AffineTransform getAffineTransform(Direction direction, int anchorX, int anchorY) {
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
		transform.rotate(radians, anchorX, anchorY);

		return transform;
	}
}