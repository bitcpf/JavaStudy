/**
 * Programming Assignment 1.
 * Percolation Statistics.
 *
 * @author Igor Elkin
 */
public class PercolationStats {

    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    /**
     * perform T independent computational experiments on an N-by-N grid
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) throw
                new IllegalArgumentException(
                        "Invalid arguments: N = " + N + ", T = " + T);

        double[] results = new double[T];

        for (int t  = 0; t < T; t++) {
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
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceLo = mean() - 1.96 * stddev() / Math.sqrt(T);
        confidenceHi = mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * sample mean of percolation threshold
     */
    public double mean() {
        return mean;
    }

    /**
     * sample standard deviation of percolation threshold
     */
    public double stddev() {
        return stddev;
    }

    /**
     * returns lower bound of the 95% confidence interval
     */
    public double confidenceLo() {
        return confidenceLo;
    }

    /**
     * returns upper bound of the 95% confidence interval
     */
    public double confidenceHi() {
        return confidenceHi;
    }

    /**
     * test client, described below
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats st = new PercolationStats(N, T);
        System.out.println("mean                    = " + st.mean());
        System.out.println("stddev                  = " + st.stddev());
        System.out.println("95% confidence interval = "
                            + st.confidenceLo() + "," + st.confidenceHi());
    }
}
