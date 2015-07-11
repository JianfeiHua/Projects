package data;
/* This utility class accept a prices series and generate exponential moving average.
 * The return is a double[] of ema values. Empty if not enough data are given. 
 * */

public class EMA {
    private double[] prices;
    private double[] ema;
    
    public EMA(double[] prices) {
        this.prices = prices;
    }
    private void generateEMA(int n) {
        if (n <= 0 || n > prices.length) {
            ema = new double[0];
            return;
        }
        SMA sma = new SMA(prices);
        ema = sma.getIndicator(n);
        double factor = 2.0 / (n + 1);

        for (int i = ema.length - 2; i >= 0; i--) {
            ema[i] = (prices[i] - ema[i+1]) * factor + ema[i+1];
        }
    }
    

    public double[] getIndicator(int n) {
        generateEMA(n);
        return ema;
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        double[] a = { 22.27, 22.19, 22.08, 22.17, 22.18, 22.13,
                       22.23, 22.43, 22.24, 22.29, 22.15,
                       22.39, 22.38, 22.61, 23.36, 24.05,
                       23.75, 23.83, 23.95, 23.63, 23.82,
                       23.87, 23.65, 23.19, 23.10, 23.33, 
                       22.68, 23.10, 22.40, 22.17};
        // invert a
        for (int i = 0; i < a.length / 2; i++) {
            double tmp = a[i];
            a[i] = a[a.length-1-i];
            a[a.length-1-i]= tmp;
        }
        
        SMA sma = new SMA(a);
        EMA ema = new EMA(a);
        System.out.println("Price series: ");
        for (double x : a) {
            System.out.print(x + ", ");
        }
        System.out.println("\n\nSMA-10 series: ");
        for (double x : sma.getIndicator(10)) {
            System.out.print(x + ", ");
        }
        System.out.println("\n\nEMA-10 series: ");
        for (double x : ema.getIndicator(10)) {
            System.out.print(x + ", ");
        }
        
    }
}
