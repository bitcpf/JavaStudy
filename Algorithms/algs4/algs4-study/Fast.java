import java.util.Arrays;

/**
 * Fast
 *
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

        // find segments on each point in order
        for (int i = 0; i < p.length - 3; i++) {
            // sort on the slope order
            Arrays.sort(aux, 0, aux.length, p[i].SLOPE_ORDER);
            int start = 0;
            double curSlope = p[i].slopeTo(aux[start]);
            for (int j = start + 1; j < aux.length; j++) {
                double nextSlope = p[i].slopeTo(aux[j]);
                if (curSlope != nextSlope) {
                    // segment found
                    if (j - start >= 3) {
                        printSegment(p[i], aux, start, j);
                    }
                    start = j;
                    curSlope = nextSlope;
                }
            }
            // check for segment that lasts till last point
            if (curSlope == p[i].slopeTo(aux[aux.length - 1])) {
                if (aux.length - start >= 3) {
                    printSegment(p[i], aux, start, aux.length);
                }
            }
        }

       StdDraw.show(0);
    }

    private static void printSegment(Point origin, Point[] aux, int start, int end) {
        // copy found points together with origin point into one array
        Point[] temp = new Point[end - start + 1];
        temp[0] = origin;
        System.arraycopy(aux, start, temp, 1, end - start);
        // sort by coordinates
        Arrays.sort(temp);
        // to avoid subsegments printing - print only if origin is leftmost point.
        if (temp[0] != origin) return;
        // print segment
        for (int i = 0; i < temp.length; i++) {
            StdOut.print(temp[i]);
            if (i < temp.length - 1)
                StdOut.print(" -> ");
            else
                StdOut.println();
        }
        // draw segment
        temp[0].drawTo(temp[temp.length - 1]);
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
