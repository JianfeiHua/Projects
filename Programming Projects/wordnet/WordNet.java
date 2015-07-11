import java.util.HashMap;
    
public class WordNet {
    private final Digraph G;
    private final String[] synsets;
    private final HashMap<String, Bag<Integer>> IDsets;
    private final SAP sap;
    private final Iterable<String> allNouns;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        this.G = hypernymsToDigraph(hypernyms);
        this.sap = new SAP(G);
        this.synsets = parseSynsets(synsets);
        this.IDsets = parseNouns();
        this.allNouns = IDsets.keySet();
        ////////
    }
    
    // helper method to construct a digraph from hypernyms.txt
    private Digraph hypernymsToDigraph(String hypernyms) {
        // read file, put all edges in a queue as (v, w), (v, w), ...
        // using max to track the max integer to determine the number of vertex
        In in = new In(hypernyms);
        Queue<Integer> q = new Queue<Integer>();
        int max = -1;
        while (!in.isEmpty()) {
            String s = in.readLine();
            String[] parts = s.split(",");
            int from = Integer.parseInt(parts[0]);
            if (from > max) max = from;
            for (int i = 1; i < parts.length; i++) {
                int to = Integer.parseInt(parts[i]);
                q.enqueue(from);
                q.enqueue(to);
                if (to > max) max = to;
            }
        }
        // construct the digraph
        // tracking if a vertex is a root using boolean[V] 
        Digraph dg = new Digraph(max + 1);
        boolean[] notRoot = new boolean[max + 1];
        while (!q.isEmpty()) {
            int from = q.dequeue();
            int to = q.dequeue();
            dg.addEdge(from, to);
            notRoot[from] = true;
        }
        // calculate the number of root
        // # of roots = 1 is required
        int count = 0;
        for (int i = 0; i < max + 1; i++) {
            if (!notRoot[i]) count++;
        }
        if (count != 1) 
            throw new IllegalArgumentException("Hypernums unrooted.");
        // check if dg is acyclic
        DirectedCycle dc = new DirectedCycle(dg);
        if (dc.hasCycle())
            throw new IllegalArgumentException("Hypernums has cycles.");
        
        return dg;
    }
    
    // helper function to initiate synsets[V]
    private String[] parseSynsets(String file) {
        In in = new In(file);
        String[] synset = new String[G.V()];
        while (!in.isEmpty()) {
            String s = in.readLine();
            String[] parts = s.split(",");
            int id = Integer.parseInt(parts[0]);
            synset[id] = parts[1];
        }
        return synset;
    }
    
    // helper function to initiate IDsets
    private HashMap<String, Bag<Integer>> parseNouns() {
        HashMap<String, Bag<Integer>> map = new HashMap<String, Bag<Integer>>(synsets.length);
        for (int i = 0; i < synsets.length; i++) {
            String s = synsets[i];
            String[] parts = s.split(" ");
            for (int j = 0; j < parts.length; j++) {
                String key = parts[j];
                if (map.containsKey(key)) {
                    Bag<Integer> bag = map.get(key);
                    bag.add(i);
                }
                else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(i);
                    map.put(key, bag);
                }
            }
        }
    return map;
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return allNouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return IDsets.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not WordNet nouns.");
        Bag<Integer> bagA = IDsets.get(nounA);
        Bag<Integer> bagB = IDsets.get(nounB);
        int dist = sap.length(bagA, bagB);
        return dist;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not WordNet nouns.");
        Bag<Integer> bagA = IDsets.get(nounA);
        Bag<Integer> bagB = IDsets.get(nounB);
        int id = sap.ancestor(bagA, bagB);
        return synsets[id];
    }

    // for unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        while (!StdIn.isEmpty()) {
            String sa = StdIn.readString();
            String sb = StdIn.readString();
            int length   = wordnet.distance(sa, sb);
            String ancestor = wordnet.sap(sa, sb);
            StdOut.printf("distance = %d, ancestor = %s\n", length, ancestor);
        }
    }
    
    
}