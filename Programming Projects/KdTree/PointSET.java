
public class PointSET {
    private SET<Point2D> set;
    private int N;
    
    public PointSET() {
        set = new SET<Point2D>();
        N = 0;
    }
    
    public boolean isEmpty() {
        return N == 0;
    }
    
    public int size() {
        return N;
    }
    
    public void insert(Point2D p) {
        if (!set.contains(p)) {
            set.add(p);
            N++;
        }
    }
    
    public boolean contains(Point2D p) {
        return set.contains(p);
    }
    
    public void draw() {
        for (Point2D p : set)  {
            p.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p))
                queue.enqueue(p);
        }
        return queue;
    }
    
    public Point2D nearest(Point2D p) {
        if (N == 0)
            return null;
        Point2D nearstp = null;
        double distSquare = Double.POSITIVE_INFINITY;
        for (Point2D point : set) {
            if (p.distanceSquaredTo(point) < distSquare) {
                nearstp = point;
                distSquare = p.distanceSquaredTo(point);
            }
        }
        return nearstp;
    }
    
    public static void main(String[] args) {
        PointSET pointSet = new PointSET();
        StdOut.println("Is pointSet empty after it is initiated:    " + pointSet.isEmpty());
        int i = 0;
        while (i < 999999) {
            Point2D p = new Point2D(Math.random(), Math.random());
            pointSet.insert(p);
            i++;
        }
        Point2D p = new Point2D(.5, .5);
        pointSet.insert(p);
        StdOut.println("What is the size after 1000 inserts:    " + pointSet.size());
        StdOut.println("Does it contain (.5, .5):    " + pointSet.contains(p));
        //pointSet.draw();
        Point2D p2 = new Point2D(.4, .4);
        StdOut.println("A nearest point to (.4, .4):    " + pointSet.nearest(p2));
        
        RectHV rect = new RectHV(.39, .39, .41, .41);
        Iterable<Point2D> queue = pointSet.range(rect);
        StdOut.println("Points in RectHV(.39, .39, .41, .41):    ");
        for (Point2D point : queue) {
            StdOut.println(point);
        }
    }
    /*
    public PointSET()                               // construct an empty set of points
    public boolean isEmpty()                        // is the set empty?
    public int size()                               // number of points in the set
    public void insert(Point2D p)                   // add the point p to the set (if it is not already in the set)
    public boolean contains(Point2D p)              // does the set contain the point p?
    public void draw()                              // draw all of the points to standard draw
    public Iterable<Point2D> range(RectHV rect)     // all points in the set that are inside the rectangle
    public Point2D nearest(Point2D p)               // a nearest neighbor in the set to p; null if set is empty
    */
}