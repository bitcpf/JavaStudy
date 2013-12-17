/**
 * BurrowsWheeler
 */
public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard
    // input and writing to standard output
    public static void encode() {
        // read the input
        String s = BinaryStdIn.readString();

        CircularSuffixArray csa = new CircularSuffixArray(s);

        int first = 0;
        char[] t = new char[s.length()];
        for (int i = 0; i < csa.length(); i++) {
            // calculate tail chars
            if (csa.index(i) == 0) {
                first = i;
                t[i] = s.charAt(s.length() - 1);
            } else {
                t[i] = s.charAt(csa.index(i) - 1);
            }
        }

        // print original string index in suffix array
        BinaryStdOut.write(first);
        // write tail chars array
        for (int i = 0; i < t.length; i++) {
            BinaryStdOut.write(t[i], 8);
        }

        // close output stream
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard
    // input and writing to standard output
    public static void decode() {
        // read original string index in suffix array
        int first = BinaryStdIn.readInt();

        // read t[] array and do counting of chars
        int [] counts = new int[256 + 1];
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            sb.append(c);
            counts[c + 1]++;
        }
        char[] t = sb.toString().toCharArray();

        // cumulate counts
        for (int i = 0; i < 256; i++) {
            counts[i + 1] += counts[i];
        }

        // fill both first chars in sorted order and next array
        char[] f = new char[t.length];
        int[] next = new int[t.length];
        for (int i = 0; i < t.length; i++) {
            f[counts[t[i]]] = t[i];
            next[counts[t[i]]] = i;
            counts[t[i]]++;
        }

        // decode
        int cur = first;
        for (int i = 0; i < next.length; i++) {
            BinaryStdOut.write(f[cur], 8);
            cur = next[cur];
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
