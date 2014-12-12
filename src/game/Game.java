package game;

import interfaces.KeyboardState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import level.Level;
import level.platforms.MovingPlatform;
import level.platforms.Platform;

/**
 * This class represents a single game of the platformer being played. it stores
 * the level and handles update time tracking.
 * 
 * @author James Talbert
 *
 */
public class Game {

	/**
	 * The system time when the {@link #update()} method was last called.
	 */
	protected long time;

	/**
	 * The current {@link Level} being played.
	 */
	protected Level level;

	/**
	 * Whether the game is paused or not. Initially false (not paused).
	 */
	private boolean paused = false;

	/**
	 * Is the game over.
	 */
	boolean gameOver;

	/**
	 * The state of the keyboard after the last call to update.
	 */
	KeyboardState oldKeyboard;
	
	/**
	 * The place to draw the next frame.
	 */
	BufferedImage nextFrame;

	/**
	 * Starts a new Game with the given level file.
	 * 
	 * @param fileName
	 *            the name of the file to use as a source for {@link #level}.
	 */
	public Game(String fileName) {
		oldKeyboard = KeyboardState.getKeyboardState();
		try {
			level = new Level(fileName, this);
		} catch (IOException e) {
			level = null;
			e.printStackTrace();
		}
		nextFrame = new BufferedImage((level.width + 1)*Level.CELL_WIDTH, (level.height + 1)*Level.CELL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		time = System.currentTimeMillis();
	}

	/**
	 * Loads a new {@link Level} into the Game, the old {@link #level} will be
	 * disposed of by the garbage collector. If the level file cannot be found
	 * or parsed, the level is unchanged.
	 * 
	 * @param fileName
	 *            the level text file to load into {@link #level}.
	 */
	public void loadLevel(String fileName) {
		Level temp = level;
		try {
			level = new Level(fileName, this);
		} catch (IOException e) {
			level = temp;
			e.printStackTrace();
		}
	}

	/**
	 * Calls clearRect on the given {@link Graphics} object, necessary for items
	 * that will be moving (such as {@link MovingPlatform}). If used, it must be
	 * called before {@link #update()}.
	 * 
	 * @param g
	 *            the graphics object, should be the same graphics object passed
	 *            into {@link #draw(Graphics)} on it's last call.
	 */
	public void clearItems() {
		Graphics g = nextFrame.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, nextFrame.getWidth(), nextFrame.getHeight());
		g.dispose();
	}

	/**
	 * Computes the time change and calls {@link Level#updateLevel(long)} on the
	 * level.
	 */
	public void update() {
		// check keyboard state deal with it.
		KeyboardState keyboard = KeyboardState.getKeyboardState();
		if (keyboard.isKeyDown("r") && oldKeyboard.isKeyUp("r")) {
			loadLevel("level1.txt");
			paused = false;
			gameOver = false;
		}
		if (keyboard.isKeyDown("p") && oldKeyboard.isKeyUp("p")) {
			if (paused) {
				time = System.currentTimeMillis();
			}
			paused = !paused;
		}
		if (!paused) {
			long elapsedTime = System.currentTimeMillis() - time;
			level.updateLevel(elapsedTime);
			time = System.currentTimeMillis();
		}
		oldKeyboard = keyboard;
	}

	/**
	 * Calls each item to draw it's representation. each {@link Platform} is
	 * limited to it's bounding rectangles for drawing.
	 * 
	 * @param g
	 *            the {@link Graphics} object to draw on.
	 */
	public void draw(Graphics canvas) {
		Graphics g = nextFrame.getGraphics();
		if (!gameOver) {
			for (Drawable p : level.getAll(Drawable.class)) {
				g.setColor(Color.black);
				Rectangle r = p.getRect();
				Graphics temp = g.create(r.x, r.y, r.width, r.height);
				p.draw(temp);
				temp.dispose();
			}
		} else {
			g.clearRect(0, 0, (level.width + 1) * Level.CELL_WIDTH,
					(level.height + 1) * Level.CELL_HEIGHT);
			g.drawString("Game Over", 0, level.height * Level.CELL_HEIGHT / 2);
		}
		canvas.drawImage(nextFrame, 0, 0, new ImageObserver() {
			
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y,
					int width, int height) {
				return true;
			}
		});
	}

	/**
	 * Returns the logical height and width of the level.
	 * 
	 * @return a new rectangle width the width/height of the level.
	 */
	public Rectangle getBounds() {
		return new Rectangle(level.width, level.height);
	}

	public void onDeath() {
		// paused = true;
		gameOver = true;
	}
}
