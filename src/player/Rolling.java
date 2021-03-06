package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import level.Level;

public class Rolling extends AbstractAvatar {

	// private static final double DEFAULT_FRICTION = .5;
	// private static final double ANGULAR_DAMPING = .2;
	// private static final double MOVEMENT_ACCELERATION = 5;
	private final double RADIUS = WIDTH / 2;

	/**
	 * Used for computing velocity after landing.
	 */
	// private static final double MASS = 1;

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
		return new Rectangle((int) (x * Level.CELL_WIDTH),
				(int) (y * Level.CELL_HEIGHT), WIDTH, HEIGHT);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		int diameter = Math.min(WIDTH, HEIGHT) - 1;
		g.fillOval((WIDTH - diameter) / 2, (HEIGHT - diameter) / 2, diameter,
				diameter);
		g.setColor(Color.ORANGE);
		Rectangle r = getRect();
		double x = (Math.cos(angle) * RADIUS) + (r.getWidth() / 2);
		double y = (Math.sin(angle) * RADIUS) + (r.getHeight() / 2);

		double x2 = (Math.cos(angle + Math.PI / 2) * RADIUS)
				+ (r.getWidth() / 2);
		double y2 = (Math.sin(angle + Math.PI / 2) * RADIUS)
				+ (r.getHeight() / 2);
		g.drawLine((int) (r.getHeight() - x), (int) (r.getHeight() - y),
				(int) (x), (int) (y));

		g.drawLine((int) (r.getHeight() - x2), (int) (r.getHeight() - y2),
				(int) (x2), (int) (y2));
	}

	@Override
	public void update(long elapsedTime, Level level) {
		super.update(elapsedTime, level);

		double time = ((double)elapsedTime)/1000.0;
		
		//24 determined through visual experimentation, not math
		angularVelocity = xVelocity / (RADIUS/24);
		
		angle += angularVelocity * time;
	}
}
