import java.util.Arrays;

/**
 * Board
 *
 * @author Igor Elkin
 */
public class Board {

    private final short[][] tiles;
    private final int N;
    private int hamming = -1;
    private int manhattan = -1;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.N = blocks.length;
        this.tiles = new short[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = (short) blocks[i][j];
            }
        }
    }

    private Board(short[][] blocks) {
        this.N = blocks.length;
        this.tiles = new short[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(blocks[i], 0, tiles[i], 0, N);
        }
    }

    // board dimension N
    public int dimension() {
        return N;
    }

    // number of blocks out of place
    public int hamming() {
        if (hamming != -1) return hamming;
        int result = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!(i == N - 1 && j == N - 1)) {
                    if (tiles[i][j] != i * N + j + 1) {
                        result++;
                    }
                }
            }
        }
        hamming = result;
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattan != -1) return manhattan;
        int result = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != 0) {
                    int val = tiles[i][j];
                    int goalI = (val - 1) / N;
                    int goalJ = (val - 1) % N;
                    result += Math.abs(goalI - i) + Math.abs(goalJ - j);
                }
            }
        }
        manhattan = result;
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!(i == N - 1 && j == N - 1)) {
                    if (tiles[i][j] != i * N + j + 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        // duplicate board;
        Board twin = new Board(tiles);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N - 1; j++) {
                if (twin.tiles[i][j] != 0 && twin.tiles[i][j+1] != 0) {
                   // exchange tiles
                    short tmp = twin.tiles[i][j];
                    twin.tiles[i][j] = twin.tiles[i][j+1];
                    twin.tiles[i][j+1] = tmp;
                    return twin;
                }
            }
        }
        return null;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return (this.N == that.N)
                && (Arrays.deepEquals(this.tiles, that.tiles));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> result = new Queue<Board>();
        // looking for empty tile
        int emptyI = -1;
        int emptyJ = -1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    emptyI = i;
                    emptyJ = j;
                    break;
                }
            }
        }

        if (emptyI > 0) {
            Board b = new Board(tiles);
            b.tiles[emptyI][emptyJ] = b.tiles[emptyI - 1][emptyJ];
            b.tiles[emptyI - 1][emptyJ] = 0;
            result.enqueue(b);
        }
        if (emptyI < N - 1) {
            Board b = new Board(tiles);
            b.tiles[emptyI][emptyJ] = b.tiles[emptyI + 1][emptyJ];
            b.tiles[emptyI + 1][emptyJ] = 0;
            result.enqueue(b);
        }
        if (emptyJ > 0) {
            Board b = new Board(tiles);
            b.tiles[emptyI][emptyJ] = b.tiles[emptyI][emptyJ - 1];
            b.tiles[emptyI][emptyJ - 1] = 0;
            result.enqueue(b);
        }
        if (emptyJ < N - 1) {
            Board b = new Board(tiles);
            b.tiles[emptyI][emptyJ] = b.tiles[emptyI][emptyJ + 1];
            b.tiles[emptyI][emptyJ + 1] = 0;
            result.enqueue(b);
        }

        return result;
    }

    // string representation of the board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
