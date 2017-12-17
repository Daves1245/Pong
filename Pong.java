import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * @author David Santamaria
 * @version 0.5.7 Several minor bug fixes and optimizations. Tons of Javadocs
 *          everywhere!
 */

public class Pong {

	public static void main(String[] args) {
		Engine.main(args);
	}

	// Objects
	Ball ball = new Ball();
	Player p1 = new Player(10, screenHeight / 2);
	Player p2 = new Player(screenWidth - Player.size.width - 10, screenHeight / 2);

	// ScreenWidth and screenHeight
	public static int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	public String difficulty;

	public Pong(String difficulty) {
		this.difficulty = difficulty;
		evaluateDifficulty();
	}

	/** Sets default difficulty at medium **/
	public Pong() {
		this("medium");
	}

	// Score-related variables
	static int p1score = 0;
	static int p2score = 0;
	static int WIN_SCORE = 9;

	/** Renders everything that needs rendering **/
	public void renderAllPong(Graphics2D g) {
		// Note to self: frame.setBackground(Color.Black) does NOT work unless
		// you want to feel like you're on drugs

		// Background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);

		// Ball
		g.setColor(Color.WHITE);
		g.fillOval(ball.x, ball.y, ball.size.width, ball.size.height);

		/* Displaying score */
		g.setFont(new Font("Arial", 1, 75));
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf(Pong.p1score), screenWidth / 2 - 150, 100);
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf(Pong.p2score), screenWidth / 2 + 100, 100);

		// Middle border
		g.setColor(Color.WHITE);
		g.fillRect(screenWidth / 2, 0, 10, screenHeight);

		// Middle circle
		g.setColor(Color.WHITE);
		g.drawOval(screenWidth / 2 - 250, screenHeight / 2 - 250, 500, 500);

		// Top and bottom borders
		g.setColor(Color.WHITE);
		g.fillRect(0, 30, screenWidth, 10);
		g.setColor(Color.WHITE);
		g.fillRect(0, screenHeight - 15, screenWidth, 10);

		// Player 1, the box to the right of the screen
		g.setColor(Color.WHITE);
		g.fillRect(p1.x, p1.y, Player.size.width, Player.size.height);

		// Player 2, to the left
		g.setColor(Color.WHITE);
		g.fillRect(p2.x, p2.y, Player.size.width, Player.size.height);

	}

	/**
	 * Invokes every method that needs to be invoked for logic and updating
	 */
	public void updateAll() {
		checkScore();
		updatePosition();
		detectCollision();
		ball.updateAll();
	}

	/** Evaluates the games stats depending on the difficulty **/
	private void evaluateDifficulty() {
		switch (difficulty) {
		case "easy":
			Player.size.setSize(40, 200);
			Player.setSpeed(5);
			ball.size.setSize(50, 50);
			ball.YMAX = 15;
			ball.XMAX = 15;
			break;

		case "medium":
			Player.size.setSize(30, 150);
			Player.setSpeed(8);
			ball.size.setSize(30, 30);
			ball.YMAX = 28;
			ball.XMAX = 28;
			break;

		case "hard":
			Player.size.setSize(20, 100);
			Player.setSpeed(12);
			ball.size.setSize(15, 15);
			ball.YMAX = 40;
			ball.XMAX = 40;
			break;

		case "impossible":
			Player.size.setSize(10, 50);
			Player.setSpeed(15);
			ball.size.setSize(10, 10);
			ball.YMAX = 40;
			ball.XMAX = 40;
			break;
		}
	}

	/** updates the position of the players **/
	private void updatePosition() {

		// Changes player's Y values (up) based on the arguments
		if (InputHandler.handler.isKeyDown(KeyEvent.VK_W)) {
			// Keeps the player within boundaries
			if (p1.y > 10)
				p1.y -= Player.speed;
		}
		if (InputHandler.handler.isKeyDown(KeyEvent.VK_UP)) {
			if (p2.y > 10)
				p2.y -= Player.speed;
		}

		if (InputHandler.handler.isKeyDown(KeyEvent.VK_S)) {
			if (p1.y < screenHeight - Player.size.height)
				p1.y += Player.speed;
		}
		if (InputHandler.handler.isKeyDown(KeyEvent.VK_DOWN)) {
			if (p2.y < screenHeight - Player.size.height)
				p2.y += Player.speed;
		}
		if (InputHandler.handler.isKeyDown(KeyEvent.VK_ESCAPE)) {
			System.exit(-1);
		}

		// Updates ball position after updates to the speed and trajectory have
		// been made
	}

	private void detectCollision() {

		// Detects if ball is within the boundaries of the box
		if (ballIsWithinBoundariesOfPlayer(1)) {

			// switch x speed to positive
			ball.xspeed = Math.abs(ball.xspeed);

			SoundHandler.handler.playSound(SoundHandler.BOUNCE);
			
			// If it hits top of player
			if (ball.centery <= p1.y + (Player.size.height / 5)) {
				ball.yspeed -= 4;
			} else if (ball.centery <= p1.y + (Player.size.height / 2.5)) {
				ball.yspeed--;
			} else if (ball.centery <= p1.y + (Player.size.height / 2)) {
				// afoinfa
			} else if (ball.centery <= p1.y + (Player.size.height / 1.5)) {
				ball.xspeed++;
			} else if (ball.centery <= p1.y + (Player.size.height / 1.2)) {
				ball.yspeed++;
			} else if (ball.centery <= p1.y + Player.size.height) {
				ball.yspeed = -ball.yspeed;
			}
		}

		// Checks to make sure ball is within boundaries of the box
		if (ballIsWithinBoundariesOfPlayer(2)) {

			// Switch x speed to negative
			ball.xspeed = -Math.abs(ball.xspeed);

			SoundHandler.handler.playSound(SoundHandler.BOUNCE);
			
			if (ball.centery <= p2.y + (Player.size.height / 5)) {
				ball.yspeed -= 4;
			} else if (ball.centery <= p2.y + (Player.size.height / 2.5)) {
				ball.yspeed--;
			} else if (ball.centery <= p2.y + (Player.size.height / 2)) {
				// afoinfa
			} else if (ball.centery <= p2.y + (Player.size.height / 1.5)) {
				ball.xspeed++;
			} else if (ball.centery <= p2.y + (Player.size.height / 1.2)) {
				ball.yspeed++;
			} else if (ball.centery <= p2.y + Player.size.height) {
				ball.yspeed += 4;
			}
		}
	}

	/**
	 * Checks if either one of the player's score is at or above the winning
	 * score number
	 */
	private void checkScore() {

		if (p1score >= WIN_SCORE || p2score >= WIN_SCORE) {
			if (p1score >= WIN_SCORE) {
				SoundHandler.handler.playSound(SoundHandler.VICTORY);
				JOptionPane.showMessageDialog(null, "Player 1 wins!");
			} else if (p2score >= WIN_SCORE) {
				SoundHandler.handler.playSound(SoundHandler.VICTORY);
				JOptionPane.showMessageDialog(null, "Player 2 wins!");
			}
			int yesno = JOptionPane.showConfirmDialog(null, "Would you like to play again?");
			if (yesno == 0)
				reset();
			else {
				System.exit(1);
			}
		}
	}

	/**
	 * Resets the players' positions, the players' keys, and the players' score
	 */
	private void reset() {

		// Sets the values of the keys that are pushed down to move the players
		// false so that the keys do not get stuck when it resets
		InputHandler.handler.keys[KeyEvent.VK_W] = false;
		InputHandler.handler.keys[KeyEvent.VK_S] = false;
		InputHandler.handler.keys[KeyEvent.VK_UP] = false;
		InputHandler.handler.keys[KeyEvent.VK_DOWN] = false;

		// Resets score
		p1score = 0;
		p2score = 0;

		// Resets positions
		p1.y = screenHeight / 2 - 100;
		p2.y = screenHeight / 2 - 100;
	}

	/**
	 * 
	 * @param player
	 *            Integer player used to symbolize player 1 or player 2
	 * @return True if the <code> x </code> and <code> y </code> coordinates of
	 *         the ball are within the boundaries of the player's
	 *         <code> x </code> and <code> y </code> coordinates
	 */
	boolean ballIsWithinBoundariesOfPlayer(int player) {
		switch (player) {
		case 1:
			return (ball.centerx >= p1.x && ball.centerx <= p1.x + Player.size.width && ball.centery >= p1.y
					&& ball.centery <= p1.y + Player.size.height);
		case 2:
			return (ball.centerx >= p2.x && ball.centerx <= p2.x + Player.size.width && ball.centery >= p2.y
					&& ball.centery <= p2.y + Player.size.height);
		}
		return false;
	}

}

