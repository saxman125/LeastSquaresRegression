import java.io.Serializable;
import java.util.Random;

/**
 * Generic three-dimensional vector for physics work
 */
public class Vector3 implements Serializable {
	
	private static final long serialVersionUID = -7926012827760512936L;
	
	/**
	 * Stores internal variables in Cartesian coordinates
	 */
	private double x;
	private double y;
	private double z;

	/**
	 * Default constructor - initializes to null vector
	 */
	public Vector3() {
		x = 0;
		y = 0;
		z = 0;
	}

	/**
	 *  Basic Cartesian constructor (right-handed coordinate system)
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Vector copy constructor
	 * @param rhs - Vector3 object to copy
	 */
	public Vector3(Vector3 rhs) {
		x = rhs.x;
		y = rhs.y;
		z = rhs.z;
	}
		
	public String toString() {
		return new String(x + "\t" + y + '\t' + z + '\t' + magnitude());
	}
	
	/**
	 * @return x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x - New x coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y - New y coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return z coordinate
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z - New z coordinate
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Adds a vector to this one and returns the result
	 * @param v2 - the other vector
	 * @return the sum of this vector and v2
	 */
	public Vector3 add(Vector3 v2) {
		return add(this, v2);
	}
	
	/**
	 * Subtracts a vector from this one and returns the result
	 * @param v2 - the other vector
	 * @return the difference of this vector and v2
	 */
	public Vector3 subtract(Vector3 v2) {
		return subtract(this, v2);
	}
	
	/**
	 * Scales this vector by a constant
	 * @param scaler - the constant to scale the vector by
	 * @return the vector scaled by scaler
	 */
	public Vector3 scale(double scaler) {
		return scale(this, scaler);
	}
	
	/**
	 * @return the magnitude of the vector
	 */
	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * @return the azimuthal angle of the vector - the angle from the x axis in the x-y plane (phi in physics, theta in math)
	 * Range of result is on interval [0, 2Pi)
	 */
	public double azimuthal() {
		double angle = Math.atan2(y, x); // on interval (-pi, pi]
		return angle < 0 ? angle + 2 * Math.PI : angle;
	}
	
	/**
	 * @return the polar angle of the vector - the angle of declination from the z axis (theta in physics, phi in math)
	 * Response is on the interval [0, pi)
	 */
	public double polar() {
		if (z == 0) {
			return Math.PI / 2;
		}
		double angle = Math.atan(Math.sqrt(x * x + y * y) / z);
		return angle > 0 ? angle : Math.PI + angle;
	}

	/**
	 * Adds two vectors
	 * @param v1 - the first addend
	 * @param v2 - the second addend
	 * @return the sum of the two vectors
	 */
	public static Vector3 add(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	/**
	 * Gives the multiplicative inverse of a vector
	 * @param v1 - the input vector
	 * @return the multiplicative inverse
	 */
	public static Vector3 inverse(Vector3 v1) {
		return new Vector3(-v1.x, -v1.y, -v1.z);
	}
	
	/**
	 * Subtracts one vector from another
	 * @param v1 - the initial vector
	 * @param v2 - the vector to be subtracted
	 * @return v1 - v2
	 */
	public static Vector3 subtract(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	
	/**
	 * Multiplies a vector by a scalar
	 * @param v1 - the initial vector
	 * @param scaler - the amount to scale the vector by
	 * @return the scaled vector
	 */
	public static Vector3 scale(Vector3 v1, double scaler) {
		return new Vector3(v1.x * scaler, v1.y * scaler, v1.z * scaler);
	}
	
	/**
	 * Gives the dot product (Euclidean scalar product) of two vectors
	 * @param v1 - one vector
	 * @param v2 - another vector
	 * @return the dot product of the vectors
	 */
	public static double dot(Vector3 v1, Vector3 v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}
	
	/**
	 * Gives the cross product of two vectors.  (Technically an axial vector, not a vector,
	 * or even better a rank-2 asymmetrical tensor, but we don't need to worry about that in this class.)
	 * @param v1 - the first vector
	 * @param v2 - the second vector
	 * @return - v1 x v2
	 */
	public static Vector3 cross(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
	}
	
	/**
	 * @return a null vector
	 */
	public static Vector3 nullVector() {
		return new Vector3(0, 0, 0);
	}
	
	/**
	 * @param v1 - the input vector
	 * @return a unit vector that points in the same direction as the input vector
	 */
	public static Vector3 unitVector(Vector3 v1) {
		if (v1.magnitude() == 0) {
			return nullVector();
		} else {
			return scale(v1, 1 / v1.magnitude());
		}
	}
	
	/**
	 * @param r The radius, or magnitude of the vector
	 * @param phi The azimuthal angle, in radians, using the physics convention (this would be theta in math)
	 * It goes from 0 to 2 pi and gives the angle in x-y plane.
	 * @param theta The polar angle, in radians, using the physics convention (this would be phi in math)
	 * It goes from 0 to pi and gives the angle from the positive z axis.
	 * @return A vector matching these three parameters
	 */
	public static Vector3 sphericalVector(double r, double phi, double theta) {
		double x = r * Math.cos(phi) * Math.sin(theta);
		double y = r * Math.sin(phi) * Math.sin(theta);
		double z = r * Math.cos(theta);

		return new Vector3(x, y, z);
	}
	
	/**
	 * @param magnitude The magnitude of the random vector
	 * @param generator The Random object used to generate the random vector.  This is defined externally
	 * so that users can control the random behavior.
	 * @return A vector of the given magnitude that points in a randomly chosen direction
	 */
	public static Vector3 randomDirection(double magnitude, Random generator) { 
		// This algorithm comes from Wolfram Mathworld, "Sphere Point Picking"
		double phi = generator.nextDouble() * 2 * Math.PI;
		double theta = Math.acos(2 * generator.nextDouble() - 1);
		
		return Vector3.sphericalVector(magnitude, phi, theta);
	}
	
	/**
	 * @param origin The origin of the axis about which the vector is to be reflected
	 * @param axis The axis of reflection that goes through the origin
	 * @return A vector that has been reflected about the given axis
	 */
	public Vector3 reflectParticle(Vector3 origin, Vector3 axis) {
		Vector3 unitN = Vector3.unitVector(axis);
		
		// From MathWorld:
		// reflection = -this + 2 * origin + 2 * unitN * ((this - origin) dot unitN)
		
		Vector3 response = new Vector3(this).scale(-1);
		response = response.add(origin.scale(2));
		response = response.add(unitN.scale(2 * Vector3.dot(Vector3.subtract(this, origin), unitN)));
		
		return response;
	}
}
