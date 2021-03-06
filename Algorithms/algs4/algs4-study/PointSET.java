import java.util.ArrayList;
import java.util.List;

/**
 * PointSET
 *
 * @author Igor Elkin
 */
public class PointSET {

    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all of the points to standard draw
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> results = new ArrayList<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                results.add(point);
            }
        }
        return results;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        Point2D nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (Point2D point : points) {
            double dist = p.distanceSquaredTo(point);
            if (nearestDist >= dist) {
                nearest = point;
                nearestDist = dist;
            }
        }
        return nearest;
    }
}
