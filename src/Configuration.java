import java.io.*;  
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Configuration {
	private List<String[]> config;

	public Configuration(String filename) {
		this.config = takeCSV(readCSV(filename));
	}

	public List<String> readCSV(String filename) {
		List<String> list = new ArrayList();
		try {
			Scanner scanner = new Scanner(new File(filename));
			scanner.useDelimiter("\n");
			while (scanner.hasNext()) {
				list.add(scanner.next());
			}
			scanner.close();
		} catch (Exception e) {
			System.out.println("Program has failed to run.");
		}
		return list;
	}

	public List<String[]> takeCSV(List<String> elementTypes) {
		List<String[]> ls = new ArrayList();
		String[] array;
		for (String s : elementTypes) {
			array = s.split(";");
			ls.add(array);
		}
		ls.remove(0);
		return ls;
	}

	public List<String[]> getConfig() {
		return this.config;
	}
}