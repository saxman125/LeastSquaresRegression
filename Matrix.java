package LeastSquaresRegression;

import java.text.DecimalFormat;

/**
 * Matrix class with some basic operations
 * 
 * @author Alan Yang
 *
 */
// TODO make everything exception - safe
public class Matrix {

	private double[][] matrix;
	private int rows;
	private int columns;

	private DecimalFormat form = new DecimalFormat("#.###");
	/**
	 * Constructor - unknown values
	 */
	public Matrix(int rows, int columns) {
		this.matrix = new double[rows][columns];
		this.rows = matrix.length;
		this.columns = matrix[0].length;
	}

	/**
	 * Constructor - known values
	 */
	public Matrix(double[][] matrix) {
		this.matrix = matrix;
		this.rows = matrix.length;
		this.columns = matrix[0].length;
	}

	/**
	 * Returns the number of rows
	 */
	public int rows() {
		return rows;
	}

	/**
	 * Returns the number of columns
	 */
	public int columns() {
		return columns;
	}

	/**
	 * Set a value at a position
	 */
	public void set(double value, int row, int column) {
		matrix[row][column] = value;
	}

	/**
	 * get the value at a position
	 */
	public double get(int row, int column) {
		return matrix[row][column];
	}
	
	/**
	 * Checks for matrix equality
	 * @param compared
	 * @return
	 */
	public boolean equals(Matrix compared) {
		if (this.rows() != compared.rows() || this.columns() != compared.columns()) {
			return false;
		}
		
		for (int i = 0; i < this.rows(); i++) {
			for (int j = 0; j < this.columns(); j++) {
				if (this.get(i, j) != compared.get(i, j)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Matrix addition
	 */
	public Matrix add(Matrix other) {
		Matrix sum = new Matrix(this.rows, this.columns);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sum.set(this.get(i, j) + other.get(i, j), i, j);
			}
		}
		return sum;
	}

	/**
	 * Matrix multiplication
	 */
	public Matrix multiply(Matrix other) {
		Matrix product = new Matrix(this.rows, other.columns);

		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < other.columns(); j++) {
				double value = 0;
				for (int k = 0; k < this.columns; k++) {
					value += this.get(i, k) * other.get(k, j);
				}
				product.set(value, i, j);
			}
		}
		return product;
	}

	/**
	 * Scalar multiplication
	 */
	public Matrix scale(double scalar) {
		Matrix response = this;

		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				response.set(this.get(i, j) * scalar, i, j);
			}
		}
		return response;
	}

	/**
	 * Matrix transpose
	 */
	public Matrix transpose() {
		Matrix response = new Matrix(this.columns, this.rows);
		for (int i = 0; i < response.rows(); i++) {
			for (int j = 0; j < response.columns(); j++) {
				response.set(this.get(j, i), i, j);
			}
		}
		return response;
	}

	/**
	 * Augments a matrix to the right side
	 */
	public Matrix augment(Matrix other) {
		Matrix newMatrix = new Matrix(this.rows, this.columns + other.columns);

		for (int i = 0; i < this.rows(); i++) {
			for (int j = 0; j < this.columns(); j++) {
				newMatrix.set(this.get(i, j), i, j);
			}
		}

		for (int i = 0; i < other.rows(); i++) {
			for (int j = 0; j < other.columns(); j++) {
				newMatrix.set(other.get(i, j), i, j + this.columns);
			}
		}
		return newMatrix;
	}

	/**
	 * Remove a row from a matrix
	 */
	public Matrix removeRow(int row) {
		Matrix oldMatrix = this.swapRows(row, 0);
		Matrix newMatrix = new Matrix(this.rows() - 1, this.columns());
		for (int i = 1; i < oldMatrix.rows(); i++) {
			for (int j = 0; j < oldMatrix.columns(); j++) {
				newMatrix.set(oldMatrix.get(i, j), i - 1, j);
			}
		}
		if (row != 0) {
			newMatrix = newMatrix.swapRows(row - 1, 0);
		}
		return newMatrix;
	}

	/**
	 * Remove a column from a matrix
	 */
	public Matrix removeColumn(int column) {
		Matrix oldMatrix = this.swapCols(column, 0);
		Matrix newMatrix = new Matrix(this.rows(), this.columns() - 1);
		for (int i = 0; i < oldMatrix.rows(); i++) {
			for (int j = 1; j < oldMatrix.columns(); j++) {
				newMatrix.set(oldMatrix.get(i, j), i, j - 1);
			}
		}
		if (column != 0) {
			newMatrix = newMatrix.swapCols(0, column - 1);
		}
		return newMatrix;
	}

	/**
	 * Returns the matrix identity for a certain dimension
	 */
	public static Matrix identity(int side) {
		Matrix identity = new Matrix(side, side);
		for (int i = 0; i < side; i++) {
			identity.set(1, i, i);
		}
		return identity;
	}

	/**
	 * Returns the permutation matrix that swaps two rows/columns. Note: row
	 * permutation multiplies on left, while column multiplies on right
	 */
	public static Matrix permutation(int dimension, int vector1, int vector2,
			boolean isColumn) {
		Matrix response = Matrix.identity(dimension);
		int dim = response.columns;
		if (isColumn)
			dim = response.rows;

		double[] v1 = new double[dim];
		double[] v2 = new double[dim];

		for (int i = 0; i < dim; i++) {
			if (isColumn) {
				v1[i] = response.get(i, vector1);
				v2[i] = response.get(i, vector2);
			} else {
				v1[i] = response.get(vector1, i);
				v2[i] = response.get(vector2, i);
			}
		}

		for (int i = 0; i < dim; i++) {
			if (isColumn) {
				response.set(v2[i], i, vector1);
				response.set(v1[i], i, vector2);
			} else {
				response.set(v2[i], vector1, i);
				response.set(v1[i], vector2, i);
			}
		}
		return response;
	}

	/**
	 * Swaps two rows in a matrix
	 */
	public Matrix swapRows(int row1, int row2) {
		Matrix permutationMatrix = permutation(this.rows, row1, row2, false);
		return permutationMatrix.multiply(this);
	}

	/**
	 * Swaps two columns in a matrix
	 */
	public Matrix swapCols(int col1, int col2) {
		Matrix permutationMatrix = permutation(this.columns, col1, col2, true);
		return permutationMatrix.multiply(this);
	}

	/**
	 * Returns the elimination matrix that subtracts a factor of one row from
	 * another. Row operation -> multiply on left
	 */
	public static Matrix elimination(int rows, double factor, int pivotRow, int changedRow){
		Matrix response = identity(rows);
		response.set(-factor, changedRow, pivotRow);
		return response;
	}

	public String toString() {
		String output = "";
		for (int i = 0; i < this.matrix.length; i++) {
			for (int j = 0; j < this.matrix[0].length; j++) {
				output += form.format(this.get(i, j)) + "\t";
			}
			output += "\n";
		}
		return output;
	}
}