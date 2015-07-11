package analysis;
import data.*;
import java.util.ArrayList;
/**
 *
 * This class wraps up the output information of a simulation of a stock for front use 
 * String[] tag: the titles of the statistics
 * double[] stat: the numbers of the statistics
 * 
 * @author JianfeHua
 *
 */
public class SimulationResult {
    private Stock stock;
    private String modelName;
    private ArrayList<int[]> trades;
    private String[] tag;
    private double[] stat;
    private String nextDay;
    
    public SimulationResult(Stock stock, String modelName) {
        this.stock = stock;
        this.modelName = modelName;
    }
    
    public SimulationResult(Stock stock, String modelName, ArrayList<int[]> trades, 
            String[] tag, double[] stat, String nextDay) {
        this.stock = stock;
        this.modelName = modelName;
        this.trades = trades;
        this.tag = tag;
        this.stat = stat;
        this.nextDay = nextDay;
    }
    
    public Stock getStock() {
        return stock;
    }
    public String getModel() {
        return modelName;
    }
    
    void setTrades(ArrayList<int[]> trades) {
        this.trades = trades;
    }
    public ArrayList<int[]> getTrades() {
        return trades;
    }
    
    void setTag(String[] tag) {
        this.tag = tag;
    }
    public String[] getTag() {
        return tag;
    }
    
    void setStat(double[] stat) {
        this.stat = stat;
    }
    public double[] getStat() {
        return stat;
    }
    
    void setNext(String next) {
        this.nextDay = next;
    }
    public String getNext() {
        return nextDay;
    }
    
    
}
