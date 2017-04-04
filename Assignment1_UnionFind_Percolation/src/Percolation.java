/**
 * Created by BoogieJay
 * 4/3/17.
 *
 * The Percolation class
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    //size equals the parameter n
    private final int size;

    //using int array to maintain the open/close state
    private int[] array;

    //unionFind class with virtualHead and virtualTail
    private final WeightedQuickUnionUF uf;

    //unionFind class with only virtualTail to solve backwash problem
    private final WeightedQuickUnionUF ufNoTail;

    //virtual head
    private final int virtualHead;

    //virtual tail
    private final int virtualTail;

    private int numOfOpens;

    /**
     * The constructor of percolation
     * @param n the size of the (n * n) 2D array
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        size = n;

        //array with n * n size, plus virtualHead(index0) and virtualTail(index n*n + 1)
        array = new int[n * n + 2];

        //two unionfind instances
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufNoTail = new WeightedQuickUnionUF(n * n + 1);


        virtualHead = 0;
        virtualTail = n * n + 1;

        numOfOpens = 0;

        //virtualHead and virtualTail are initially open
        array[virtualTail] = 1;
        array[virtualHead] = 1;

    }


    /**
     * Open a site with row and col specified
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        //convert to 1d index
        int index = xyTo1D(row, col);

        // if site already open, return
        if (isOpen(row, col)) {
            return;
        }

        //open the site
        array[index] = 1;

        //adding number of open site
        numOfOpens++;

        //if site is on the top row, connect it to the virtualHead
        if (row == 1) {
            uf.union(index, virtualHead);
            ufNoTail.union(index, virtualHead);
        }

        //if site is on the bottom row, connect it to the virtualTail
        if (row == size) {
            uf.union(index, virtualTail);
        }

        //connect to its neighbor
        connectNeighbor(row, col);

    }

    /**
     * Check a site is open or not
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col) {
        int index = xyTo1D(row, col);

        return array[index] == 1;
    }

    /**
     * Check a site is full(connecting to the virtualHead) or not
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col) {
        int index = xyTo1D(row, col);

        return ufNoTail.connected(index, virtualHead);
    }

    /**
     * get totoal number of open sites
     * @return the totoal number of open site
     */
    public int numberOfOpenSites() {
        return numOfOpens;
    }

    /**
     * check weather the whole diagram is percolates
     * @return
     */
    public boolean percolates() {
        return uf.connected(virtualHead, virtualTail);
    }

    /**
     * convert 2D indexs to 1D index
     * @param x row
     * @param y Col
     * @return index of 1D array
     */
    private int xyTo1D(int x, int y) {
        if (x < 1 || x > size || y < 1 || y > size) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        return (x - 1) * size + (y - 1) + 1;
    }

    /**
     * connect a site to its neighbor
     * @param row
     * @param col
     */
    private void connectNeighbor(int row, int col) {

        int index = xyTo1D(row, col);

        //calculate the indexs of its neighbor in 1D representation
        int up = index - size;
        int left = index - 1;
        int down = index + size;
        int right = index + 1;

        //connect up
        if (row - 1 >= 1 && isOpen(row - 1, col)) {

            if (!ufNoTail.connected(index, up)) {
                uf.union(index, up);
                ufNoTail.union(index, up);
            }
        }

        //connect left
        if (col - 1 >= 1 && isOpen(row, col - 1)) {

            if (!ufNoTail.connected(index, left)) {
                uf.union(index, left);
                ufNoTail.union(index, left);
            }
        }

        //connect down
        if (row + 1 <= size && isOpen(row + 1, col)) {

            if (!ufNoTail.connected(index, down)) {
                uf.union(index, down);
                ufNoTail.union(index, down);
            }
        }

        //connect right
        if (col + 1 <= size && isOpen(row, col + 1)) {

            if (!ufNoTail.connected(index, right)) {
                uf.union(index, right);
                ufNoTail.union(index, right);
            }
        }
    }
}
