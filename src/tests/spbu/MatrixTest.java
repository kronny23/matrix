package spbu;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;


public class MatrixTest {
    public SparseMatrix sparseMatrix1, sparseMatrix2, resultMatrix1, resultMatrix3;
    public DenseMatrix denseMatrix1, denseMatrix2, resultMatrix2, resultMatrix4;

    public MatrixTest() throws IOException {
        sparseMatrix1 = new SparseMatrix(new BufferedReader(new FileReader("in1")));
        sparseMatrix2 = new SparseMatrix(new BufferedReader(new FileReader("in2")));
        denseMatrix1 = new DenseMatrix(new BufferedReader(new FileReader("in3")));
        denseMatrix2 = new DenseMatrix(new BufferedReader(new FileReader("in4")));
        resultMatrix1 = new SparseMatrix(new BufferedReader(new FileReader("resultS1xS2")));
        resultMatrix2 = new DenseMatrix(new BufferedReader(new FileReader("resultD1xD2")));
        resultMatrix3 = new SparseMatrix(new BufferedReader(new FileReader("resultS1xD1")));
        resultMatrix4 = new DenseMatrix(new BufferedReader(new FileReader("resultD1xS1")));
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

    @Test
    public void mulSD() {
        SparseMatrix result = sparseMatrix1.mulSparseDense(denseMatrix1);
        assertTrue(result.equals(resultMatrix3));
    }

    @Test
    public void mulDS() {
        DenseMatrix result = denseMatrix1.mulDenseSparse(sparseMatrix1);
        assertTrue(result.equals(resultMatrix4));
    }

    @Test
    public void mulDenseMultiThread() {
        DenseMatrix result = denseMatrix1.dMulDenseDense(denseMatrix2);
        assertTrue(result.equals(resultMatrix2));
    }

    @Test
    public void mulSparseMultiThread() {
        SparseMatrix result = sparseMatrix1.newDMulSparseSparse(sparseMatrix2);
        assertTrue(result.equals(resultMatrix1));
    }
}
