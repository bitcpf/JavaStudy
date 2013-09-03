/**
 * Programming Assignment 1.
 * Percolation Implementation.
 *
 * @author Igor Elkin
 */
public class Percolation {

    /**
     * Percolation Grid size
     */
    private int N;

    /**
     * Union-find for percolation
     */
    private WeightedQuickUnionUF percUf;

    /**
     * Separate Union-find for checks if site is full (to avoid backwash).
     */
    private WeightedQuickUnionUF fullUf;
    /**
     * Percolation grid (false - empty, true - full)
     */
    private boolean[][] grid;

    /**
     * create N-by-N grid, with all sites blocked
     */
    public Percolation(int N) {
        this.N = N;
        // 2 extra slots are used for virtual top (0) and bottom (N + 1)
        this.percUf = new WeightedQuickUnionUF(N * N + 2);
        // 1 extra slot is used for virtual top (0)
        this.fullUf = new WeightedQuickUnionUF(N * N + 1);

        // Create percolate grid
        // extra row/column is to allow 1..N indexing.
        this.grid = new boolean[N + 1][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new boolean[N + 1];
        }
    }

    /**
     * open site (row i, column j) if it is not already
     */
    public void open(int i, int j) {
        if (isOpen(i, j)) return;
        validateIndices(i, j);

        // set open
        grid[i][j] = true;
        // connect open site to neighbor open sites if any
        int idx = xyTo1D(i, j);
        if (i > 1 && isOpen(i - 1, j)) {
            percUf.union(idx, xyTo1D(i - 1, j));
            fullUf.union(idx, xyTo1D(i - 1, j));
        }
        if (i < N && isOpen(i + 1, j)) {
            percUf.union(idx, xyTo1D(i + 1, j));
            fullUf.union(idx, xyTo1D(i + 1, j));
        }
        if (j > 1 && isOpen(i, j - 1)) {
            percUf.union(idx, xyTo1D(i, j - 1));
            fullUf.union(idx, xyTo1D(i, j - 1));
        }
        if (j < N && isOpen(i, j + 1)) {
            percUf.union(idx, xyTo1D(i, j + 1));
            fullUf.union(idx, xyTo1D(i, j + 1));
        }
        // connect virtual top to top site
        if (i == 1) {
            percUf.union(0, xyTo1D(i, j));
            fullUf.union(0, xyTo1D(i, j));
        }
        // connect virtual bottom to bottom site - only for percolation UF
        if (i == N) percUf.union(N * N + 1, xyTo1D(i, j));

    }

    /**
     * is site (row i, column j) open?
     */
    public boolean isOpen(int i, int j) {
        validateIndices(i, j);
        return grid[i][j];
    }

    /**
     * is site (row i, column j) full?
     */
    public boolean isFull(int i, int j) {
        validateIndices(i, j);
        // is open and virtual top connect to current site
        return grid[i][j] && fullUf.connected(0, xyTo1D(i, j));
    }

    /**
     * does the system percolate?
     */
    public boolean percolates() {
        // is virtual top is connected to virtual bottom
        return percUf.connected(0, N * N  + 1);
    }

    /**
     * Validates indices for range [1;N]
     */
    private void validateIndices(int i, int j) {
        if (i <= 0 || i > N || j <= 0 || j > N)
            throw new IndexOutOfBoundsException("Index is out of bounds.");
    }

    /**
     * Converts 2D array coordinates in range [1, N] to 1D array index [1, N*N].
     */
    private int xyTo1D(int i, int j) {
        return (i - 1) * N + j;
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(4);
        p.open(1, 1);
        p.open(1, 2);
        p.open(2, 2);
        p.open(3, 3);
        p.open(4, 4);
        StdOut.println(p.isFull(2, 2));
        StdOut.println(p.isFull(4, 4));
    }
}
