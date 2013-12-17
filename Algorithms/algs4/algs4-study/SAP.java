import java.util.Arrays;
import java.util.List;

/**
 * SAP
 */
public class SAP {

    private final Digraph g;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        g = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkIndexIsInBounds(v);
        checkIndexIsInBounds(w);
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(g, w);
        boolean [] marked = new boolean[g.V()];
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(v);
        marked[v] = true;
        int min = Integer.MAX_VALUE;
        while (!q.isEmpty()) {
            int n = q.dequeue();
            if (bfsw.distTo(n) != Integer.MAX_VALUE
                    && (bfsv.distTo(n) + bfsw.distTo(n) < min)) {
                min = bfsv.distTo(n) + bfsw.distTo(n);
            }
            for (int m : g.adj(n)) {
                // prevent cycles
                if (!marked[m]) {
                    q.enqueue(m);
                    marked[m] = true;
                }
            }
        }

        if (min != Integer.MAX_VALUE)
            return min;
        else
            return -1;
    }

    private void checkIndexIsInBounds(int v) {
        if (v < 0 || v > g.V() - 1)
            throw new IndexOutOfBoundsException("Index is out of bounds: " + v);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        checkIndexIsInBounds(v);
        checkIndexIsInBounds(w);
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(g, v);

        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(g, w);
        boolean [] marked = new boolean[g.V()];
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(v);
        marked[v] = true;
        int min = Integer.MAX_VALUE;
        int ancestor = -1;
        while (!q.isEmpty()) {
            int n = q.dequeue();
            if (bfsw.distTo(n) != Integer.MAX_VALUE
                    && (bfsv.distTo(n) + bfsw.distTo(n) < min)) {
                min = bfsv.distTo(n) + bfsw.distTo(n);
                ancestor = n;
            }
            for (int m : g.adj(n)) {
                // prevent cycles
                if (!marked[m]) {
                    q.enqueue(m);
                    marked[m] = true;
                }
            }
        }

        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v
    // and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (int val : v) {
            checkIndexIsInBounds(val);
        }
        for (int val : w) {
            checkIndexIsInBounds(val);
        }
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(g, w);
        boolean [] marked = new boolean[g.V()];
        Queue<Integer> q = new Queue<Integer>();
        for (int i : v) {
            q.enqueue(i);
            marked[i] = true;
        }

        int min = Integer.MAX_VALUE;
        while (!q.isEmpty()) {
            int n = q.dequeue();
            if (bfsw.distTo(n) != Integer.MAX_VALUE
                    && (bfsv.distTo(n) + bfsw.distTo(n) < min)) {
                min = bfsv.distTo(n) + bfsw.distTo(n);
            }
            for (int m : g.adj(n)) {
                // prevent cycles
                if (!marked[m]) {
                    q.enqueue(m);
                    marked[m] = true;
                }
            }
        }

        if (min != Integer.MAX_VALUE)
            return min;
        else
            return -1;
    }

    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (int val : v) {
            checkIndexIsInBounds(val);
        }
        for (int val : w) {
            checkIndexIsInBounds(val);
        }
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(g, w);
        boolean [] marked = new boolean[g.V()];
        Queue<Integer> q = new Queue<Integer>();
        for (int i : v) {
            q.enqueue(i);
            marked[i] = true;
        }

        int min = Integer.MAX_VALUE;
        int ancestor = -1;
        while (!q.isEmpty()) {
            int n = q.dequeue();
            if (bfsw.distTo(n) != Integer.MAX_VALUE
                    && (bfsv.distTo(n) + bfsw.distTo(n) < min)) {
                min = bfsv.distTo(n) + bfsw.distTo(n);
                ancestor = n;
            }
            for (int m : g.adj(n)) {
                // prevent cycles
                if (!marked[m]) {
                    q.enqueue(m);
                    marked[m] = true;
                }
            }
        }

        return ancestor;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

        List<Integer> vlist = Arrays.asList(6, 2);
        List<Integer> wlist = Arrays.asList(10, 12);
        int length   = sap.length(vlist, wlist);
        int ancestor = sap.ancestor(vlist, wlist);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

    }
}
