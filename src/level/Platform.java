package level;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Platform {

	public static final int PLATFORM_WIDTH = 20 * 2;
	public static final int PLATFORM_HEIGHT = 15 * 2;

	public double x;
	public double y;
	public int type;
	private static HashMap<Pattern, Object> types;

	public static void setUp() {
		types = new HashMap<Pattern, Object>();
		// Copy the line and change the character and the name of the class that
		// should handle construction of the platform

		// stable platform
		types.put(Pattern.compile("-"), new Platform());
		// Moving platform
		types.put(Pattern.compile(MovingPlatform.totalRegex),
				new MovingPlatform());
		// potential regex for teleport (to specific location):
		// "\\{[Tt](?:ele(?:port)?)? +\\d+ +\\d+\\}"
	}

	public static Platform getPlatform(String str, int x, int y) {
		Object objToUse = null;
		for (Pattern p : types.keySet()) {
			if (p.matcher(str).matches()) {
				// System.out.println(types.get(p).getClass().getName());
				objToUse = types.get(p);
				break;
			}
		}
		if (objToUse == null) {
			// System.out.println("The pattern was not recognised.");
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
		return getRect().intersects(p.getRect());
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
//					System.out.printf("Collision:\n\t%s\n\t%s\n", this, p);
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

	public Platform makePlatform(String str, Integer x, Integer y) {
		if (str.equals("-")) {
			return new Platform(x, y, 0);
		}
		return null;
	}

	protected Platform(double x, double y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	protected Platform() {
		x = -1;
		y = -1;
		type = -1;
	}

	@Override
	public String toString() {
		if (type == 0)
			return String.format("'-' at (%f, %f)", x, y);
		// should never happen
		else
			return "Unknown";
	}

	public void draw(Graphics g) {
		Rectangle rect = getRect();
		g.fillRoundRect(rect.x, rect.y, rect.width, rect.height,
				(int) rect.width / 5, (int) rect.height / 5);
	}

	public void Update(long milliseconds, Level level) {
		return;
	}
}
