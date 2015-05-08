import java.util.ArrayList;

public class RegressionDriver {

	private static String fileName = "regressionData.txt";

	public static void main(String[] args) {
		RegressionVisualizer visualizer = RegressionVisualizer.createVisualizer();

		ArrayList<Vector3> dataPoints = FileParser.parseFile(fileName);

		if (dataPoints.size() > 0) {
			Matrix A = new Matrix(dataPoints.size(), 2);
			Matrix b = new Matrix(dataPoints.size(), 1);

			for (int i = 0; i < dataPoints.size(); i++) {
				A.set(dataPoints.get(i).getX(), i, 0);
				A.set(1, i, 1);
				b.set(dataPoints.get(i).getY(), i, 0);
				visualizer.addPoint(dataPoints.get(i));
			}

			Matrix squareSymmetric = A.transpose().multiply(A);
			Matrix AtransposeB = A.transpose().multiply(b);
			
			LinearSolver solver = new LinearSolver(squareSymmetric, AtransposeB);

			Matrix solution = solver.getSolution();
			
			double slope = solution.get(0, 0);
			double yIntercept = solution.get(1, 0);
			visualizer.setLine(slope, yIntercept);
		}
	}
}
