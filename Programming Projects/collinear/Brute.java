/**************
 * Algorithms 1 asssignment 3
 * Brute
 * Jianfei Hua
 * ************/
import java.util.Arrays;

public class Brute {
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
        
        // checking with brute force
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                for (int k = j + 1; k < N; k++) {
                    for (int l = k + 1; l < N; l++) {
                        double slopeIJ = points[i].slopeTo(points[j]);
                        double slopeIK = points[i].slopeTo(points[k]);
                        double slopeIL = points[i].slopeTo(points[l]);
                        if (slopeIJ == slopeIK && slopeIJ == slopeIL) {
                            Point[] linedPoints = { points[i], points[j], 
                                                    points[k], points[l] };
                            //for (int m = 0; m < 4; m++) 
                            //    linedPoints[m].draw();
                            linedPoints[0].drawTo(linedPoints[3]);
                            StdOut.println(linedPoints[0] + " -> " 
                                           + linedPoints[1] + " -> "  
                                           + linedPoints[2] + " -> " 
                                           + linedPoints[3]);
                            
                        }
                    }
                }
            }
        }
        StdDraw.show(0);
    }
    
}