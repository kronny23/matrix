import java.util.*;

public class Main {
    public static void main(String[] args) {
        int size;
        int matrix1[][];
        int matrix2[][];

        Scanner input = new Scanner(System.in);
        System.out.println("Imput rang");
        size = input.nextInt();
        matrix1 = new int[size][size];
        System.out.println("imput first matrix");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix1[i][j] = input.nextInt();
            }
        }

        System.out.println("imput second matrix");
        matrix2 = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix2[i][j] = input.nextInt();
            }
        }

        System.out.println("imput third matrix");
        Hashtable a1 = new Hashtable<>();
        for (int i = 0; i < size*size; i++) {
            int b = input.nextInt();
            if (b != 0){
                a1.put(i,b);
            }
        }

        System.out.println("imout fourth matrix");
        Hashtable a2 = new Hashtable<>();
        for (int i = 0; i < size*size; i++) {
            int b = input.nextInt();
            if (b != 0){
                a2.put(i,b);
            }
        }

        System.out.println();

        DenseMatrix m1 = new DenseMatrix(matrix1, size);
        DenseMatrix m2 = new DenseMatrix(matrix2, size);

        m2.MatrixTrans();

        System.out.println();

        m2.MatrixOut();

        System.out.println();

        DenseMatrix m3 = m1.mulDenseDense(m2);

        m3.MatrixOut();
        SparseMatrix m01 = new SparseMatrix(a1, size);
        SparseMatrix m02 = new SparseMatrix(a2, size);
        SparseMatrix m03 = m01.mulSparseSparse(m02);
        m03.HashOut();
    }
}