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
 * @author Scott
 * 
 */

public class NiceGui extends JPanel implements SimulationListener {

	/**
	 * Number of pixels for cars to increment per second.
	 */
	public static final int PIXELS_PER_TICK = 8;

	/**
	 * Size of the visulisation screen
	 */
	public static final int FRAME_SIZE = 576;

	/**
	 * Number of different images of cars
	 */
	public static final int CARS_IMAGE_COUNT = 11;

	/**
	 * Number of different images of VTL cars
	 */
	public static final int VTL_CARS_IMAGE_COUNT = 11;

	/**
	 * Background image of the roads
	 */
	private BufferedImage backgroundImage;

	/**
	 * Image of the pedestrians
	 */
	private BufferedImage pedImage;

	/**
	 * Images of all the cars
	 */
	private ArrayList<BufferedImage> carImages = new ArrayList<BufferedImage>();

	/**
	 * Images of all the VTL vars
	 */
	private ArrayList<BufferedImage> vtlCarImages = new ArrayList<BufferedImage>();

	/**
	 * Images of the traffic lights and pedestrians lights
	 */
	private Map<String, BufferedImage> lightImages = new HashMap<String, BufferedImage>();

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4825117902654245497L;

	/**
	 * The simulation for the visualisation.
	 */
	private Simulator simulator;

	/**
	 * Constructor
	 * 
	 * @param simulator
	 *            The simulation for the visualisation.
	 */
	public NiceGui(Simulator simulator) {
		this.simulator = simulator;
		simulator.addSimListener(this);
		initImages();
	}

