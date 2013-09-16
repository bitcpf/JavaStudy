import java.util.Arrays;

/**
 * Brute
 *
 */
public class Brute {
    public static void main(String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        String inputFile = args[0];
        // read p from file
        Point[] p = readPoints(inputFile);

        Arrays.sort(p);

        // draw points
        for (Point point : p) {
            point.draw();
        }

        // find segments
        for (int i = 0; i < p.length - 3; i++) {
            for (int j = i + 1; j < p.length - 2; j++) {
                for (int k = j + 1; k < p.length - 1; k++) {
                    for (int l = k + 1; l < p.length; l++) {
                        double ij = p[i].slopeTo(p[j]);
                        double ik = p[i].slopeTo(p[k]);
                        if (ij == ik) {
                            double il = p[i].slopeTo(p[l]);
                            if (ik == il) {
                                // print segment
                                StdOut.print(p[i]);
                                StdOut.print(" -> ");
                                StdOut.print(p[j]);
                                StdOut.print(" -> ");
                                StdOut.print(p[k]);
                                StdOut.print(" -> ");
                                StdOut.println(p[l]);
                                // draw segment
                                p[i].drawTo(p[l]);
                            }
                        }
                    }
                }
            }
        }

        StdDraw.show(0);
    }

    private static Point[] readPoints(String inputFile) {
        Point[] points;
        In in = new In(inputFile);
        int count = in.readInt();
        points = new Point[count];
        int i = 0;
        while (!in.isEmpty()) {
            int x = in.readInt();
            int y = in.readInt();
            points[i++] = new Point(x, y);
        }
        return points;
    }
}
