public class DenseMatrix {
    public int size;
    public int matrix[][];

    public DenseMatrix(int[][] matrix, int size) {
        this.size = size;
        this.matrix = matrix;
    }

    public DenseMatrix(int size) {
        this.matrix = new int[size][size];
        this.size = size;
    }

    public DenseMatrix mulDenseDense(DenseMatrix other) {
        DenseMatrix res = new DenseMatrix(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    res.matrix[i][j] += this.matrix[i][k] * other.matrix[j][k];
                }
            }
        }
        return res;
    }

    public void MatrixTrans() {
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                int aT = this.matrix[i][j];
                this.matrix[i][j] = this.matrix[j][i];
                this.matrix[j][i] = aT;
            }
        }
    }

    public void MatrixOut() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j] + " ");

            }
            System.out.println();
        }
    }
}