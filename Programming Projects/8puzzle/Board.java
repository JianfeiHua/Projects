public class Board {
    
    private final int N;
    private final int[][] blocks;
    private final int distance;
    
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {         
        this.N = blocks.length;                 
        this.blocks = copyBlocks(blocks);
        distance = this.manhattan(blocks);
    }
    // board dimension N
    public int dimension() {                
        return N;
    }
    // number of blocks out of place
    public int hamming() {                 
        int counter = -1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != N * i + (j+1))
                    counter++;
            }
        }
        return counter;
    }
    // sum of Manhattan distances between blocks and goal
   private int manhattan(int[][] initial) {                
        int counter = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (initial[i][j] == 0) continue;
                int igoal = (initial[i][j]-1) / N;
                int jgoal = (initial[i][j]-1) % N;
                counter = counter + Math.abs(i - igoal) + Math.abs(j - jgoal);
            }
        }
        return counter;    
    }
   
    public int manhattan() {                
        return distance;    
    }
    // is this board the goal board?
    public boolean isGoal() {               
        if (distance == 0) return true;
        return false;
    }
    
    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {                  
        int[][] blocksTwin = copyBlocks();
        if (blocksTwin[0][0] != 0 && blocksTwin[0][1] != 0) {
            int first = blocksTwin[0][0];
            blocksTwin[0][0] = blocksTwin[0][1];
            blocksTwin[0][1] = first;
        }
        else {
            int first = blocksTwin[1][0];
            blocksTwin[1][0] = blocksTwin[1][1];
            blocksTwin[1][1] = first; 
        }
        Board theTwin = new Board(blocksTwin);
        return theTwin;
    }
        
    // does this board equal y?
    public boolean equals(Object y) {       
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.distance != that.distance) return false;
        for (int i = N - 1; i > -1; i--) {
            for (int j = N - 1; j > -1; j--) {
                if (this.blocks[i][j] != that.blocks[i][j])
                    return false;
            }
        }
        return true;
    }
        
    // all neighboring boards
    public Iterable<Board> neighbors() {   
        Stack<Board> boardStack = new Stack<Board>();
        int i0 = 0;
        int j0 = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    break;
                }   
            }
        }        
        if (i0 > 0) {
            int[][] blocksCopy = copyBlocks();
            blocksCopy[i0][j0] = blocks[i0-1][j0];
            blocksCopy[i0-1][j0] = 0;
            Board neighbor = new Board(blocksCopy);
            boardStack.push(neighbor);
        }
        if (j0 > 0) {
            int[][] blocksCopy = copyBlocks();
            blocksCopy[i0][j0] = blocks[i0][j0-1];
            blocksCopy[i0][j0-1] = 0;
            Board neighbor = new Board(blocksCopy);
            boardStack.push(neighbor);
        }        

        if (j0 < N -1) {
            int[][] blocksCopy = copyBlocks();
            blocksCopy[i0][j0] = blocks[i0][j0+1];
            blocksCopy[i0][j0+1] = 0;
            Board neighbor = new Board(blocksCopy);
            boardStack.push(neighbor);
        }    
        if (i0 < N -1) {
            int[][] blocksCopy = copyBlocks();
            blocksCopy[i0][j0] = blocks[i0+1][j0];
            blocksCopy[i0+1][j0] = 0;
            Board neighbor = new Board(blocksCopy);
            boardStack.push(neighbor);
        }
        return boardStack;
    }
    // string representation of the board (in the output format specified below)
    public String toString() {              
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    private int[][] copyBlocks() {
        int[][] blocks2 = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks2[i][j] = blocks[i][j];
            }
        }
        return blocks2;
    }
        
    private int[][] copyBlocks(int[][] initial) {
        int[][] blocks2 = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks2[i][j] = initial[i][j];
            }
        }
        return blocks2;
    }
    
    public static void main(String[] args) {
    // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        
        Board initial = new Board(blocks);
        Board theTwin = initial.twin();
        Iterable<Board> neighbors = initial.neighbors();
        
        StdOut.println("initial board:");
        StdOut.println(initial);
        
        StdOut.println("initial methods test: dimension(); hamming(); manhattan():    "
                           + initial.dimension() + "; " + initial.hamming() + "; " 
                           + initial.manhattan());
        
        StdOut.println("initial methods test: isGoal():    " + initial.isGoal());
        
        StdOut.println();
        StdOut.println("A twin board:");
        StdOut.println(theTwin);
        
        StdOut.print("Does the initial equal to the twin:    ");
        StdOut.println(initial.equals(theTwin));
        
        StdOut.println();
        StdOut.println("Neighbors of the initial:");
        for (Board board : neighbors) {
            StdOut.println(board);
        }
    }
}