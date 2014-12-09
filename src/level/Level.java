package level;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Level {
	Platform[] elements;
	int width;
	int height;
	//private static final Pattern singleCharacter = Pattern.compile(".", Pattern.DOTALL);
	//private static final Pattern notBrace = Pattern.compile("[^\\{\\}]");
	private static final String validLine = "(([^\\{\\}])|(\\{[^\\{\\} ]+[^\\{\\}]+\\}))+";
	private static final Pattern token = Pattern.compile("(([^\\{\\}])|(\\{[^\\{\\} ]+[^\\{\\}]+\\}))");
	
	
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
			throw new IOException("The first line of the file must be of the form 'width: # height #'");
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
					System.out.println(token);
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
	
	public static void main(String[] args) throws IOException {
		Level level = new Level("level1.txt");
		System.out.println(Arrays.toString(level.elements));
	}
}
