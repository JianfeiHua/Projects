import java.awt.Color;

public class SeamCarver {
    private Picture image;
    private int[][][] pixels;
    private double[][] energies;
    private double[][] distTo;
    private int[][] pixelTo;
    private boolean isUpdated;
    private final double BORDER = 195075.0;
    
    public SeamCarver(Picture picture) {
        image = new Picture(picture);
        pixels = picToPixels(picture);
        energies = picToEnergies(picture);
        isUpdated = false;
    }
    // initiate the energy matrix
    private double[][] picToEnergies(Picture pic) {
        double[][] energy = new double[pic.height()][pic.width()];
        for (int x = 0; x < pic.width(); x++) {
            energy[0][x] = BORDER;
            energy[pic.height() - 1][x] = BORDER;
        }
        for (int y = 1; y < pic.height() - 1; y++) {
            energy[y][0] = BORDER;
            energy[y][pic.width() - 1] = BORDER;
        }
        for (int x = 1; x < pic.width() - 1; x++) {
            for (int y = 1; y < pic.height() - 1; y++) { 
                energy[y][x] = getEnergy(x, y);
            }
        }
       return energy; 
    }
    
    private int[][][] picToPixels(Picture pic) {
        int[][][] pix = new int[pic.height()][pic.width()][3];
        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                Color color = pic.get(x, y);
                pix[y][x][0] = color.getRed();
                pix[y][x][1] = color.getGreen();
                pix[y][x][2] = color.getBlue();
            }
        }
        return pix;
    }
    
    private double getEnergy(int x, int y) {
        int[] left  = pixels[y][x - 1];
        int[] right = pixels[y][x + 1];
        int[] upper = pixels[y - 1][x];
        int[] lower = pixels[y + 1][x];
        int rx = left[0] - right[0];
        int gx = left[1] - right[1];
        int bx = left[2] - right[2];
        int ry = upper[0] - lower[0];
        int gy = upper[1] - lower[1];
        int by = upper[2] - lower[2];
        return rx*rx + gx*gx + bx*bx + ry*ry + gy*gy + by*by;
    }
                
    // current picture
    public Picture picture() {                       
        if (!isUpdated) return image;
        image = updateImage();
        return image;
    }
    
    private Picture updateImage() {
        Color color;
        Picture pic = new Picture(width(), height());
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                color = new Color(pixels[y][x][0], pixels[y][x][1], pixels[y][x][2]);
                pic.set(x, y, color);
            }
        }
        isUpdated = false;
        return pic;
    }
    
    // width  of current picture
    public int width() {                        
        return energies[0].length;
    }
    
    // height of current picture
    public int height() {                       
        return energies.length;
    }
    
    // energy of pixel at column x and row y in current picture
    public  double energy(int x, int y) {
        if (x < 0 || x > width() -1 || y < 0 || y > height() - 1)
            throw new IndexOutOfBoundsException("x/y must be within [0, w/h - 1].");
        return energies[y][x];
    }
    
    // sequence of indices for horizontal seam in current picture
    public int[] findHorizontalSeam() {
        double[][] oldEnergies = energies;
        energies = transposeEnergies(energies);
        // for debug
        // StdOut.printf("old enegies: %1d x %1d; new energies %1d x %1d.\n", oldEnergies.length, oldEnergies[0].length, energies.length, energies[0].length);
        int[] seam = findVerticalSeam();
        energies = oldEnergies;
        return seam;
    }
    
    private static double[][] transposeEnergies(double[][] matrix) {
        double[][] newMatrix = new double[matrix[0].length][matrix.length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                newMatrix[x][y] = matrix[y][x];
            }
        }
        return newMatrix;
    }
    
    private static int[][][] transposePixels(int[][][] matrix) {
        int[][][] newMatrix = new int[matrix[0].length][matrix.length][matrix[0][0].length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                newMatrix[x][y] = matrix[y][x];
            }
        }
        return newMatrix;        
    }
    
    // sequence of indices for vertical   seam in current picture
    public int[] findVerticalSeam() {
        distTo = new double[height()][width()];
        pixelTo = new int[height()][width()];
        for (int y = 1; y < height() - 1; y++) {
            for (int x = 1; x < width() - 1; x++) 
                relax(x, y);
        }
        double minEnergy = Double.POSITIVE_INFINITY;
        int ymax = height() - 2;
        int xmin = 0; 
        for (int x = 1; x < width() - 1; x++) {
            if (minEnergy > distTo[ymax][x]) {
                minEnergy = distTo[ymax][x];
                xmin = x;
            }
        }
        int[] seam = new int[height()];
        int y = height() - 2;
        seam[height() - 1] = xmin;
        for (int x = xmin; x != 0; x = pixelTo[y--][x]) {
            seam[y] = x;
            // for debug
            //StdOut.printf("current x: %1d; next x: %1d.\n", x, pixelTo[y][x]); 
        }
        seam[0] = seam[1];
        distTo = null;
        pixelTo = null;
        return seam;
    }
    
    private void relax(int x, int y) {
        int[] from = { 0, 0 };
        if (x > 1 && x < width() - 2) {
            int[][] fromPixels = { { x-1, y-1 }, { x, y-1 }, { x+1, y-1 } }; 
            from = minPixel(fromPixels);
        }
        else if (x == 1 && x < width() - 2) {
            int[][] fromPixels = { { x, y-1 }, { x+1, y-1 } };
            from = minPixel(fromPixels);
        }
        else if (x == width() - 2 && x > 1) {
            int[][] fromPixels = { { x-1, y-1 }, { x, y-1 } };
            from = minPixel(fromPixels);           
        }
        else if (x == width() - 2 && x == 1) { 
            from[0] = x;
            from[1] = y;
        }
        distTo[y][x] = energy(x, y) + distTo[from[1]][from[0]];
        pixelTo[y][x] = from[0];
        //for debug
        //StdOut.printf("distTo[%1d][%1d]: %6.1f;   previous pixel (%1d, %1d) with distTo %6.1f energy %6.1f\n", 
        //              y, x, distTo[y][x], from[1], from[0], distTo[from[1]][from[0]], energy(from[0], from[1])); 
    }
    
    private int[] minPixel(int[][] pixel) {
        double minEnergy = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (int i = 0; i < pixel.length; i++) {
            if (minEnergy > distTo[pixel[i][1]][pixel[i][0]]) {
                minEnergy = distTo[pixel[i][1]][pixel[i][0]];
                minIndex = i;
            }
        }
        return pixel[minIndex];
    }
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] a) {
        energies = transposeEnergies(energies);
        pixels = transposePixels(pixels);
        removeVerticalSeam(a);
        energies = transposeEnergies(energies);
        pixels = transposePixels(pixels);
    }
    
    // remove vertical   seam from current picture
    public void removeVerticalSeam(int[] a) {
        //check if the seam is valid
        if (a.length != height())
            throw new IllegalArgumentException("Seam length not equal to w/h.");
        for (int y = 0; y < height(); y++) {
            if (a[y] < 0 || a[y] > width())
                throw new IllegalArgumentException("x/y not within [0, w/h - 1].");
            if (y > 0 && (a[y] - a[y-1] > 1 || a[y] - a[y-1] < -1))
                throw new IllegalArgumentException("Pixels not nearby.");
        }
        if (width() < 2)
            throw new IllegalArgumentException("Pixels depleted.");
        
        int h = height();
        int w = width();
        // update pixels[][][],energies[][]
        int[][][] newPixels = new int[h][w - 1][3];
        double[][] newEnergies = new double[h][w - 1];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int newX;
                if (x <= a[y]) newX = x;
                else newX = x - 1;
                newPixels[y][newX] = pixels[y][x];
                newEnergies[y][newX] = energies[y][x];
            }
        }
        pixels = newPixels;
        
        // update enegies around seam[] a
        for (int y = 1; y < h - 1; y++) {
            if (a[y] > 1 && w > 3) newEnergies[y][a[y] - 1] = getEnergy(a[y] - 1, y);
            if (a[y] < w - 2 && w > 3) newEnergies[y][a[y]] = getEnergy(a[y], y);
        }
        energies = newEnergies;
        isUpdated = true;
    }
    
    public static void main(String[] args) {
        Picture pic = new Picture("10x12.png");
        SeamCarver sc = new SeamCarver(pic);
        StdOut.println("Width: " + sc.width() + "    Height: " + sc.height());
        
        StdOut.println("The energies arrays: ");
        for (int y = 0; y < sc.height(); y++) {
            for (int x = 0; x < sc.width(); x++) {
                StdOut.print(sc.energies[y][x] + "    ");
            }
            StdOut.println("");
        }
        
        int[] a = sc.findVerticalSeam();
        StdOut.println("The vertical seam: ");
        for (int i = 0; i < a.length; i++) 
            StdOut.print(a[i] + ", ");
        StdOut.println("");
        /*
        StdOut.println("the distTo[][] matrix");
        for (int y = 0; y < sc.height(); y++) {
            for (int x = 0; x < sc.width(); x++) {
                StdOut.print(sc.distTo[y][x] + "    ");
            }
            StdOut.println("");
        }
        
        sc.removeVerticalSeam(a);
        StdOut.println("The new energies arrays: ");
        for (int y = 0; y < sc.height(); y++) {
            for (int x = 0; x < sc.width(); x++) {
                StdOut.print(sc.energies[y][x] + "    ");
            }
            StdOut.println("");
        }
        */
        
        int[] b = sc.findHorizontalSeam();
        StdOut.println("The horizontal seam: ");
        for (int i = 0; i < b.length; i++) 
            StdOut.print(b[i] + ", ");
        StdOut.println("");
        /*
        StdOut.println("the distTo[][] matrix");
        for (int y = 0; y < sc.distTo.length; y++) {
            for (int x = 0; x < sc.distTo[0].length; x++) {
                StdOut.print(sc.distTo[y][x] + "    ");
            }
            StdOut.println("");
        }
        StdOut.println("the pixelTo[][] matrix");
        for (int y = 0; y < sc.pixelTo.length; y++) {
            for (int x = 0; x < sc.pixelTo[0].length; x++) {
                StdOut.print(sc.pixelTo[y][x] + "    ");
            }
            StdOut.println("");
        }
        */
    }
}