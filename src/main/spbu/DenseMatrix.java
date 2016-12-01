package spbu;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DenseMatrix {
    int size;
    int matrix[][];

    public DenseMatrix(int[][] matrix, int size) {
        this.size = size;
        this.matrix = matrix;
    }

    public DenseMatrix(int size) {
        this.matrix = new int[size][size];
        this.size = size;
    }


    public DenseMatrix(BufferedReader io) {
        Scanner scanner = new Scanner(io);
        List<Integer> integers = new ArrayList<>();
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                integers.add(scanner.nextInt());
            } else {
                scanner.next();
            }
        }
        int size = (int)Math.sqrt(integers.size());
        int[][] array = new int[size][size];
        for (int i = 0; i < integers.size(); i++) {
            array[i / size][i % size] = integers.get(i);
        }
        this.matrix = array;
        this.size   = size;
    }

    public DenseMatrix mulDenseDense(DenseMatrix other) {
        DenseMatrix res = new DenseMatrix(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    res.matrix[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return res;
    }
    public DenseMatrix mulDenseSparse(SparseMatrix other) {
        DenseMatrix res = new DenseMatrix(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    res.matrix[i][j] += this.matrix[i][k] * other.hash.get(k * size + j);
                }
            }
        }
        return res;
    }

    public DenseMatrix dMulDenseDense(DenseMatrix other) {
        class Dispatcher {
            int value = 0;
            public int next() {
                synchronized (this) {
                    return value++;
                }
            }
        }

        DenseMatrix result = new DenseMatrix(this.size);
        Dispatcher dispatcher = new Dispatcher();

        class RowMultiplier implements Runnable {
            Thread thread;
            public RowMultiplier() {
                this.thread = new Thread(this);
                this.thread.start();
            }

            public void run() {
                int i = dispatcher.next();
                for (int j = 0; j < size; j++) {
                    int sum = 0;
                    for (int k = 0; k < size; k++) {
                        sum += matrix[i][k] * other.matrix[k][j];
                    }
                    result.matrix[i][j] = sum;
                }
            }
        }

        RowMultiplier[] rowMultipliers = new RowMultiplier[size];
        for (int i = 0; i < size; i++) {
            rowMultipliers[i] = new RowMultiplier();
        }
        try {
            for (int i = 0; i < size; i++) {
                rowMultipliers[i].thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
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

    public boolean equals(DenseMatrix matrix) {
        if (this.size != matrix.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if ( !Arrays.equals(this.matrix[i], matrix.matrix[i]) ) {
                return false;
            }
        }
        return true;
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