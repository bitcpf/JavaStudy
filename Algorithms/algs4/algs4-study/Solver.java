/**
 * Solver
 *
 * @author Igor Elkin
 */
public class Solver {

    private Node last;
    private boolean solvable;
    private int moves = -1;

    private static class Node implements Comparable<Node> {
        Board board;
        Node prev;
        int moves;

        private Node(Board board, Node prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
        }

        public int compareTo(Node that) {
            if(this == that) return 0;
            if(this.board.manhattan() > that.board.manhattan()) {
                return 1;
            } else if(this.board.manhattan() < that.board.manhattan()) {
                return -1;
            }
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        Board twin = initial.twin();
        MinPQ<Node> pq = new MinPQ<Node>();
        Node node = new Node(initial, null, 0);
        pq.insert(node);

        MinPQ<Node> twinPQ = new MinPQ<Node>();
        Node twinNode = new Node(twin, null, 0);
        twinPQ.insert(twinNode);

        int count = 0;
        do {
            count++;
            node = pq.delMin();
            for (Board neighbor : node.board.neighbors()) {
                if(node.prev == null || !neighbor.equals(node.prev.board)) {
                    pq.insert(new Node(neighbor, node, count));
                }
            }

            twinNode = twinPQ.delMin();
            for (Board neighbor : twinNode.board.neighbors()) {
                if(node.prev == null || !neighbor.equals(twinNode.prev.board)) {
                    twinPQ.insert(new Node(neighbor, twinNode, count));
                }
            }
        } while(!node.board.isGoal() && !twinNode.board.isGoal());

        if(node.board.isGoal()) {
            moves = count;
            last = node;
            solvable = true;
        } else {
            moves = -1;
            last = null;
            solvable = false;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        if(last == null) return null;
        Stack<Board> solution = new Stack<Board>();
        Node cur = last;
        while(cur != null) {
            solution.push(cur.board);
            cur = cur.prev;
        }
        return solution;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        System.out.println(initial);
        System.out.println("M:" + initial.manhattan() +", H:" + initial.hamming());
        System.out.println(initial.twin());
        System.out.println("Neighbours:");
        for (Board board : initial.neighbors()) {
            System.out.println(board);
        }

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
