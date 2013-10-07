import java.util.ArrayList;
import java.util.List;

/**
 * KdTree
 *
 * @author Igor Elkin
 */
public class KdTree {

    private Node root;
    private int size = 0;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle
                                // corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // in case of tree is emtpy
        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1, 1));
            size++;
        } else {
            put(root, p, root.rect, true);
        }
    }

    private Node put(Node node, Point2D p, RectHV rect, boolean byX) {
        if (node == null) {
            size++;
            return new Node(p, rect);
        }
        int cmp = comparePoints(p, node.p, byX);
        if (cmp < 0) {
            RectHV r;
            if (node.lb == null || node.lb.rect == null) {
                if (byX) {
                    r = new RectHV(rect.xmin(), rect.ymin(),
                                    node.p.x(), rect.ymax());
                } else {
                    r = new RectHV(rect.xmin(), rect.ymin(),
                                    rect.xmax(), node.p.y());
                }
            } else {
                r = node.lb.rect;
            }
            node.lb = put(node.lb, p, r, !byX);
        } else if (cmp > 0) {
            RectHV r;
            if (node.rt == null || node.rt.rect == null) {
                if (byX) {
                    r = new RectHV(node.p.x(), rect.ymin(),
                                    rect.xmax(), rect.ymax());
                } else {
                    r = new RectHV(rect.xmin(), node.p.y(),
                                    rect.xmax(), rect.ymax());
                }
            } else {
                r = node.rt.rect;
            }
            node.rt = put(node.rt, p, r, !byX);
        }
        return node;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }

    private boolean contains(Node x, Point2D p, boolean byX) {
        if (x == null) return false;
        int cmp = comparePoints(p, x.p, byX);
        if (cmp < 0) return contains(x.lb, p, !byX);
        else if (cmp > 0) return contains(x.rt, p, !byX);
        else return true;
    }

    private int comparePoints(Point2D p1, Point2D p2, boolean byX) {
        int cmp;
        if (byX) {
            if (p1.x() < p2.x()) cmp = -1;
            else if (p1.x() > p2.x()) cmp = 1;
            else {
                if (p1.y() == p2.y()) cmp = 0;
                else cmp = -1;
            }
        } else {
            if (p1.y() < p2.y()) cmp = -1;
            else if (p1.y() > p2.y()) cmp = 1;
            else {
                if (p1.x() == p2.x()) cmp = 0;
                else cmp = -1;
            }
        }
        return cmp;
    }


    // draw all of the points to standard draw
    public void draw() {
        draw(root, true);
    }

    private void draw(Node node, boolean byX) {
        if (node == null) return;

        StdDraw.setPenRadius();
        if (byX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        node.p.draw();

        draw(node.lb, !byX);
        draw(node.rt, !byX);
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> results = new ArrayList<Point2D>();
        if (!isEmpty())
            range(root, rect, results);
        return results;
    }

    private void range(Node node, RectHV rect, List<Point2D> results) {
        if (rect.contains(node.p)) results.add(node.p);
        if (node.lb != null) {
            if (rect.intersects(node.lb.rect)) {
                range(node.lb, rect, results);
            }
        }
        if (node.rt != null) {
            if (rect.intersects(node.rt.rect)) {
                range(node.rt, rect, results);
            }
        }
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        return nearest(root, p, null, true);
    }

    private Point2D nearest(Node node, Point2D p, Point2D nearest, boolean byX) {
        Point2D currentNearest = nearest;
        if ((currentNearest == null)
                || (currentNearest.distanceSquaredTo(p)
                    > node.p.distanceSquaredTo(p))) {
            currentNearest = node.p;
        }

        Node firstNode, secondNode;
        if (byX) {
            if (p.x() < node.p.x()) {
                firstNode = node.lb;
                secondNode = node.rt;
            } else {
                firstNode = node.rt;
                secondNode = node.lb;
            }
        } else {
            if (p.y() < node.p.y()) {
                firstNode = node.lb;
                secondNode = node.rt;
            } else {
                firstNode = node.rt;
                secondNode = node.lb;
            }
        }

        if (firstNode != null) {
            if (currentNearest.distanceSquaredTo(p)
                    > firstNode.rect.distanceSquaredTo(p)) {
                currentNearest = nearest(firstNode, p, currentNearest, !byX);
            }
        }

        if (secondNode != null) {
            if (currentNearest.distanceSquaredTo(p)
                    > secondNode.rect.distanceSquaredTo(p)) {
                currentNearest = nearest(secondNode, p, currentNearest, !byX);
            }
        }

        return currentNearest;
    }
}
