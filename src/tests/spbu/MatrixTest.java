package spbu;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;


public class MatrixTest {
    private SparseMatrix sparseMatrix1, sparseMatrix2, resultMatrix1;
    private DenseMatrix denseMatrix1, denseMatrix2, resultMatrix2;

    public MatrixTest() throws IOException {
        sparseMatrix1  = new SparseMatrix(new BufferedReader(new FileReader("in1")));
        sparseMatrix2  = new SparseMatrix(new BufferedReader(new FileReader("in2")));
        resultMatrix1  = new SparseMatrix(new BufferedReader(new FileReader("result1")));
        denseMatrix1   = new DenseMatrix(new BufferedReader(new FileReader("in3")));
        denseMatrix2   = new DenseMatrix(new BufferedReader(new FileReader("in4")));
        resultMatrix2  = new DenseMatrix(new BufferedReader(new FileReader("result2")));
    }

    @Test
    public void mulSS() {
        SparseMatrix result = sparseMatrix1.mulSparseSparse(sparseMatrix2);
        assertTrue(result.equals(resultMatrix1));
    }

    @Test
    public void mulDD() {
        DenseMatrix result = denseMatrix1.mulDenseDense(denseMatrix2);
        assertTrue(result.equals(resultMatrix2));
    }
}
