/* ********************
 * Algorithms, Part 1, Programming Assignments 2
 * Subset
 * Jianfei Hua 2/16/2014
 ***********************/
public class Subset {
    public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);
    RandomizedQueue<String> rq = new RandomizedQueue<String>();
    while (!StdIn.isEmpty()) {
        String item = StdIn.readString();
        rq.enqueue(item); 
    }
    while (rq.size() > k) {
        rq.dequeue();
    }
    
    for (String s : rq) {
        StdOut.println(s);
    }
    }
}
