import java.util.Arrays;

/**
 * Fast
 *
 * @author Игорь Елькин (ielkin@nvision-group.com)
 */
public class Fast {
    public static void main(String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        String inputFile = args[0];
        // read p from file
        Point[] p = readPoints(inputFile);
        Arrays.sort(p);

        // Aux array for sorting on slope order
        Point[] aux = Arrays.copyOf(p, p.length);

        // draw points
        for (Point point : p) {
            point.draw();
        }

        // find segments
        for (int i = 0; i < p.length; i++) {
            // sort on slope order
            Arrays.sort(aux, p[i].SLOPE_ORDER);
            double curSlope = p[i].slopeTo(aux[0]);
            int start = 0;
            for (int j = 1; j < aux.length; j++) {
                double nextSlope = p[i].slopeTo(aux[j]);
                if (curSlope != nextSlope) {
                    // segment found
                    if (j - start >= 3) {
                        printSegment(aux, start, j - 1);
                    }
                    start = j;
                    curSlope = nextSlope;
                }
            }
            // check for segment that lasts till last point
            if (curSlope == p[i].slopeTo(aux[aux.length - 1])) {
                if (aux.length - start >= 3) {
                    printSegment(aux, start, aux.length - 1);
                }
            }
        }

        StdDraw.show(0);
    }

    private static void printSegment(Point[] aux, int start, int end) {
        Arrays.sort(aux, start, end + 1);
        // print segment
        for (int i = start; i <= end; i++) {
            StdOut.print(aux[i]);
            if(i < end)
                StdOut.print(" -> ");
            else
                StdOut.println();
        }
        // draw segment
        aux[start].drawTo(aux[end]);
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
