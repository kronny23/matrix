package spbu;

import groovy.json.internal.ArrayUtils;

import java.io.BufferedReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SparseMatrix {
    int size;
    Hashtable<Integer, Integer> hash;

    public SparseMatrix(BufferedReader io) {
        Scanner scanner = new Scanner(io);
        Hashtable<Integer, Integer> result = new Hashtable<>();
        Integer nextIndex = 0;
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                Integer nextInt = scanner.nextInt();
                if (nextInt != 0) {
                    result.put(nextIndex++, nextInt);
                }
            } else {
                scanner.next();
            }
        }
        this.hash = result;
        this.size = (int)Math.sqrt(nextIndex);
    }

    public SparseMatrix(Hashtable hash, int size) {
        this.hash = hash;
        this.size = size;
    }

    public SparseMatrix(int size) {
        this.hash = new Hashtable<>();
        this.size = size;
    }

    public int get(int y, int x) {
        if (this.hash.containsKey(y * size + x)) {
            return this.hash.get(y * size + x);
        }
        return 0;
    }
    public void set(int y, int x, int value) {
        if (value == 0) {
            if (this.hash.containsKey(y * size + x)) {
                this.hash.remove(y * size + x);
            }
        } else {
            this.hash.put(y * size + x, value);
        }
    }
    public void add(int y, int x, int additional) {
        set(y, x, get(y, x) + additional);
    }

    public SparseMatrix mulSparseSparse(SparseMatrix other) {
        SparseMatrix result = new SparseMatrix(size);

        for (int keys : hash.keySet()) {
            int y = keys / size;
            int x = keys % size;
            int value = this.get(y, x);
            int sum = 0;
            for (int i = 0; i < size; i++) {
                result.add(y, i, value * other.get(x, i));
            }
        }

        return result;
    }

    public SparseMatrix mulSparseDense(DenseMatrix other) {
        SparseMatrix result = new SparseMatrix(size);

        for (int keys : hash.keySet()) {
            int y = keys / size;
            int x = keys % size;
            int value = this.get(y, x);
            for (int i = 0; i < size; i++) {
                result.add(y, i, value * other.matrix[x][i]);
            }
        }

        return result;
    }

    public SparseMatrix dMulSparseSparse(SparseMatrix other) {
        SparseMatrix result = new SparseMatrix(size);
        Integer[] matrixKeys =  hash.keySet().toArray(new Integer[hash.size()]);
        org.apache.commons.lang.ArrayUtils.reverse(matrixKeys);

        class MultRow implements Runnable {
            public Thread thread;
            int firstKeyPosition, lastKeyPosition;
            public MultRow(int firstKeyPosition, int lastKeyPosition) {
                System.out.println(firstKeyPosition + " " + lastKeyPosition);
                this.firstKeyPosition = firstKeyPosition;
                this.lastKeyPosition  = lastKeyPosition;
                thread = new Thread(this);
                thread.start();
            }
            public void run() {
                for (int i = firstKeyPosition; i <= lastKeyPosition; i++) {
                    int key = matrixKeys[i];
                    int y = key / size;
                    int x = key % size;
                    int value = get(y, x);
                    for (int j = 0; j < size; j++) {
                        result.add(y, j, value * other.get(x, j));
                    }
                }
            }
        }

        ArrayList<MultRow> rowCalculators = new ArrayList<>();
        int matrixKeyOfNextRowStart = 0;
        for (int curMatrixKey = 1; curMatrixKey < matrixKeys.length; curMatrixKey++) {
            if (matrixKeys[matrixKeyOfNextRowStart] / size != matrixKeys[curMatrixKey] / size) {
                rowCalculators.add(new MultRow(matrixKeyOfNextRowStart, curMatrixKey - 1));
                matrixKeyOfNextRowStart = curMatrixKey;
            }
        }
        rowCalculators.add(new MultRow(matrixKeyOfNextRowStart, matrixKeys.length - 1));
        try {
            for (MultRow rowCalculator : rowCalculators) {
                rowCalculator.thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean equals(SparseMatrix matrix) {
        return this.size == matrix.size && this.hash.equals(matrix.hash);
    }

    public void HashOut()
    {
        for (int i = 0; i < size; i++) {
            System.out.println();
            for (int j = 0; j < size; j++) {
                System.out.print(this.get(i, j) + " ");
            }
        }
    }

}