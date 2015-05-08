package LeastSquaresRegression;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FileParser {

	private static Scanner scanner;

	public static ArrayList<Vector3> parseFile(String fileName) {
		ArrayList<Vector3> response = new ArrayList<Vector3>();
		try {
			scanner = new Scanner(new File(fileName));
			while (scanner.hasNextLine()) {
				try {
					String line = scanner.nextLine();
					response.add(processLine(line));
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		return response;
	}

	private static Vector3 processLine(String line) {
		String[] divided = line.split("\t");
		Vector3 response = new Vector3(0, 0, 0);
		if (divided.length > 2) {
			System.out.println("Error: Not 2D Vector!");
		} else {
			try {
				response.setX(Double.parseDouble(divided[0]));
				response.setY(Double.parseDouble(divided[1]));
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
		return response;
	}
}