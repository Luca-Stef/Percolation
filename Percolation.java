/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */


import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private final int n;
    private int nOpen;
    private final int virtualTop;
    private final int virtualBottom;
    private final WeightedQuickUnionUF wquf;
    private final WeightedQuickUnionUF wqufFull;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n < 1) {
            throw new IllegalArgumentException(
                    "length and height of grid must be positive integers");
        }

        this.n = n;
        int nSquared = n * n;
        this.grid = new boolean[n][n];
        virtualTop = nSquared;
        virtualBottom = nSquared + 1;
        wquf = new WeightedQuickUnionUF(nSquared + 2);
        wqufFull = new WeightedQuickUnionUF(nSquared + 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        validate(row, col);

        if (grid[row - 1][col - 1]) {
            return;
        }

        this.grid[row - 1][col - 1] = true;
        this.nOpen++;

        // virtual top site
        if (row == 1) {
            wquf.union(map(row, col, ""), virtualTop);
            wqufFull.union(map(row, col, ""), virtualTop);
        }

        // virtual bottom site
        if (row == n) {
            wquf.union(map(row, col, ""), virtualBottom);
        }

        // site below
        if (row < n) {
            if (grid[row][col - 1]) {
                wquf.union(map(row, col, ""), map(row, col, "down"));
                wqufFull.union(map(row, col, ""), map(row, col, "down"));
            }
        }

        // site above
        if (row - 2 >= 0) {
            if (grid[row - 2][col - 1]) {
                wquf.union(map(row, col, ""), map(row, col, "up"));
                wqufFull.union(map(row, col, ""), map(row, col, "up"));
            }
        }

        // right adjacent
        if (col < n) {
            if (grid[row - 1][col]) {
                wquf.union(map(row, col, ""), map(row, col, "right"));
                wqufFull.union(map(row, col, ""), map(row, col, "right"));
            }
        }

        // left adjacent
        if (col - 2 >= 0) {
            if (grid[row - 1][col - 2]) {
                wquf.union(map(row, col, ""), map(row, col, "left"));
                wqufFull.union(map(row, col, ""), map(row, col, "left"));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {

        validate(row, col);

        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {

        validate(row, col);

        return wqufFull.find(map(row, col, "")) == wqufFull.find(virtualTop) && isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return wquf.find(virtualTop) == wquf.find(virtualBottom);
    }

    // validate indices
    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("row and column indices must be between 1 and n");
        }
    }

    // maps from 2D to 1D indices
    private int map(int row, int col, String shift) {

        row--;
        col--;

        if (shift.equals("up")) {
            return n * row + col - n;
        }

        else if (shift.equals("down")) {
            return n * row + col + n;
        }

        else if (shift.equals("left")) {
            return n * row + col - 1;
        }

        else if (shift.equals("right")) {
            return n * row + col + 1;
        }

        else {
            return n * row + col;
        }
    }
}
