import java.io.File;
import java.util.Date;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File("matrix_12.dat"));
        int row = in.nextInt();
        int col = row;
        System.out.printf("Matrix is of %dx%d%n", row, col);
        // System.out.println("Enter the # of rows:");
        // row = in.nextInt();
        // System.out.println("Enter the # of cols:");
        // col = in.nextInt();
        double[][] m = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int num = in.nextInt();
                m[i][j] = num;
            }
        }
        in.close();
        Matrix matrix = new Matrix(m);
        Date start = new Date();
        System.out.println("Determinant = " + matrix.getDeterminant());
        Date end = new Date();
        System.out.printf("Time elapsed = %.2fs%n", (end.getTime() - start.getTime()) / 1000.0);
    }
}
