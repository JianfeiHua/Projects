public class PercolationStats {
    private int number;
    private int size;
    private double[] test;
    
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {   
        if (N <= 0) throw new IllegalArgumentException("Grid size N <= 0"); 
        if (T <= 0) throw new IllegalArgumentException("Trial T <= 0");   
        number = T;
        size   = N;
        test   = new double[T];
        for (int i = 0; i < number; i++) {
            Percolation percolation = new Percolation(size);
            int count               = 0;
            while (!percolation.percolates()) {
                int ii = StdRandom.uniform(1, size + 1);
                int jj = StdRandom.uniform(1, size + 1);
                if (!percolation.isOpen(ii, jj)) {
                    percolation.open(ii, jj);
                    count++;
                }
                else continue;
            }
            test[i] = (double) count / (size * size);
        }
    }
    
    // sample mean of percolation threshold
    public double mean() {                    
        return StdStats.mean(test);
    }
    
   // sample standard deviation of percolation threshold
    public double stddev() {                 
        return StdStats.stddev(test);
    }
    
    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {            
        return mean() - 1.96 * stddev() / Math.sqrt(number);
    }
    
    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {             
        return mean() + 1.96 * stddev() / Math.sqrt(number);
    }
    
    // test client, as described
    public static void main(String[] args) {  
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(N, T);
        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = " + percolationStats.confidenceLo()
                           + ", " + percolationStats.confidenceHi());
    }
}