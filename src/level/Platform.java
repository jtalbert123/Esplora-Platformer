package level;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Platform {
	public double x;
	public double y;
	public int type;
	private static HashMap<Pattern, Object> types;

	public static void setUp() {
		types = new HashMap<Pattern, Object>();
		// Copy the line and change the character and the name of the class that
		// should handle construction of the platform

		// stable platform
		types.put(Pattern.compile("-"), new Platform(-1, -1, -1));
		// Moving platform
		types.put(Pattern.compile(MovingPlatform.totalRegex), new MovingPlatform(-1, -1, -1,
				false, false, 0));
		//potential regex for teleport (to specific location):
		//"\\{[Tt](?:ele(?:port)?)? +\\d+ +\\d+\\}"
	}

	public static Platform getPlatform(String str, int x, int y) {
		Object objToUse = null;
		for (Pattern p : types.keySet()) {
			if (p.matcher(str).matches()) {
				System.out.println(types.get(p).getClass().getName());
				objToUse = types.get(p);
				break;
			}
		}
		if (objToUse == null) {
			//System.out.println("The pattern was not recognised.");
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

	@Override
	public String toString() {
		if (type == 0)
			return String.format("'-' at (%d, %d)", x, y);
		// should never happen
		else
			return "Unknown";
	}

	public void Update(long milliseconds, Level level) {
		return;
	}
}
