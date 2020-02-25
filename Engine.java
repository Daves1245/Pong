import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * @author David Santamaria
 * @version 0.2.7 Removed irrelevant code and made general improvements to
 *          structure Contains a <code> clock </code> that updates and renders
 *          for every in-game tick. Ticks and fps are now shown on-screen during
 *          play
 */

class Dimension {
	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int width;
	public int height;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}

public class Engine {

	// Global constants
	public static Dimension screenSize = new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
			(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
	final static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	
	int fps = 150;

	public static boolean paused;
	static String stats = "";

	private Pong pong = new Pong();
	private JFrame frame;
	InputHandler handler;
	static Graphics g2;
	static Graphics2D g2d;
	BufferedImage i;
	private boolean running = true;

	public SoundHandler sHandler;

	public static void main(String[] args) {
		new Engine();
	}

	/**
	 * Invokes the <code> run </code> method Terminates the program if stops running
	 */
	public Engine() {
		run();
		System.exit(-1);
	}

	/**	
	 * Initializes variables
	 */
	void initialize() {
		// Big Objects
		sHandler = SoundHandler.getNewScoreHandler(null);

		// Initialize frame and set it to full screen mode by default
		
		frame = new JFrame("Game");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		device.setFullScreenWindow(frame);
		frame.setVisible(true);

		// Objects
		handler = new InputHandler(frame);

		// Graphics Stuff
		i = new BufferedImage(screenSize.getWidth(), screenSize.getHeight(), BufferedImage.TYPE_INT_RGB);
		g2d = i.createGraphics();
		g2 = frame.getGraphics();
	}

	// Run the engine
	void run() {
		initialize();
		clock();
	}

	/**
	 * The game's "clock".
	 */
	void clock() {
		// SoundHandler.handler.reset();

		long time = System.nanoTime();
		final double tick = 60.0;
		double ms = 1000000000 / tick;
		double deltaTime = 0;
		int ticks = 0;
		int fps = 0;
		long timer = System.currentTimeMillis();
		int seconds = 0;
		int minutes = 0;
		int hours = 0;

		while (running) {
			long currentTime = System.nanoTime();
			deltaTime += (currentTime - time) / ms;

			time = currentTime;
			if (deltaTime >= 1) {
				update();
				ticks++;
				deltaTime--;
			}
			render();

			fps++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				seconds++;
				if (seconds > 59) {
					seconds %= 60;
					minutes++;
				}

				stats = "Ticks: " + ticks + ", FPS: " + fps + ", " + seconds + ":" + minutes + ":" + hours;
				ticks = 0;
				fps = 0;
			}
		}
	}

	/**
	 * Updates the Engine
	 */
	void update() {
		pong.updateAll();
	}

	void spam(String message, int numOfTimes) {
		for (int i = 0; i < numOfTimes; i++) {
			System.out.println(message);
		}
	}

	/**
	 * Renders the image, sets all the colors, draws all the shapes
	 */
	void render() {

		pong.renderAllPong(g2d);
		g2d.setFont(new Font("Arial", 1, 25));
		g2d.setColor(Color.GREEN);
		g2d.drawString(Engine.stats, 10, 60);
		g2.drawImage(i, 0, 0, frame);
	}

	// Pause may be configured differently to different games
	public void pause() {
		if (InputHandler.handler.isKeyDown(KeyEvent.VK_U))
			paused = false;

		if (paused) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pause();
		} else
			clock();
	}

}
