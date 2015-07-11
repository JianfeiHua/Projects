/**************
 * Algorithms 1 asssignment 3
 * Fast
 * Jianfei Hua
 * ************/
import java.util.Arrays;

public class Fast {
    public static void main(String[] args) {
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        // read in the input
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point point = new Point(x, y);
            points[i] = point;
            point.draw();
        }
            
        Arrays.sort(points);
        
        // initiating helper arrays
        Point[] subPoints = new Point[N]; 
        double[] slopes = new double[N];
        
        for (int i = 0; i < N - 3; i++) {
            // assign values to helper array subPoints
            for (int j = 0; j < N; j++) 
                subPoints[j] = points[j];

            // sorting subPoints by slope
            Arrays.sort(subPoints, points[i].SLOPE_ORDER);
            
            // calculate slopes of points[i] to subPoints
            for (int j = 0; j < N; j++) 
                slopes[j] = points[i].slopeTo(subPoints[j]);
            
            // checking if 4+ points collinear
            double slope = slopes[0];
            int counter = 1;
            for (int j = 1; j < N; j++) {
                //if (slope == Double.NEGATIVE_INFINITY) continue; // ?
                if (slope == slopes[j]) {
                    counter++;
                    
                    // if j = N - 1, check if a line is found
                    if (j == N - 1 && counter > 2) {
                        boolean isFirst = true;
                        for (int k = 0; k < counter; k++) {
                            if (points[i].compareTo(subPoints[j-k]) > 0) {
                                isFirst = false;
                                break;
                            }
                        }
                        // draw and print 
                        if (isFirst) {
                            Point[] linedPoints =  new Point[counter+1];
                            linedPoints[0] = points[i];
                            for (int k = 1; k <= counter; k++) 
                                linedPoints[k] = subPoints[j-counter+k];
                            linedPoints[0].drawTo(linedPoints[counter]);
                            for (int k = 0; k < counter; k++) 
                                StdOut.print(linedPoints[k] + " -> ");
                            StdOut.println(linedPoints[counter]);
                        }    
                    }
                  
                }
                else {
                    if (counter > 2) {
                        // check if point[i] is the first by natural order
                        // if not, the it is a subsegment
                        boolean isFirst = true;
                        for (int k = 1; k <= counter; k++) {
                            if (points[i].compareTo(subPoints[j-k]) > 0) {
                                isFirst = false;
                                break;
                            }
                        }
                        // draw a line if it is not a subsegment
                        if (isFirst) {
                            Point[] linedPoints =  new Point[counter+1];
                            linedPoints[0] = points[i];
                            for (int k = 1; k <= counter; k++) 
                                linedPoints[k] = subPoints[j-counter+k-1];
                            linedPoints[0].drawTo(linedPoints[counter]);
                            for (int k = 0; k < counter; k++) 
                                StdOut.print(linedPoints[k] + " -> ");
                            StdOut.println(linedPoints[counter]);
                        }
                    }
                    counter = 1;
                    slope = slopes[j];
                }
            }
        }
        StdDraw.show(0);
    }
}