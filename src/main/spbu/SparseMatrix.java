package spbu;

import java.io.BufferedReader;
import java.util.*;

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

    public SparseMatrix mulSparseSparse(SparseMatrix other) {
        SparseMatrix res = new SparseMatrix(size);
        for (int h = 0; h < size; h++) {
            for (int i = 0; i < size; i++) {
                int sum = 0;
                for (int j = 0; j < size; j++) {
                    int keyA = h * size + j;
                    int keyB = j * size + i;
                    if ((this.hash.get(keyA) != null) && (other.hash.get(keyB) != null)) {
                        sum += this.hash.get(keyA) * other.hash.get(keyB);
                    }
                }
                if (sum != 0) {
                    res.hash.put(h * size + i, sum);
                }
            }
        }
        return res;
    }

    public SparseMatrix mulSparseDense(DenseMatrix other) {
        SparseMatrix res = new SparseMatrix(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int sum = 0;
                for (int k = 0; k < size; k++) {
                    if (this.hash.get(i * size + k) != null) {
                        sum += this.hash.get(i * size + k) * other.matrix[k][j];
                    }
                }
                if ( sum != 0 ) {
                    res.hash.put(i * size + j, sum);
                }
            }
        }
        return res;
    }

    public SparseMatrix dMulSparseSparse(SparseMatrix other) {
        class Dispatcher {
            int value = 0;
            public int next() {
                synchronized (this) {
                    return value++;
                }
            }
        }

        SparseMatrix result = new SparseMatrix(this.size);
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
                        if (hash.get(i * size + k) != null && other.hash.get(k * size + j) != null) {
                            sum += hash.get(i * size + k) * other.hash.get(k * size + j);
                        }
                    }
                    if (sum != 0) {
                        result.hash.put(i * size + j, sum);
                    }
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

    public boolean equals(SparseMatrix matrix) {
        return this.size == matrix.size && this.hash.equals(matrix.hash);
    }

    public void HashOut()
    {
        for (int i = 0; i < size*size; i++)
        {
            if(i%size == 0){
                System.out.println();
            }
            if (hash.get(i)!=null)
            {
                System.out.print((int)hash.get(i) + " ");
            }
            else
            {
                System.out.print("0 ");
            }
        }
    }

}