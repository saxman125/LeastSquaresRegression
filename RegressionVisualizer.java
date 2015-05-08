package LeastSquaresRegression;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RegressionVisualizer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Vector3> points = new ArrayList<>();
	private Vector3 line;
	private double slope;
	private double yIntercept;

	public RegressionVisualizer() {
		setBackground(Color.WHITE);

		final int width = 800;
		final int height = 800;
		this.setSize(width, height);
	}
	
	private static int DOT_SIZE = 5;

	private static double SIZE = 11;

	@Override
	public void paint(Graphics arg0) {
		super.paint(arg0);

		for (Vector3 point : points) {
			drawPoint(arg0, point, Color.BLUE);
		}

			double xyslope = line.getY() / line.getX();
			double yMin = -SIZE * xyslope;
			double yMax = SIZE * xyslope;
			double xMin = -SIZE;
			double xMax = SIZE;
			
			Point min = transform(xMin, yMin);
			Point max = transform(xMax, yMax);
			arg0.drawLine(min.x, min.y, max.x, max.y);
			
			drawText(arg0, (int)(0.25 * max.x), (int)(max.y - 20));
	}
	
	public double chiSquared(Vector3 pointOnLine, ArrayList<Vector3> pointList) {
		double chi2 = 0;
		for (Vector3 point : pointList) {
			double difference = distance(point, pointOnLine);
			chi2 += difference * difference;
		}
		return chi2;
	}
	
	public double distance(Vector3 point, Vector3 pointOnLine) {
		Vector3 crossProduct = Vector3.cross(point, Vector3.subtract(point, pointOnLine));
		return crossProduct.magnitude() / pointOnLine.magnitude();
	}
	
	private Point transform(double x, double y) {
		// xprime = M*(x/S + 1/2)
		int xCoord = (int)Math.round((x / SIZE + .5) * getSize().getWidth());
		// yprime = M*(-y/S+1/2)
		int yCoord = (int)Math.round((.5 - y / SIZE) * (int)getSize().getHeight());
		return new Point(xCoord, yCoord);
	}

	private void drawPoint(Graphics arg0, Vector3 point, Color color) {
		Point newPoint = transform(point.getX(), point.getY());

		arg0.setColor(color);
		arg0.fillOval(newPoint.x, newPoint.y, DOT_SIZE, DOT_SIZE);
	}
	
	private void drawText(Graphics arg0, int x, int y) {
		String equation = "y = " + String.valueOf(slope) + "x + " + String.valueOf(yIntercept);
		String chi2 = String.valueOf(chiSquared(line, points));
		arg0.drawString(equation + "\tChi Squared: " + chi2, x, y);
	}
	
	public void addPoint(Vector3 point) {
		points.add(point);
		repaint();
	}
	
	public void setLine(double slope, double yIntercept) {
		this.slope = slope;
		this.yIntercept = yIntercept;
		line = new Vector3(0, yIntercept, 0);
	}
	
	public void clear() {
		points.clear();
	}
	
	static public RegressionVisualizer createVisualizer() {
		RegressionVisualizer visualizer = new RegressionVisualizer();
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(visualizer);
		frame.setSize(visualizer.getWidth(), visualizer.getHeight());

		frame.setVisible(true);	
		
		return visualizer;
	}
}
