package level;

import game.Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import platforms.Platform;

/**
 * A single level of a platformer {@link Game}.
 * @author James Talbert
 *
 */
public class Level implements Iterable<Platform> {
	/**
	 * The list of {@link level.platforms.Platform Platforms} in the level.
	 */
	private Platform[] elements;

	/**
	 * The width of the level, specified explicitly in the file.
	 */
	public int width;

	/**
	 * The height of the level, specified explicitly in the file.
	 */
	public int height;

	/**
	 * matches any non { or } character, or any set of characters contained by {
	 * and }
	 */
	private static final Pattern token = Pattern
			.compile("(([^\\{\\}])|(\\{[^\\{\\} ]+[^\\{\\}]*\\}))");
	/**
	 * matches any number of tokens in a row
	 */
	private static final String validLine = token + "*";

	/**
	 * Loads the level file as a new Level, using the
	 * {@link level.platforms.Platform} class to parse the individual tokens.
	 * 
	 * @param fileName
	 *            the path the the level file.
	 * @throws IOException
	 *             if the file could not be found or if the file did not specify
	 *             a width and height.
	 */
	public Level(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists())
			throw new FileNotFoundException();
		Platform.setUp();
		Scanner text = new Scanner(file);
		String line = text.nextLine();
		if (line.matches("[Ww](?:idth)?\\: *\\d+[^Hh]*[Hh](?:eight)?\\: *\\d+")) {
			Matcher m = Pattern.compile("\\d+").matcher(line);
			m.find();
			width = Integer.parseInt(m.group());
			m.find();
			height = Integer.parseInt(m.group());
		} else {
			text.close();
			throw new IOException(
					"The first line of the file must be of the form 'width: # height #'");
		}
		ArrayList<Platform> platforms = new ArrayList<>();
		int row = 0;
		while (text.hasNextLine()) {
			Platform p;
			line = text.nextLine();
			if (line.matches(validLine)) {
				Matcher validate = token.matcher(line);
				int column = 0;
				while (validate.find()) {
					String token = validate.group();
					p = Platform.getPlatform(token, column, row);
					if (p != null)
						platforms.add(p);
					column++;
				}
				row++;
			} else {
				System.out.println("Invalid line");
			}
		}
		text.close();
		elements = platforms.toArray(new Platform[platforms.size()]);
	}

	/**
	 * Updates the entire level, platform-by-platform.
	 * 
	 * @param milliseconds
	 *            the time since the last update.
	 * @see Platform#update(long, Level)
	 * @see Game#update(long)
	 */
	public void updateLevel(long milliseconds) {
		for (Platform p : elements) {
			p.update(milliseconds, this);
		}
	}

	@Override
	public Iterator<Platform> iterator() {
		return Arrays.asList(elements).iterator();
	}

	/**
	 * Gets an array (typed by the given class, no casting necessary) of all
	 * {@link level.platforms.Platform platforms} in the level with the given
	 * type (type.isInstance({@link level.platforms.Platform platform}) returns
	 * true). Subclasses of the given type are included.
	 * 
	 * @param type
	 *            the type of {@link level.platforms.Platform platforms} to
	 *            include in the output array.
	 * @return an array of the matching {@link level.platforms.Platform
	 *         platforms}.
	 */
	@SuppressWarnings("unchecked")
	public <PlatformType> PlatformType[] getAll(Class<PlatformType> type) {
		ArrayList<PlatformType> subset = new ArrayList<>();
		for (Platform p : elements) {
			if (type.isInstance(p)) {
				subset.add((PlatformType) p);
			}
		}
		return subset.toArray(((PlatformType[]) new Object[subset.size()]));
	}
}
