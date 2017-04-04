import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by BoogieJay
 * 4/3/17.
 */


public class Percolation {

    private final int size;
    private int[] array;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufNoTail;
    private final int virtualHead;
    private final int virtualTail;
    private int numOfOpens;

    private int xyTo1D(int x, int y) {
        if (x < 1 || x > size || y < 1 || y > size) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        return (x - 1) * size + (y - 1) + 1;
    }

    private void connectNeighbor(int row, int col) {
        int index = xyTo1D(row, col);
        int up = index - size;
        int left = index - 1;
        int down = index + size;
        int right = index + 1;

        if (row - 1 >= 1 && isOpen(row - 1, col)){

            if (!ufNoTail.connected(index, up)){
                System.out.println("connect up");
                uf.union(index, up);
                ufNoTail.union(index, up);
            }
        }
        if (col - 1 >= 1 && isOpen(row, col - 1)){

            if (!ufNoTail.connected(index, left)){
                System.out.println("connect left");
                uf.union(index, left);
                ufNoTail.union(index, left);
            }
        }
        if (row + 1 <= size && isOpen(row + 1, col)){

            if (!ufNoTail.connected(index, down)){
                System.out.println("connect down");
                uf.union(index, down);
                ufNoTail.union(index, down);
            }
        }
        if (col + 1 <= size && isOpen(row, col + 1)){

            if (!ufNoTail.connected(index, right)){
                System.out.println("connect right");
                uf.union(index, right);
                ufNoTail.union(index, right);
            }
        }
    }

    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        size = n;
        array = new int[n * n + 2];
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufNoTail = new WeightedQuickUnionUF(n * n + 1);
        virtualHead = 0;
        virtualTail = n * n + 1;
        numOfOpens = 0;

        array[virtualTail] = 1;
        array[virtualHead] = 1;

    }

    public void open(int row, int col) {
        int index = xyTo1D(row, col);
        index = xyTo1D(row, col);

        if (isOpen(row, col)) {
            return;
        }

        array[index] = 1;
        numOfOpens++;

        if (row == 1) {
            uf.union(index, virtualHead);
            ufNoTail.union(index, virtualHead);
        }
        if (row == size) {
            uf.union(index, virtualTail);
        }

        connectNeighbor(row, col);

    }

    public boolean isOpen(int row, int col) {
        int index = xyTo1D(row, col);

        return array[index] == 1;
    }

    public boolean isFull(int row, int col) {
        int index = xyTo1D(row, col);

        return ufNoTail.connected(index, virtualHead);
    }

    public int numberOfOpenSites() {
        return numOfOpens;
    }

    public boolean percolates() {
        return uf.connected(virtualHead, virtualTail);
    }

}
