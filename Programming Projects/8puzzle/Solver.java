// equals(): add a step of manhanttan;
// compared from bottom to top
public class Solver {
    private Node init;
    private Node twin;
    private Node goal;

     // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {          
        init = new Node();
        init.current = initial;
        init.moves = 0;
        init.previous = null;
        
        twin = new Node();
        twin.current = init.current.twin();
        twin.moves = 0;
        twin.previous = null;
        
        goal = aStar();

    }
        
    private class Node implements Comparable<Node> {
        private Board current;
        private int moves;
        private Node previous;
       
        public int compareTo(Node that) {
            int dif = (this.current.manhattan() + this.moves) - (that.current.manhattan() + that.moves);
            return dif;
        }
    }
    
    
    private Node aStar() {
        MinPQ<Node> pq = new MinPQ<Node>();
        pq.insert(init);
        pq.insert(twin);
        
        while (true) {
            Node popNode = pq.delMin();
            if (popNode.current.isGoal())
                return popNode;
            Iterable<Board> neighbors = popNode.current.neighbors();
            for (Board board : neighbors) {
                Node oldNode = popNode;
                boolean isOld = false;
                // check if the board is one of the previous board
                while(oldNode != null) {
                    if (oldNode.current.equals(board)) {
                        isOld = true;
                        break;
                    }
                    oldNode = oldNode.previous;
                }
                
                if (isOld) continue;
                
                Node node = new Node();
                node.current = board;
                node.moves = popNode.moves + 1;
                node.previous = popNode;
                pq.insert(node);
                
            }
        }
        /*
        while (true) {
            Node popNode = pq.delMin();
            if (popNode.current.isGoal())
                return popNode;
            Iterable<Board> neighbors = popNode.current.neighbors();
            for (Board board : neighbors) {
                if (!popNode.current.equals(board)) {
                    Node node = new Node();
                    node.current = board;
                    node.moves = popNode.moves + 1;
                    node.previous = popNode;
                    pq.insert(node);
                }
            }
        }
        */
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {            
        Node node = goal;
        while (true) {
            if (node.previous == null) 
                break;
            node = node.previous;
        }
        return node.equals(init);
    }
    // min number of moves to solve initial board; -1 if no solution
    public int moves() {                     
        if (isSolvable()) return goal.moves;
        else return -1;
    }
    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {      
        if (!isSolvable()) return null;
        Stack<Board> sol = new Stack<Board>();
        Node node = goal;
        while (true) {
            sol.push(node.current);
            node = node.previous;
            if (node == null) break;
        }
        return sol;
    }
    /*   
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board b = new Board(blocks);
        Stopwatch stopwatch = new Stopwatch();
        
        Solver s = new Solver(b);
        boolean end = false;
        Node goal = s.goal;
        while (!end) {
            StdOut.println("Moves:    " + goal.moves);
            StdOut.println(goal.current);
            if (goal.previous != null)
                goal = goal.previous;
            else
                end = true;
        }
        
        StdOut.println("****** elapsed time ******" + "\n" + stopwatch.elapsedTime());
    }
    */
    
    
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
}