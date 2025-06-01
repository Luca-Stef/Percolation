/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] thresholds;
    private final int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "Grid size and number of trials must be positive integers");
        }

        this.trials = trials;
        thresholds = new double[trials];
        for (int t = 0; t < trials; t++) {

            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {

                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                if (perc.isOpen(row, col)) {
                    continue;
                }

                perc.open(row, col);
            }

            thresholds[t] = (double) perc.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percStats = new PercolationStats(n, t);

        System.out.println("mean                    = " + percStats.mean());
        System.out.println("stddev                  = " + percStats.stddev());
        System.out.println(
                "95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats
                        .confidenceHi() + "]");
        System.out.println();
    }
}
