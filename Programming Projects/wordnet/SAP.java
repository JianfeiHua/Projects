public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph G;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] s = path(v, w);
        if (s[0] == INFINITY) return -1;
        return s[0];
    }
    
    private int[] path(int v, int w) {
        if (v<0 || w<0 || v>G.V()-1 || w>G.V()-1)
            throw new IndexOutOfBoundsException("Arguments not within [0, V-1]");
        SAPPath vPath = new SAPPath(G, v);
        SAPPath wPath = new SAPPath(G, w);
        int[] distV = vPath.breath();
        int[] distW = wPath.breath();
        int sap = INFINITY;
        int distVW = INFINITY;
        int ancestor = INFINITY;
        for (int i = 0; i < G.V(); i++) {
            if (distV[i] != INFINITY && distW[i] != INFINITY) {
                distVW = distV[i] + distW[i];
                if (distVW < sap) {
                    sap = distVW;
                    ancestor = i;
                }
            }
        }
        int[] p = { sap, ancestor };
        return p;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] s = path(v, w);
        if (s[1] == INFINITY) return -1;
        return s[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] s = path(v, w);
        if (s[0] == INFINITY) return -1;
        return s[0];
    }
    
    private int[] path(Iterable<Integer> v, Iterable<Integer> w) {
        boolean isWithin = true;
        for (int i : v) {
            if (i < 0 || i > G.V() - 1) isWithin = false;
        }
        for (int i : w) {
            if (i < 0 || i > G.V() - 1) isWithin = false;
        }
        if (!isWithin) 
            throw new IndexOutOfBoundsException("Arguments not within [0, V-1]");
        
        SAPPath vPath = new SAPPath(G, v);
        SAPPath wPath = new SAPPath(G, w);
        int[] distV = vPath.breath();
        int[] distW = wPath.breath();
        int sap = INFINITY;
        int distVW = INFINITY;
        int ancestor = INFINITY;
        for (int i = 0; i < G.V(); i++) {
            if (distV[i] != INFINITY && distW[i] != INFINITY) {
                distVW = distV[i] + distW[i];
                if (distVW < sap) {
                    sap = distVW;
                    ancestor = i;
                }
            }
        }
        int[] p = { sap, ancestor };
        return p;
    }
    
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] s = path(v, w);
        if (s[1] == INFINITY) return -1;
        return s[1];
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
   /*
    public static void main(String args[]) {
        In in = new In(args[0]);
        Digraph dg = new Digraph(in);
        int a = (int) (dg.V() * Math.random());
        int b = (int) (dg.V() * Math.random());
        SAP sap = new SAP(dg);
        StdOut.println("try int pairs:");
        StdOut.println("SAP of " + a + " and " + b + " : " + sap.length(a, b));
        StdOut.println("ancestor of " + a + " and " + b + " : " + sap.ancestor(a, b));
        
        Bag<Integer> aa = new Bag<Integer>();
        Bag<Integer> bb = new Bag<Integer>();
        sap = new SAP(dg);
        for (int i = 0; i < 10; i++) {
            aa.add((int) (dg.V() * Math.random()));
            bb.add((int) (dg.V() * Math.random()));
        }
        StdOut.println("try two bags of 10:");
        StdOut.println("SAP of " + "bag1" + " and " + "bag2" + " : " + sap.length(aa, bb));
        StdOut.println("ancestor of " + "bag1" + " and " + "bag2" + " : " + sap.ancestor(aa, bb));
    }
    */
}