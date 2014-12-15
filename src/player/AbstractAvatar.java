package player;

import game.Collidable;
import game.Drawable;
import interfaces.KeyboardState;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import level.Level;
import level.platforms.EndPoint;

public abstract class AbstractAvatar implements Drawable, Collidable {
	protected final int WIDTH = 30;
	protected final int HEIGHT = 30;

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
		if (up != null) {
			if (yVelocity < up.getYVelocity()) {
				yVelocity = up.getYVelocity() - yVelocity / 2;
			}
		}
		if (down == null) {
			yAcceleration = GRAVITATIONAL_ACCELERATION;
		} else {
			yAcceleration = 0;
			if (yVelocity > down.getYVelocity()) {
				yVelocity = down.getYVelocity();
			} else {
				yVelocity = down.getYVelocity();
			}
			if (!canJump) {
				y = down.getLogicalBounds().getY() - ((double) HEIGHT)
						/ ((double) Level.CELL_HEIGHT) - .1;
			}
			x+=down.getXVelocity()*time;
			canJump = true;
		}

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
		
		xAcceleration = trim(xAcceleration, .1);
		yAcceleration = trim(yAcceleration, .1);
		
		x = x + xVelocity * time;
		xVelocity = xVelocity * (1 - DAMPING) + xAcceleration * time;


		y += yVelocity * time;
		yVelocity += yAcceleration * time;
		
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
		Rectangle2D thisRect = getLogicalBounds();
		scaleRect(thisRect, .85, 1);
		for (Collidable c : level) {
			if (c != this) {
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
		Rectangle2D thisRect = getLogicalBounds();
		scaleRect(thisRect, .85, 1);
		for (Collidable c : level) {
			if (c != this) {
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
		Rectangle2D thisRect = getLogicalBounds();
		scaleRect(thisRect, 1, .9);
		for (Collidable c : level) {
			if (c != this) {
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
		Rectangle2D thisRect = getLogicalBounds();
		scaleRect(thisRect, 1, .9);
		for (Collidable c : level) {
			if (c != this) {
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

	protected boolean isCollidingWith(Collidable c, Rectangle2D r) {
		return c.tangible(this) && c.getLogicalBounds().intersects(r);
	}
	
	protected void scaleRect(Rectangle2D r, double xScale, double yScale) {
		double width = r.getWidth() * xScale;
		double height = r.getHeight() * yScale;
		double x = r.getX() + (r.getWidth() - width)/2;
		double y = r.getY() + (r.getHeight() - height)/2;
		r.setRect(x, y, width, height);
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

	@Override
	public boolean tangible(Collidable c) {
		if (EndPoint.class.isInstance(c))
			return true;
		return false;
	}
}
