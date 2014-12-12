package game;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface Drawable {

	/**
	 * draws the representation of the object in the top left corner of the
	 * provided graphics object
	 * 
	 * @param g
	 *            the graphics object to be used
	 */
	public void draw(Graphics g);
	
	/**
	 * Gets the bounding {@link Rectangle} of the Platform.
	 * 
	 * @return the bounding rectangle of the platform in pixels <b>not logical grid spaces</b>.
	 */
	public Rectangle getRect();
}
