public class Percolation {
    private int gridSize;
    private WeightedQuickUnionUF grid;
    private boolean[] status;
    private WeightedQuickUnionUF backWash;
    
    // create N-by-N grid, with all sites blocked    
    public Percolation(int N) {
        gridSize = N;
        grid     = new WeightedQuickUnionUF(N * N + 2);
        status   = new boolean[N * N + 2];
        backWash = new WeightedQuickUnionUF(N * N + 1);
        status[0]         = true;
        status[N * N + 1] = true;
    }
    
    // 2D to 1D
    private int xyToN(int i, int j) {
        return ((i - 1) * gridSize + j);
    }
    
    // test if (i, j) is valid
    private void isValid(int i, int j) {
        if (i < 1 || i > gridSize) 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j < 1 || j > gridSize) 
            throw new IndexOutOfBoundsException("column index j out of bounds");
    }
    
    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        isValid(i, j);
        int n = xyToN(i, j);
        if (status[n]) return;
        status[n]   = true;
        if (i > 1 && status[xyToN(i - 1, j)]) {
            grid.union(n, xyToN(i - 1, j));
            backWash.union(n, xyToN(i - 1, j));
        }
        else if (i == 1) {
            grid.union(n, 0);
            backWash.union(n, 0);
        }
        if (i < gridSize && status[xyToN(i + 1, j)]) {
            grid.union(n, xyToN(i + 1, j));
            backWash.union(n, xyToN(i + 1, j));
        }
        else if (i == gridSize)
            grid.union(n, gridSize * gridSize + 1);
        if (j > 1 && status[xyToN(i, j - 1)]) {
            grid.union(n, xyToN(i, j - 1));
            backWash.union(n, xyToN(i, j - 1));
        }
        if (j < gridSize && status[xyToN(i, j + 1)]) {
            grid.union(n, xyToN(i, j + 1));
            backWash.union(n, xyToN(i, j + 1));
        }
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        isValid(i, j);
        return status[xyToN(i, j)];
    }
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        isValid(i, j);
        int n = xyToN(i, j);
        return (isOpen(i, j) && grid.connected(n, 0) && backWash.connected(n, 0));
    }
    // does the system percolate? 
    public boolean percolates() {
        return (grid.connected(0, gridSize * gridSize + 1));
    }
  
    // test if two sites (i, j) and (k, l) are connected
    private boolean isConnect(int i, int j, int k, int l) {
        int n = xyToN(i, j);
        int m = xyToN(k, l);
        return grid.connected(m, n);
    }
    /*
    private void toPrint() {
        for (int i = 0; i < gridSize * gridSize; i ++) {
            StdOut.println("N gridSize status[i]"); 
            StdOut.println(i + " " + gridSize + " " + status[i]);
        }
    }
    */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        Percolation percolation = new Percolation(N);
        percolation.open(1, 2);
        percolation.open(2, 3);
        StdOut.println("Are (1, 2) and (2, 3) connected?");
        StdOut.println(percolation.isConnect(1, 2, 2, 3));
    }
}