/**
 * Contains ball-related variables, and methods
 * 
 * @author David Santamaria
 */
class Ball {
	Dimension size = new Dimension(30, 30);

	/** Screen Width **/
	int screenWidth = Pong.screenWidth;
	/** Screen Height **/
	int screenHeight = Pong.screenHeight;

	/** Coordinate x value **/
	public int x = screenWidth / 2;
	/** Coordinate y value **/
	public int y = screenHeight / 2;

	/**
	 * Center coordinate x value of the ball The CENTER of the ball is used for
	 * collision detection rather than the x and y values to make the collision
	 * detection less buggy. Since the <code> Graphics2D </code> draws an oval
	 * (the ball) from the top left position, it would be less realistic to only
	 * have collision detection on that one point at the top left corner of the
	 * ball instead of the center
	 **/
	public int centerx = x + (size.width / 2);
	/**
	 * Center coordinate y value of the ball The CENTER of the ball is used for
	 * collision detection rather than the x and y values to make the collision
	 * detection less buggy. Since the <code> Graphics2D </code> draws an oval
	 * (the ball) from the top left position, it would be less realistic to only
	 * have collision detection on that one point at the top left corner of the
	 * ball instead of the center
	 **/
	public int centery = y + (size.height / 2);

	/** Speed at which the ball is moving in the x-axis **/
	public int xspeed = 15;
	/** Speed at which the ball is moving in the y-axis **/
	public int yspeed = 0;

