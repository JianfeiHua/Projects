public class BaseballElimination {
    private int N;
    private int[][] matches;
    private int[][] scores;
    private String[] names;
    private ST<String, Integer> st;

    private FlowNetwork fn;
    private FordFulkerson ff;
    private double gamesLeft;
    private int trivialVictor;
    
    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        In in = new In(filename);
        N = in.readInt();
        
        st = new ST<String, Integer>();
        matches = new int[N][N];
        scores = new int[N][3];
        names = new String[N];
        for (int i = 0; i < N; i++) {
            String team = in.readString();
            st.put(team, i);
            names[i] = team;
            int win = in.readInt();
            int loss = in.readInt();
            int remaining = in.readInt();
            scores[i][0] = win;
            scores[i][1] = loss;
            scores[i][2] = remaining;
            for (int j = 0; j < N; j++) {
                matches[i][j] = in.readInt();
            }
        }
    }
    
    public int numberOfTeams() {
        // number of teams
        return N;
    }
    public Iterable<String> teams() {
        // all teams
        return st.keys();
    }
    public int wins(String team) {
        // number of wins for given team
        check(team);
        int i = st.get(team);
        return scores[i][0];
    }
    public int losses(String team) {
        // number of losses for given team
        check(team);
        int i = st.get(team);
        return scores[i][1];
    }
    public int remaining(String team) {
        // number of remaining games for given team
        check(team);
        int i = st.get(team);
        return scores[i][2];
    }
    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        check(team1);
        check(team2);
        int i = st.get(team1);
        int j = st.get(team2);        
        return matches[i][j];
    }
    
    public boolean isEliminated(String team) {
        // is given team eliminated?
        check(team);
        fn = null;
        trivialVictor = -1;
        
        if (trivial(team))
            return true;
        initFN(team);
        ff = new FordFulkerson(fn, 0, 1);
        double mf = ff.value();
        return  gamesLeft > mf; 
    }
    
    
    
    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; 
        check(team);
        // null if not eliminated
        if (!isEliminated(team))
            return null;
        // the team that trivially eliminates x;
        if (trivialVictor != -1) {
            Bag<String> bag = new Bag<String>();
            bag.add(names[trivialVictor]);
            return bag;
        }
        // mathematical elimination
        SET<String> set = new SET<String>();
        int x = st.get(team);
        int counter = N + 2;
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                if (j == x || i == x) {
                    counter++;
                    continue;
                }
                if (ff.inCut(counter)) {
                    set.add(names[i]);
                    set.add(names[j]);
                }
                counter++;
            }
        }
        return set;
    }
    
    private boolean trivial(String team) {
        int x = st.get(team);
        int max = scores[x][0] + scores[x][2];
        for (int i = 0; i < N; i++) {
            if (i == x) continue;
            if (scores[i][0] > max) {
                trivialVictor = i;
                return true;
            }
        }
        return false;
    }
    
    private void initFN(String team) {
        int n = 1 + 1 + N + (N-1)*N/2;
        fn = new FlowNetwork(n);
        /* *
         * Construct the FlowNetwork: 
         * 0: s; 
         * 1: t;
         * [2, N+1]: team vertices;
         * [N+2, N+1 + N*(N-1)/2]: game vertices;
         * */
        int x = st.get(team);
        int max = scores[x][0] + scores[x][2];
        //add flow edges between team vertices and t
        for (int i = 0; i < N; i++) {
            int cap = max - scores[i][0];
            if (i == x) cap = 0;            
            FlowEdge e = new FlowEdge(i+2, 1, cap);
            fn.addEdge(e);
        }
        // add flow edges between s and game vertices, and between team vertices and game vertices, 
        // flow = 0 if a vertex involves x
        int counter = N + 2;
        gamesLeft = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                int cap = matches[i][j];
                if (j == x || i == x) 
                    cap = 0;
                FlowEdge e = new FlowEdge(0, counter, cap);
                gamesLeft += cap; // update number of games with team x.
                fn.addEdge(e);
                e = new FlowEdge(counter, i+2, Double.POSITIVE_INFINITY);
                fn.addEdge(e);
                e = new FlowEdge(counter, j+2, Double.POSITIVE_INFINITY);
                fn.addEdge(e);
                counter++;
            }
        }
    }
    
    private void check(String team) {
        if (!st.contains(team))
            throw new IllegalArgumentException("Not a valid team");
    }
    
    /*
    private int ijToIndex(int i, int j) {
        int stt = N + 1;
        int upper = 0;
        if (i == 0) upper = 0;
        else if (i == 1) upper = N - 1;
        else 
            upper = ((N-1) + (N-i)) * i / 2;
        int left = j - i;
        int index = stt + upper + left;
        return index;
    }
    */
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}