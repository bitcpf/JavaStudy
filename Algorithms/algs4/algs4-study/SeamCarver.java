import java.awt.Color;

/**
 * SeamCarver
 *
 * @author Igor Elkin
 */
public class SeamCarver {

    // Energy function of border pixels
    private static final double BORDER_ENERGY = 255*255*3;

    private Picture picture;
    private double[][] energyMatrix;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width  of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y in current picture
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1)
            throw new IndexOutOfBoundsException("X is out of range.");
        if (y < 0 || y > height() - 1)
            throw new IndexOutOfBoundsException("Y is out of range.");

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
            return BORDER_ENERGY;

        return deltaSquared(picture.get(x - 1, y), picture.get(x + 1, y))
                + deltaSquared(picture.get(x, y - 1), picture.get(x, y + 1));
    }

    private double deltaSquared(Color leftPix, Color rightPix) {
        int redDiff = leftPix.getRed() - rightPix.getRed();
        int greenDiff = leftPix.getGreen() - rightPix.getGreen();
        int blueDiff = leftPix.getBlue() - rightPix.getBlue();
        return redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff;
    }

    // sequence of indices for horizontal seam in current picture
    public int[] findHorizontalSeam() {
        double[][] matrix = getEnergyMatrix();
        return new SPFinder(getTransposed(matrix)).shortestPath();
    }

    // sequence of indices for vertical seam in current picture
    public int[] findVerticalSeam() {
        double[][] matrix = getEnergyMatrix();
        return new SPFinder(matrix).shortestPath();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] a) {
        checkHorisontalSeam(a);
        Picture newPicture = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            int y = 0;
            for (int j = 0; j < height(); j++) {
                // skip removed pixel
                if (j != a[i]) {
                    newPicture.set(i, y, picture.get(i, j));
                    y++;
                }
            }
        }
        this.picture = newPicture;
    }

    private void checkHorisontalSeam(int[] a) {
        if (height() <= 1)
            throw new IllegalArgumentException("Height is less or equal to 1.");
        if (width() != a.length)
            throw new IllegalArgumentException("Seam is not equal to width");
        checkSeamContinuity(a);
    }

    private void checkSeamContinuity(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (Math.abs(a[i] - a[i-1]) > 1)
                throw new IllegalArgumentException("Not continuous seam.");
        }
    }

    // remove vertical   seam from current picture
    public void removeVerticalSeam(int[] a) {
        checkVerticalSeam(a);
        Picture newPicture = new Picture(width() - 1, height());
        for (int i = 0; i < height(); i++) {
            int x = 0;
            for (int j = 0; j < width(); j++) {
                // skip removed pixel
                if (j != a[i]) {
                    newPicture.set(x, i, picture.get(j, i));
                    x++;
                }
            }
        }
        this.picture = newPicture;
    }

    private void checkVerticalSeam(int[] a) {
        if (width() <= 1)
            throw new IllegalArgumentException("Width is less or equal to 1.");
        if (height() != a.length)
            throw new IllegalArgumentException("Seam is not equal to height");
        checkSeamContinuity(a);
    }

    private double[][] getEnergyMatrix() {
        //if (energyMatrix == null) {
            energyMatrix = new double[height()][width()];
            for (int i = 0; i < energyMatrix.length; i++) {
                double[] row = energyMatrix[i];
                for (int j = 0; j < row.length; j++) {
                     energyMatrix[i][j] = energy(j, i);
                }
            }
        //}
        return energyMatrix;
    }

    private double[][] getTransposed(double[][] matrix) {
        double [][] result = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            double[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                result[j][i] = row[j];
            }
        }
        return result;
    }

    /**
     * Class searches for SP in matrix of energies.
     * Uses virtual start and end nodes to traverse all pixels.
     */
    private class SPFinder {
        private int[] shortestPath;
        private int[][] edgeTo;
        private double[][] distTo;
        private double distToLast = Double.POSITIVE_INFINITY;
        private int edgeToLast = -1;

        public SPFinder(double [][] matrix) {
            shortestPath = new int[matrix.length];
            edgeTo = new int[matrix.length][matrix[0].length];
            distTo = new double[matrix.length][matrix[0].length];

            // init to infinite distances
            for (int i = 0; i < distTo.length; i++) {
                double[] rows = distTo[i];
                for (int j = 0; j < rows.length; j++) {
                    rows[j] = Double.POSITIVE_INFINITY;
                }
            }

            // init to no previous edges
            for (int i = 0; i < edgeTo.length; i++) {
                int[] rows = edgeTo[i];
                for (int j = 0; j < rows.length; j++) {
                    rows[j] = -1;
                }
            }

            // distance to first row of pixels is zero
            for (int i = 0; i < distTo[0].length; i++) {
                distTo[0][i] = 0;
            }

            // construct path (excluding last row)
            for (int i = 0; i < matrix.length - 1; i++) {
                double[] row = matrix[i];
                for (int j = 0; j < row.length; j++) {
                    // left node
                    if (j > 0) {
                        if (distTo[i + 1][j - 1]
                                > distTo[i][j] + matrix[i + 1][j - 1]) {
                            distTo[i + 1][j - 1]
                                    = distTo[i][j] + matrix[i + 1][j - 1];
                            edgeTo[i + 1][j - 1] = j;
                        }
                    }
                    // center node
                    if (distTo[i + 1][j] > distTo[i][j] + matrix[i + 1][j]) {
                        distTo[i + 1][j] = distTo[i][j] + matrix[i + 1][j];
                        edgeTo[i + 1][j] = j;
                    }

                    // right node
                    if (j < row.length - 1) {
                        if (distTo[i + 1][j + 1]
                                > distTo[i][j] + matrix[i + 1][j + 1]) {
                            distTo[i + 1][j + 1]
                                    = distTo[i][j] + matrix[i + 1][j + 1];
                            edgeTo[i + 1][j + 1] = j;
                        }
                    }
                }
            }

            // dealing with last row
            for (int i = 0; i < matrix[matrix.length - 1].length; i++) {
                if (distToLast > distTo[matrix.length - 1][i]) {
                    distToLast = distTo[matrix.length - 1][i];
                    edgeToLast = i;
                }
            }
            // restoring shortest path
            shortestPath[matrix.length - 1] = edgeToLast;
            for (int i = matrix.length - 1; i > 0; i--) {
                shortestPath[i-1] = edgeTo[i][shortestPath[i]];
            }
        }

        public int[] shortestPath() {
            return shortestPath;
        }
    }
}
