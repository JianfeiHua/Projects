public class Outcast {
    private final WordNet wordnet;
    
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
        
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[][] allDist = new int[nouns.length][nouns.length];
        int maxDist = -1;
        int outcast = -1;
        for (int i = 0; i < nouns.length; i++) {
            for (int j = i; j < nouns.length; j++) {
                int dist = wordnet.distance(nouns[i], nouns[j]);
                allDist[i][j] = dist;
                allDist[j][i] = dist;
            }
        }
        
        for (int i = 0; i < nouns.length; i++) {
            int oneDist = 0;
            for (int j = 0; j < nouns.length; j++) {
                oneDist += allDist[i][j];
            }
            if (maxDist < oneDist) {
                maxDist = oneDist;
                outcast = i;
            }
        }
        return  nouns[outcast];  
    }
        
        
    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}