import java.util.HashSet;
import java.util.Set;

/**
 * BoggleSolver
 *
 * TODO: Possible optimization - prefix search from the last found char
 * Use char[] instead of String
 */
public class BoggleSolver {

    private static final Integer ONE = 1;
    // Using Trie for simple prefix operations.
    private BoggleTrieST<Integer> dict = new BoggleTrieST<Integer>();

    // Initializes the data structure using the given array
    // of strings as the dictionary.
    // (You can assume each word in the dictionary contains only
    // the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        constructDict(dictionary);
    }

    private void constructDict(String[] dictionary) {
        for (String s : dictionary) {
            dict.put(s, ONE);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> result = new HashSet<String>();

        // look for possible words starting from every cell in the board.
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                searchWords(board, new Cell(i, j), result);
            }
        }

        return result;
    }

    private void searchWords(BoggleBoard board, Cell cell, Set<String> words) {
        boolean[][] inPath = new boolean[board.rows()][board.cols()];
        searchRecursively(board, cell, words, inPath, "");
    }

    private void searchRecursively(BoggleBoard board, Cell cell, Set<String> words,
                                   boolean[][] inPath, String prefix) {
        // skip already visited cells in this path
        if (inPath[cell.row][cell.col]) return;

        // add current char to the prefix
        char letter = board.getLetter(cell.row, cell.col);
        prefix = prefix + (letter == 'Q' ? "QU" : letter);

//        // check if current prefix is word or prefix
//        switch(dict.getNodeType(prefix)) {
//            case MATCH: words.add(prefix); break;
//            case NON_MATCH: return;
//            case PREFIX:
//        }
        if (prefix.length() > 2 && dict.contains(prefix)) {
            words.add(prefix);
        } else
        // check if it is prefix of any word
        // if not there is reason to add more chars
        if (!dict.isPrefix(prefix)) {
            return;
        }

        // add cell to current path
        inPath[cell.row][cell.col] = true;
        // do DFS for all adjacent cells
        if (cell.row > 0) {
            if (cell.col > 0) {
                searchRecursively(board, new Cell(cell.row - 1, cell.col - 1), words, inPath, prefix);
            }
            searchRecursively(board, new Cell(cell.row - 1, cell.col), words, inPath, prefix);
            if (cell.col < board.cols() - 1) {
                searchRecursively(board, new Cell(cell.row - 1, cell.col + 1), words, inPath, prefix);
            }
        }
        if (cell.col > 0) {
            searchRecursively(board, new Cell(cell.row, cell.col - 1), words, inPath, prefix);
        }
        if (cell.col < board.cols() - 1) {
            searchRecursively(board, new Cell(cell.row, cell.col + 1), words, inPath, prefix);
        }
        if (cell.row < board.rows() - 1) {
            if (cell.col > 0) {
                searchRecursively(board, new Cell(cell.row + 1, cell.col - 1), words, inPath, prefix);
            }
            searchRecursively(board, new Cell(cell.row + 1, cell.col), words, inPath, prefix);
            if (cell.col < board.cols() - 1) {
                searchRecursively(board, new Cell(cell.row + 1, cell.col + 1), words, inPath, prefix);
            }
        }
        // remove cell to current path
        inPath[cell.row][cell.col] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dict.get(word) != null) {
            if (word.length() < 3) {
                return 0;
            } else if (word.length() < 5) {
                return 1;
            } else if (word.length() < 6) {
                return 2;
            } else if (word.length() < 7) {
                return 3;
            } else if (word.length() < 8) {
                return 5;
            } else {
                return 11;
            }
        }
        return 0;
    }

    /**
     * Cell in the board
     */
    private static class Cell {
        public final int row;
        public final int col;

        private Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cell cell = (Cell) o;

            if (col != cell.col) return false;
            if (row != cell.row) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            return result;
        }
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
