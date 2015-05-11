import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class RegressionVisualizer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final Vector3 xAxis = new Vector3(1, 0, 0);
	private static final Vector3 yAxis = new Vector3(1E-6, 1, 0);

	private static final int WIDTH = 1100;
	private static final int HEIGHT = 1100;
	
	private ArrayList<Vector3> points = new ArrayList<>();
	private ArrayList<Vector3> lines = new ArrayList<>();	
	
	
	private Vector3 line = Vector3.nullVector();
	private double slope = 0;
	private double yIntercept = 0;
	private static final int shift = 0;
	
	public RegressionVisualizer() {
		setBackground(Color.WHITE);

		this.setSize(WIDTH, HEIGHT);
	}
	
	private static int DOT_SIZE = 10;

	private static double SIZE = 22;

	@Override
	public void paint(Graphics arg0) {
		
		super.paint(arg0);
		
		for (Vector3 point : points) {
			drawPoint(arg0, point, Color.BLUE);
		}
		drawFit(arg0);
		arg0.setColor(Color.BLUE);
		for (Vector3 pointOnLine : lines) {
			double xyslope = pointOnLine.getY() / pointOnLine.getX();
			double yMin = -SIZE * xyslope;
			double yMax = SIZE * xyslope;
			double xMin = -SIZE;
			double xMax = SIZE;
			
			Point min = transform(xMin, yMin);
			Point max = transform(xMax, yMax);
			arg0.drawLine(min.x, min.y, max.x, max.y);
		}
		arg0.setColor(Color.RED);
		drawText(arg0, 5, 25);

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
		arg0.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		arg0.drawString(equation + "  Chi Squared:  " + chi2, x, y);
	}
	
	public void addPoint(Vector3 point) {
		points.add(point.add(new Vector3(0, shift, 0)));
		repaint();
	}
	
	public void setLine(double slope, double yIntercept) {
		this.slope = slope;
		this.yIntercept = yIntercept + shift;
		lines.add(xAxis);
		lines.add(yAxis);
		repaint();
	}
	
	private void drawFit(Graphics arg0) {
		arg0.setColor(Color.RED);
		Vector3 point1 = new Vector3(0, yIntercept, 0);
		Vector3 point2 = new Vector3(1, slope + yIntercept, 0);
		Vector3 fittedLine = point1.subtract(point2);
		this.line = fittedLine;
		
		double xyslope = fittedLine.getY() / fittedLine.getX();
		double yMin = -SIZE * xyslope + yIntercept;
		double yMax = SIZE * xyslope + yIntercept;
		double xMin = -SIZE;
		double xMax = SIZE;
		
		Point min = transform(xMin, yMin);
		Point max = transform(xMax, yMax);
		
		//Point p = transform(0, yIntercept);
		arg0.drawLine(min.x, min.y, max.x, max.y);
	}
	
	public void clear() {
		points.clear();
		line = Vector3.nullVector();
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