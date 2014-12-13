package player;

import game.Collidable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import level.Level;

public class Rolling extends AbstractAvatar {

	// private static final double DEFAULT_FRICTION = .5;
//	private static final double ANGULAR_DAMPING = .2;
//	private static final double MOVEMENT_ACCELERATION = 5;
	private static final double RADIUS = .5;

	/**
	 * Used for computing velocity after landing.
	 */
	private static final double MASS = 1;

	protected double angle;
	protected double angularVelocity;
	protected double angularAcceleration;

	// for now, assume infinite friction with the ground
	// protected double friction;

	public Rolling() {
		super();
		angle = 0;
		angularVelocity = 0;
		angularAcceleration = 0;
	}
	
	public Rolling(double x, double y) {
		super(x, y);
		angle = 0;
		angularVelocity = 0;
		angularAcceleration = 0;
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle((int)(x*Level.CELL_WIDTH), (int)(y*Level.CELL_HEIGHT), WIDTH, HEIGHT);
	}

	@Override
	public boolean tangible(Collidable c) {
		return false;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		int diameter = Math.min(WIDTH, HEIGHT) - 1;
		g.fillOval((WIDTH - diameter)/2, (HEIGHT - diameter)/2, diameter, diameter);
		g.setColor(Color.ORANGE);
		g.setPaintMode();
		Rectangle r = getRect();
		g.drawLine((int)(r.getCenterX()), (int)(r.getCenterY()), (int)(r.getCenterX())-5, (int)(r.getCenterY()));
				//, (int)(WIDTH*RADIUS*Math.cos(angle)), (int)(WIDTH*RADIUS*Math.sin(angle)));
	}

	@Override
	public void update(long elapsedTime, Level level) {
		super.update(elapsedTime, level);
		angle = x/RADIUS;
	}
}
