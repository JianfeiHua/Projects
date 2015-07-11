package data;
/* This utility class accept a prices series and generate exponential moving average.
 * The return is a double[] of ema values. Empty if not enough data are given. 
 * 
 * 2015.4.18 Fixed a bug when instance variable did not refresh after an unsuccessful call of getIndicator();
 * */
public class SMA {
    private double[] prices;
    private double[] sma;
    
    public SMA(double[] prices) {
        this.prices = prices;
    }
    private void generateSMA(int n) {
        if (n <= 0 || prices == null || n > prices.length) {
            sma = new double[0];
            return;
        }
        sma = new double[prices.length - n + 1];
        double sum = 0.0;
        for (int i = 0; i < n - 1; i++) {
            sum += prices[i];
        }
        for (int i = 0; i < sma.length; i++) {
            sum += prices[i+n-1];
            sma[i] = sum / n;
            sum -= prices[i];
        }
    }
    

    public double[] getIndicator(int n) {
        generateSMA(n);
        return sma;
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        double[] a = { 11,12,13,14,15,16,17 };
        SMA sma5 = new SMA(a);
        for (double x : sma5.getIndicator(5)) {
            System.out.print(x + ", ");
        }
    }

}