	/** Maximum X speed **/
	public int YMAX = 20;
	/** Maximum Y speed **/
	public int XMAX = 28;

	/** Update everything that needs to be updated every tick **/
	public void updateAll() {
		keepInMaxValues();
		keepInBounds();
		updatePosition();
		score();
	}

	/**
	 * Resets the ball's x and y values with <code> initx </code>, adding a
	 * random value to <code> xspeed </code> and <code> yspeed</code>
	 */
	private void spawn() {
		Random r = new Random();

		/*
		 * Resets the x, y, centerx, and centery variables
		 */
		x = screenWidth / 2;
		y = screenHeight / 2;
		updateCenter();
		int initx = r.nextInt(2);
		xspeed = (initx == 0) ? 5 : -5;
		yspeed = r.nextInt(11) + (-5);
	}

	/**
	 * Keeps the ball within maximum values, previously declared in the
	 * <code> Ball </code> class.
	 */
	private void keepInMaxValues() {
		if (yspeed < -YMAX)
			yspeed = -YMAX;
		if (xspeed < -XMAX)
			xspeed = -XMAX;

		if (yspeed > YMAX)
			yspeed = YMAX;
		if (xspeed > XMAX)
			xspeed = XMAX;
	}

	/**
	 * Moves the ball from one point to another with <code>xspeed</code> and
	 * <code>yspeed</code>
	 */
	private void updatePosition() {
		x += xspeed;
		y += yspeed;
		updateCenter();
	}

	/**
	 * Keeps the vall within the uppper and lower limit of the screen using
	 * <code> screenHeight </code>
	 * 
	 * @return Negative <code>yspeed </code>if <code> yspeed</code> is out of
	 *         bounds
	 */
	private void keepInBounds() {
		if (y < 10 || centery >= screenHeight) {
			yspeed = -yspeed;
			SoundHandler.handler.playSound(SoundHandler.BOUNCE);
		}
	}

	/**
	 * Evaluates the score if the ball goes past the player
	 */
	private void score() {

		if (centerx < -10) {
			Pong.p2score++;
			SoundHandler.handler.playSound(SoundHandler.SCORE);
			spawn();
		}
		if (centerx > screenWidth + 10) {
			Pong.p1score++;
			SoundHandler.handler.playSound(SoundHandler.SCORE);
			spawn();
		}
	}

	/**
	 * Updates the value of the center of the ball
	 */
	private void updateCenter() {
		centerx = x + (size.width / 2);
		centery = y + (size.height / 2);
	}
}

/** Player class **/
class Player {

	/** Coordinate x value of the player **/
	public int x;
	/** Coordinate y value of the player **/
	public int y;

	/**
	 * Sets the instance's <code> x </code> and <code> y </code> variables
	 * equals to the parameters
	 * 
	 * @param x
	 *            Player's x-coordinate
	 * @param y
	 *            Player's y-coordinate
	 */
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Used for simplicity when calling the player's <code> size </code>
	 */
	static Dimension size = new Dimension(30, 150);

	/**
	 * How fast the player moves across the screen
	 */
	static int speed = 8;

	/**
	 * Sets the speed of the players (notice - Player<b>s</b> not player) to the
	 * new speed
	 * 
	 * @param speed
	 *            Value of the new speed
	 */
	public static void setSpeed(int speed) {
		Player.speed = speed;
	}

}
