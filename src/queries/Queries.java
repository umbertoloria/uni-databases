package queries;

import java.util.ArrayList;
import java.util.Scanner;

public class Queries {

	public static String[] queries, titles;

	public static void init() {
		ArrayList<String> queriesList = new ArrayList<>();
		ArrayList<String> titlesList = new ArrayList<>();
		Scanner f = new Scanner(Queries.class.getResourceAsStream("queries.txt"));
		StringBuilder box = new StringBuilder();
		String line;
		while (f.hasNextLine()) {
			line = f.nextLine();
			if (line.startsWith("#")) {
				titlesList.add(line.substring(2));
				if (box.length() > 0) {
					queriesList.add(box.toString());
				}
				box.delete(0, box.length());
			} else {
				box.append(line);
				box.append('\n');
			}
		}
		queriesList.add(box.toString());
		f.close();
		titles = titlesList.toArray(new String[0]);
		queries = queriesList.toArray(new String[0]);
	}

}
