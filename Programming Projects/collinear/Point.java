/*************************************************************************
 * Name: Jianfei Hua
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();       

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if (that.y == this.y && that.x != this.x) return +0.0;
        if (that.y != this.y && that.x == this.x) return Double.POSITIVE_INFINITY;
        if (that.y == this.y && that.x == this.x) return Double.NEGATIVE_INFINITY;
        return 1.0 * (that.y - this.y) / (that.x - this.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y == that.y && this.x < that.x) return -1;
        if (this.y == that.y && this.x == that.x) return 0;
        return 1;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }
    
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point point1, Point point2) {
            if (slopeTo(point1) < slopeTo(point2)) return -1;
            if (slopeTo(point1) == slopeTo(point2)) return 0;
            return 1;
        }
    }

    // unit test
    public static void main(String[] args) {
        int n1 = Integer.parseInt(args[0]);
        int n2 = Integer.parseInt(args[1]);
        int n3 = Integer.parseInt(args[2]);
        int n4 = Integer.parseInt(args[3]);
        Point point1 = new Point(n1, n2);
        Point point2 = new Point(n3, n4);
        StdOut.println("point1: " + point1.toString());
        StdOut.println("point2: " + point2.toString());
        StdOut.println("Slope: " + point1.slopeTo(point2));
    }
}