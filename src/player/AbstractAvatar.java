package player;

import game.Collidable;
import game.Drawable;
import interfaces.KeyboardState;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import level.Level;

public abstract class AbstractAvatar implements Drawable, Collidable {
	protected static final int WIDTH = 30;
	protected static final int HEIGHT = 30;

	protected static final double GRAVITATIONAL_ACCELERATION = 9.8;
	protected static final double DAMPING = .2;

	protected double x;
	protected double y;

	protected double xVelocity;
	protected double yVelocity;

	protected double xAcceleration;
	protected double yAcceleration;

	boolean alive;
	boolean canJump;

	KeyboardState oldKeyboard;

	public AbstractAvatar() {
		x = 0;
		y = 0;

		xVelocity = 0;
		yVelocity = 0;

		xAcceleration = 0;
		yAcceleration = 0;

		alive = true;
		canJump = true;
		oldKeyboard = KeyboardState.getKeyboardState();
	}

	public AbstractAvatar(double x, double y) {
		this.x = x;
		this.y = y;

		xVelocity = 0;
		yVelocity = 0;

		xAcceleration = 0;
		yAcceleration = 0;

		alive = true;
		canJump = true;
		oldKeyboard = KeyboardState.getKeyboardState();
	}

	@Override
	public void update(long elapsedTime, Level level) {
		double time = elapsedTime / 1000.0;
		Collidable up = platformAbove(level);
		Collidable down = platformBelow(level);
		Collidable right = platformRight(level);
		Collidable left = platformLeft(level);

		KeyboardState keyboard = KeyboardState.getKeyboardState();

		if ((keyboard.isKeyDown("w") || keyboard.isKeyDown(" ")) && canJump) {
			yVelocity = -8;
			y -= .25;
			canJump = false;
		}
		if (down == null) {
			yAcceleration = GRAVITATIONAL_ACCELERATION;
		} else {
			yAcceleration = 0;
			if (yVelocity > down.getYVelocity()) {
				yVelocity = down.getYVelocity() - yVelocity / 2;
			} else {
				yVelocity = down.getYVelocity();
			}
			if (!canJump) {
				y = down.getLogicalBounds().getY() - ((double) HEIGHT)
						/ ((double) Level.CELL_HEIGHT) - .1;
			}
			canJump = true;
		}
		if (up != null) {
			if (yVelocity < up.getYVelocity()) {
				yVelocity = up.getYVelocity() - yVelocity / 2;
			}
		}

		y += yVelocity * time;
		yVelocity += yAcceleration * time;

		if (keyboard.isKeyDown("d") && keyboard.isKeyUp("a")) {
			xAcceleration = 50;
		} else if (keyboard.isKeyDown("a") && keyboard.isKeyUp("d")) {
			xAcceleration = -50;
		} else {
			xAcceleration = 0;
		}

		if (right != null) {
			xVelocity = Math.min(right.getXVelocity(), xVelocity);
			xAcceleration = Math.min(xAcceleration, 0);
		}
		if (left != null) {
			xVelocity = Math.max(left.getXVelocity(), xVelocity);
			xAcceleration = Math.max(xAcceleration, 0);
		}

		x = x + xVelocity * time;
		xVelocity = xVelocity * (1 - DAMPING) + xAcceleration * time;
		if (x < 0) {
			x = 0;
			xVelocity = Math.max(xVelocity, 0);
		} else if (x > (level.width + ((double)Level.CELL_WIDTH - WIDTH)/((double)Level.CELL_WIDTH))) {
			x = (level.width + ((double)Level.CELL_WIDTH - WIDTH)/((double)Level.CELL_WIDTH));
			xVelocity = Math.min(xVelocity, 0);
		}
		if (y > level.height + 1) {
			alive = false;
		}
		oldKeyboard = keyboard;
	}

	protected static double trim(double value, double threshold) {
		if (Math.abs(value) < threshold) {
			value = 0;
		}
		return value;
	}

	public boolean isAlive() {
		return alive;
	}

	protected Collidable platformAbove(Level level) {

		for (Collidable c : level) {
			if (c != this) {
				Rectangle2D thisRect = getLogicalBounds();
				thisRect.setRect(
						thisRect.getX() + .1, thisRect.getY(),
						thisRect.getWidth()-.2, thisRect.getHeight());
				if (this.isCollidingWith(c, thisRect)) {
					Rectangle2D cRect = c.getLogicalBounds();
					int outCode = thisRect.outcode(cRect.getCenterX(),
							cRect.getCenterY());
					if (outCode == (outCode | Rectangle2D.OUT_TOP)) {
						return c;
					}
				}
			}
		}
		return null;
	}

	protected Collidable platformBelow(Level level) {
		for (Collidable c : level) {
			if (c != this) {
				Rectangle2D thisRect = getLogicalBounds();
				thisRect.setRect(
						thisRect.getX() + .1, thisRect.getY(),
						thisRect.getWidth()-.2, thisRect.getHeight());
				if (this.isCollidingWith(c, thisRect)) {
					Rectangle2D cRect = c.getLogicalBounds();
					int outCode = thisRect.outcode(new Point2D.Double(cRect
							.getCenterX(), cRect.getCenterY()));
					if (outCode == (outCode | Rectangle2D.OUT_BOTTOM)) {
						return c;
					}
				}
			}
		}
		return null;
	}

	protected Collidable platformRight(Level level) {
		for (Collidable c : level) {
			if (c != this) {
				Rectangle2D thisRect = getLogicalBounds();
				thisRect.setRect(thisRect.getX(), thisRect.getY()+.1, thisRect.getWidth(), thisRect.getHeight()-.2);
				if (this.isCollidingWith(c, thisRect)) {
					Rectangle2D cRect = c.getLogicalBounds();
					int outCode = thisRect.outcode(cRect.getCenterX(),
							cRect.getCenterY());
					if (outCode == (outCode | Rectangle2D.OUT_RIGHT)) {
						return c;
					}
				}
			}
		}
		return null;
	}

	protected Collidable platformLeft(Level level) {

		for (Collidable c : level) {
			if (c != this) {
				Rectangle2D thisRect = getLogicalBounds();
				thisRect.setRect(thisRect.getX(), thisRect.getY()+.1, thisRect.getWidth(), thisRect.getHeight()-.2);
				if (this.isCollidingWith(c, thisRect)) {
					Rectangle2D cRect = c.getLogicalBounds();
					int outCode = thisRect.outcode(cRect.getCenterX(),
							cRect.getCenterY());
					if (outCode == (outCode | Rectangle2D.OUT_LEFT)) {
						return c;
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean isCollidingWith(Collidable c) {
		return c.tangible(this)
				&& c.getLogicalBounds().intersects(getLogicalBounds());
	}

	private boolean isCollidingWith(Collidable c, Rectangle2D r) {
		return c.tangible(this) && c.getLogicalBounds().intersects(r);
	}

	@Override
	public Rectangle2D getLogicalBounds() {
		return new Rectangle2D.Double(x, y, ((double) WIDTH)
				/ ((double) Level.CELL_WIDTH), ((double) HEIGHT)
				/ ((double) Level.CELL_HEIGHT));
	}

	@Override
	public double getYVelocity() {
		return yVelocity;
	}

	@Override
	public double getXVelocity() {
		return xVelocity;
	}

	@Override
	public double getWidth() {
		return WIDTH;
	}

	@Override
	public double getHeight() {
		return HEIGHT;
	}
}
