import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Matrix {
   protected double[][] matrix;
   protected int m;
   protected int n;
   public static final int TWOBYTWO_MATRIX = 2;
   public static final int LINE_ROW = 0;

   public Matrix() {
      this(null);
      m = n = 0;
   }

   public Matrix(double[][] in) {
      if (in == null) {
         return;
      }
      m = in.length;
      n = in[0].length;
      matrix = new double[m][n];
      for (int i = 0; i < m; i++) {
         for (int j = 0; j < n; j++) {
            matrix[i][j] = in[i][j];
         }
      }
   }

   public double getDeterminant() {
      if (m != n) {
         return 0.0;
      }
      RecursiveTask<Double> task = new MatrixDeterminantTask(matrix, m);
      ForkJoinPool pool = new ForkJoinPool();
      return pool.invoke(task);
   }

   class MatrixDeterminantTask extends RecursiveTask<Double> {
      private double[][] M;
      private int currN;

      public MatrixDeterminantTask() {
         this(null, 0);
      }

      public MatrixDeterminantTask(double[][] subMatrix, int n) {
         if (subMatrix == null) {
            return;
         }
         currN = n;
         M = new double[currN][currN];
         for (int i = 0; i < currN; i++) {
            for (int j = 0; j < currN; j++) {
               M[i][j] = subMatrix[i][j];
            }
         }
      }

      @Override
      public Double compute() {
         if (currN > TWOBYTWO_MATRIX) {
            double determinant = 0.0;
            for (int i = 0; i < currN; i++) {
               double c = Math.pow(-1, i) * M[LINE_ROW][i];
               RecursiveTask<Double> sub = new MatrixDeterminantTask(reduceMatrix(M, currN - 1, LINE_ROW, i),
                     currN - 1);
               sub.fork();
               determinant += c * sub.join();
            }
            return determinant;
         }
         return calc2x2Matrix(M);
      }

      private double[][] reduceMatrix(double[][] M, int currN, int e_row, int e_col) {
         if (e_col > currN)
            return null;

         double[][] subMatrix = new double[currN][currN];
         int i, j;
         int a, b;
         // reassign matrix
         for (i = 0, a = 0; i < currN + 1; i++, a++) {
            // skip the elimination row
            if (i == e_row) {
               a--;
               continue;
            }
            for (j = 0, b = 0; j < currN + 1; j++, b++) {
               // skip the elimination col
               if (j == e_col) {
                  b--;
                  continue;
               }
               subMatrix[a][b] = M[i][j];
            }
         }
         return subMatrix;

      }

      private double calc2x2Matrix(double[][] M) {
         if (M == null)
            return 0.0;
         return M[1][1] * M[0][0] - M[0][1] * M[1][0];
      }

   }
}
