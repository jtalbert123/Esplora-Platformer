package level.platforms;

import game.Collidable;

import java.awt.Graphics;
import java.util.regex.Pattern;

import level.Level;

public class MovingPlatformStop extends Platform {
	public static final Pattern regex = Pattern.compile("\\{(?:(?:[Ss]top)|(?:[Mm][Pp][Ss]top))\\}");

	public MovingPlatformStop() {
		super();
	}
	
	public MovingPlatformStop(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean tangible(Collidable c) {
		if (MovingPlatform.class.isInstance(c)) {
			return true;
		}
		return false;
	}

	@Override
	public Collidable makePlatform(String str, Integer x, Integer y) {
		return new MovingPlatformStop(x, y);
	}

	@Override
	public void draw(Graphics g) {
		//Invisible type.
		return;
	}

	@Override
	public void update(long currentTime, Level level) {
		return;
	}

}
