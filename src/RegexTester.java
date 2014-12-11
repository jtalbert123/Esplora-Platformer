import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexTester {
	public static void main(String[] args) {
		
	}

	@SuppressWarnings("unused")
	private static void testRegEx(String str, String regex) {
		Pattern p1 = Pattern.compile(regex);
		Matcher m1 = p1.matcher(str);
		System.out.println("Searching: " + str);
		System.out.println("With: " + regex);
		int i = 1;
		while (m1.find()) {
			System.out.println(m1.group());
			i++;
		}
		System.out.println(i + " matches found.");
	}
}
