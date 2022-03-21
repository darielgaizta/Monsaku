import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;

public class Configuration {
	public static void readElementTypes(String filename) {
		char character;
		String buffer = "";

		FileInputStream in;

		try {
			in = new FileInputStream(filename);

			int bit = in.read();
			while (bit != -1) {
				character = (char) bit;
				if (buffer.equals("source") || buffer.equals("target") || buffer.equals("effectivity")) {
					buffer = "";
				} else if (character != ';') {
					buffer += character;
				} else {
					System.out.print(removeCharAt(buffer, 1));
					buffer = "";
				}
				bit = in.read();
			}
			System.out.println(buffer);
			in.close();
		} catch (Exception e) {
			System.out.println("Program has failed to run.");
			System.out.println(e);
		}
		System.out.println("--- EOP ---");
	}

	public static String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}

	public static void main(String[] args) {
		readElementTypes("ElementTypesEffectivity.txt");
	}
}