public class KdTree {
    private Node root;
    private int N;
    private boolean rootStart;
    private RectHV nextRect;
    private Point2D neighbor;
    private double distSquare;
    
    public KdTree() {
        root = null;
        N = 0;
        rootStart = true;
        nextRect = null;
        neighbor = null;
        distSquare = 0;
    }
    
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        
        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }
    
    public boolean isEmpty() {
        return N == 0;
    }
    
    public int size() {
        return N;
    }
    
    /***********************************************************************
    *  Insert a Point2D into KdTree
    *  If the same Point2D already exists, do nothing
    ***********************************************************************/
    public void insert(Point2D p) {
        if (p == null) return;
        if (isEmpty()) {
            root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0), null, null);
            //StdOut.println(root.p);
            //StdOut.println(root.rect);
            N++;
        }
        else { root = insert(root, p, rootStart); N++; }
    }
    
    private Node insert(Node x, Point2D p, boolean isX) {
        if (x == null) {
            //StdOut.println(p);
            //StdOut.println(nextRect);
            return new Node(p, nextRect, null, null);
        }
        
        int cmp = compareXY(x.p, p, isX);
        if (cmp < 0) {
            if (x.rt == null) nextRect = childRect(x, cmp, isX); 
            x.rt = insert(x.rt, p, !isX);
        }
        else if (cmp > 0) {
            if (x.lb == null) nextRect = childRect(x, cmp, isX);
            x.lb = insert(x.lb, p, !isX);
        }
        else {
            if (x.p.equals(p)) { N--; return x; }
            else {
                if (x.rt == null) nextRect = childRect(x, cmp, isX);
                x.rt = insert(x.rt, p, !isX);
            }
        }
        return x;
    }
    
   /***********************************************************************
    *  update the nextRect for a child
    ***********************************************************************/
    private RectHV childRect(Node parentNode, int cmp, boolean isX) {
        if (cmp <= 0) {
            //if (parentNode.rt != null) return parentNode.rt.rect;
            if (isX) {
                //StdOut.println(parentNode.p + ", " + parentNode.rect);
                return new RectHV(parentNode.p.x(), parentNode.rect.ymin(), parentNode.rect.xmax(), parentNode.rect.ymax());
            }
            else
                return new RectHV(parentNode.rect.xmin(), parentNode.p.y(), parentNode.rect.xmax(), parentNode.rect.ymax());
        }
        else {
            //if (parentNode.lb != null) return parentNode.lb.rect;
            if (isX)
                return new RectHV(parentNode.rect.xmin(), parentNode.rect.ymin(), parentNode.p.x(), parentNode.rect.ymax());
            else
                return new RectHV(parentNode.rect.xmin(), parentNode.rect.ymin(), parentNode.rect.xmax(), parentNode.p.y());
        }
    }
    
   /***********************************************************************
    *  Compare two Point2Ds on X or Y, with starting X/Y provided
    ***********************************************************************/    
    private int compareXY(Point2D nodeP, Point2D newP, boolean isX) {
        int cmp = 0;
        if (isX) cmp = Point2D.X_ORDER.compare(nodeP, newP);
        else     cmp = Point2D.Y_ORDER.compare(nodeP, newP);
        return cmp;
    }
    
   /***********************************************************************
    *  implements contains with a helper method
    ***********************************************************************/ 
    public boolean contains(Point2D p) {
        return contains(root, p, rootStart);
    }
    
    private boolean contains(Node x, Point2D p, boolean isX) {
        if (x == null) return false;
        int cmp = compareXY(x.p, p, isX);
        if      (cmp < 0) return contains(x.rt, p, !isX);
        else if (cmp > 0) return contains(x.lb, p, !isX);
        else {
            if (x.p.equals(p)) return true;
            else return contains(x.rt, p, !isX);
        }
    }

   /***********************************************************************
    *  draw all nodes, similar to inorder traversal in BST
    ***********************************************************************/ 
    public void draw() {
        draw(root, rootStart);
        StdDraw.rectangle(.5, .5, .5, .5);
    }
    
    private void draw(Node x, boolean isX) {
        if (x == null) return;
        draw(x.lb, !isX); 
        drawNode(x, isX);
        draw(x.rt, !isX);
    }
    
    private void drawNode(Node x, boolean isX) {
        if (isX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        StdDraw.setPenColor();
        StdDraw.setPenRadius(.01); 
        x.p.draw();
        StdDraw.setPenRadius(); 
    }
    
   /***********************************************************************
    *  all points in the set that are inside the rectangle
    ***********************************************************************/
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, rect, queue, rootStart);
        return queue;
    }
    
    private void range(Node x, RectHV rect, Queue<Point2D> queue, boolean isX) {
        if (x == null) return;
        if (x.lb != null && x.lb.rect.intersects(rect)) range(x.lb, rect, queue, !isX);
        if (rect.contains(x.p)) queue.enqueue(x.p);
        if (x.rt != null && x.rt.rect.intersects(rect)) range(x.rt, rect, queue, !isX);
    }
   
   /***********************************************************************
    *  find the nearest neighbor to a point recursively
    ***********************************************************************/
    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        neighbor = root.p;
        distSquare = root.p.distanceSquaredTo(p);
        nearest(root, p);
        return neighbor;
    }
    
    private void nearest(Node x, Point2D p) {
        if (x.p.distanceSquaredTo(p) < distSquare) {
            neighbor = x.p;
            distSquare = x.p.distanceSquaredTo(p);
        }
        if (x.lb == null && x.rt == null) return;
        else if (x.lb != null && x.rt == null) {
            if (x.lb.rect.distanceSquaredTo(p) < distSquare)
                nearest(x.lb, p);
        }
        else if (x.lb == null && x.rt != null) {
            if (x.rt.rect.distanceSquaredTo(p) < distSquare)
                nearest(x.rt, p);
        }
        else {
            double distLB = x.lb.rect.distanceSquaredTo(p);
            double distRT = x.rt.rect.distanceSquaredTo(p);
            if (distLB < distRT) {
                if (distLB < distSquare) nearest(x.lb, p);
                if (distRT < distSquare) nearest(x.rt, p);
            }
            else {
                if (distRT < distSquare) nearest(x.rt, p);
                if (distLB < distSquare) nearest(x.lb, p);
            }
        }
    }
    
    /* Test client*/
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        StdOut.println("Is kdTree empty after it is initiated:    " + kdTree.isEmpty());
        if (args.length == 0) {
            int i = 0;
            while (i < 999999) {
                Point2D p = new Point2D(Math.random(), Math.random());
                kdTree.insert(p);
                i++;
            }
        }
        else {
            In in = new In(args[0]);
            while (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                Point2D p = new Point2D(x, y);
                kdTree.insert(p);
            }
        }
            Point2D p = new Point2D(.5, .5);
            kdTree.insert(p);
            StdOut.println("What is the size after 10000 inserts:    " + kdTree.size());
            StdOut.println("Does it contain (.5, .5):    " + kdTree.contains(p));
            //StdDraw.clear();
            //kdTree.draw();
            RectHV rect = new RectHV(.39, .39, .41, .41);
            //rect.draw();
            Iterable<Point2D> queue = kdTree.range(rect);
            StdOut.println("Points in RectHV(.39, .39, .41, .41):    ");
            for (Point2D point : queue) {
                StdOut.println(point);
            }
            Point2D p2 = new Point2D(.4, .4);
            StdOut.println("A nearest point to (.4, .4):    " + kdTree.nearest(p2));
    }
}