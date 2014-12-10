package level;

import java.util.Scanner;

public class MovingPlatform extends Platform {
	protected static double THRESHOLD = .1;

	private static String horizontalRegex = "[hH](?:oriz(?:ontal)?)?";
	private static String verticalRegex = "[vV](?:ert(?:ical)?)?";
	// above works
	private static String rightRegex = "(?:[rR](?:ight|t)?)|1";
	private static String leftRegex = "([lL](eft|t)?)|-1";
	private static String upRegex = "([uU]p?)|1";
	private static String downRegex = "(?:[dD](?:(?:own)|n)?)|-1";
	private static String horizCombinedRegex = horizontalRegex + " (?:"
			+ rightRegex + "|" + leftRegex + ")";
	private static String vertCombinedRegex = verticalRegex + " (?:" + upRegex
			+ "|" + downRegex + ")";
	public static final String totalRegex = "\\{(?:" + horizCombinedRegex + "|"
			+ vertCombinedRegex + ") \\d+(?:\\.\\d+)?\\}";
	// true for horizontal
	// false for vertical
	boolean axis;
	// true for right/up
	// false for left/down
	boolean direction;
	// units per second
	double speed;

	public MovingPlatform(double x, double y, int type, boolean direction,
			boolean axis, double speed) {
		super(x, y, type);
		this.direction = direction;
		this.axis = axis;
		this.speed = speed;
	}

	@Override
	public Platform makePlatform(String str, Integer x, Integer y) {
		Scanner scan = new Scanner(str.substring(1, str.length() - 1));
		scan.useDelimiter(" ");
		String axis = scan.next();
		String direction = scan.next();
		double speed = scan.nextDouble();
		if (axis.matches(horizontalRegex)) {
			if (direction.matches(rightRegex)) {
				scan.close();
				return new MovingPlatform(x, y, 1, true, true, speed);
			}
			if (direction.matches(leftRegex)) {
				scan.close();
				return new MovingPlatform(x, y, 1, false, true, speed);
			}
		}
		if (axis.matches(verticalRegex)) {
			if (direction.matches(upRegex)) {
				scan.close();
				return new MovingPlatform(x, y, 1, true, false, speed);
			}
			if (direction.matches(downRegex)) {
				scan.close();
				return new MovingPlatform(x, y, 1, false, false, speed);
			}
		}

		scan.close();
		return null;
	}

	@Override
	public String toString() {
		String dir = (axis) ? ((direction) ? "Right" : "Left")
				: ((direction) ? "Up" : "Down");
		return (String.format("Platform at (%f, %f), moving %s", x, y, dir));
	}

	@Override
	public void Update(long milliseconds, Level level) {
		super.Update(milliseconds, level);
		double time = ((double) milliseconds) / 1000.0;

		// pseudocode
		// if this can move in it's direction, without hitting anything, do so.
		// else, reverse direction

		if (axis) {
			// horizontal
			if (direction) {
				// right
				direction = !this.rightBlocked(level);
				// left
			} else {
				direction = this.leftBlocked(level);
			}
			// vertical
		} else {
			if (direction) {
				// up
				if (this.upBlocked(level)) {
					direction = false;
					System.out.println("Up blocked.");
				}
			} else {
				// down
				direction = this.downBlocked(level);
			}
		}
		if (axis) {
			// horizontal
			if (direction) {
				// right

				if (x >= level.width) {
					direction = false;
				} else {
					x += speed * time;
				}
				// left
			} else {
				if (x <= 0) {
					direction = true;
				} else {
					x -= speed * time;
				}
			}
			// vertical
		} else {
			if (direction) {
				// up
				if (y <= 0) {
					direction = false;
				} else {
					y -= speed * time;
				}
			} else {
				// down
				if (y >= level.height) {
					direction = true;
				} else {
					y += speed * time;
				}
			}
		}
	}
}
