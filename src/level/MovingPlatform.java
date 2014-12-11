package level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
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
			+ vertCombinedRegex + ") (?:(?:\\d+(?:\\.\\d+)?)|(?:\\.\\d+)?)\\}";

	Direction direction;
	// units per second
	double speed;

	public MovingPlatform(double x, double y, int type, Direction direction,
			double speed) {
		super(x, y, type);
		this.direction = direction;
		this.speed = speed;
	}

	public MovingPlatform() {
		super();
		direction = Direction.UP;
		speed = 0;
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
				return new MovingPlatform(x, y, 1, Direction.RIGHT, speed);
			}
			if (direction.matches(leftRegex)) {
				scan.close();
				return new MovingPlatform(x, y, 1, Direction.LEFT, speed);
			}
		}
		if (axis.matches(verticalRegex)) {
			if (direction.matches(upRegex)) {
				scan.close();
				return new MovingPlatform(x, y, 1, Direction.UP, speed);
			}
			if (direction.matches(downRegex)) {
				scan.close();
				return new MovingPlatform(x, y, 1, Direction.DOWN, speed);
			}
		}

		scan.close();
		return null;
	}

	/**
	 * Returns true if p will collide with this.
	 */
	@Override
	public boolean isCollidingWith(Platform p) {
		boolean intersects = getRect().intersects(p.getRect());
		if (!intersects) {
			return false;
		}
		if (p.getClass() != MovingPlatform.class) {
			return true;
		}
		// MovingPlatform pm = (MovingPlatform)p;
		Rectangle thisRect = this.getRect();
		Rectangle pRect = p.getRect();
		double xDisplacement = pRect.getCenterX() - thisRect.getCenterX();
		double yDisplacement = pRect.getCenterY() - thisRect.getCenterY();
		// System.out.println(thisRect.getCenterY() - pRect.getCenterY());
		// System.out.println(thisRect + "\n" + pRect);
		double angle = Math.atan2(-yDisplacement, xDisplacement);

		double angleURCorner = Math.atan2(PLATFORM_HEIGHT, PLATFORM_WIDTH);
		double angleLRCorner = Math.atan2(-PLATFORM_HEIGHT, PLATFORM_WIDTH);
		double angleLLCorner = Math.atan2(-PLATFORM_HEIGHT, -PLATFORM_WIDTH);
		double angleULCorner = Math.atan2(PLATFORM_HEIGHT, -PLATFORM_WIDTH);

		/*
		 * System.out.printf("angleURCorner: %f\n" + "angleULCorner: %f\n" +
		 * "angleLRCorner: %f\n" + "angleLLCorner: %f\n", angleURCorner/Math.PI,
		 * angleULCorner/Math.PI, angleLRCorner/Math.PI, angleLLCorner/Math.PI);
		 */
		System.out.println("Angle: " + angle / Math.PI);
		if (this.direction == Direction.UP) {
			if (angle <= angleULCorner && angle >= angleURCorner) {
				System.out.println("collision");
				return true;
			}
		} else if (this.direction == Direction.RIGHT) {
			if (angle <= angleURCorner && angle >= angleLRCorner) {
				System.out.println("collision");
				return true;
			}
		} else if (this.direction == Direction.DOWN) {
			if (angle <= angleLRCorner && angle >= angleLLCorner) {
				System.out.println("collision");
				return true;
			}
		} else if (this.direction == Direction.LEFT) {
			if (angle <= angleLLCorner && angle >= angleULCorner) {
				System.out.println("collision");
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return (String.format("Platform at (%f, %f), moving %s", x, y,
				direction));
	}

	@Override
	public void Update(long milliseconds, Level level) {
		super.Update(milliseconds, level);
		double time = ((double) milliseconds) / 1000.0;

		// pseudocode
		// if this can move in it's direction, without hitting anything, do so.
		// else, reverse direction
		Platform blocking = null;
		if (direction == Direction.UP) {
			blocking = upBlocked(level);
			if (y <= 0 || blocking != null) {
				direction = Direction.DOWN;
				y += time * speed;
				if (blocking != null && blocking.getClass() == MovingPlatform.class
						&& ((MovingPlatform) blocking).direction == Direction.DOWN) {
					((MovingPlatform) blocking).direction = Direction.UP;
					blocking.y -= time * ((MovingPlatform) blocking).speed;
				}
			} else {
				y -= time * speed;
			}
		} else if (direction == Direction.DOWN) {
			blocking = downBlocked(level);
			if (y >= level.height || blocking != null) {
				direction = Direction.UP;
				y -= time * speed;
				if (blocking != null && blocking.getClass() == MovingPlatform.class
						&& ((MovingPlatform) blocking).direction == Direction.UP) {
					((MovingPlatform) blocking).direction = Direction.DOWN;
					blocking.y += time * ((MovingPlatform) blocking).speed;
				}
			} else {
				y += time * speed;
			}
		} else if (direction == Direction.RIGHT) {
			blocking = rightBlocked(level);
			if (x >= level.width || blocking != null) {
				direction = Direction.LEFT;
				x += time * speed;
				if (blocking != null && blocking.getClass() == MovingPlatform.class
						&& ((MovingPlatform) blocking).direction == Direction.LEFT) {
					((MovingPlatform) blocking).direction = Direction.RIGHT;
					blocking.x += time * ((MovingPlatform) blocking).speed;
				}
			} else {
				x += time * speed;
			}
		} else if (direction == Direction.LEFT) {
			blocking = leftBlocked(level);
			if (x <= 0 || blocking != null) {
				direction = Direction.RIGHT;
				x += time * speed;
				if (blocking != null && blocking.getClass() == MovingPlatform.class
						&& ((MovingPlatform) blocking).direction == Direction.RIGHT) {
					((MovingPlatform) blocking).direction = Direction.LEFT;
					blocking.x -= time * ((MovingPlatform) blocking).speed;
				}
			} else {
				x -= time * speed;
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(Color.darkGray);
		Rectangle rect = getRect();
		g.fillRoundRect(rect.x + rect.width / 4, rect.y + rect.height / 4,
				rect.width / 2, rect.height / 2, (int) rect.width / 10,
				(int) rect.height / 10);
	}

	private static enum Direction {
		UP, RIGHT, DOWN, LEFT

	}
}
