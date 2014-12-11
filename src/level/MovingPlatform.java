package level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Scanner;
import java.util.regex.Pattern;

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
	private static final String totalRegex = "\\{(?:" + horizCombinedRegex + "|"
			+ vertCombinedRegex + ") (?:(?:\\d+(?:\\.\\d+)?)|(?:\\.\\d+)?)\\}";
	public static final Pattern regex = Pattern.compile(totalRegex);
	
	Direction direction;
	// units per second
	double speed;

	public MovingPlatform(double x, double y, Direction direction,
			double speed) {
		this.x = x;
		this.y = y;
		this.type = 1;
		this.direction = direction;
		this.speed = speed;
	}

	public MovingPlatform() {
		type = -1;
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
				return new MovingPlatform(x, y, Direction.RIGHT, speed);
			}
			if (direction.matches(leftRegex)) {
				scan.close();
				return new MovingPlatform(x, y, Direction.LEFT, speed);
			}
		}
		if (axis.matches(verticalRegex)) {
			if (direction.matches(upRegex)) {
				scan.close();
				return new MovingPlatform(x, y, Direction.UP, speed);
			}
			if (direction.matches(downRegex)) {
				scan.close();
				return new MovingPlatform(x, y, Direction.DOWN, speed);
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
		double angle = Math.atan2(-yDisplacement, xDisplacement);

		double angleURCorner = Math.atan2(PLATFORM_HEIGHT, PLATFORM_WIDTH);
		double angleLRCorner = Math.atan2(-PLATFORM_HEIGHT, PLATFORM_WIDTH);
		double angleLLCorner = Math.atan2(-PLATFORM_HEIGHT, -PLATFORM_WIDTH);
		double angleULCorner = Math.atan2(PLATFORM_HEIGHT, -PLATFORM_WIDTH);

		if (this.direction == Direction.UP) {
			if (angle <= angleULCorner && angle >= angleURCorner) {
				return true;
			}
		} else if (this.direction == Direction.RIGHT) {
			if (angle <= angleURCorner && angle >= angleLRCorner) {
				return true;
			}
		} else if (this.direction == Direction.DOWN) {
			if (angle <= angleLRCorner && angle >= angleLLCorner) {
				return true;
			}
		} else if (this.direction == Direction.LEFT) {
			if (angle <= angleLLCorner || angle >= angleULCorner) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return (String.format("MovingPlatform at (%f, %f), moving %s", x, y,
				direction));
	}

	@Override
	public void update(long milliseconds, Level level) {
		updateDir(level);
		updatePos(milliseconds);
	}

	private Platform updateDir(Level level) {
		Platform blocking = null;
		if (direction == Direction.UP) {
			blocking = upBlocked(level);
			if (y <= 0 || blocking != null) {
				direction = Direction.DOWN;
			}
		} else if (direction == Direction.DOWN) {
			blocking = downBlocked(level);
			if (y >= level.height || blocking != null) {
				direction = Direction.UP;
			}
		} else if (direction == Direction.RIGHT) {
			blocking = rightBlocked(level);
			if (x >= level.width || blocking != null) {
				direction = Direction.LEFT;
			}
		} else if (direction == Direction.LEFT) {
			blocking = leftBlocked(level);
			if (x <= 0 || blocking != null) {
				direction = Direction.RIGHT;
			}
		}
		if (blocking != null && blocking.getClass() == MovingPlatform.class
				&& ((MovingPlatform) blocking).direction == this.direction) {
			((MovingPlatform) blocking).direction = opposite(((MovingPlatform) blocking).direction);
		}
		return blocking;
	}

	private void updatePos(long milliseconds) {
		double time = milliseconds / 1000.0;
		if (direction == Direction.UP) {
			y -= time * speed;
		} else if (direction == Direction.DOWN) {
			y += time * speed;
		} else if (direction == Direction.RIGHT) {
			x += time * speed;
		} else if (direction == Direction.LEFT) {
			x -= time * speed;
		}
	}

	@Override
	public void draw(Graphics g) {
		Rectangle rect = getRect();
		g.fillRoundRect(0, 0, rect.width, rect.height, (int) rect.width / 5,
				(int) rect.height / 5);
		
		g.setColor(Color.darkGray);
		
		g.fillRoundRect(rect.width / 4, rect.height / 4,
				rect.width / 2, rect.height / 2, (int) rect.width / 10,
				(int) rect.height / 10);
	}

	private static enum Direction {
		UP, RIGHT, DOWN, LEFT
	}

	private static Direction opposite(Direction dir) {
		if (dir == Direction.UP) {
			return Direction.DOWN;
		} else if (dir == Direction.DOWN) {
			return Direction.UP;
		} else if (dir == Direction.RIGHT) {
			return Direction.LEFT;
		} else {
			return Direction.RIGHT;
		}
	}
}
