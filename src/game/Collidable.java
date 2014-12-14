package game;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

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
	 * Checks for a collision with the given Collidable c. Note that this method
	 * depends on {@link #tangible(Collidable)}, and as such is not commutative.
	 * Even when the {@link #tangible(Collidable)} methods are commutative for
	 * the <b>this</b> and c, the implementation may still not be commutative.
	 * 
	 * @param p
	 *            the platform that may be colliding with <b>this</b>
	 * @return true if they intersect in a way that would illicit a reaction
	 *         from <b>this</b> Platform.
	 * @see #isCollidingWith(Rectangle)
	 */
	public abstract boolean isCollidingWith(Collidable c);

	/**
	 * Determines if c should illicit a reaction from <b>this</b> (if they were
	 * to actually collide). Note that as with
	 * {@link #isCollidingWith(Collidable)}, a.tangible(b) may not ==
	 * b.tangible(a).
	 * 
	 * @return
	 */
	public boolean tangible(Collidable c);

	/**
	 * Returns the logical bounds of the object. Everything is in terms of the
	 * logical grid in level.
	 * 
	 * @return
	 */
	public Rectangle2D getLogicalBounds();
	
	public double getYVelocity();
	public double getXVelocity();
	
	public double getWidth();
	public double getHeight();

}