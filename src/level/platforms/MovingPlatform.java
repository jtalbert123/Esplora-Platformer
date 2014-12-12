package level.platforms;

import game.Collidable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Scanner;
import java.util.regex.Pattern;

import level.Level;

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
	
	private static final String totalRegex = "\\{(?:" + horizCombinedRegex
			+ "|" + vertCombinedRegex
			+ ") (?:(?:\\d+(?:\\.\\d+)?)|(?:\\.\\d+)?)\\}";
	
	/**
	 * The MovingPlatform string is as follows: {'axis' 'direction' 'speed'}
	 * where axis is one of the following:<br>h, horiz, horizontal, v, vert, or
	 * vertical<br>In all six cases the first letter may be capitalized. The
	 * direction is one of the following if the axis is horizontal (or h or
	 * horiz):<br>r, rt, right, l, lt, or left<br>Once again the first letter may be
	 * capitalized. If the axis is vertical the direction may be:<br>u, up, d, dn,
	 * or down<br>The speed is a decimal number greater than or equal to 0.
	 */
	public static final Pattern regex = Pattern.compile(totalRegex);

	/**
	 * The direction the platform is traveling.
	 */
	protected Direction direction;

	/**
	 * The speed the the platform will travel at. Units: (logical grid
	 * spaces)/second, <b>not pixels</b>. One logical grid space = {@link Platform#PLATFORM_WIDTH} or {@link Platform#PLATFORM_HEIGHT}
	 */
	protected double speed;

	public MovingPlatform(double x, double y, Direction direction, double speed) {
		this.x = x;
		this.y = y;
		this.type = 1;
		this.direction = direction;
		this.speed = speed;
	}

	public MovingPlatform() {
		this.x = -1;
		this.y = -1;
		this.type = -1;
		this.direction = null;
		this.speed = -1;
	}

	/**
	 * Creates a MovingPlatform as specified by str.
	 * 
	 *@param str
	 * the String to be parsed, it will match {@link MovingPlatform#regex}.
	 * @see Platform#makePlatform
	 * @see MovingPlatform#speed
	 */
	@Override
	public Collidable makePlatform(String str, Integer x, Integer y) {
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
	 * Returns if <b>this</b> should react to a collision with p. Firstly if
	 * there is no overlap, then the method returns false. Then if the collision
	 * is on the leading edge of the {@link level.platforms.Platform platform},
	 * it returns true. Always returns false if p is not tangible.
	 * 
	 * @see Platform#isCollidingWith(Platform)
	 */
	@Override
	public boolean isCollidingWith(Collidable c) {
		if (!super.isCollidingWith(c)) {
			return false;
		}
		Rectangle thisRect = this.getRect();
		Rectangle cRect = c.getRect();
		double xDisplacement = cRect.getCenterX() - thisRect.getCenterX();
		double yDisplacement = cRect.getCenterY() - thisRect.getCenterY();
		double angle = Math.atan2(-yDisplacement, xDisplacement);

		double angleURCorner = Math.atan2(thisRect.height, thisRect.width);
		double angleLRCorner = Math.atan2(-thisRect.height, thisRect.width);
		double angleLLCorner = Math.atan2(-thisRect.height, -thisRect.width);
		double angleULCorner = Math.atan2(thisRect.height, -thisRect.width);

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

	/**
	 * Determines if the MovintPlatform can move left, based solely upon the
	 * positions of other platforms.
	 * 
	 * @param level
	 *            the level that the MovingPlatform is in.
	 * @return the platform that is in the way, null if there is no blockage.
	 */
	protected Collidable leftBlocked(Level level) {
		for (Collidable p : level) {
			if (p != this) {
				if (this.isCollidingWith(p))
					return p;
			}
		}
		return null;
	}

	/**
	 * Determines if the MovintPlatform can move right, based solely upon the
	 * positions of other platforms.
	 * 
	 * @param level
	 *            the level that the MovingPlatform is in.
	 * @return the platform that is in the way, null if there is no blockage.
	 */
	protected Collidable rightBlocked(Level level) {
		for (Collidable p : level) {
			if (p != this) {
				if (this.isCollidingWith(p))
					return p;
			}
		}
		return null;
	}

	/**
	 * Determines if the MovintPlatform can move down, based solely upon the
	 * positions of other platforms.
	 * 
	 * @param level
	 *            the level that the MovingPlatform is in.
	 * @return the platform that is in the way, null if there is no blockage.
	 */
	protected Collidable downBlocked(Level level) {
		for (Collidable p : level) {
			if (p != this) {
				if (this.isCollidingWith(p)) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Determines if the MovintPlatform can move up, based solely upon the
	 * positions of other platforms.
	 * 
	 * @param level
	 *            the level that the MovingPlatform is in.
	 * @return the platform that is in the way, null if there is no blockage.
	 */
	protected Collidable upBlocked(Level level) {
		for (Collidable p : level) {
			if (p != this) {
				if (this.isCollidingWith(p)) {
					return p;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return (String.format("MovingPlatform at (%f, %f), moving %s", x, y,
				direction));
	}

	/**
	 * Updates the MovingPlatform by first updating it's direction and then
	 * updating it's position.
	 * 
	 * @param milliseconds
	 *            the interval since the last call to update.
	 * 
	 * @see #updateDir(Level)
	 * @see #updatePos(long)
	 */
	@Override
	public void update(long milliseconds, Level level) {
		updateDir(level);
		updatePos(milliseconds);
	}

	/**
	 * If this MovingPlatform has run into anther {@link Platform} in a head on
	 * collision, <b>this</b> is hitting something as opposed to being hit, the
	 * direction is reversed.
	 * 
	 * @param level
	 *            the level the MovingPlatform is in.
	 * @return the platform that <b>this</b> hit (or null).
	 */
	private Collidable updateDir(Level level) {
		Collidable blocking = null;
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

	/**
	 * Moves the platform the distance it should move according to {@link #speed} in the given time.
	 * @param milliseconds
	 * the elapsed milliseconds since the method was last called.
	 */
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

	/**
	 * Draws a black rounded rectangle with a smaller grey one inside.
	 * @see Platform#getRect()
	 */
	@Override
	public void draw(Graphics g) {
		Rectangle rect = getRect();
		g.fillRoundRect(0, 0, rect.width, rect.height, (int) rect.width / 5,
				(int) rect.height / 5);

		g.setColor(Color.darkGray);

		g.fillRoundRect(rect.width / 4, rect.height / 4, rect.width / 2,
				rect.height / 2, (int) rect.width / 10, (int) rect.height / 10);
	}

	private static enum Direction {
		UP, RIGHT, DOWN, LEFT
	}

	/**
	 * 
	 * @param dir
	 * @return
	 * the enum value representing the opposite direction of dir.
	 * @see Direction
	 */
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
	
	public double getXVelocity() {
		if (direction == Direction.UP || direction == Direction.DOWN) {
			return 0;
		} else if (direction == Direction.RIGHT) {
			return speed;
		} else {
			return -speed;
		}
	}
	
	public double getYVelocity() {
		if (direction == Direction.RIGHT || direction == Direction.LEFT) {
			return 0;
		} else if (direction == Direction.UP) {
			return speed;
		} else {
			return -speed;
		}
	}
	
	@Override
	public boolean tangible(Collidable c) {
		return true;
	}
}
