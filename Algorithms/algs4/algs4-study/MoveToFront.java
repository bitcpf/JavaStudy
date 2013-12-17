/**
 * MoveToFront
 *
 */
public class MoveToFront {

    // apply move-to-front encoding, reading from standard
    // input and writing to standard output
    public static void encode() {

        char[] alphabet = prepareAlphabet();

        while (!BinaryStdIn.isEmpty()) {
            char found = BinaryStdIn.readChar(8);
            int idx = indexOf(alphabet, found);
            // update alphabet
            if (idx != 0) {
                System.arraycopy(alphabet, 0, alphabet, 1, idx);
                alphabet[0] = found;
            }
            // write result
            BinaryStdOut.write(idx, 8);
        }
        // close output stream
        BinaryStdOut.close();
    }

    private static int indexOf(char[] alphabet, char found) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == found) {
                return i;
            }
        }
        return -1;
    }

    private static char[] prepareAlphabet() {
        char [] alphabet = new char[256];
        for (char i = 0; i < 256; i++) {
            alphabet[i] = i;
        }
        return alphabet;
    }

    // apply move-to-front decoding, reading from standard
    // input and writing to standard output
    public static void decode() {

        char[] alphabet = prepareAlphabet();

        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readChar(8);
            char c = alphabet[idx];
            // update alphabet
            if (idx != 0) {
                System.arraycopy(alphabet, 0, alphabet, 1, idx);
                alphabet[0] = c;
            }
            // write result
            BinaryStdOut.write(c, 8);
        }
        // close output stream
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
