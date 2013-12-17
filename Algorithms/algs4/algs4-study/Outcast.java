/**
 * Outcast
 */
public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = null;
        int maxDist = 0;
        for (String noun : nouns) {
            int dist = 0;
            for (String noun2 : nouns) {
                if (!noun.equals(noun2)) {
                    dist += wordnet.distance(noun, noun2);
                }
            }
            if (dist > maxDist) {
                maxDist = dist;
                outcast = noun;
            }
        }
        return outcast;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
