/**
 * Programming Assignment 1.
 * Percolation Statistics.
 *
 * @author Igor Elkin
 */
public class PercolationStats {

    private double[] results;
    private int T;
    /**
     * perform T independent computational experiments on an N-by-N grid
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T < 0) throw new IndexOutOfBoundsException("Invalid arguments: N = " + N + ", T = " + T);

        this.T = T;
        results = new double[T];

        for(int t  = 0; t < T; t++) {
            Percolation p = new Percolation(N);
            int opened = 0;
            do {
                // get random blocked site
                int i, j;
                do {
                    i = StdRandom.uniform(1, N + 1);
                    j = StdRandom.uniform(1, N + 1);
                } while(p.isOpen(i, j));
                // open it
                p.open(i, j);
                opened++;
            } while(!p.percolates());
            double ratio = ((double) opened) / (N * N);
            results[t] = ratio;
        }
    }

    /**
     * sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /**
     * returns lower bound of the 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * returns upper bound of the 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * test client, described below
     */
    public static void main(String[] args) {
        PercolationStats st = new PercolationStats(200, 100);
        System.out.println("mean                    =" + st.mean());
        System.out.println("stddev                  =" + st.stddev());
        System.out.println("95% confidence interval =" + st.confidenceLo() + "," + st.confidenceHi());
    }
}
