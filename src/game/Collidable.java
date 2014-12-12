package game;

import java.awt.Rectangle;

import level.Level;

public interface Collidable {

	/**
	 * Updates the Drawable object. Implementations will vary greatly among
	 * subclasses.
	 * 
	 * @param currentTime
	 *            the current system time, not the interval.
	 * @param level
	 *            the level the platform is in, necessary for collision
	 *            checking.
	 */
	public void update(long elapsedTime, Level level);

	/**
	 * Checks for a collision with the given platform, use when evaluating any
	 * Platform collisions as it allows for more detailed analysis.
	 * 
	 * @param p
	 *            the platform that may be colliding with <b>this</b>
	 * @return true if they intersect in a way that would illicit a reaction
	 *         from <b>this</b> Platform.
	 * @see #isCollidingWith(Rectangle)
	 */
	public abstract boolean isCollidingWith(Collidable c);

	/**
	 * Gets the bounding {@link Rectangle} of the Platform.
	 * 
	 * @return the bounding rectangle of the platform in pixels <b>not logical
	 *         grid spaces</b>.
	 */
	public abstract Rectangle getRect();

	public boolean tangible();

}