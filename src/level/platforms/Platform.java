package level.platforms;

import game.Collidable;
import game.Drawable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.regex.Pattern;

import level.Level;

/**
 * Represents a platform object with enough information for most uses.
 * 
 * @author James Talbert
 *
 */
public abstract class Platform implements Collidable, Drawable {

	/**
	 * The width of a platform object, used for rendering and collision
	 * detection ({@link #getRect()}).
	 */
	public static final int WIDTH = Level.CELL_WIDTH;

	/**
	 * The height of a platform object, used for rendering and collision
	 * detection ({@link #getRect()}).
	 */
	public static final int HEIGHT = Level.CELL_HEIGHT;

	/**
	 * The logical horizontal position of a platform (from left). Use
	 * {@link #getRect()} for collision detection and rendering.
	 */
	protected double x;

	/**
	 * The logical vertical position of a platform (from top). Use
	 * {@link #getRect()} for collision detection and rendering.
	 */
	protected double y;

	/**
	 * The type of platform, unused as of Dec. 10, 2014. Usable for quick
	 * identification of generic platform types.
	 */
	protected int type;

	/**
	 * The list of all types of platforms that can be created from the level
	 * file. The Regular Expression associated with each one should be specific
	 * enough that it will match only the items that it can evaluate. The
	 * Platform should be a instance of the desired platform created with a
	 * 0-argument constructor. Reflect is used to invoke the makePlatform method
	 * on the given platform, makePlatform must be defined (guaranteed by this
	 * abstract class's inclusion of it as an abstract method and the
	 * requirement that the value in the map be an instantiated object).
	 */
	private static HashMap<Pattern, Platform> types;

	/**
	 * Initializes the types HashMap. Must be called once before
	 * {@link #getPlatform(String, int, int) getPlatform(...)} is ever called.
	 */
	public static void setUp() {
		types = new HashMap<Pattern, Platform>();
		// Copy the line and change the character and the name of the class that
		// should handle construction of the platform

		// stable platform
		types.put(Pattern.compile("-"), new BasicPlatform());
		// Moving platform
		types.put(MovingPlatform.regex, new MovingPlatform());
		types.put(Spawn.regex, new Spawn());
		types.put(MovingPlatformStop.regex, new MovingPlatformStop());
		// potential regex for teleport (to specific location):
		// "\\{[Tt](?:ele(?:port)?)? +\\d+ +\\d+\\}"
	}

	/**
	 * Looks in the types HashMap for a match for the token, and if a match is
	 * found, returns the platform created by makePlatform.
	 * 
	 * @param token
	 *            the string to be matched and evaluated into a platform.
	 * @param x
	 *            the current horizontal location in the level file, may be used
	 *            by {@link #makePlatform(String, Integer, Integer)
	 *            makePlatform(...)} to position the new Platform object.
	 * @param y
	 * @return the platform specified by the token
	 */
	public static Collidable getPlatform(String token, int x, int y) {
		Platform objToUse = null;
		// System.out.println(str);
		for (Pattern p : types.keySet()) {
			// System.out.println("\t" + p);
			if (p.matcher(token).matches()) {
				objToUse = types.get(p);
				break;
			}
		}
		if (objToUse == null) {
			return null;
		}
		return objToUse.makePlatform(token, x, y);
	}

	/**
	 * Determines if this platform is colliding with the given rectangle in such
	 * a way that <b>this</b> platform should react (as defined in the update
	 * code for the platform. Probably used by {@link #update(long, Level)
	 * update(...)}.
	 * 
	 * @param r
	 *            the rectangle that is being checked.
	 * @return true if they intersect in a way that would illicit a reaction
	 *         from <b>this</b> Platform.
	 * @deprecated use isCollidingWith(Collidable c)
	 */
	public boolean isCollidingWith(Rectangle r) {
		return getRect().intersects(r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see level.platforms.Collidable#isCollidingWith(level.platforms.Platform)
	 */
	@Override
	public boolean isCollidingWith(Collidable c) {
		return c.tangible(this)
				&& getLogicalBounds().intersects(c.getLogicalBounds());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see level.platforms.Collidable#getRect()
	 */
	@Override
	public Rectangle getRect() {
		return new Rectangle((int) (x * Level.CELL_WIDTH),
				(int) (y * Level.CELL_HEIGHT), (int) WIDTH, (int) HEIGHT);
	}

	@Override
	public Rectangle2D getLogicalBounds() {
		return new Rectangle2D.Double(x, y, ((double) WIDTH)
				/ ((double) Level.CELL_WIDTH), ((double) HEIGHT)
				/ ((double) Level.CELL_HEIGHT));
	}

	/**
	 * Parses the input and makes a new Platform from the String.
	 * 
	 * @param str
	 *            the String to be parsed, matched by the subclasses
	 *            "static final String regex".
	 * @param x
	 *            the location that the token occurred in the level file.
	 * @param y
	 *            the location that the token occurred in the level file.
	 * @return the new platform.
	 */
	public abstract Collidable makePlatform(String str, Integer x, Integer y);

	@Override
	public String toString() {
		return String.format("Platform at (%f, %f)", x, y);
	}

	public abstract void draw(Graphics g);

	public abstract void update(long currentTime, Level level);

	public double getXVelocity() {
		return 0;
	}

	public double getYVelocity() {
		return 0;
	}
}
