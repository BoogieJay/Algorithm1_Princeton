/**
 * Created by BoogieJay
 * 4/4/17.
 *
 * The Statistic class to evaluate the percolation
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    //array to store the percent of each trial
    private final double[] percents;

    //time of trials
    private final int trials;

    //total sites
    private final double totoalSites;

    /**
     * perform trials independent experiments on an n-by-n grid
     * @param n
     * @param trials
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        //initial variables
        percents = new double[trials];
        this.trials = trials;
        totoalSites = n * n;


        //perform percolation process on each trial
        for (int i = 0; i < trials; i++) {

            //create new instance of percolation
            Percolation perco = new Percolation(n);

            int randomRow = 0;
            int randomCol = 0;

            //while the system not percolate, keeping on opening new site
            while (!perco.percolates()){

                //get random row and column
                randomRow = StdRandom.uniform(n) + 1;
                randomCol = StdRandom.uniform(n) + 1;

                perco.open(randomRow, randomCol);
            }

            //store current percent into percents array
            percents[i] = perco.numberOfOpenSites() / totoalSites;

        }

    }

    /**
     * sample mean of percolation threshold
     * @return mean
     */
    public double mean() {
        return StdStats.mean(percents);
    }

    /**
     * sample standard deviation of percolation threshold
     * @return stddev
     */
    public double stddev() {
        if (trials == 1){
            return Double.NaN;
        }
        return StdStats.stddev(percents);
    }

    /**
     * low  endpoint of 95% confidence interval
     * @return low endpoint
     */
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(trials);
    }

    /**
     * high endpoint of 95% confidence interval
     * @return high endpoint
     */
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(trials);
    }

    /**
     * test client
     * @param args
     */
    public static void main(String[] args) {

        //check arguments
        if (args.length != 2) {
            throw new java.lang.IllegalArgumentException();
        }

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats pcs = new PercolationStats(n, t);

        System.out.println("Mean                          = " + pcs.mean());
        System.out.println("Stddev                        = " + pcs.stddev());
        System.out.println("95% confidence interval       = [" + pcs.confidenceLo() + ", " +  pcs.confidenceHi() + "]");
    }
}
