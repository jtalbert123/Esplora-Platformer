package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;

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
public class Game extends AbstractAction implements Action {

	/**
	 * The system time when the {@link #update()} method was last called.
	 */
	protected long time;

	/**
	 * The current {@link Level} being played.
	 */
	protected Level level;
	
	private boolean paused = false;

	/**
	 * Starts a new Game with the given level file.
	 * 
	 * @param fileName
	 *            the name of the file to use as a source for {@link #level}.
	 */
	public Game(String fileName) {
		try {
			level = new Level(fileName);
		} catch (IOException e) {
			level = null;
			e.printStackTrace();
		}
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
			level = new Level(fileName);
		} catch (IOException e) {
			level = temp;
			e.printStackTrace();
		}
	}

	/**
	 * Calls clearRect on the given {@link Graphics} object, necessary for items
	 * that will be moving (such as {@link MovingPlatform}). If used, it must be called before {@link #update()}.
	 * 
	 * @param g
	 *            the graphics object, should be the same graphics object passed
	 *            into {@link #draw(Graphics)} on it's last call.
	 */
	public void clearItems(Graphics g) {
		for (Platform p : level) {
			if (p.getClass() != Platform.class) {
				Rectangle r = p.getRect();
				g.clearRect(r.x, r.y, r.width, r.height);
			}
		}
	}

	/**
	 * Computes the time change and calls {@link Level#updateLevel(long)} on the
	 * level.
	 */
	public void update() {
		//System.out.println("Update: " + System.currentTimeMillis());
		if (!paused) {
			level.updateLevel(System.currentTimeMillis() - time);
			time = System.currentTimeMillis();
		}
	}

	/**
	 * Calls each item to draw it's representation. each {@link Platform} is limited to it's bounding rectangls for drawing.
	 * @param g
	 * 	the {@link Graphics} object to draw on.
	 */
	public void draw(Graphics g) {
		for (Platform p : level) {
			g.setColor(Color.black);
			Rectangle r = p.getRect();
			Graphics temp = g.create(r.x, r.y, r.width, r.height);
			p.draw(temp);
			temp.dispose();
		}
	}

	/**
	 * Returns the logical height and width of the level.
	 * @return a new rectangle width the width/height of the level.
	 */
	public Rectangle getBounds() {
		return new Rectangle(level.width, level.height);
	}

	// KeyboardListener
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getActionCommand());
	}
}
