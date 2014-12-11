package level;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Pattern;

public abstract class Platform {

	public static final int PLATFORM_WIDTH = 20 * 2;
	public static final int PLATFORM_HEIGHT = 15 * 2;

	public double x;
	public double y;
	public int type;
	public boolean tangible;
	private static HashMap<Pattern, Object> types;

	public static void setUp() {
		types = new HashMap<Pattern, Object>();
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

	public static Platform getPlatform(String str, int x, int y) {
		Object objToUse = null;
		System.out.println(str);
		for (Pattern p : types.keySet()) {
			System.out.println("\t" + p);
			if (p.matcher(str).matches()) {
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
			return (Platform) m.invoke(objToUse, str, x, y);
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

	public boolean isCollidingWith(Rectangle r) {
		return getRect().intersects(r);
	}

	public boolean isCollidingWith(Platform p) {
		return p.tangible && getRect().intersects(p.getRect());
	}

	protected Platform leftBlocked(Level level) {
		for (Platform p : level) {
			if (p != this) {
				if (this.isCollidingWith(p))
					return p;
			}
		}
		return null;
	}

	protected Platform rightBlocked(Level level) {
		for (Platform p : level) {
			if (p != this) {
				if (this.isCollidingWith(p))
					return p;
			}
		}
		return null;
	}

	protected Platform downBlocked(Level level) {
		for (Platform p : level) {
			if (p != this) {
				if (this.isCollidingWith(p)) {
					return p;
				}
			}
		}
		return null;
	}

	protected Platform upBlocked(Level level) {
		for (Platform p : level) {
			if (p != this) {
				if (this.isCollidingWith(p)) {
					return p;
				}
			}
		}
		return null;
	}

	public Rectangle getRect() {
		return new Rectangle((int) (x * PLATFORM_WIDTH),
				(int) (y * PLATFORM_HEIGHT), (int) PLATFORM_WIDTH,
				(int) PLATFORM_HEIGHT);
	}

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

	public abstract void update(long milliseconds, Level level);
}
