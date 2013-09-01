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
     * Union-find
     */
    private WeightedQuickUnionUF uf;

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
        this.uf = new WeightedQuickUnionUF(N * N + 2);

        // Create percolate grid
        // extra row/column is to allow 1..N indexing.
        this.grid = new boolean[N + 1][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new boolean[N + 1];
        }

        for (int i = 1; i <= N; i++) {
            // connect virtual top to all top sites
            uf.union(0, xyTo1D(i, 1));
            // connect virtual bottom to all bottom sites
            uf.union(N * N + 1, xyTo1D(i, N));
        }
    }

    /**
     * open site (row i, column j) if it is not already
     */
    public void open(int i, int j) {
        if(isOpen(i, j)) return;
        validateIndices(i, j);

        // set open
        grid[i][j] = true;
        // connect open site to neighbor open sites if any
        int idx = xyTo1D(i, j);
        if(i > 1 && isOpen(i - 1, j)) uf.union(idx, xyTo1D(i - 1, j));
        if(i < N && isOpen(i + 1, j)) uf.union(idx, xyTo1D(i + 1, j));
        if(j > 1 && isOpen(i, j - 1)) uf.union(idx, xyTo1D(i, j - 1));
        if(j < N && isOpen(i, j + 1)) uf.union(idx, xyTo1D(i, j + 1));
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
        // is virtual top connect to current site
        return uf.connected(0, xyTo1D(i, j));
    }

    /**
     * does the system percolate?
     */
    public boolean percolates() {
        // is virtual top is connected to virtual bottom
        return uf.connected(0, N * N  + 1);
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
        return (j - 1) * N + i;
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(4);
        p.open(1, 1);
        p.open(1, 2);
        p.open(2, 2);
        p.open(3, 3);
        p.open(4, 4);
        System.out.println(p.isFull(2, 2));
        System.out.println(p.isFull(4, 4));
    }
}