	/**
	 * Initialises all the images before they can be used.
	 */
	private void initImages() {
		try {
			// ==================================================
			// background image
			// ==================================================
			backgroundImage = ImageIO.read(new File("res/background.png"));
			pedImage = ImageIO.read(new File("res/ped.png"));

			// ==================================================
			// cars
			// ==================================================
			for (int i = 0; i < CARS_IMAGE_COUNT; i++) {
				carImages.add(ImageIO.read(new File("res/Cars/" + (i + 1)
						+ ".png")));
			}

			// ==================================================
			// vtl cars
			// ==================================================
			for (int i = 0; i < VTL_CARS_IMAGE_COUNT; i++) {
				vtlCarImages.add(ImageIO.read(new File("res/VTLCars/" + (i + 1)
						+ ".png")));
			}

			// ==================================================
			// lights
			// ==================================================
			String[] lightNames = new String[] { "EW_G", "EW_O", "EW_R",
					"NS_G", "NS_O", "NS_R", "PED_EW_G", "PED_EW_R",
					"PED_EW_NULL", "PED_NS_G", "PED_NS_R", "PED_NS_NULL" };
			for (String lightName : lightNames) {
				lightImages.put(lightName, ImageIO.read(new File("res/Lights/"
						+ lightName + ".png")));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the simulation thread
	 */
	@Override
	public void simulationUpdated(String simulationState) {
		new RenderThread().start();
	}

	/**
	 * temporary image for painting
	 */
	private BufferedImage currentImage = null;

	/**
	 * Draws the graphics.
	 */
	@Override
	public void paint(Graphics g) {
		if (currentImage != null) {
			g.drawImage(currentImage, 0, 0, this);
		} else {
			super.paint(g);
		}
	}

	/**
	 * Class that represents the thread to start when the simulation is running.
	 * Handles all the interface re-rendering. 
	 * 
	 * @author sgoo052
	 */
	protected class RenderThread extends Thread {
		
		/**
		 * Runs on every tick
		 */
		public void run() {

			// ==================================================
			// BACKGROUND IMAGE
			// ==================================================
			BufferedImage img = new BufferedImage(FRAME_SIZE, FRAME_SIZE,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			g.drawImage(backgroundImage, 0, 0, null);

			// ==================================================
			// TRAFFIC LIGHTS
			// ==================================================
			
			// Draws the north and south traffic lights
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
			g.drawImage(imageNS, 288 - imageNS.getWidth() / 2, 288 - imageNS
					.getHeight() / 2, null);

			// Draws the east and west traffic lights
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
			g.drawImage(imageEW, 288 - imageEW.getWidth() / 2, 288 - imageEW
					.getHeight() / 2, null);

			// ==================================================
			// PEDESTRIANS LIGHTS
			// ==================================================
			
			// Draws the east and west pedestrian lights
			BufferedImage imagePedEW = null;
			System.out.println(simulator.tick);
			if (Direction.E.hasPeds(simulator.tick)
					|| Direction.W.hasPeds(simulator.tick)) {
				if (Direction.E.isPedsCrossing(simulator.tick)
						|| Direction.W.isPedsCrossing(simulator.tick)) {
					imagePedEW = lightImages.get("PED_EW_G");
				} else {
					imagePedEW = lightImages.get("PED_EW_R");
				}
			} else {
				imagePedEW = lightImages.get("PED_EW_NULL");
			}
			g.drawImage(imagePedEW, 288 - imagePedEW.getWidth() / 2,
					288 - imagePedEW.getHeight() / 2, null);

			// Draws the north and south pedestrian lights
			BufferedImage imagePedNS = null;
			System.out.println(simulator.tick);
			if (Direction.N.hasPeds(simulator.tick)
					|| Direction.S.hasPeds(simulator.tick)) {
				if (Direction.N.isPedsCrossing(simulator.tick)
						|| Direction.S.isPedsCrossing(simulator.tick)) {
					imagePedNS = lightImages.get("PED_NS_G");
				} else {
					imagePedNS = lightImages.get("PED_NS_R");
				}
			} else {
				imagePedNS = lightImages.get("PED_NS_NULL");
			}
			g.drawImage(imagePedNS, 288 - imagePedNS.getWidth() / 2,
					288 - imagePedNS.getHeight() / 2, null);

			// ==================================================
			// PEOPLE
			// ==================================================

			// Draws the pedestrians
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
				if (d.isPedsCrossing(simulator.tick)) {

					if (d == Direction.S) {
						y -= (325 - 245) * (simulator.tick - d.pedsCrossing)
								/ Direction.PED_CORSSING_TIME;
					} else if (d == Direction.N) {
						y += (325 - 245) * (simulator.tick - d.pedsCrossing)
								/ Direction.PED_CORSSING_TIME;
					} else if (d == Direction.E) {
						x -= (325 - 245) * (simulator.tick - d.pedsCrossing)
								/ Direction.PED_CORSSING_TIME;
					} else if (d == Direction.W) {
						x += (325 - 245) * (simulator.tick - d.pedsCrossing)
								/ Direction.PED_CORSSING_TIME;
					}
					g.drawImage(pedImage, x, y, null);
				}
			}

			// ==================================================
			// CARS AND VTL CARS
			// ==================================================
			for (Car c : simulator.cars) {
				drawCar(g, c);
			}

			for (Car c : simulator.leavingCars) {
				drawCar(g, c);
			}

			currentImage = img;
			repaint();

		}

		/**
		 * Draws a car on the screen based on a car object in the simulation.
		 * 
		 * @param g graphics to draw the car on
		 * @param c the car object to draw
		 */
		private void drawCar(Graphics2D g, Car c) {
			int x = 0, y = 0;

			int adjustedDistanceFromIntersection = c.distanceFromIntersection;
			if (adjustedDistanceFromIntersection >= 0)
				adjustedDistanceFromIntersection += 1;

			switch (c.direction) {
			case N:
				x = Intersection.VTL_SPAN * PIXELS_PER_TICK;
				y = (Intersection.VTL_SPAN - 8 - adjustedDistanceFromIntersection)
						* PIXELS_PER_TICK;
				break;

			case S:
				x = Intersection.VTL_SPAN * PIXELS_PER_TICK;
				y = (Intersection.VTL_SPAN + 8 + adjustedDistanceFromIntersection)
						* PIXELS_PER_TICK;
				break;

			case E:
				x = (Intersection.VTL_SPAN + 8 + adjustedDistanceFromIntersection)
						* PIXELS_PER_TICK;
				y = Intersection.VTL_SPAN * PIXELS_PER_TICK;
				break;

			case W:
				x = (Intersection.VTL_SPAN - 8 - adjustedDistanceFromIntersection)
						* PIXELS_PER_TICK;
				y = Intersection.VTL_SPAN * PIXELS_PER_TICK;
				break;

			}

			g.drawImage(getCarImage(c), x, y, null);
		}
	}

	/**
	 * The map containing all the cars images corresponding to all the cars on the visualisation.
	 */
	private Map<Car, BufferedImage> carMap = new HashMap<Car, BufferedImage>();

	/**
	 * Get the car image based on the car
	 * 
	 * @param car car object
	 * @return the image of the car
	 */
	private BufferedImage getCarImage(Car car) {
		if (!carMap.containsKey(car)) {

			BufferedImage image = null;
			if (car instanceof VTLCar) {
				image = copyImage(vtlCarImages
						.get((int) (Math.random() * vtlCarImages.size())));

			} else {
				image = copyImage(carImages
						.get((int) (Math.random() * carImages.size())));
			}

			image = rotateImage(image, car.direction, image.getWidth(), image
					.getHeight());

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

	/**
	 * Draws the circle on top of the cars for VTL cars based on the colour of the
	 * traffic lights on their dashboards.
	 * 
	 * @param g the graphics to draw on
	 * @param car the car object in order to get the direction and hence the traffic light.
	 */
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
	 * Deep copies an buffered image
	 * 
	 * @param bufferedImage the bufferedImage to copy
	 * @return a copy of the buffered image.
	 */
	private BufferedImage copyImage(BufferedImage bufferedImage) {
		ColorModel cm = bufferedImage.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bufferedImage.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * To rotate a buffered image based on the direction. The initial direction
	 * is south (meaning that it comes from the south so its facing north).
	 * 
	 * @param image the image to rotate
	 * @param direction the direction to rotate the image to
	 * @param anchorX the x location on the image to act as the centre of rotation
	 * @param anchorY the y location on the image to act as the centre of rotation
	 * @return the rotation buffered image
	 */
	private BufferedImage rotateImage(BufferedImage image, Direction direction,
			int anchorX, int anchorY) {

		AffineTransform transform = getAffineTransform(direction, anchorX,
				anchorY);

		AffineTransformOp op = new AffineTransformOp(transform,
				AffineTransformOp.TYPE_BILINEAR);

		return op.filter(image, null);
	}

	/**
	 * get the affine transformation of a rotation transformation.
	 * 
	 * @param direction the direction of the rotation.
	 * @param anchorX the x location on the image to act as the centre of rotation
	 * @param anchorY the y location on the image to act as the centre of rotation
	 * @returns the affine transformation.
	 */
	private AffineTransform getAffineTransform(Direction direction,
			int anchorX, int anchorY) {
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
