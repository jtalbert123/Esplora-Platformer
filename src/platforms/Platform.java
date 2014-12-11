package platforms;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Pattern;

import level.Level;

public abstract class Platform {

	/**
	 * The width of a platform object, used for rendering and collision
	 * detection (getRect()).
	 */
	public static final int PLATFORM_WIDTH = 20 * 2;

	/**
	 * The height of a platform object, used for rendering and collision
	 * detection (getRect()).
	 */
	public static final int PLATFORM_HEIGHT = 15 * 2;

	/**
	 * The logical horizontal position of a platform (from left). Use getRect()
	 * for collision detection and rendering.
	 */
	protected double x;

	/**
	 * The logical vertical position of a platform (from top). Use getRect() for
	 * collision detection and rendering.
	 */
	protected double y;

	/**
	 * The type of platform, unused as of Dec. 10, 2014. Usable for quick
	 * identification of generic platform types.
	 */
	protected int type;

	/**
	 * Weather or not the platform can physically interact with other game
	 * objects. Example: a tangible platform can collide with a moving platform
	 * (causing a direction change), an intangible object cannot.
	 */
	protected boolean tangible;

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
	 * Initializes the types HashMap. Must be called once before getPlatform()
	 * is ever called.
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
	 *            by makePlatform() to position the new Platform object.
	 * @param y
	 * @return
	 */
	public static Platform getPlatform(String token, int x, int y) {
		Object objToUse = null;
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
		try {
			Method m = objToUse.getClass().getMethod("makePlatform",
					new Class[] { String.class, Integer.class, Integer.class });
			return (Platform) m.invoke(objToUse, token, x, y);
		} catch (NoSuchMethodException e) {
			throw new ClassCastException(String.format(
					"%s does not contain makePlatform.", objToUse.getClass()
							.getName()));
		} catch (SecurityException e) {
			throw new ClassCastException(String.format(
					"The makePlatform method of %s could not be accessed.",
					objToUse.getClass().getName()));
		} catch (IllegalAccessException e) {
			throw new ClassCastException(String.format(
					"The makePlatform method of %s could not be accessed.",
					objToUse.getClass().getName()));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Determines if this platform is colliding with the given rectangle in such
	 * a way that <b>this</b> platform should react (as defined in the update
	 * code for the platform. Probably used by update().
	 * 
	 * @param r
	 *            the rectangle that is being checked.
	 * @return true if they intersect in a way that would illicit a reaction
	 *         from <b>this</b> Platform.
	 */
	public boolean isCollidingWith(Rectangle r) {
		return getRect().intersects(r);
	}

	/**
	 * Checks for a collision with the given platform, use when evaluating any
	 * Platform collisions as it allows for more detailed analysis.
	 * 
	 * @param p
	 *            the platform that may be colliding with <b>this</b>
	 * @return true if they intersect in a way that would illicit a reaction
	 *         from <b>this</b> Platform.
	 */
	public boolean isCollidingWith(Platform p) {
		return p.tangible && getRect().intersects(p.getRect());
	}

	/**
	 * Gets the bounding rectangle of the Platform.
	 * 
	 * @return
	 */
	public Rectangle getRect() {
		return new Rectangle((int) (x * PLATFORM_WIDTH),
				(int) (y * PLATFORM_HEIGHT), (int) PLATFORM_WIDTH,
				(int) PLATFORM_HEIGHT);
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
	public abstract Platform makePlatform(String str, Integer x, Integer y);

	@Override
	public String toString() {
		return String.format("Platform at (%f, %f)", x, y);
	}

	/**
	 * draws the representation of the object in the top left corner of the
	 * provided graphics object
	 * 
	 * @param g
	 *            the graphics object to be used
	 */
	public abstract void draw(Graphics g);

	/**
	 * Updates the Platform object. Implementations will vary greatly among
	 * subclasses.
	 * 
	 * @param milliseconds
	 *            the current system time, not the interval.
	 * @param level
	 *            the level the platform is in, necessarry for collision
	 *            checking.
	 */
	public abstract void update(long currentTime, Level level);
}
