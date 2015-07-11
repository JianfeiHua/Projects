package data;
/**
 * @author JianfeiHua
 * This utility class generate the bollinger band overlay.
 * The output is a double[3] matrix:
 *     matrix[0]: upper band
 *     matrix[1]: middle band, sma(n);
 *     matrix[2]: lower band
 * An empty matrix will be given if not enough data are supplied.
 */
public class BollingerBand {
    private double[] prices;
    private double[] sma;
    private double[] upper;
    private double[] lower;
    /**
     * @param args
     */
    public BollingerBand (double[] prices) {
        this.prices = prices;
    }
    
    /**
     * generate Bollinger band
     * @param n: averaging period, default 20
     * @param m: multiplying factor of standard deviation, default 2
     */
    private void generateBB(int n, int m) {
        if (n < 1 || m < 1 || prices == null || n > prices.length) {
            sma = new double[0];
            upper = new double[0];
            lower = new double[0];
            return;
        }
        
        SMA sma2 = new SMA(prices);
        sma = sma2.getIndicator(n);
        double[] sd = new double[sma.length];
        for (int i = 0; i < sd.length; i++) {
            double sqr = 0.0;
            for (int j = 0; j < n; j++) {
                sqr += (prices[i+j] - sma[i]) * (prices[i+j] - sma[i]);
            }
            sd[i] = Math.sqrt(sqr / n);
        }
        upper = new double[sd.length];
        lower = new double[sd.length];
        for (int i = 0; i < sd.length; i++) {
            upper[i] = sma[i] + sd[i] * m;
            lower[i] = sma[i] - sd[i] * m;
        }
    }
    
    public double[][] getIndicator(int n, int m) {
        generateBB(n, m);
        double[][] bb = new double[3][0];
        bb[0] = upper;
        bb[1] = sma;
        bb[2] = lower;
        return bb;
    }
    
    public double[][] getIndicator() {
        generateBB(20, 2);
        double[][] bb = new double[3][0];
        bb[0] = upper;
        bb[1] = sma;
        bb[2] = lower;
        return bb;
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Stock stock = new Stock("spy");
        HistoryManager.loadHistory("wavechaser", stock);
        BollingerBand b = new BollingerBand(stock.close);
        double[][] bb = b.getIndicator();
        for (int i = 0; i < bb.length; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.printf("%5.2f,", bb[i][j]);
            }
            System.out.println();
        }
        
    }

}
