package config;

import java.io.*;
import java.util.*;

public class Configuration {
	private List<String[]> config;

	public Configuration(String filename) {
		this.config = readByBufferedReader(filename);
	}

	public List<String[]> getConfig() {
		return this.config;
	}

	public List<String[]> readByBufferedReader(String filename) {
		String line = "";
		String delimiter = ";";
		String[] arr;
		List<String[]> retval = new ArrayList<String[]>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				arr = line.split(delimiter);
				retval.add(arr);
			}
		} catch (Exception e) {
			System.out.println("Error occured.");
		}
		
		retval.remove(0);
		return retval;
	}
}