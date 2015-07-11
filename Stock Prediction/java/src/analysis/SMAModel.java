package analysis;
import data.*;
import java.util.ArrayList;
public class SMAModel {
    public final String MODEL_NAME = "SMAModel";
    
    private Stock stock;
    private int trendMin;
    private int trendMax;
    private int trendStep;
    private int setupMin;
    private int setupMax;
    
    private int bestTrend = 0;
    private int bestSetup = 0;
    private double winRate = 0.0;
    private double avg = 0.0;
    
    public SMAModel(Stock stock) {
        this.stock = stock;
        trendMin = 100;
        trendMax = 400;
        trendStep = 1;
        setupMin = 5;
        setupMax = 20;
    }
    
    public void simulate() {

        for (int i = trendMin; i <= trendMax; i += trendStep) {
            SMA sma = new SMA(stock.close);
            double[] trend = sma.getIndicator(i);
            
            for (int j = setupMin; j <= setupMax; j++) {
                ArrayList<int[]> trades = new ArrayList<int[]>();
                int position = 0;            // = 0: no position; < 0: short; > 0: long
                for (int k = Math.min(stock.N - i, 3700); k >= 0; k--) {
                   // details of this model is not made public right now. 
                   
                }
                double[] result = getPerformance(trades);
                if (avg < result[1]) {
                    avg = result[1];
                    winRate = result[0];
                    bestTrend = i;
                    bestSetup = j;
                }
            }
        }
        output();
    }
    
    public double[] getPerformance(ArrayList<int[]> trades) {
        double[] result = new double[2];
        if (trades.isEmpty()) return result;
        
        int nWin = 0;
        double rates = 0;
        for (int i = 0; i < trades.size(); i++) {
            int[] trade = trades.get(i);
            if (stock.close[trade[1]] > stock.close[trade[0]]) nWin++;
            rates += stock.close[trade[1]] / stock.close[trade[0]] - 1;
        }
        result[0] = 1.0 * nWin / trades.size();
        result[1] = rates * 255 / Math.min(stock.N - 300, 3700);
        return result;
    }
    
    public void output() {
        System.out.println("Best Trend: " + bestTrend + "\nBest Setup: " + bestSetup + 
                "\nWin rate: " + (winRate*100) + "%\nYearly performance: " + avg);
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String[] stocks = { "spy", "iwm", "dia", "qqq", "aapl", "axp", "ba", "cat",
                "csco", "cvx", "dd", "dis", "ge", "gs", "hd", "ibm", "intc", "jnj", 
                "jpm", "ko", "mcd", "mmm", "mrk", "msft", "nke", "pfe", "pg", "trv",
                "unh", "utx", "v", "vz", "wmt", "xom" };
        
        for (String s : stocks) {
            Stock stock = new Stock(s);
            SMAModel model = new SMAModel(stock);
            System.out.println("\nAnalyzing " + s + "...");
            model.simulate();
        }
    }

}
