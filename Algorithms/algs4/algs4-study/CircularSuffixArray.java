/**
 * CircularSuffixArray
 */
public class CircularSuffixArray {

    private int[] indices;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        indices = new int[s.length()];
        // Construct original array
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        // Sort suffixes
        Quick3Suffixes.sort(s, indices);
    }

    // length of s
    public int length() {
        return indices.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return indices[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++) {
            System.out.print(" " + csa.index(i));
        }
    }

}
