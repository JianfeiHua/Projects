package data;

public class Stock {
    public String symbol;
    // parsed and adjusted historical data
    public int[] id;
    public String[] date;
    public double[] open;
    public double[] high;
    public double[] low;
    public double[] close;
    public int[] volume;
    // the number of days
    public int N;
    
    public Stock(String s) {
        symbol = s;
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
