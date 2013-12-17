/**
 * WordNet
 *
 */
public class WordNet {

    private SET<String> nouns = new SET<String>();
    private SeparateChainingHashST<Integer, String> synsetStrings
            = new SeparateChainingHashST<Integer, String>();
    private SeparateChainingHashST<String, Bag<Integer>> synsets
            = new SeparateChainingHashST<String, Bag<Integer>>();
    private int count;
    private Digraph hypernims;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        readSynsets(synsets);
        readHypernyms(hypernyms);
    }

    private void readSynsets(String synsetsFile) {
        In in = new In(synsetsFile);
        int id = -1;
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] fields = line.split(",");
            id = Integer.parseInt(fields[0]);
            String[] words = fields[1].split("\\s");
            synsetStrings.put(id, fields[1]);

            for (String word : words) {
                // adding word to nouns set
                nouns.add(word);
                // add synset to current word
                Bag syns = synsets.get(word);
                if (syns == null) {
                    syns = new Bag<Integer>();
                    synsets.put(word, syns);
                }
                syns.add(id);
            }
        }
        count = id + 1;
    }

    private void readHypernyms(String hypernymsFile) {
        In in = new In(hypernymsFile);
        hypernims = new Digraph(count);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int hyperId = Integer.parseInt(fields[i]);
                hypernims.addEdge(id, hyperId);
            }
        }
        checkRootedDAG(hypernims);
        sap = new SAP(hypernims);
    }

    private void checkRootedDAG(Digraph digraph) {
        DirectedCycle dcycle = new DirectedCycle(digraph);

        if (dcycle.hasCycle())
            throw new IllegalArgumentException("Graph has cycle!");
        // looking for root
        int root = -1;
        for (int i = 0; i < digraph.V(); i++) {
            Iterable<Integer> adjs = digraph.adj(i);
            int n = 0;
            for (int adj : adjs) {
                n++;
            }
            if (n == 0) {
                if (root != -1)
                    throw new IllegalArgumentException("More than one root!");
                else
                   root = i;
            }
        }
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Should be nouns");
        }
        return sap.length(synsets.get(nounA), synsets.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor
    // of nounA and nounB in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Should be nouns");
        }
        return synsetStrings.get(
                sap.ancestor(synsets.get(nounA), synsets.get(nounB)));
    }

    // for unit testing of this class
    public static void main(String[] args) {
        WordNet net1 = new WordNet(args[0], args[1]);

        System.out.println(net1.sap("individual", "edible_fruit"));
        System.out.println(net1.distance("Black_Plague", "black_marlin"));
        System.out.println(net1.distance("American_water_spaniel", "histology"));
        System.out.println(net1.distance("Brown_Swiss", "barrel_roll"));

    }
}
