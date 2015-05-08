import java.util.*;

/**
 * Solves Ax=b using Gaussian Elimination and Back Substitution
 * 
 * @author Alan Yang
 *
 */
public class LinearSolver {
	private static final double error = 1E-30;
	Map<Integer, Integer> swaps;
	Matrix coefficients;
	Matrix rightSide;
	Matrix solution;

	public LinearSolver(Matrix coefficients, Matrix rightSide) {
		swaps = new HashMap<Integer, Integer>();
		this.coefficients = coefficients;
		this.rightSide = rightSide;

		Matrix Augmented = upperTriangular(coefficients, rightSide);
		solution = backSubstitute(Augmented);
	}

	public Matrix getSolution() {		
		return solution;
	}

	/*
	 * returns upper triangular matrix, augmented with the right side
	 */
	public Matrix upperTriangular(Matrix coefficients, Matrix rightSide) {
		int rows = coefficients.rows();
		int columns = coefficients.columns();
		Matrix augmented = coefficients.augment(rightSide);
		
		// get 0's below each pivot
		for (int j = 0; j < columns; j++) {
			// if 0 at pivot position, swap or move on to next column
			if (Math.abs(augmented.get(j, j)) <= error){
				for (int i = j + 1; i < rows; i++) {
					// if a suitable pivot is found, do a row swap.
					if (Math.abs(augmented.get(i, j)) >= error){
						augmented = augmented.swapRows(i, j);
						swaps.put(i, j);
					}
				}
			} else{	// if no row swaps needed, proceed as normal for this column
				for (int i = j + 1; i < rows; i++){
					double multiplier = augmented.get(i, j) / augmented.get(j, j);
					Matrix elementary = Matrix.identity(rows);
					elementary.set(-multiplier, i, j);
					augmented = elementary.multiply(augmented);
				}
			}
		}
		return augmented;
	}

	/*
	 * back substitutes for solution, once row echelon form reached
	 */
	public Matrix backSubstitute(Matrix Ab) {
		int components = Ab.rows();
		double[][] solution = new double[components][1];

		for (int i = components - 1; i >= 0; i--) {
			double rightSide = Ab.get(i, Ab.columns() - 1);
			for (int j = 0; j < Ab.columns() - 1; j++) {
				if (j != i) {
					rightSide -= solution[j][0] * Ab.get(i, j);
				}
			}

			if (Math.abs(Ab.get(i, i)) >= error) {
				solution[i][0] = rightSide / Ab.get(i, i);
			} else {
				// no solution or infinitely many
				return null;
			}
		}
		Matrix response = new Matrix(solution);

		// undo row swaps from upperTriangular
		for (int row : swaps.keySet()) {
			response.swapRows(row, swaps.get(row));
		}

		swaps.clear();
		return response;
	}
}